package core.basesyntax.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import core.basesyntax.model.FruitType;
import core.basesyntax.model.FruitTypeFactory;
import org.junit.jupiter.api.Test;

class DefaultFruitTypeFactoryTest {
    private final FruitTypeFactory factory = new DefaultFruitTypeFactory();

    @Test
    void create_Null_Ok() {
        FruitType actual = factory.create(null);
        assertNotNull(actual);
        assertNull(actual.name());
    }

    @Test
    void create_EmptyName_Ok() {
        FruitType actual = factory.create("");
        assertNotNull(actual);
        assertEquals("", actual.name());
    }

    @Test
    void create_RegularName_Ok() {
        String expectedName = "name";
        FruitType actual = factory.create(expectedName);
        assertNotNull(actual);
        assertEquals(expectedName, actual.name());
    }
}
