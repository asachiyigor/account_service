package faang.school.accountservice.service.account;

import faang.school.accountservice.dto.account.AccountDtoCloseBlock;
import faang.school.accountservice.dto.account.AccountDtoFilter;
import faang.school.accountservice.dto.account.AccountDtoOpen;
import faang.school.accountservice.dto.account.AccountDtoResponse;
import faang.school.accountservice.dto.account.AccountDtoVerify;
import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.filter.FilterSpecification;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.model.account.Account;
import faang.school.accountservice.model.owner.Owner;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.OwnerRepository;
import faang.school.accountservice.repository.AccountJpaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class AccountService {
    private final AccountRepository accountRepository;
    private final OwnerRepository ownerRepository;
    private final AccountMapper accountMapper;
    private final AccountJpaRepository accountJpaRepository;
    private final List<FilterSpecification<Account, AccountDtoFilter>> filters;

    private final Set<String> accountNumbers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public AccountDtoResponse open(@NotNull @Valid AccountDtoOpen dto) {
        Account account = accountMapper.toAccount(dto);
        String accountNumber = getAccountNumber();
        account.setAccountNumber(accountNumber);
        Owner owner = ownerRepository.findOwnerByIdAndType(dto.getOwnerId(), dto.getOwnerType())
                .orElseGet(() -> ownerRepository.save(
                        Owner.builder()
                                .ownerId(dto.getOwnerId())
                                .type(dto.getOwnerType())
                                .accounts(new ArrayList<>())
                                .build()
                ));
        account.setOwner(owner);
        account.setStatus(AccountStatus.PENDING);
        Account savedAccount = accountRepository.save(account);
        owner.getAccounts().add(savedAccount);
        ownerRepository.save(owner);
        accountNumbers.add(accountNumber);
        return accountMapper.toDto(savedAccount);
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public AccountDtoResponse verify(@NotNull @Valid AccountDtoVerify dto) {
        Account account = accountRepository.getAccountByIdAndStatus(dto.getId(), AccountStatus.PENDING);
        account.setStatus(AccountStatus.ACTIVE);
        return accountMapper.toDto(accountRepository.save(account));
    }

    public AccountDtoResponse getAccount(@NotNull @Positive Long id) {
        return accountMapper.toDto(accountRepository.getAccountById(id));
    }

    public List<AccountDtoResponse> getAccounts(@NotNull AccountDtoFilter filterDto) {
        Specification<Account> specification = filters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .map(filter -> filter.apply(filterDto))
                .reduce(Specification.where(null), Specification::and);

        return accountJpaRepository.findAll(specification).stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public AccountDtoResponse closeAccount(@NotNull @Valid AccountDtoCloseBlock dtoClose) {
        Account account = dtoClose.getId() != null
                ? accountRepository.getAccountById(dtoClose.getId())
                : accountRepository.getAccountByAccountNumber(dtoClose.getAccountNumber());
        checkAccountClosedBlocked(account);
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 3000L))
    public AccountDtoResponse blockAccount(@NotNull @Valid AccountDtoCloseBlock dtoCloseBlock) {
        Account account = dtoCloseBlock.getId() != null
                ? accountRepository.getAccountById(dtoCloseBlock.getId())
                : accountRepository.getAccountByAccountNumber(dtoCloseBlock.getAccountNumber());
        checkAccountClosedBlocked(account);
        account.setStatus(AccountStatus.BLOCKED);
        account.setClosedAt(LocalDateTime.now());
        return accountMapper.toDto(accountRepository.save(account));
    }

    private String getAccountNumber() {
        String numbers;
        do {
            numbers = generateRandomNumber();
        } while (accountNumbers.contains(numbers) || numbers.length() < 12 || numbers.length() > 20);
        return numbers;
    }

    private String generateRandomNumber() {
        StringBuilder stringNumbers = new StringBuilder();
        int length = ThreadLocalRandom.current().nextInt(12, 21);
        for (int i = 0; i < length; i++) {
            stringNumbers.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return stringNumbers.toString();
    }

    private static void checkAccountClosedBlocked(@NotNull Account account) {
        if (account.getStatus().equals(AccountStatus.CLOSED)) {
            throw new IllegalStateException("Account is already closed by id: " + account.getId());
        }
        if (account.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new IllegalStateException("Account is already blocked by id: " + account.getId());
        }
    }
}
