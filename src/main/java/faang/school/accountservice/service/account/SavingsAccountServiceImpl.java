package faang.school.accountservice.service.account;

import faang.school.accountservice.dto.account.SavingsAccountDto;
import faang.school.accountservice.mapper.SavingsAccountMapper;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import faang.school.accountservice.model.account.Account;
import faang.school.accountservice.model.account.SavingsAccount;
import faang.school.accountservice.model.account.SavingsAccountRate;
import faang.school.accountservice.model.balance.Balance;
import faang.school.accountservice.repository.AccountJpaRepository;
import faang.school.accountservice.repository.BalanceRepository;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.TariffHistoryRepository;
import faang.school.accountservice.repository.TariffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsAccountServiceImpl implements SavingsAccountService {
    private final SavingsAccountMapper savingsAccountMapper;
    private final AccountJpaRepository accountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final TariffRepository tariffRepository;
    private final TariffHistoryRepository tariffHistoryRepository;
    private final BalanceRepository balanceRepository;

    @Transactional
    @Override
    public SavingsAccountDto openSavingsAccount(SavingsAccountDto savingsAccountDto) {
        Tariff tariff = tariffRepository.findById(savingsAccountDto.getTariffId())
                .orElseThrow(() -> new EntityNotFoundException("Tariff with id " + savingsAccountDto.getTariffId() + " not found"));
        Account account = accountRepository.findById(savingsAccountDto.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + savingsAccountDto.getAccountId() + " not found"));
        SavingsAccount savingsAccount = SavingsAccount.builder()
                .account(account)
                .build();
        savingsAccount = savingsAccountRepository.save(savingsAccount);
        TariffHistory tariffHistory = TariffHistory.builder()
                .savingsAccount(savingsAccount)
                .tariff(tariff)
                .build();
        tariffHistoryRepository.save(tariffHistory);
        SavingsAccountDto resultDto = savingsAccountMapper.toSavingsAccountDto(savingsAccount);
        resultDto.setTariffId(tariff.getId());
        return resultDto;
    }

    @Override
    public SavingsAccountDto getSavingsAccount(Long id) {
        Optional<SavingsAccountDto> savingsAccountDto = savingsAccountRepository.findSavingsAccountWithDetails(id);
        return savingsAccountDto
                .orElseThrow(() -> new EntityNotFoundException("SavingsAccount with id " + id + " not found"));
    }

    @Override
    public List<SavingsAccountDto> getSavingsAccountByUserId(Long userId) {
        List<String> numbers = accountRepository.findNumbersByUserId(userId);
        if (numbers.isEmpty()) {
            throw new EntityNotFoundException("Accounts with user id " + userId + " not found");
        }
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.getSavingsAccountsWithLastTariffRate(numbers);
        if (savingsAccounts.isEmpty()) {
            throw new EntityNotFoundException("Accounts with user id " + userId + " not found");
        }
        return savingsAccounts.stream()
                .map(this::mapToSavingsAccountDto)
                .toList();
    }

    @Transactional
    @Async("calculatePercentsExecutor")
    @Override
    public void calculatePercent(Long balanceId, BigDecimal rate, Long savingsAccountId) {
        SavingsAccount savingsAccount = savingsAccountRepository.findById(savingsAccountId)
                .orElseThrow(() -> new EntityNotFoundException("SavingsAccount with id " + savingsAccountId + " not found"));

        Balance balance = balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id " + balanceId + " not found"));

        int currentYearDays = Year.now().length();
        BigDecimal dailyRate = rate.divide(BigDecimal.valueOf(currentYearDays), 8, RoundingMode.HALF_UP);
        BigDecimal dailyInterest = balance.getActualValue()
                .multiply((dailyRate.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP)));
        BigDecimal newBalance = balance.getActualValue().add(dailyInterest);
        balance.setActualValue(newBalance);
        LocalDateTime currentTime = LocalDateTime.now();
        savingsAccount.setUpdatedAt(currentTime);
        savingsAccount.setLastDatePercent(currentTime);
    }

    private SavingsAccountDto mapToSavingsAccountDto(SavingsAccount savingsAccount) {
        Optional<TariffHistory> lastTariffHistory = Optional.ofNullable(savingsAccount.getTariffHistory())
                .orElse(Collections.emptyList())
                .stream()
                .max(Comparator.comparing(TariffHistory::getCreatedAt));
        Long tariffId = lastTariffHistory.map(TariffHistory::getId).orElse(null);
        BigDecimal rate = lastTariffHistory.flatMap(th ->
                th.getSavingsAccountRates().stream()
                        .max(Comparator.comparing(SavingsAccountRate::getCreatedAt))
                        .map(SavingsAccountRate::getRate)
        ).orElse(null);
        return SavingsAccountDto.builder()
                .id(savingsAccount.getId())
                .tariffId(tariffId)
                .rate(rate)
                .lastDatePercent(savingsAccount.getLastDatePercent())
                .createdAt(savingsAccount.getCreatedAt())
                .updatedAt(savingsAccount.getUpdatedAt())
                .build();
    }
}