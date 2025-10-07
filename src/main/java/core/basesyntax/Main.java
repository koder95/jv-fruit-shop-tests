package core.basesyntax;

import core.basesyntax.db.Database;
import core.basesyntax.db.impl.MapDatabase;
import core.basesyntax.io.ReportSerializer;
import core.basesyntax.io.TransactionRequestDeserializer;
import core.basesyntax.io.impl.CsvReportSerializer;
import core.basesyntax.io.impl.CsvTransactionRequestDeserializer;
import core.basesyntax.model.Report;
import core.basesyntax.model.Reporter;
import core.basesyntax.model.TransactionRequest;
import core.basesyntax.model.TransactionType;
import core.basesyntax.model.impl.DefaultFruitTypeFactory;
import core.basesyntax.model.impl.ReporterImpl;
import core.basesyntax.service.TransactionHandler;
import core.basesyntax.service.TransactionService;
import core.basesyntax.service.impl.BalanceTransactionHandler;
import core.basesyntax.service.impl.MapTransactionService;
import core.basesyntax.service.impl.PurchaseTransactionHandler;
import core.basesyntax.service.impl.ReturnTransactionHandler;
import core.basesyntax.service.impl.SupplyTransactionHandler;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final Path MAIN_RESOURCES = Path.of("src/main/resources");
    public static final Path LOAD_PATH = MAIN_RESOURCES.resolve("reportToRead.csv");
    public static final Path SAVE_PATH = MAIN_RESOURCES.resolve("finalReport.csv");

    public static void main(String[] args) {
        // read data
        List<TransactionRequest> transactionRequests = loadTransactionRequestList();
        // process data
        Database database = createDatabase();
        database.handleAll(transactionRequests);
        // generate raport
        Report report = createReport(database);
        // write raport
        saveReport(report);
    }

    private static Report createReport(Database database) {
        Reporter reporter = new ReporterImpl();
        return reporter.reportWith(database);
    }

    private static MapDatabase createDatabase() {
        Map<TransactionType, TransactionHandler> handlers = createTransactionTypeMap();
        TransactionService service = createTransactionService(handlers);
        return new MapDatabase(service);
    }

    private static TransactionService createTransactionService(
            Map<TransactionType, TransactionHandler> handlers) {
        return new MapTransactionService(handlers);
    }

    private static CsvTransactionRequestDeserializer createTransactionRequestDeserializer() {
        return new CsvTransactionRequestDeserializer(new DefaultFruitTypeFactory());
    }

    private static Map<TransactionType, TransactionHandler> createTransactionTypeMap() {
        HashMap<TransactionType, TransactionHandler> map = new HashMap<>();
        map.put(TransactionType.BALANCE, new BalanceTransactionHandler());
        map.put(TransactionType.SUPPLY, new SupplyTransactionHandler());
        map.put(TransactionType.PURCHASE, new PurchaseTransactionHandler());
        map.put(TransactionType.RETURN, new ReturnTransactionHandler());
        return Map.copyOf(map);
    }

    private static List<TransactionRequest> loadTransactionRequestList() {
        TransactionRequestDeserializer deserializer = createTransactionRequestDeserializer();
        return deserializer.loadFrom(LOAD_PATH);
    }

    private static void saveReport(Report report) {
        ReportSerializer serializer = new CsvReportSerializer();
        serializer.save(report, SAVE_PATH);
    }
}
