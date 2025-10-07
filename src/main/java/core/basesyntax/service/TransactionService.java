package core.basesyntax.service;

import core.basesyntax.model.TransactionType;

public interface TransactionService {
    TransactionHandler selectHandler(TransactionType transactionType);
}
