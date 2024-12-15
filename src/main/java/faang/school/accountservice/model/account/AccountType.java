package faang.school.accountservice.model.account;

public enum AccountType {
    CURRENT("Расчётный счёт"),
    SAVINGS("Сберегательный счёт"),
    FOREIGN_EXCHANGE("Валютный счёт"),
    CREDIT("Кредитный счёт"),
    JOINT("Совместный счёт"),
    BUSINESS("Бизнес-счёт"),
    ESCROW("Эскроу-счёт"),
    DEPOSIT("Депозитный счёт");

    private final String displayName;

    AccountType(String description) {
        this.displayName = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
