package puzzle8;

import java.util.*;

public class Node {
    // Data fields
    private final int[][] state; // represents the state of the board in a matrix form
    private Node parent;
    private final List<Node> children; // represents the children of the node as an adjacency list
    /**
     * <pre>depth:int</pre> to keep track of the node's depth <br>
     * <pre>missingTileRow:int , missingTileCol: int</pre>
     * to know where empty tile is (represented by a zero in the matrix form)
     */
    private int depth = 0, missingTileRow, missingTileCol, cost = 0;
    // indicates the action used to reach the current node state
    private Action action;
    // For comparison in Breadth-First Search, Depth-First Search & A* (Manhattan distance)
    String stringState;

    // Constructor
    public Node(int[][] state) {
        this.state = state;
        this.parent = null;
        this.children = new ArrayList<>();
        this.action = null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    this.missingTileRow = i;
                    this.missingTileCol = j;
                    break;
                }
            }
        }
        this.stringState = createStringBoard();
    }

    // Methods
    public void addChild(Node child) { // helper function
        child.setParent(this);
        child.setDepth(this.getDepth() + 1);
        child.setCost(this.getCost() + 1);
        this.children.add(child);
    }

    public Node createChild(int a, int b) {
        int[][] t = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, t[i], 0, 3);
        }
        t[missingTileRow][missingTileCol] = t[a][b];
        // might be wrong but leave as is for now.
        // int cost = state[a][b];
        t[a][b] = 0;
        Node child = new Node(t);
        child.setCost(1);
        addChild(child);
        return child;
    }

    @Override
    //Hashcode generated from String version of board
    public int hashCode() {
        //int result = 17;
        return this.stringState.hashCode();
    }

    public String createStringBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(state[i][j]);
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node checker)) {
            return false;
        }
        return checker.getStringState().equals(this.getStringState());
    }

    public boolean isGoal() {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        Node GoalState = new Node(goalState);
        return this.equals(GoalState);
    }

    // Setters
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    // Getters
    public Action getAction() {
        return action;
    }

    public int getDepth() {
        return depth;
    }

    public int getMissingTileCol() {
        return missingTileCol;
    }

    public int getMissingTileRow() {
        return missingTileRow;
    }

    public int getRow(int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == value) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getCol(int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == value)
                    return j;
            }
        }
        return -1;
    }


    public Node getParent() {
        return parent;
    }

    public int getCost() {
        return cost;
    }

    public String getStringState() {
        return stringState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(this.state[i][j]);
                if (j != 2) sb.append("\t");
            }
            sb.append("\n");
        }
        sb.append("Depth : ").append(this.depth).append("\n");
        return sb.toString();
    }
}
