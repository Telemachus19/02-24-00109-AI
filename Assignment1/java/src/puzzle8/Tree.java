package puzzle8;

import java.util.*;

public class Tree {
    Node root;

    public Tree(int[][] initialState) {
        root = new Node(initialState);
    }

    // Expand function
    public List<Node> expand(Node node) {
        int row = node.getMissingTileRow(), col = node.getMissingTileCol();
        List<Node> list = new ArrayList<>();
        // Left Action
        if (col != 0) {
            Node leftNode = node.createChild(row, col - 1);
            leftNode.setDirection(Action.Left);
            list.add(leftNode);
        }
        // Right Action
        if (col != 2) {
            Node rightNode = node.createChild(row, col + 1);
            rightNode.setDirection(Action.Right);
            list.add(rightNode);
        }
        // Up Action
        if (row != 0) {
            Node upNode = node.createChild(row - 1, col);
            upNode.setDirection(Action.Up);
            list.add(upNode);
        }
        // Down Action
        if (row != 2) {
            Node downNode = node.createChild(row + 1, col);
            downNode.setDirection(Action.Down);
            list.add(downNode);
        }
        return list;
    }

    // TODO: Implement DepthFirstSearch - Abdelrahman

    public void depthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Stack<Node> frontier = new Stack<>();
        HashMap<Integer, Node> reached = new HashMap<>();
        if (root.isGoal()) {
            double endTime = System.currentTimeMillis();
            ActionPath path = new ActionPath(root, root);
            path.printPath();
            System.out.println("-----------------------");
            System.out.println("Time: " + (endTime - startTime) + " millie seconds");
            System.out.println("Space: " + size);
            return;
        }
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.pop();
            for (Node child : expand(node)) {
                if (child.isGoal()) {
                    double endTime = System.currentTimeMillis();
                    size += 1;
                    ActionPath path = new ActionPath(root, child);
                    path.printPath();
                    System.out.println("-----------------------");
                    System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                    System.out.println("Space: " + size);
                    return;
                }
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.push(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
    }
    // TODO: Implement BreadthFirst Search - Tony

    // TODO: Implement A* search - Manhattan heuristic, euclidean distance-
    private int manhattanDistance(Node n) {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        int h = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] container = n.getRowCol(goalState[i][j]);
                h += Math.abs(i - container[0]) + Math.abs(j - container[1]);
            }
        }
        return h;
    }

    private int euclideanDistance(Node n) {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        int h = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] container = n.getRowCol(goalState[i][j]);
                h += Math.sqrt((i - container[0]) * (i - container[0]) + Math.abs(j - container[1]) * Math.abs(j - container[1]));
            }
        }
        return h;
    }

    private class f_1 implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return (manhattanDistance(o1) + o1.getCost()) - (manhattanDistance(o2) + o2.getCost());
        }
    }

    private class f_2 implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return (euclideanDistance(o1) + o1.getCost()) - (euclideanDistance(o2) + o2.getCost());
        }
    }

    public void aStar(int i) {
        double startTime = System.currentTimeMillis();
        PriorityQueue<Node> frontier;
        if (i == 1) {
            frontier = new PriorityQueue<>(new f_1());
        } else {
            frontier = new PriorityQueue<>(new f_2());
        }
        int size = 0;
        HashMap<Integer, Node> explored = new HashMap<>();
        frontier.add(root);
        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            explored.put(node.hashCode(), node);
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("-----------------------");
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return;
            }
            for (Node n : expand(node)) {
                if (!(frontier.contains(n)) && !(explored.containsKey(n.hashCode()))) {
                    frontier.add(n);
                    size += 1;
                }
            }
        }
    }
}
