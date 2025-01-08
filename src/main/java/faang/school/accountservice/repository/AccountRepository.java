package faang.school.accountservice.repository;

import faang.school.accountservice.enums.AccountStatus;
import faang.school.accountservice.model.account.Account;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final AccountJpaRepository accountJpaRepository;

    public Account save(Account account) {
        return accountJpaRepository.save(account);
    }

    public Account getAccountByIdAndStatus(long id, AccountStatus accountStatus) {
        return accountJpaRepository.findByIdAndStatus(id, accountStatus).orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + id + " and status: " + accountStatus));
    }

    public Account getAccountById(long id) {
        return accountJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + id));
    }

    public List<Account> findAll() {
        return accountJpaRepository.findAll();
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountJpaRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new EntityNotFoundException("Account not found by number: " + accountNumber));
    }
}