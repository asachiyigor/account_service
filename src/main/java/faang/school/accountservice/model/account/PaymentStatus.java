package faang.school.accountservice.model.account;

public enum PaymentStatus {
    ACTIVE("Действующий"),
    FROZEN("Заморожен"),
    CLOSED("Закрыт"),
    PENDING("Ожидание платежа"),
    IN_PROGRESS("Платёж в процессе"),
    PAID("Оплачен"),
    ERROR("Ошибка при платеже"),
    CANCELLED("Отменён"),
    OVERDUE("Просрочен"),
    REFUNDED("Возвращён"),
    PARTIALLY_PAID("Частично оплачен"),
    AWAITING_CONFIRMATION("Ожидает подтверждения"),
    ARCHIVED("Архивирован");

    private final String displayName;

    PaymentStatus(String description) {
        this.displayName = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
