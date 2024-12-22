package faang.school.accountservice.service.account;

import faang.school.accountservice.dto.account.AccountDtoFilter;
import faang.school.accountservice.model.account.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountSpecification {

    public static Specification<Account> byFilter(AccountDtoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getAccountNumber() != null) {
                predicates.add(cb.equal(root.get("accountNumber"), filter.getAccountNumber()));
            }
            if (filter.getOwnerIds() != null && !filter.getOwnerIds().isEmpty()) {
                predicates.add(root.get("owner").get("ownerId").in(filter.getOwnerIds()));
            }
            if (filter.getOwnerType() != null) {
                predicates.add(cb.equal(root.get("owner").get("type"), filter.getOwnerType()));
            }
            if (filter.getAccountTypes() != null && !filter.getAccountTypes().isEmpty()) {
                predicates.add(root.get("accountType").in(filter.getAccountTypes()));
            }
            if (filter.getCurrencies() != null && !filter.getCurrencies().isEmpty()) {
                predicates.add(root.get("currency").in(filter.getCurrencies()));
            }
            if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(filter.getStatuses()));
            }
            if (filter.getCreatedAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtStart()));
            }
            if (filter.getCreatedAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtEnd()));
            }
            if (filter.getClosedAtStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("closedAt"), filter.getClosedAtStart()));
            }
            if (filter.getClosedAtEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("closedAt"), filter.getClosedAtEnd()));
            }
            if (filter.getIsVerified() != null) {
                predicates.add(cb.equal(root.get("isVerified"), filter.getIsVerified()));
            }
            if (filter.getNotes() != null) {
                predicates.add(cb.like(root.get("notes"), "%" + filter.getNotes() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
