package eu.yalacirodev.numberbox;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private StateView myDisplay;
    private State state;
    private int currentLevel;
    private boolean jumpMode;
    private Context context;

    private static final String BUNDLE_CURRENT_LEVEL_KEY = "CURRENT LEVEL KEY";
    private static final String BUNDLE_JUMP_MODE_KEY = "JUMP MODE KEY";
    private static final String BUNDLE_STATE_KEY = "STATE KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        context = this;

        myDisplay = (StateView) findViewById(R.id.my_display);

        // we remember currentLevel and state when the Activity is destroyed
        if (savedInstanceState != null) {
            currentLevel = savedInstanceState.getInt(BUNDLE_CURRENT_LEVEL_KEY);
            jumpMode = savedInstanceState.getBoolean(BUNDLE_JUMP_MODE_KEY);
            state = (State) savedInstanceState.getSerializable(BUNDLE_STATE_KEY);
        }
        else {
            currentLevel = 0;
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

    private int numberOfLevels() {
        return Levels.getInstance(context).levels.length;
    }

    /* UPDATES FOR COMMANDS UI */

    private void updateLevelInfo() {
        TextView textView = (TextView) findViewById(R.id.info);
        if (state.isWinning()) {
            String s = String.format(getResources().getString(R.string.levelcompleted), currentLevel);
            textView.setText(s);
            return;
        }
        if (state.isSurelyLoosing()) {
            String s = String.format(getResources().getString(R.string.levellost), currentLevel);
            textView.setText(s);
            return;
        }
        String s = String.format(getResources().getString(R.string.level), currentLevel);
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

    public void actionEast(View v) {
        if (jumpMode) {
            state.doAction(State.JUMP_EAST);
        } else {
            state.doAction(State.PUSH_EAST);
        }
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionWest(View v) {
        if (jumpMode) {
            state.doAction(State.JUMP_WEST);
        } else {
            state.doAction(State.PUSH_WEST);
        }
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionNorth(View v) {
        if (jumpMode) {
            state.doAction(State.JUMP_NORTH);
        } else {
            state.doAction(State.PUSH_NORTH);
        }
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionSouth(View v) {
        if (jumpMode) {
            state.doAction(State.JUMP_SOUTH);
        } else {
            state.doAction(State.PUSH_SOUTH);
        }
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionRetry(View v) {
        state = getLevelInitialState(currentLevel);
        myDisplay.showState(state);
        updateLevelInfo();
    }

    public void actionNextLevel(View v) {
        currentLevel = (currentLevel + 1) % numberOfLevels();
        updateLevelInfo();
        state = getLevelInitialState(currentLevel);
        myDisplay.showState(state);
        updateLevelInfo();
    }
}
