package puzzle8;

import java.util.*;

public class Node {
    // Data fields
    private int[][] state; // represents the state of the board in a matrix form
    private Node parent;
    private List<Node> children; // represents the children of the node as an adjacency list
    /**
     * <pre>depth:int</pre> to keep track of the node's depth <br>
     * <pre>missingTileRow:int , missingTileCol: int</pre>
     * to know where empty tile is (represented by a zero in the matrix form)
     */
    private int depth, missingTileRow, missingTileCol, cost, maxCost;
    // indicates the action used to reach the current node state
    private Action action;
    // For comparision in Breadth-First Search, Depth-First Search & A* (Manhatten distance)
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
    public void addChild(Node child) {
        child.setParent(this);
        child.setDepth(this.getDepth() + 1);
        child.setMaxCost(child.getCost());
        this.children.add(child);
    }

    public Node createChild(int a, int b) {
        int[][] t = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                t[i][j] = state[i][j];
            }
        }
        t[missingTileRow][missingTileCol] = t[a][b];
        // might be wrong but leave as is for now.
        int cost = state[a][b];
        t[a][b] = 0;
        Node child = new Node(t);
        child.setCost(cost);
        addChild(child);
        return child;
    }

    @Override
    //Hashcode generated from String version of board
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getStringState().hashCode();
        return result;
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
    public void setState(int[][] state) {
        this.state = state;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setMissingTileCol(int missingTileCol) {
        this.missingTileCol = missingTileCol;
    }

    public void setMissingTileRow(int missingTileRow) {
        this.missingTileRow = missingTileRow;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = this.parent.getMaxCost() + maxCost;
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

    public int[][] getState() {
        return state;
    }
    public int getRow(int value){
        for(int i = 0; i < 3;i++){
            for(int j = 0; j < 3;j++){
                if(state[i][j] == value){
                    return i;
                }
            }
        }
        return  -1;
    }
    public int getCol(int value){
        for(int i = 0;i < 3;i++){
            for(int j = 0;j < 3;j++){
                if(state[i][j] == value)
                    return j;
            }
        }
        return -1;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public int getCost() {
        return cost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public String getStringState() {
        return stringState;
    }
    /*@Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 3;i++){
            for(int j = 0; j < 3;j++){
                sb.append(this.state[i][j]);
                if(j != 2) sb.append("\t");
            }
            sb.append("\n");
        }
        sb.append("Action taken : ");
        return sb.toString();
    }*/
}
