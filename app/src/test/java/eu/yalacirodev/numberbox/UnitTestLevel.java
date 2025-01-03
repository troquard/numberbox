package eu.yalacirodev.numberbox;

import org.junit.Test;

/**
 * Created by nico on 12/02/17.
 */

public class UnitTestLevel {

    private static final int[][] levelM =
            {       {0,1,2,0,1,0},
                    {0,3,0,0,2,0},
                    {0,3,4,2,0,0},
                    {0,0,0,0,0,0}};
    private static final int levelX = 5;
    private static final int levelY = 2;


    String jsonMXY = "{\"matrix\" : [[0,1,2,0,1,0],[0,3,0,0,2,0],[0,3,4,2,0,0],[0,0,0,0,0,0]],\"X\" : 5,\"Y\" : 2}";

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
            "\"name\" : \"coton05\"\n" +
            "}";

    // too big to solve at the moment 2017-02-13
    String coton06 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,2,0,1,0],\n" +
            "[0,1,3,0,2,0],\n" +
            "[0,2,4,2,0,0],\n" +
            "[0,0,0,0,0,0],\n" +
            "[0,0,0,0,0,0],\n" +
            "[0,0,2,3,0,0],\n" +
            "[0,0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 5,\n" +
            "\"Y\" : 2,\n" +
            "\"bestknownsolution\" : 60,\n" +
            "\"name\" : \"coton06\"\n" +
            "}";


    String dashpot00 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,0,0],\n" +
            "[0,3,1,2,0],\n" +
            "[0,1,2,0,0],\n" +
            "[0,3,1,3,0],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"dashpot00\"\n" +
            "}";


    String dashpot01 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,0,0],\n" +
            "[0,3,1,3,0],\n" +
            "[0,1,2,0,0],\n" +
            "[0,3,1,3,0],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"dashpot01\"\n" +
            "}";

    String dashpot02 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,0,0],\n" +
            "[0,3,1,2,0],\n" +
            "[0,3,2,3,0],\n" +
            "[0,2,1,3,0],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"dashpot02\"\n" +
            "}";

    String candidate_dashpot03 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,0,0],\n" +
            "[0,3,1,1,0],\n" +
            "[0,4,3,2,0],\n" +
            "[0,2,1,4,0],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"candidate_dashpot03\"\n" +
            "}";


    String full00 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,0,0],\n" +
            "[0,3,1,4,2],\n" +
            "[0,3,2,3,0],\n" +
            "[0,0,2,0,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"full00\"\n" +
            "}";


    String full01 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,0,2,0],\n" +
            "[0,4,1,3,2],\n" +
            "[0,3,2,1,0],\n" +
            "[0,0,0,2,0]\n" +
            "],\n" +
            "\"X\" : 0,\n" +
            "\"Y\" : 0,\n" +
            "\"name\" : \"full01\"\n" +
            "}";

    String full02 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,1,0,0],\n" +
            "[2,1,2,2,1],\n" +
            "[0,4,3,1,0],\n" +
            "[2,0,1,3,1],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 2,\n" +
            "\"Y\" : 2,\n" +
            "\"name\" : \"full02\"\n" +
            "}";

    String full03 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,1,0,0],\n" +
            "[2,1,2,3,1],\n" +
            "[0,3,4,1,0],\n" +
            "[2,0,1,4,0],\n" +
            "[0,0,0,0,0]\n" +
            "],\n" +
            "\"X\" : 2,\n" +
            "\"Y\" : 1,\n" +
            "\"name\" : \"full03\"\n" +
            "}";

    String full04 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,0,1,0,0],\n" +
            "[1,2,3,2,1],\n" +
            "[0,4,1,0,0],\n" +
            "[2,3,2,1,0],\n" +
            "[0,2,0,0,0]\n" +
            "],\n" +
            "\"X\" : 3,\n" +
            "\"Y\" : 2,\n" +
            "\"name\" : \"full04\"\n" +
            "}";

    String full05 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,1,0,0],\n" +
            "[1,3,1,2,1],\n" +
            "[0,1,1,0,0],\n" +
            "[2,3,2,4,0],\n" +
            "[0,2,0,0,0]\n" +
            "],\n" +
            "\"X\" : 3,\n" +
            "\"Y\" : 2,\n" +
            "\"name\" : \"full05\"\n" +
            "}";

    String testy01 = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,2,0,2,0,0],\n" +
            "[0,1,3,0,3,0,0],\n" +
            "[0,0,4,2,0,0,1],\n" +
            "[2,0,0,0,0,0,0],\n" +
            "[0,3,0,0,0,3,0],\n" +
            "[0,4,2,3,4,0,2],\n" +
            "[0,0,0,0,0,2,0]\n" +
            "],\n" +
            "\"X\" : 2,\n" +
            "\"Y\" : 2,\n" +
            "\"bestknownsolution\" : 169,\n" + // thoroughness 4600 + heuristic 2
            "\"name\" : \"testy01\"\n" +
            "}";

    String testy = "{\n" +
            "\"matrix\" : [\n" +
            "[0,1,2,0,2,0,0],\n" +
            "[0,1,3,0,3,0,0],\n" +
            "[0,0,4,0,0,0,1],\n" +
            "[0,0,5,0,2,3,0],\n" +
            "[0,1,0,0,0,4,0],\n" +
            "[0,3,4,4,2,0,2],\n" +
            "[1,0,0,1,0,0,0]\n" +
            "],\n" +
            "\"X\" : 2,\n" +
            "\"Y\" : 2,\n" +
            "\"bestknownsolution\" : \"?\",\n" + // thoroughness ?
            "\"name\" : \"testy\"\n" +
            "}";


    //int MAX_CANDIDATES_PER_ROUND = Solver.ITERATIVE;
    int MAX_CANDIDATES_PER_ROUND = 16100;
    String json1 = coton05;
    String json2 = full04;
    String json3 = full05;
    String json4 = coton06;


    @Test
    public void hasSolution_json1() throws Exception {
        State level = new State(json1);
        Solver solver = new Solver(level, MAX_CANDIDATES_PER_ROUND);
        assert(solver.solve());
        solver.printSolution();
        System.out.println("#MOVES " + (solver.getSolution().size() - 1));
    }

    //@Test
    public void hasSolution_json2() throws Exception {
        State level = new State(json2);
        Solver solver = new Solver(level, MAX_CANDIDATES_PER_ROUND);
        assert(solver.solve());
        solver.printSolution();
        System.out.println("#MOVES " + (solver.getSolution().size() - 1));
    }

    //@Test
    public void hasSolution_json3() throws Exception {
        State level = new State(json3);
        Solver solver = new Solver(level, MAX_CANDIDATES_PER_ROUND);
        assert(solver.solve());
        //solver.printSolution();
        System.out.println("#MOVES " + (solver.getSolution().size() - 1));
    }

    //@Test
    public void hasSolution_json4() throws Exception {
        State level = new State(json4);
        Solver solver = new Solver(level, MAX_CANDIDATES_PER_ROUND);
        assert(solver.solve());
        //solver.printSolution();
        System.out.println("#MOVES " + (solver.getSolution().size() - 1));
    }

}
