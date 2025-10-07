package core.basesyntax.service;

import core.basesyntax.db.Database;
import core.basesyntax.model.FruitType;
import java.math.BigInteger;

public interface TransactionHandler {
    void handle(Database database, FruitType fruitType, BigInteger quantity);
}
