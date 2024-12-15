package faang.school.accountservice.model.account;

public enum Currency {
    USD("Доллар США"),
    EUR("Евро"),
    RUB("Российский рубль"),
    GBP("Фунт стерлингов"),
    JPY("Японская йена"),
    CNY("Китайский юань"),
    CHF("Швейцарский франк"),
    AUD("Австралийский доллар"),
    CAD("Канадский доллар"),
    NZD("Новозеландский доллар"),
    SGD("Сингапурский доллар"),
    HKD("Гонконгский доллар"),
    INR("Индийская рупия"),
    BRL("Бразильский реал"),
    ZAR("Южноафриканский ранд"),
    KRW("Южнокорейская вона"),
    MXN("Мексиканский песо"),
    SEK("Шведская крона"),
    NOK("Норвежская крона"),
    DKK("Датская крона"),
    AED("Дирхам ОАЭ"),
    PLN("Польский злотый");

    private final String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
