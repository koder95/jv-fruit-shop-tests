package core.basesyntax.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import core.basesyntax.io.impl.CsvReportSerializer;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.Report;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SerializersTest {
    private static final Path SAVE_PATH = Path.of("testReport.csv");
    private static final FruitType BANANA = new FruitType("banana");

    private final ReportSerializer serializer = new CsvReportSerializer();

    @Test
    void save_NullNull_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer.save(null, null));
    }

    @Test
    void save_NullReport_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer.save(null, SAVE_PATH));
    }

    @Test
    void save_NullPath_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer
                .save(Report.builder().get(), null));
    }

    @Test
    void save_EmptyReport_Ok() {
        serializer.save(Report.builder().get(), SAVE_PATH);
        try (BufferedReader reader = Files.newBufferedReader(SAVE_PATH)) {
            Stream<String> lines = reader.lines();
            Optional<String> first = lines.findFirst();
            assertTrue(first.isPresent());
            first.ifPresent(line -> assertEquals("fruit,quantity", line));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void save_RegularReport_Ok() {
        serializer.save(Report.builder().set(BANANA, BigInteger.TEN).get(), SAVE_PATH);
        try (BufferedReader reader = Files.newBufferedReader(SAVE_PATH)) {
            Stream<String> lines = reader.lines();
            Optional<String> first = lines.skip(1).findFirst();
            assertTrue(first.isPresent());
            first.ifPresent(line -> assertEquals("banana,10", line));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void save_RegularReportToReadOnly_NotOk() {
        File file = SAVE_PATH.toFile();
        try {
            boolean created = file.createNewFile();
            assertTrue(created);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean readOnly = file.setWritable(false);
        assertTrue(readOnly);
        assertThrows(RuntimeException.class, () -> serializer
                .save(Report.builder().set(BANANA, BigInteger.TEN).get(), SAVE_PATH));
        boolean readable = file.setWritable(true);
        assertTrue(readable);
    }

    @AfterEach
    void clearSaveFile() {
        try {
            Files.deleteIfExists(SAVE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
