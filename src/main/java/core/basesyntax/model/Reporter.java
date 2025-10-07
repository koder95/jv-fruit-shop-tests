package core.basesyntax.model;

import core.basesyntax.db.Database;

public interface Reporter {
    Report reportWith(Database database);
}
