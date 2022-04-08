package puzzle8;

import java.util.ArrayList;
import java.util.List;

/*
 * Our node for the board needs
 * - a parent
 * - a state (a description of this state as a string for the hash table)
 * - children
 * - position of missing tile
 * - Action taken to reach this node
 * */
public class Node {
    // Data fields
    private Node parent;
    private List<Node> children;
    private int[][] state;
    private String stringState;
    private Action direction;
    private int depth = 0, missingTileRow, missingTileCol, cost;

    // Constructor
    public Node(int[][] state) {
        this.state = state;
        this.stringState = createStringBoard();
        this.parent = null;
        this.direction = null;
        this.children = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    missingTileRow = i;
                    missingTileCol = j;
                    break;
                }
            }
        }
    }

    // Methods
    /*بتحول ال matrix ل string عشان المقرنة بعدين*/
    public String createStringBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                sb.append(this.state[i][j]);
        }
        return sb.toString();
    }

    // Add child function which will act as a helper function later
    /*داله مساعدة عشان لما نيجي تعمل ال child ل ال node*/
    public void addChild(Node child) {
        child.setParent(this);
        child.setDepth(this.depth + 1);
        child.setCost(this.cost + 1);
        this.children.add(child);
    }

    /*
     * Create a child which is the main way that we will use to actually add children with a state
     * a,b represent new position of missing tile
     * It returns a node which will be used later in expand fn
     * */
    /*
    * ال a و ال b مكان الصفرة الجديدة
    * بترجع node عشان نقدر نستعملها في ال expand */
    public Node createChild(int a, int b) {
        int[][] placeholder = new int[3][3];
        // copying the state matrix into the placeholder and switching the missing tile
        for (int i = 0; i < 3; i++)
            System.arraycopy(this.state[i], 0, placeholder[i], 0, 3);
        placeholder[missingTileRow][missingTileCol] = placeholder[a][b];
        placeholder[a][b] = 0;
        Node child = new Node(placeholder);
        child.setCost(1);
        this.addChild(child); // using the helper fn created earlier
        return child;
    }

    /*بترجع لنا مكان قيمة معينه في ال matrix
    * المكان بيرجع علي هيئة array
    * 0 -> س
    * 1 -> ص*/
    public int[] getRowCol(int value) {
        int[] container = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == value) {
                    container[0] = i;
                    container[1] = j;
                    break;
                }
            }
        }
        return container;
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

    /*بيحول ال String ل كود عشان لما تنجي نعمل جدول تحتفظة فيه بال node اللي شوفناها قبل كدة*/
    @Override
    public int hashCode() {
        return this.stringState.hashCode();
    }

    // getters
    public String getStringState() {
        return stringState;
    }

    public int getMissingTileRow() {
        return missingTileRow;
    }

    public int getMissingTileCol() {
        return missingTileCol;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    public Node getParent() {
        return parent;
    }

    public Action getDirection() {
        return direction;
    }

    // Setters
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDirection(Action direction) {
        this.direction = direction;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(state[i][j]).append('\t');
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
