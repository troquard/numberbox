package eu.yalacirodev.numberbox;


import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;


/**
 * Created by nico on 20/02/17.
 */

public class TestBestScores {

    BestScores bestScores;
    State state;

    String coton05 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,2,0,1,0],\n" +
            "[0,3,0,0,2,0],\n" +
            "[0,3,4,2,0,0],\n" +
            "[0,0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 5,\n" +
            "\"Y\" : 2,\n" +
            "\"bestsolution\" : 61,\n" +
            "\"name\" : \"coton 05\"\n" +
            "}";


    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        bestScores = BestScores.getInstance(appContext);
        state = new State(coton05);
        bestScores.deleteData(state.getLevelName());
    }

    @Test
    public void testInsert() throws Exception {
        bestScores.insertData(state.getLevelName(), 45);
        assertThat(bestScores.getBest(state.getLevelName()), is(45));
        bestScores.deleteData(state.getLevelName());
    }

    @Test
    public void testUpdate() throws Exception {
        bestScores.insertData(state.getLevelName(), 123);
        bestScores.updateData(state.getLevelName(), 13);
        assertThat(bestScores.getBest(state.getLevelName()), is(13));
        bestScores.deleteData(state.getLevelName());
    }

}
