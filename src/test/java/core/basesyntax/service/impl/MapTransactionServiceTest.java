package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import core.basesyntax.model.TransactionType;
import core.basesyntax.service.TransactionHandler;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MapTransactionServiceTest {

    private static MapTransactionService INSTANCE;

    @BeforeAll
    static void setUp() {
        HashMap<TransactionType, TransactionHandler> handlers = new HashMap<>();
        handlers.put(TransactionType.BALANCE, new BalanceTransactionHandler());
        handlers.put(TransactionType.SUPPLY, new SupplyTransactionHandler());
        handlers.put(TransactionType.PURCHASE, new PurchaseTransactionHandler());
        handlers.put(TransactionType.RETURN, new ReturnTransactionHandler());
        INSTANCE = new MapTransactionService(handlers);
    }

    @Test
    void selectHandler_Balance_Ok() {
        TransactionHandler actual = INSTANCE.selectHandler(TransactionType.BALANCE);
        assertNotNull(actual);
        assertEquals(BalanceTransactionHandler.class, actual.getClass());
    }

    @Test
    void selectHandler_Supply_Ok() {
        TransactionHandler actual = INSTANCE.selectHandler(TransactionType.SUPPLY);
        assertNotNull(actual);
        assertEquals(SupplyTransactionHandler.class, actual.getClass());
    }

    @Test
    void selectHandler_Purchase_Ok() {
        TransactionHandler actual = INSTANCE.selectHandler(TransactionType.PURCHASE);
        assertNotNull(actual);
        assertEquals(PurchaseTransactionHandler.class, actual.getClass());
    }

    @Test
    void selectHandler_Return_Ok() {
        TransactionHandler actual = INSTANCE.selectHandler(TransactionType.RETURN);
        assertNotNull(actual);
        assertEquals(ReturnTransactionHandler.class, actual.getClass());
    }
}
