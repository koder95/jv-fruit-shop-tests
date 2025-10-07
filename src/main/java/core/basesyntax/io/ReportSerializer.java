package core.basesyntax.io;

import core.basesyntax.model.Report;
import java.nio.file.Path;

public interface ReportSerializer {
    void save(Report report, Path path);
}
