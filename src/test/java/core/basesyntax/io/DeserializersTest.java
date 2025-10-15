package core.basesyntax.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.io.impl.CsvTransactionRequestDeserializer;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.FruitTypeFactory;
import core.basesyntax.model.TransactionRequest;
import core.basesyntax.model.TransactionType;
import core.basesyntax.model.impl.DefaultFruitTypeFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class DeserializersTest {
    private static final Path LOAD_PATH = Path.of("testTransactions.csv");
    private final FruitTypeFactory defaultFruitTypeFactory = new DefaultFruitTypeFactory();
    private final TransactionRequestDeserializer deserializer
            = new CsvTransactionRequestDeserializer(defaultFruitTypeFactory);

    @Test
    void loadFrom_Null_NotOk() {
        assertThrows(NullPointerException.class, () -> deserializer.loadFrom(null));
    }

    @Test
    void loadFrom_EmptyFile_Ok() {
        try {
            Files.createFile(LOAD_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertArrayEquals(new TransactionRequest[0], deserializer
                .loadFrom(LOAD_PATH).toArray(TransactionRequest[]::new));
    }

    @Test
    void loadFrom_OneCorrectTransactionFile_Ok() {
        try (BufferedWriter writer = Files.newBufferedWriter(LOAD_PATH)) {
            writer.write("operation,fruit,quantity");
            writer.newLine();
            writer.write("b,banana,100");
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TransactionRequest expected = new TransactionRequest(TransactionType.BALANCE,
                new FruitType("banana"), BigInteger.valueOf(100));
        Optional<TransactionRequest> first = deserializer.loadFrom(LOAD_PATH).stream().findFirst();
        assertTrue(first.isPresent());
        first.ifPresent(actual -> assertEquals(expected, actual));
    }

    @AfterEach
    void removeLoadFile() {
        try {
            Files.deleteIfExists(LOAD_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
