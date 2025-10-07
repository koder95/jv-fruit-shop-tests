package core.basesyntax.io.impl;

import core.basesyntax.io.ReportSerializer;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.Report;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReportSerializer implements ReportSerializer {
    @Override
    public void save(Report report, Path path) {
        List<String> csvRows = createCsvRows(report);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : csvRows) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> createCsvRows(Report report) {
        List<String> lines = new ArrayList<>();
        lines.add("fruit,quantity");
        report.consume((fruitType, amount) -> lines.add(createCsvRow(fruitType, amount)));
        return lines;
    }

    private String createCsvRow(FruitType fruitType, BigInteger quantity) {
        return String.join(",", fruitType.name(), quantity.toString());
    }
}
