package eu.yalacirodev.numberbox;

import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private StateView myDisplay;
    private State state;
    private int currentLevel;
    private int numMoves;
    private boolean jumpMode;
    private BestScores bestScores;
    private Context context;

    private static final String BUNDLE_CURRENT_LEVEL_KEY = "CURRENT LEVEL KEY";
    private static final String BUNDLE_NUM_MOVES_KEY = "NUM MOVES KEY";
    private static final String BUNDLE_JUMP_MODE_KEY = "JUMP MODE KEY";
    private static final String BUNDLE_STATE_KEY = "STATE KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        context = this;

        bestScores = BestScores.getInstance(context);

        myDisplay = (StateView) findViewById(R.id.my_display);

        // we remember currentLevel and state when the Activity is destroyed
        if (savedInstanceState != null) {
            currentLevel = savedInstanceState.getInt(BUNDLE_CURRENT_LEVEL_KEY);
            numMoves = savedInstanceState.getInt(BUNDLE_NUM_MOVES_KEY);
            jumpMode = savedInstanceState.getBoolean(BUNDLE_JUMP_MODE_KEY);
            state = (State) savedInstanceState.getSerializable(BUNDLE_STATE_KEY);
        }
        else {
            currentLevel = 0;
            numMoves = 0;
            jumpMode = false;
            state = getLevelInitialState(currentLevel);
        }

        myDisplay.showState(state);

        updateLevelInfo();
        updateColorButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putInt(BUNDLE_CURRENT_LEVEL_KEY, currentLevel);
        b.putInt(BUNDLE_NUM_MOVES_KEY, numMoves);
        b.putBoolean(BUNDLE_JUMP_MODE_KEY, jumpMode);
        b.putSerializable(BUNDLE_STATE_KEY, state);
    }

    /*
    *  HIDE BARS -- IMMERSIVE MODE
    *  See https://developer.android.com/training/system-ui/immersive.html
    *  */


    private void hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    /* LEVELS */

    private State getLevelInitialState(int l) {
        return new State(Levels.getInstance(context).levels[l]);
    }

    private int getLevelBestSolution(int l) {
        return Levels.getInstance(context).bests[l];
    }

    private int numberOfLevels() {
        return Levels.getInstance(context).levels.length;
    }

    /* BEST SCORES MANAGEMENT */

    private void updateScores() {
        if (!state.isWinning()) {
            return;
        }

        int currentBestScore = bestScores.getBest(state.getLevelName());

        if (currentBestScore == BestScores.NOT_COMPLETED)
            bestScores.insertData(state.getLevelName(), numMoves);
        else if (numMoves < currentBestScore)
            bestScores.updateData(state.getLevelName(), numMoves);
    }

    private static final String NO_STARS = "";
    private static final String ONE_STAR = "\u2605";
    private static final String TWO_STARS = "\u2605\u2605";
    private static final String THREE_STARS = "\u2605\u2605\u2605";

    private String getStarsAndScore(int numMoves, int bestSolution) {
        String stars = NO_STARS;
        if (numMoves <= 3 * bestSolution / 2)
            stars = ONE_STAR;
        if (numMoves <= 5 * bestSolution / 4)
            stars = TWO_STARS;
        if (numMoves <= bestSolution)
            stars = THREE_STARS;

        if (numMoves != BestScores.NOT_COMPLETED) {
            return stars + "(" + numMoves + ")";
        } else {
            return "";
        }
    }

    /* UPDATES FOR COMMANDS AND UI */

    private void updateLevelInfo() {
        TextView textView = (TextView) findViewById(R.id.info);
        int bestSolution = getLevelBestSolution(currentLevel);
        if (state.isWinning()) {
            String stars = getStarsAndScore(numMoves, bestSolution);
            String s = String.format(getResources().getString(R.string.levelcompleted), currentLevel);
            s += "\n" + stars;
            textView.setText(s);
            return;
        }
        if (state.isSurelyLoosing()) {
            String s = String.format(getResources().getString(R.string.levellost), currentLevel);
            s += "\n";
            textView.setText(s);
            return;
        }
        String stars = getStarsAndScore(bestScores.getBest(state.getLevelName()), bestSolution);
        String s = String.format(getResources().getString(R.string.level), currentLevel);
        s += "\n" + stars + "\n";
        textView.setText(s);
    }

    private void updateColorButtons() {
        Button j = (Button) findViewById(R.id.jump);
        Button g = (Button) findViewById(R.id.push);
        if (jumpMode) {
            j.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtonActionOn));
            g.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtonActionOff));
        } else {
            j.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtonActionOff));
            g.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtonActionOn));
        }
    }

    /* ACTIONS */

    public void actionPushMode(View v) {
        jumpMode = false;
        updateColorButtons();
    }

    public void actionJumpMode(View v) {
        jumpMode = true;
        updateColorButtons();
    }

    private static final int ACTION_EAST = 0;
    private static final int ACTION_WEST = 1;
    private static final int ACTION_NORTH = 2;
    private static final int ACTION_SOUTH = 3;

    private void actionGenericDirection(int action) {
        if (state.isWinning()) {
            return;
        }

        if (jumpMode) {
            switch (action) {
                case ACTION_EAST: state.doAction(State.JUMP_EAST); break;
                case ACTION_WEST: state.doAction(State.JUMP_WEST); break;
                case ACTION_NORTH: state.doAction(State.JUMP_NORTH); break;
                case ACTION_SOUTH: state.doAction(State.JUMP_SOUTH); break;
            }
        } else {
            switch (action) {
                case ACTION_EAST: state.doAction(State.PUSH_EAST); break;
                case ACTION_WEST: state.doAction(State.PUSH_WEST); break;
                case ACTION_NORTH: state.doAction(State.PUSH_NORTH); break;
                case ACTION_SOUTH: state.doAction(State.PUSH_SOUTH); break;
            }
        }

        myDisplay.showState(state);
        numMoves++;
        updateScores();
        updateLevelInfo();
    }

    public void actionEast(View v) {
        actionGenericDirection(ACTION_EAST);
    }

    public void actionWest(View v) {
        actionGenericDirection(ACTION_WEST);
    }

    public void actionNorth(View v) {
        actionGenericDirection(ACTION_NORTH);
    }

    public void actionSouth(View v) {
        actionGenericDirection(ACTION_SOUTH);
    }

    public void actionRetry(View v) {
        state = getLevelInitialState(currentLevel);
        numMoves = 0;
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionNextLevel(View v) {
        currentLevel = (currentLevel + 1) % numberOfLevels();
        state = getLevelInitialState(currentLevel);
        numMoves = 0;
        myDisplay.showState(state);
        updateLevelInfo();
    }
}
