package core.basesyntax.model;

public enum TransactionType {
    BALANCE("b"), SUPPLY("s"), PURCHASE("p"), RETURN("r");

    private final String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
