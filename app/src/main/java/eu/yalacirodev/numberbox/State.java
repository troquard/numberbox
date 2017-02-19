package eu.yalacirodev.numberbox;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nico on 08/02/17.
 */

class State implements Serializable {

    static final int PUSH_NORTH = 0;
    static final int PUSH_SOUTH = 1;
    static final int PUSH_EAST = 2;
    static final int PUSH_WEST = 3;
    static final int JUMP_NORTH = 4;
    static final int JUMP_SOUTH = 5;
    static final int JUMP_EAST = 6;
    static final int JUMP_WEST = 7;

    private int[][] matrix;
    private int posX;
    private int posY;

    State(int[][] matrix, int posX, int posY) {
        this.matrix = matrix;
        this.posX = posX;
        this.posY = posY;
    }

    /*
       Getting a copy of state.
     */
    State(State state) {
        this.matrix = new int[state.getSizeY()][state.getSizeX()];
        for (int i = 0 ; i < state.getSizeX() ; i++) {
            for (int j = 0; j < state.getSizeY() ; j++) {
                this.matrix[j][i] = state.matrix[j][i];
            }
        }
        this.posX = state.posX;
        this.posY = state.posY;
    }

    /*
       Getting a State from a JSON string.

       The parameter must be a JSON string. E.g.,

      "{
        \"matrix\" : [
        [0,1,2,0,1,0],
        [0,3,0,0,2,0],
        [0,3,4,2,0,0],
        [0,0,0,0,0,0]
        ],
        \"X\" : 5,
        \"Y\" : 2
        }"

     */
    State(String JSONString) throws JSONException {

        JSONObject jo = new JSONObject(JSONString);

        // recovering the level matrix
        JSONArray jmatrix = jo.getJSONArray("matrix");
        int[][] matrix = new int[jmatrix.length()][];
        for (int i = 0; i < jmatrix.length(); i++) {
            JSONArray row = jmatrix.getJSONArray(i);
            matrix[i] = new int[row.length()];
            for (int j = 0; j < row.length(); j++) {
                matrix[i][j] = row.getInt(j);
            }
        }

        this.matrix = matrix;
        this.posX = jo.getInt("X");
        this.posY = jo.getInt("Y");

    }

    int getSizeX() {
        return this.matrix[0].length;
    }

    int getSizeY() {
        return this.matrix.length;
    }

    int getPosX() {
        return this.posX;
    }

    int getPosY() {
        return this.posY;
    }

    int getHeight(int x, int y) {
        return this.matrix[y][x];
    }

    private void setPosX(int x) {
        this.posX = x;
    }

    private void setPosY(int y) {
        this.posY = y;
    }

    private void setHeight(int x, int y, int h) {
        this.matrix[y][x] = h;
    }


    private boolean jump(int x, int y) {
        if (Math.abs(x) + Math.abs(y) != 1) {
            return false;
        }
        if(getPosX() + x >= getSizeX()) {
            return false;
        }
        if(getPosX() + x < 0) {
            return false;
        }
        if(getPosY() + y >= getSizeY()) {
            return false;
        }
        if(getPosY() + y < 0) {
            return false;
        }
        int currentHeight = getHeight(getPosX(), getPosY());
        int destinationHeight = getHeight(getPosX() + x, getPosY() + y);
        if (destinationHeight > currentHeight + 1) {
            return false;
        }
        setPosX(getPosX() + x);
        setPosY(getPosY() + y);

        return true;
    }

    private boolean push(int x, int y) {
        if (Math.abs(x) + Math.abs(y) != 1) {
            return false;
        }
        if(getPosX() + x >= getSizeX()) {
            return false;
        }
        if(getPosX() + x < 0) {
            return false;
        }
        if(getPosY() + y >= getSizeY()) {
            return false;
        }
        if(getPosY() + y < 0) {
            return false;
        }

        int currentHeight = getHeight(getPosX(), getPosY());
        int destinationHeight = getHeight(getPosX() + x, getPosY() + y);


        if (destinationHeight > currentHeight + 1) {
            return false;
        }
        if (destinationHeight == currentHeight) {
            setPosX(getPosX() + x);
            setPosY(getPosY() + y);
            return true;
        }
        if (destinationHeight == currentHeight + 1) {
            if(getPosX() + 2*x >= getSizeX()) {
                return false;
            }
            if(getPosX() + 2*x < 0) {
                return false;
            }
            if(getPosY() + 2*y >= getSizeY()) {
                return false;
            }
            if(getPosY() + 2*y < 0) {
                return false;
            }

            int behindHeight = getHeight(getPosX() + 2*x, getPosY() + 2*y);

            if (destinationHeight <= behindHeight) {
                return false;
            }
            if (destinationHeight > behindHeight) {
                setHeight(getPosX() + x, getPosY() + y, destinationHeight - 1);
                setHeight(getPosX() + 2*x, getPosY() + 2*y, behindHeight + 1);
                setPosX(getPosX() + x);
                setPosY(getPosY() + y);
                return true;
            }


            return true;
        }
        if (destinationHeight <= currentHeight) {
            setPosX(getPosX() + x);
            setPosY(getPosY() + y);
            return true;
        }


        return true;
    }

    void doAction(int move) {
        if (isWinning()) {
            // no further action allowed in a winning state
            return;
        }
        switch(move) {
            case JUMP_SOUTH: {
                jump(0,1);
                break;
            }
            case JUMP_NORTH: {
                jump(0,-1);
                break;
            }
            case JUMP_WEST: {
                jump(-1,0);
                break;
            }
            case JUMP_EAST: {
                jump(1,0);
                break;
            }
            case PUSH_SOUTH: {
                push(0,1);
                break;
            }
            case PUSH_NORTH: {
                push(0,-1);
                break;
            }
            case PUSH_WEST: {
                push(-1,0);
                break;
            }
            case PUSH_EAST: {
                push(1,0);
                break;
            }
        }
    }


    boolean isWinning() {
        for (int i = 0; i < getSizeX() ; i++) {
            for (int j = 0 ; j < getSizeY() ; j++) {
                if (getHeight(i,j) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
        This function implements a partial algorithm to detect losing states.
        If it returns true, the state is losing.
        If it returns false, the state still might be losing.

        To simplify the logic, it catches ArrayIndexOutOfBoundsException but does not do anything.
     */
    boolean isSurelyLoosing() {

        // A GLOBAL CHECK WHETHER THE HEIGHTS ARE INCONSISTENT WITH A WINNING STATE
        int highest = 0;
        int secondHighest = 0;
        int heightSum = 0;
        for (int i = 0; i < getSizeX() ; i++) {
            for (int j = 0 ; j < getSizeY() ; j++) {
                int height = getHeight(i,j);
                heightSum += height;
                if (height > highest) {
                    secondHighest = highest;
                    highest = height;
                } else if (height > secondHighest) {
                    secondHighest = height;
                }
            }
        }
        // too many boxes to fit flat is loosing -- this is an issue with the level design
        if (heightSum > getSizeX() * getSizeY()) {
            return true;
        }
        // a stack comparatively too high is loosing
        if (highest > secondHighest + 1) {
            return true;
        }

        // SEVERAL LOCAL CHECKS OF STACKS IN A CORNER, ALONG A WALL, OR NEXT TO EACH OTHERS

        // a stack higher than 2 against a wall is losing
        for(int i = 0 ; i < getSizeX() ; i++) {
            if (getHeight(i,0) > 2 || getHeight(i,getSizeY()-1) > 2) {
                return true;
            }
        }
        for(int j = 0 ; j < getSizeY() ; j++) {
            if (getHeight(0,j) > 2 || getHeight(getSizeX()-1,j) > 2) {
                return true;
            }
        }
        // a stack higher than 1 in a corner is loosing
        if (getHeight(0,0) > 1 ||
            getHeight(getSizeX()-1,getSizeY()-1) > 1 ||
            getHeight(0,getSizeY()-1) > 1 ||
            getHeight(getSizeX()-1,0) > 1) {
            return true;
        }

        for (int i = 0 ; i < getSizeX() ; i++) {
            for (int j = 0 ; j < getSizeY() ; j++) {

                if (getHeight(i,j) <= 1) {
                    continue;
                }

                // two stacks higher than 1 next to each other along a wall is loosing
                try {
                    if (i == 0 || i == getSizeX()-1) {
                        if (getHeight(i,j-1) > 1) {
                            return true;
                        }
                        if (getHeight(i,j+1) > 1) {
                            return true;
                        }
                    }
                    if (j == 0 || j == getSizeY()-1) {
                        if (getHeight(i-1,j) > 1) {
                            return true;
                        }
                        if (getHeight(i+1,j) > 1) {
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Doesn't matter! If the stack is an indication of a losing state,
                    // it'll be checked from another position that doesn't raise an exception.
                }

                // a square block of four stacks higher than 1 is loosing
                try {
                    if (getHeight(i+1,j+1) > 1) {
                        if (getHeight(i,j+1) > 1 && getHeight(i+1,j) > 1) {
                            return true;
                        }
                    }
                    if (getHeight(i-1,j-1) > 1) {
                        if (getHeight(i,j-1) > 1 && getHeight(i-1,j) > 1) {
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // Doesn't matter! If the stack is an indication of a losing state,
                    // it'll be checked from another position that doesn't raise an exception.
                }
            }
        }
        return false; // maybe...
    }

    String toString(String vSep, String hSep, String posPlayer, String posGeneric, String newRow) {

        StringBuilder res = new StringBuilder();

        for (int i = 0 ; i < getSizeY() ; i++) {
            for (int j = 0 ; j < getSizeX() ; j++) {
                res.append(vSep);
                res.append(hSep);
            }
            res.append(vSep);
            res.append(newRow);
            for (int j = 0 ; j < getSizeX() ; j++) {
                res.append(vSep);
                if (getPosX() == j && getPosY() == i) {
                    res.append(posPlayer);
                } else {
                    res.append(posGeneric);
                }
                res.append(getHeight(j, i));
            }
            res.append(vSep);
            res.append(newRow);

        }
        for (int j = 0 ; j < getSizeX() ; j++) {
            res.append(vSep);
            res.append(hSep);
        }
        res.append(vSep);

        return res.toString();
    }

    @Override
    public String toString() {
        String vSep = " ";
        String hSep = "  ";
        String posPlayer = "@";
        String posGeneric = " ";
        String newRow = "\n";

        return toString(vSep, hSep, posPlayer, posGeneric, newRow);
    }
}
