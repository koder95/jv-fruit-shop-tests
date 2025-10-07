package core.basesyntax.model;

import java.math.BigInteger;

public record TransactionRequest(TransactionType transactionType,
                                 FruitType fruitType, BigInteger quantity) {
}
