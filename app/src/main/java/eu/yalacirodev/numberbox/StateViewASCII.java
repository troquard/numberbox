package eu.yalacirodev.numberbox;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by nico on 08/02/17.
 */

public class StateViewASCII extends android.support.v7.widget.AppCompatTextView implements StateView {

    public StateViewASCII(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showState (State state) {
        int sizeX = state.getSizeX();
        int sizeY = state.getSizeY();
        int textSize = 30 - (int) Math.round(Math.max(sizeX,sizeY) * 1.5);
        // this is a very cheap way of adapting the size of a displayed level
        // and the results will be mediocre, especially accross different screen sizes
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        this.setText(state.toString());
    }

}