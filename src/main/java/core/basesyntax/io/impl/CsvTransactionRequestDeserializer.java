package core.basesyntax.io.impl;

import core.basesyntax.io.TransactionRequestDeserializer;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.FruitTypeFactory;
import core.basesyntax.model.TransactionRequest;
import core.basesyntax.model.TransactionType;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvTransactionRequestDeserializer implements TransactionRequestDeserializer {
    private final FruitTypeFactory fruitTypeFactory;

    public CsvTransactionRequestDeserializer(FruitTypeFactory fruitTypeFactory) {
        this.fruitTypeFactory = fruitTypeFactory;
    }

    @Override
    public List<TransactionRequest> loadFrom(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines().skip(1).map(this::parseLine).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionRequest parseLine(String line) {
        List<String> cells = Arrays.stream(line.split(",")).map(String::trim).toList();
        TransactionType transactionType = Arrays.stream(TransactionType.values())
                .filter(tt -> tt.getCode().equals(cells.get(0)))
                .findFirst()
                .orElseThrow();
        FruitType fruitType = fruitTypeFactory.create(cells.get(1));
        return new TransactionRequest(transactionType, fruitType, new BigInteger(cells.get(2)));
    }
}
