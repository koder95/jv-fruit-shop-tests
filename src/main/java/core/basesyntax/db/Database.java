package core.basesyntax.db;

import core.basesyntax.model.FruitType;
import core.basesyntax.model.TransactionRequest;
import core.basesyntax.service.TransactionService;
import java.math.BigInteger;
import java.util.List;
import java.util.function.BiConsumer;

public interface Database {
    void consume(BiConsumer<FruitType, BigInteger> consumer);

    TransactionService getTransactionService();

    default void handle(TransactionRequest request) {
        getTransactionService()
                .selectHandler(request.transactionType())
                .handle(this, request.fruitType(), request.quantity());
    }

    default void handleAll(List<TransactionRequest> requests) {
        requests.forEach(this::handle);
    }

    void set(FruitType fruitType, BigInteger amount);

    void add(FruitType fruitType, BigInteger amount);

    void remove(FruitType fruitType, BigInteger amount);
}
