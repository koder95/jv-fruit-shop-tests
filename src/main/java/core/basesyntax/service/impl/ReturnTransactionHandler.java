package core.basesyntax.service.impl;

import core.basesyntax.db.Database;
import core.basesyntax.model.FruitType;
import core.basesyntax.service.TransactionHandler;
import java.math.BigInteger;

public class ReturnTransactionHandler implements TransactionHandler {
    @Override
    public void handle(Database database, FruitType fruitType, BigInteger quantity) {
        if (quantity == null || quantity.signum() < 0) {
            throw new IllegalArgumentException("Quantity is wrong. Cannot be: " + quantity);
        }
        database.add(fruitType, quantity);
    }
}
