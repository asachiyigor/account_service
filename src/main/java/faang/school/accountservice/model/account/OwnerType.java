package faang.school.accountservice.model.account;

public enum OwnerType {
    USER("Пользователь"),
    PROJECT("Проект");

    private final String displayName;

    OwnerType(String description) {
        this.displayName = description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
