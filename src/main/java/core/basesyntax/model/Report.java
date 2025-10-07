package core.basesyntax.model;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class Report {
    private final Map<FruitType, BigInteger> recordMap;

    private Report(Map<FruitType, BigInteger> recordMap) {
        this.recordMap = Collections.unmodifiableMap(Objects.requireNonNull(recordMap));
    }

    public void consume(BiConsumer<FruitType, BigInteger> consumer) {
        recordMap.forEach(consumer);
    }

    public static Builder builder(Map<FruitType, BigInteger> initMap) {
        return new Builder(initMap);
    }

    public static Builder builder() {
        return builder(new HashMap<>());
    }

    public static class Builder implements Supplier<Report> {
        private final Map<FruitType, BigInteger> recordMap;

        public Builder(Map<FruitType, BigInteger> recordMap) {
            this.recordMap = recordMap;
        }

        public Builder set(FruitType fruitType, BigInteger quantity) {
            recordMap.put(fruitType, quantity == null ? BigInteger.ZERO : quantity);
            return this;
        }

        public Report get() {
            return new Report(recordMap);
        }
    }
}
