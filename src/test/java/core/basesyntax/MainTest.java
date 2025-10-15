package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void main_Default_Ok() {
        String input = """
                type,fruit,quantity
                b,banana,20
                b,apple,100
                s,banana,100
                p,banana,13
                r,apple,10
                p,apple,20
                p,banana,5
                s,banana,50
                """;
        try {
            Files.createDirectories(Main.LOAD_PATH.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Main.LOAD_PATH)) {
            IOException ex = new IOException("Cannot write");
            input.lines().forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    ex.addSuppressed(e);
                }
            });
            if (ex.getSuppressed().length > 0) {
                throw ex;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Main.main(new String[0]);
        try (BufferedReader reader = Files.newBufferedReader(Main.SAVE_PATH)) {
            String expected = """
                    fruit,quantity
                    banana,152
                    apple,90
                    """;
            assertArrayEquals(expected.lines().toArray(String[]::new),
                    reader.lines().toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
