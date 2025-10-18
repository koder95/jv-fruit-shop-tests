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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DeserializersTest {
    private static final String TO_LOAD_FILENAME = "testTransactions.csv";
    private final FruitTypeFactory defaultFruitTypeFactory = new DefaultFruitTypeFactory();
    private final TransactionRequestDeserializer deserializer
            = new CsvTransactionRequestDeserializer(defaultFruitTypeFactory);
    @TempDir
    private Path tmpDir;
    private Path toLoad;

    @BeforeEach
    void setUp() {
        toLoad = tmpDir.resolve(TO_LOAD_FILENAME);
    }

    @Test
    void loadFrom_Null_NotOk() {
        assertThrows(NullPointerException.class, () -> deserializer.loadFrom(null));
    }

    @Test
    void loadFrom_Dir_NotOk() {
        assertThrows(RuntimeException.class, () -> deserializer.loadFrom(tmpDir));
    }

    @Test
    void loadFrom_EmptyFile_Ok() {
        try {
            Files.createFile(toLoad);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertArrayEquals(new TransactionRequest[0], deserializer
                .loadFrom(toLoad).toArray(TransactionRequest[]::new));
    }

    @Test
    void loadFrom_OneCorrectTransactionFile_Ok() {
        try (BufferedWriter writer = Files.newBufferedWriter(toLoad)) {
            writer.write("operation,fruit,quantity");
            writer.newLine();
            writer.write("b,banana,100");
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TransactionRequest expected = new TransactionRequest(TransactionType.BALANCE,
                new FruitType("banana"), BigInteger.valueOf(100));
        Optional<TransactionRequest> first = deserializer.loadFrom(toLoad)
                .stream().findFirst();
        assertTrue(first.isPresent());
        first.ifPresent(actual -> assertEquals(expected, actual));
    }
}
