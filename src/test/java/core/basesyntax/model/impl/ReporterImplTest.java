package core.basesyntax.model.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Database;
import core.basesyntax.db.impl.MapDatabase;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.Report;
import core.basesyntax.model.Reporter;
import core.basesyntax.model.TransactionType;
import core.basesyntax.service.TransactionHandler;
import core.basesyntax.service.TransactionService;
import core.basesyntax.service.impl.MapTransactionService;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ReporterImplTest {

    private static final FruitType BANANA = new FruitType("banana");
    private static final FruitType APPLE = new FruitType("apple");

    private final HashMap<TransactionType, TransactionHandler> handlers = new HashMap<>();
    private final TransactionService service = new MapTransactionService(handlers);
    private final Database database = new MapDatabase(service);
    private final Reporter reporter = new ReporterImpl();

    @Test
    void reportWith_EmptyDatabase_Ok() {
        Report report = reporter.reportWith(database);
        AtomicBoolean test = new AtomicBoolean(false);
        report.consume((fruitType, integer) -> test.set(true));
        assertFalse(test.get());
    }

    @Test
    void reportWith_Banana_Ok() {
        database.set(BANANA, BigInteger.TEN);
        Report report = reporter.reportWith(database);
        AtomicBoolean test = new AtomicBoolean(true);
        report.consume((fruitType, integer) -> test
                .set(test.get() && fruitType.equals(BANANA) && integer.equals(BigInteger.TEN)));
        assertTrue(test.get());
    }

    @Test
    void reportWith_BananaAndApple_Ok() {
        database.set(BANANA, BigInteger.TEN);
        database.set(APPLE, BigInteger.TEN);
        Report report = reporter.reportWith(database);
        AtomicBoolean bananaTest = new AtomicBoolean(true);
        AtomicBoolean appleTest = new AtomicBoolean(true);
        report.consume((fruitType, integer) -> {
            if (fruitType.equals(BANANA)) {
                bananaTest.set(bananaTest.get() && integer.equals(BigInteger.TEN));
            } else if (fruitType.equals(APPLE)) {
                appleTest.set(appleTest.get() && integer.equals(BigInteger.TEN));
            } else {
                bananaTest.set(false);
                appleTest.set(false);
            }
        });
        assertTrue(bananaTest.get() && appleTest.get());
    }

    @AfterEach
    void clearDatabase() {
        database.set(BANANA, BigInteger.ZERO);
        database.set(APPLE, BigInteger.ZERO);
    }
}
