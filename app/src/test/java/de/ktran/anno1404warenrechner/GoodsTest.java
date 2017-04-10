package de.ktran.anno1404warenrechner;

import org.junit.Test;

import java.text.MessageFormat;

import de.ktran.anno1404warenrechner.data.Goods;

import static org.junit.Assert.*;

public class GoodsTest {
    @Test
    public void allGoodsProducible() throws Exception {
        for (Goods goods : Goods.values()) {
            assertNotNull(MessageFormat.format("Production of {0} not possible.", goods.id), goods.getProductionBuilding());
        }
    }
}