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

    private static class Manhattan implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (o1.getCost() + h(o1)) - (o2.getCost() + h(o2));
        }
    }

    // Expand function
    public List<Node> expand(Node node) {
        int row = node.getMissingTileRow(), col = node.getMissingTileCol();
        List<Node> list = new ArrayList<>();
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
        return list;
    }
    public void breadthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Queue<Node> frontier = new LinkedList<>();
        Hashtable<Integer, Node> reached = new Hashtable<>();
        frontier.add(root);
        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            reached.put(node.hashCode(), node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return;
            }
            for (Node i : expand(node)) {
                if ((!(frontier.contains(i))) && !(reached.containsKey(i.hashCode()))) {
                    frontier.add(i);
                    size += 1;
//                    System.out.println(i);
                }
            }
        }
    }
    public void depthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Stack<Node> frontier = new Stack<>();
        Hashtable<Integer, Node> reached = new Hashtable<>();
        frontier.push(root);
        while (!frontier.isEmpty()) {
            Node node = frontier.pop();
            reached.put(node.hashCode(), node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return;
            }
            for (Node i : expand(node)) {
                if ((!(frontier.contains(i))) && !(reached.containsKey(i.hashCode()))) {
                    frontier.push(i);
//                    System.out.println(i);
                    size += 1;
                }
            }
        }
    }

    public void ASStartManhattan() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        PriorityQueue<Node> frontier = new PriorityQueue<>(new Manhattan());
        Hashtable<Integer, Node> reached = new Hashtable<>();
        frontier.add(root);
        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            reached.put(node.hashCode(), node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return;
            }
            for (Node i : expand(node)) {
                if ((!(frontier.contains(i))) && !(reached.contains(i))) {
                    frontier.add(i);
//                    System.out.println(i);
                    size += 1;
                }
            }
        }
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
