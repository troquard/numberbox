package eu.yalacirodev.numberbox;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by nico on 13/02/17.
 */

public class TestLevels {

    @Test
    public void levelHasSolution() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Solver solver = new Solver(
                Levels.getInstance(appContext).levels[0],
                Solver.EXHAUSTIVE);
        assertEquals(solver.solve(),true);

    }
}
