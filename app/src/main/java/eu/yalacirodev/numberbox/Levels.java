package eu.yalacirodev.numberbox;


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nico on 10/02/17.
 */


class Levels {

    // levels filenames as they are in the assets directory
    private static String[] LEVELS_FILENAMES = {
            "basic00.nbj", // 2 moves
            "basic01.nbj", // 12 moves
            "basic02.nbj", // 16 moves
            "basic03.nbj", // 30 moves
            "coton00.nbj", // 28 moves
            "coton01.nbj", // 28 moves
            "coton02.nbj", // 35 moves
            "coton03.nbj", // EASY PEASY 41 moves
            "coton04.nbj", // 33 moves
            "coton06.nbj", // EASY best 60 moves (better solution might exist)
            "coton07.nbj", // 31 moves
            "coton08.nbj", // EASY 21 moves
            "coton09.nbj", // 32 moves
            "coton10.nbj", // 43 moves
            "coton11.nbj", // 50 moves
            "coton05.nbj", // HARD 61 moves
            "dashpot00.nbj",
            "dashpot01.nbj",
            "dashpot02.nbj",
            "full00.nbj",
            "full01.nbj",
            "full02.nbj",
            "full03.nbj",
            "full04.nbj",
            "full05.nbj"
    };

    private static final int DEFAULT_BEST = 1017;

    private static Levels instance = null;
    State[] levels;
    int[] bests;

    private Levels() {
        // defeat instantiation
    }

    static Levels getInstance(Context context) {
        if (instance == null) {
            instance = new Levels();
            instance.levels = new State[LEVELS_FILENAMES.length];
            instance.bests = new int[LEVELS_FILENAMES.length];
            for (int i = 0 ; i < LEVELS_FILENAMES.length ; i++) {
                instance.levels[i] = getStateFromJSONFile(LEVELS_FILENAMES[i], context);
                instance.bests[i] = getBestFromJSONFile(LEVELS_FILENAMES[i], context);
            }
        }
        return instance;
    }

    private static State getStateFromJSONFile(String filename, Context context) {

        String JSONString = null;

        try {
            InputStream input = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            reader.close();
            input.close();
            JSONString = sb.toString();
        } catch (IOException e) {
            System.out.println("IO: Something wrong with " + filename);
        }

        try {
            return new State(JSONString);
        } catch (JSONException e) {
            System.out.println("JSON: Something wrong with " + filename);
        }

        return null;
    }

    private static int getBestFromJSONFile(String filename, Context context) {

        String JSONString = null;

        try {
            InputStream input = context.getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            reader.close();
            input.close();
            JSONString = sb.toString();
        } catch (IOException e) {
            System.out.println("IO: Something wrong with " + filename);
        }

        try {
            JSONObject jo = new JSONObject(JSONString);
            if (jo.has("bestsolution")) {
                return jo.getInt("bestsolution");
            } else {
                return jo.getInt("bestknownsolution");
            }
        } catch (JSONException e) {
            System.out.println("JSON: Something wrong with " + filename);
        }

        return DEFAULT_BEST;
    }

}
