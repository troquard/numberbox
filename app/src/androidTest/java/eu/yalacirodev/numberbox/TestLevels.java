package eu.yalacirodev.numberbox;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;


/**
 * Created by nico on 13/02/17.
 */

public class TestLevels {

    @Test
    public void levelHasSolution() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Solver solver = new Solver(
                Levels.getInstance(appContext).levels[5],
                Solver.EXHAUSTIVE);
        assertThat(solver.solve(), is(true));

    }
}
