package core.basesyntax.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Database;
import core.basesyntax.db.impl.MapDatabase;
import core.basesyntax.model.FruitType;
import core.basesyntax.model.TransactionRequest;
import core.basesyntax.model.TransactionType;
import core.basesyntax.service.TransactionHandler;
import core.basesyntax.service.TransactionService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionHandlersTest {

    private static final FruitType BANANA = new FruitType("banana");
    private static TransactionService SERVICE;
    private Database database;

    @BeforeAll
    static void setUp() {
        HashMap<TransactionType, TransactionHandler> handlers = new HashMap<>();
        handlers.put(TransactionType.BALANCE, new BalanceTransactionHandler());
        handlers.put(TransactionType.SUPPLY, new SupplyTransactionHandler());
        handlers.put(TransactionType.PURCHASE, new PurchaseTransactionHandler());
        handlers.put(TransactionType.RETURN, new ReturnTransactionHandler());
        SERVICE = new MapTransactionService(handlers);
    }

    @BeforeEach
    void prepare() {
        database = new MapDatabase(SERVICE);
    }

    @Test
    void balanceHandle_EmptyDatabase_Ok() {
        BigInteger expected = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.BALANCE, BANANA, expected));
        database.consume((type, quantity) -> assertEquals(expected, quantity));
    }

    @Test
    void balanceHandle_NotEmptyDatabase_Ok() {
        database.add(BANANA, BigInteger.TEN);
        BigInteger expected = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.BALANCE, BANANA, expected));
        database.consume((type, quantity) -> assertEquals(expected, quantity));
    }

    @Test
    void negativeBalanceHandle_EmptyDatabase_NotOk() {
        BigInteger negative = BigInteger.valueOf(-123);
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.BALANCE, BANANA, negative)));
    }

    @Test
    void purchaseHandle_EmptyDatabase_Ok() {
        BigInteger quantity = BigInteger.valueOf(123);
        BigInteger expected = BigInteger.valueOf(-123);
        database.handle(new TransactionRequest(TransactionType.PURCHASE, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(expected, actual));
    }

    @Test
    void negativePurchaseHandle_EmptyDatabase_NotOk() {
        BigInteger negative = BigInteger.valueOf(-123);
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.PURCHASE, BANANA, negative)));
    }

    @Test
    void purchaseHandle_NotEmptyDatabase_Ok() {
        database.add(BANANA, BigInteger.valueOf(123));
        BigInteger quantity = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.PURCHASE, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(BigInteger.ZERO, actual));
    }

    @Test
    void returnHandle_EmptyDatabase_Ok() {
        BigInteger quantity = BigInteger.valueOf(123);
        BigInteger expected = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.RETURN, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(expected, actual));
    }

    @Test
    void returnHandle_NotEmptyDatabase_Ok() {
        database.add(BANANA, BigInteger.valueOf(-123));
        BigInteger quantity = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.RETURN, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(BigInteger.ZERO, actual));
    }

    @Test
    void negativeReturnHandle_EmptyDatabase_NotOk() {
        BigInteger negative = BigInteger.valueOf(-123);
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.RETURN, BANANA, negative)));
    }

    @Test
    void supplyHandle_EmptyDatabase_Ok() {
        BigInteger quantity = BigInteger.valueOf(123);
        BigInteger expected = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.SUPPLY, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(expected, actual));
    }

    @Test
    void supplyHandle_NotEmptyDatabase_Ok() {
        database.add(BANANA, BigInteger.valueOf(-123));
        BigInteger quantity = BigInteger.valueOf(123);
        database.handle(new TransactionRequest(TransactionType.SUPPLY, BANANA, quantity));
        database.consume((type, actual) -> assertEquals(BigInteger.ZERO, actual));
    }

    @Test
    void negativeSupplyHandle_EmptyDatabase_NotOk() {
        BigInteger negative = BigInteger.valueOf(-123);
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.SUPPLY, BANANA, negative)));
    }

    @Test
    void nullHandle_EmptyDatabase_NotOk() {
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.BALANCE, BANANA, null)));
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.SUPPLY, BANANA, null)));
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.PURCHASE, BANANA, null)));
        assertThrows(IllegalArgumentException.class, () -> database
                .handle(new TransactionRequest(TransactionType.RETURN, BANANA, null)));
    }

    @Test
    void handle_ParamNull_Ok() {
        assertThrows(NullPointerException.class, () -> database.handle(null));
    }

    @Test
    void handleAll_ParamNull_NotOk() {
        assertThrows(NullPointerException.class, () -> database.handleAll(null));
    }

    @Test
    void handleAll_EmptyList_Ok() {
        database.handleAll(new ArrayList<>());
    }

    @Test
    void handleAll_OneElemList_Ok() {
        ArrayList<TransactionRequest> requests = new ArrayList<>();
        requests.add(new TransactionRequest(TransactionType.BALANCE, BANANA, BigInteger.TEN));
        database.handleAll(requests);
        database.consume((fruitType, actual) -> assertEquals(BigInteger.TEN, actual));
    }

    @AfterEach
    void clear() {
        database.set(BANANA, BigInteger.ZERO);
    }
}
