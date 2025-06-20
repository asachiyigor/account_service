package faang.school.accountservice.controller.account;

import faang.school.accountservice.dto.account.AccountDtoCloseBlock;
import faang.school.accountservice.dto.account.AccountDtoFilter;
import faang.school.accountservice.dto.account.AccountDtoOpen;
import faang.school.accountservice.dto.account.AccountDtoResponse;
import faang.school.accountservice.dto.account.AccountDtoVerify;
import faang.school.accountservice.service.account.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/open")
    public AccountDtoResponse open(@RequestBody @Valid AccountDtoOpen dtoOpen) {
        return accountService.open(dtoOpen);
    }

    @PutMapping("/verify")
    public AccountDtoResponse verify(@RequestBody @Valid AccountDtoVerify dtoVerify) {
        return accountService.verify(dtoVerify);
    }

    @GetMapping("/{id}")
    public AccountDtoResponse get(@PathVariable("id") @NotNull @Positive Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    public List<AccountDtoResponse> getAccounts(@RequestBody @Valid AccountDtoFilter dtoFilter) {
        return accountService.getAccounts(dtoFilter);
    }

    @PutMapping("/close")
    public AccountDtoResponse close(@RequestBody @Valid AccountDtoCloseBlock dtoCloseBlock) {
        return accountService.closeAccount(dtoCloseBlock);
    }

    @PutMapping("/block")
    public AccountDtoResponse block(@RequestBody @Valid AccountDtoCloseBlock dtoCloseBlock) {
        return accountService.blockAccount(dtoCloseBlock);
    }

}
