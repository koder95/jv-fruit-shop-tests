package core.basesyntax.model.impl;

import core.basesyntax.db.Database;
import core.basesyntax.model.Report;
import core.basesyntax.model.Reporter;

public class ReporterImpl implements Reporter {
    @Override
    public Report reportWith(Database database) {
        Report.Builder builder = Report.builder();
        database.consume(builder::set);
        return builder.get();
    }
}
