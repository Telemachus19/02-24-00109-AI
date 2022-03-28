package puzzle8;

import java.util.*;

/**
 * @author telemachus19 😊
 */

public class Tree {
    // Data fields
    Node root;

    // Constructor
    public Tree(int[][] initialState) {
        this.root = new Node(initialState);
    }

    private static class Manhatten implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2)
        {
            return h(o1) - h(o2);
        }
    }

    // Expand function
    public List<Node> expand(Node node) {
        int row = node.getMissingTileRow(), col = node.getMissingTileCol();
        List<Node> list = new ArrayList<>();
        // Up Action
        if (row != 0) {
            Node upNode = node.createChild(row - 1, col);
            upNode.setAction(Action.Up);
            list.add(upNode);
        }
        // Down Action
        if (row != 2) {
            Node downNode = node.createChild(row + 1, col);
            downNode.setAction(Action.Down);
            list.add(downNode);
        }
        // Left Action
        if (col != 0) {
            Node leftNode = node.createChild(row, col - 1);
            leftNode.setAction(Action.Left);
            list.add(leftNode);
        }
        // Right Action
        if (col != 2) {
            Node rightNode = node.createChild(row, col + 1);
            rightNode.setAction(Action.Right);
            list.add(rightNode);
        }
        return list;
    }

    public boolean breadthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Queue<Node> frontier = new LinkedList<>();
        Set<Node> reached = new HashSet<>();
        Node node = root;
        if (node.isGoal()) return true;
        frontier.add(node);
        while (!frontier.isEmpty()) {
            node = frontier.poll();
            reached.add(node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            List<Node> expansion = expand(node);
            for (Node i : expansion) {
                boolean isFound = reached.contains(i) && frontier.contains(i);
                if (!isFound) {
                    frontier.add(i);
                    size += 1;
                }
            }
        }
        return false;
    }

    public boolean depthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Stack<Node> frontier = new Stack<>();
        //HashMap<Integer, Node> reached = new HashMap<>();
        Node node = root;
        Set<Node> reached = new HashSet<>();
        if (node.isGoal()) return true;
        frontier.push(node);
        while (!frontier.isEmpty()) {
            node = frontier.pop();
            reached.add(node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            List<Node> expansion = expand(node);
            for (Node i : expansion) {
                boolean isFound = reached.contains(i);
                if (!isFound) {
                    if (!(frontier.contains(i))) {
                        frontier.push(i);
                        size += 1;
                    }
                }
            }
        }
        return false;
    }
    public boolean AStartManhatten() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Queue<Node> frontier = new PriorityQueue<>(new Manhatten());
        Set<Node> reached = new HashSet<>();
        Node node = root;
        if (node.isGoal()) return true;
        frontier.add(node);
        while (!frontier.isEmpty()) {
            node = frontier.poll();
            reached.add(node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            List<Node> expansion = expand(node);
            for (Node i : expansion) {
                boolean isFound = reached.contains(i) && frontier.contains(i);
                if (!isFound) {
                    frontier.add(i);
                    size += 1;
                }
            }
        }
        return false;
    }

    public static int h(Node n) {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        int h = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                h += Math.abs(i - n.getRow(goalState[i][j])) + (j - n.getCol(goalState[i][j]));
            }
        }
        return h;
    }
}
