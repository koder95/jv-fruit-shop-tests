package core.basesyntax.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import core.basesyntax.io.impl.CsvReportSerializer;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.Report;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SerializersTest {
    private static final FruitType BANANA = new FruitType("banana");
    private static final String TO_SAVE_FILENAME = "testReport.csv";

    private final ReportSerializer serializer = new CsvReportSerializer();
    @TempDir
    private Path tmpDir;
    private Path toSave;

    @BeforeEach
    void setUp() {
        toSave = tmpDir.resolve(TO_SAVE_FILENAME);
    }

    @Test
    void save_NullNull_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer.save(null, null));
    }

    @Test
    void save_NullReport_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer.save(null, toSave));
    }

    @Test
    void save_NullPath_NotOk() {
        assertThrows(NullPointerException.class, () -> serializer
                .save(Report.builder().get(), null));
    }

    @Test
    void save_EmptyReport_Ok() {
        serializer.save(Report.builder().get(), toSave);
        try (BufferedReader reader = Files.newBufferedReader(toSave)) {
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
        serializer.save(Report.builder().set(BANANA, BigInteger.TEN).get(), toSave);
        try (BufferedReader reader = Files.newBufferedReader(toSave)) {
            Stream<String> lines = reader.lines();
            Optional<String> first = lines.skip(1).findFirst();
            assertTrue(first.isPresent());
            first.ifPresent(line -> assertEquals("banana,10", line));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void save_RegularReportToDir_NotOk() {
        assertThrows(RuntimeException.class, () -> serializer
                .save(Report.builder().set(BANANA, BigInteger.TEN).get(), tmpDir));
    }
}
