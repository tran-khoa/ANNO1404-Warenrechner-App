package de.ktran.anno1404warenrechner.data;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class LogicTest {
    @Test
    public void testPopulationCalculationsOccidental() throws Exception {
        final Game game = Game.newGame(0, "");
        final Logic logic = new Logic(game);
        for (int total = 0; total < 5000; total++) {
            final Map<Population, Integer> res = logic.calculateAscensionRightsOccidental(total);
            int sum = 0;
            for (Map.Entry<Population, Integer> e : res.entrySet()) {
                assertTrue(e.getValue() >= 0);
                sum += e.getValue();

            }

            assertTrue(total == sum);
        }
    }

    @Test
    public void testPopulationCalculationsOriental() throws Exception {
        final Game game = Game.newGame(0, "");
        final Logic logic = new Logic(game);

        for (int total = 0; total < 5000; total++) {
            final Map<Population, Integer> res = logic.calculateAscensionRightsOriental(total);
            int sum = 0;
            for (Map.Entry<Population, Integer> e : res.entrySet()) {
                assertTrue(e.getValue() >= 0);
                sum += e.getValue();
                System.out.println(e.getKey().name() + ": " + e.getValue());
            }

            assertTrue(total == sum);
        }
    }
}
