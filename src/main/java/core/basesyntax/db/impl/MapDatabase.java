package core.basesyntax.db.impl;

import core.basesyntax.db.Database;
import core.basesyntax.model.FruitType;
import core.basesyntax.service.TransactionService;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class MapDatabase implements Database {
    private final Map<FruitType, BigInteger> fruitQuantityMap;
    private final TransactionService service;

    public MapDatabase(Map<FruitType, BigInteger> fruitQuantityMap, TransactionService service) {
        this.fruitQuantityMap = fruitQuantityMap;
        this.service = service;
    }

    public MapDatabase(TransactionService service) {
        this(new ConcurrentHashMap<>(), service);
    }

    @Override
    public TransactionService getTransactionService() {
        return service;
    }

    @Override
    public void set(FruitType fruitType, BigInteger quantity) {
        fruitQuantityMap.put(fruitType, quantity);
    }

    @Override
    public void add(FruitType fruitType, BigInteger quantity) {
        BigInteger oldOrZero = fruitQuantityMap.getOrDefault(fruitType, BigInteger.ZERO);
        set(fruitType, oldOrZero.add(quantity));
    }

    @Override
    public void remove(FruitType fruitType, BigInteger quantity) {
        BigInteger oldOrZero = fruitQuantityMap.getOrDefault(fruitType, BigInteger.ZERO);
        set(fruitType, oldOrZero.subtract(quantity));
    }

    @Override
    public void consume(BiConsumer<FruitType, BigInteger> consumer) {
        fruitQuantityMap.forEach(consumer);
    }
}
