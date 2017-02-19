package eu.yalacirodev.numberbox;

/**
 * Created by nico on 12/02/17.
 */


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/*
    This class provides a solver to check whether an instance of class State has a solution. If
    *solve* does find a solution, it can be retrieved as object Stack<States>. At the top of the
    stack, there is (a copy) of the initial state, passed in parameter at the instanciation. At the
    bottom of the stack, there is a winning situation.

    Solver can be instantiated with an int parameter *thoroughness* whose value
    indicates the maximum number of candidates to consider at each round.

    HOWEVER:
    If *solve* returns true, a solution has been found. BUT IT NEEDS NOT BE OPTIMAL!
    If *solve* returns false, THERE MIGHT STILL BE A SOLUTION.

    For a complete and optimal search, Solver must be instantiated
    with the parameter *thoroughness* set to Solver.EXHAUSTIVE.
 */
class Solver {

    static final int EXHAUSTIVE = -13;
    static final int ITERATIVE = -27;


    private static final boolean VERBOSE = true;

    private static final int[] ACTIONS = {
            State.PUSH_NORTH,
            State.PUSH_SOUTH,
            State.PUSH_EAST,
            State.PUSH_WEST,
            State.JUMP_NORTH,
            State.JUMP_SOUTH,
            State.JUMP_EAST,
            State.JUMP_WEST
    };


    private static final String STATE_KEY_VSEP = "";
    private static final String STATE_KEY_HSEP = "";
    private static final String STATE_KEY_POSPLAYER = "@";
    private static final String STATE_KEY_POSGENERIC = "";
    private static final String STATE_KEY_NEWROW = "";


    private class StateCouple {

        State state;
        State from;

        StateCouple(State state, State from) {
            this.state = state;
            this.from = from;
        }

    }

    private class StateHeightComparator implements Comparator<State> { // FIXME


        // SECOND HEURISTIC
        @Override
        public int compare(State s1, State s2) {
            int heightSum1 = 0;
            int heightSum2 = 0;
            for (int i = 0; i < s1.getSizeX() ; i++) {
                for (int j = 0 ; j < s1.getSizeY() ; j++) {
                    if (s1.getHeight(i,j) > 3) heightSum1 += s1.getHeight(i,j);
                    if (s2.getHeight(i,j) > 3) heightSum2 += s2.getHeight(i,j);
                }
            }
            if (heightSum1 == heightSum2) {
                return 0;
            }
            if (heightSum1 > heightSum2) {
                return 1;
            } else {
                return -1;
            }

        }


    /*
        // FIRST HEURISTIC
        @Override
        public int compare(State s1, State s2) {
            int heighestIn1 = 0;
            int heighestIn2 = 0;
            for (int i = 0 ; i < s1.getSizeX() ; i++) {
                for (int j = 0 ; j < s1.getSizeY() ; j++) {
                    int ins1 = s1.getHeight(i,j);
                    int ins2 = s2.getHeight(i,j);
                    if (ins1 > heighestIn1) {
                        heighestIn1 = ins1;
                    }
                    if (ins2 > heighestIn2) {
                        heighestIn2 = ins2;
                    }
                }
            }
            if (heighestIn1 == heighestIn2) {
                return 0;
            }
            if (heighestIn1 > heighestIn2) {
                return 1;
            } else {
                return -1;

            }
        }
        */

    } // FIXME




    // we maintain a map of StateCouple sc:
    // 1. sc.state will record an already visited state.
    // This is to avoid redoing computations.
    // 2. sc.from will record the state we came from
    // This is to allow the recovering of the history to the solution.
    private HashMap<String, StateCouple> map;

    private State initial;

    private Stack<State> solution;

    private int maxCandidatesPerRound;

    private StateHeightComparator fromLowestToHighest; // FIXME


    Solver(State s, int thoroughness) {
        map = new HashMap<>();
        initial = new State(s);
        solution = null;
        maxCandidatesPerRound = thoroughness;
        fromLowestToHighest = new StateHeightComparator(); // FIXME
    }

    boolean solve() {
        if (initial.isSurelyLoosing()) {
            if (VERBOSE) { System.out.println("initial state is already surely losing"); }
            return false;
        }
        LinkedList<State> start = new LinkedList<>();
        map.put(key(initial), new StateCouple(initial, null));
        start.add(initial);
        if (maxCandidatesPerRound == EXHAUSTIVE || maxCandidatesPerRound >= 0) {
            return solve(start);
        } else {
            maxCandidatesPerRound = 2;
            while(!solve(start)) {
                maxCandidatesPerRound *= 2;
                if (VERBOSE) { System.out.println("ITERATIVE : trying with thoroughness " + maxCandidatesPerRound); }
                map = new HashMap<>();
                map.put(key(initial), new StateCouple(initial, null));
            }
            return true;
        }
    }

    private static String key(State s) {
        return s.toString(
                STATE_KEY_VSEP,
                STATE_KEY_HSEP,
                STATE_KEY_POSPLAYER,
                STATE_KEY_POSGENERIC,
                STATE_KEY_NEWROW);
    }

    private Stack<State> getHistoryTo(State s) {
        Stack<State> stack = new Stack<>();
        StateCouple sc = map.get(key(s));
        while (true) {
            stack.push(sc.state);
            if (sc.from != null) {
                sc = map.get(key(sc.from));
            } else {
                return stack;
            }
        }
    }

    Stack<State> getSolution() {
        return solution;
    }

    void printSolution() {
        if (getSolution() == null) {
            if (VERBOSE) { System.out.println("no solution found (yet?)"); }
            return;
        }
        Stack<State> stack = new Stack<>();
        stack.addAll(getSolution());
        while (!stack.empty()) {
            System.out.println(stack.pop().toString());
        }
    }

    /*
        Returns true when a solution is found from one of the states in candidates.
     */
    private boolean solve(LinkedList<State> candidates) {
        if (VERBOSE) { System.out.println("#candidates " + candidates.size()); }
        if (candidates.isEmpty()) {
            if (VERBOSE) { System.out.println("no solution found"); }
            return false;
        }

        LinkedList<State> newCandidates = new LinkedList<>();
        int numCandidates = 0; // FIXME

        outerLoop:
        for (State s : candidates) {
            if (s.isWinning()) {
                if (VERBOSE) { System.out.println("solution found"); }
                solution = getHistoryTo(s);
                return true;
            }
            for (int a : ACTIONS) {
                State newS = new State(s);
                newS.doAction(a);
                if (map.get(key(newS)) == null && !newS.isSurelyLoosing()) {
                    // newS is not present in the map and it is not obviously loosing
                    map.put(key(newS), new StateCouple(newS, s));
                    newCandidates.add(newS);
                    numCandidates++; // FIXME
                    if (maxCandidatesPerRound != EXHAUSTIVE
                            && numCandidates >= maxCandidatesPerRound) {
                        break outerLoop; // FIXME
                    }
                }
            }
        }
        Collections.sort(newCandidates, fromLowestToHighest); // FIXME MAKE add USE Comparator instead
        return solve(newCandidates);
    }

} // Solver
