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


    public boolean depthFirstSearch() {
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
            return true;
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
                    return true;
                }
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.push(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    public boolean breadthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Queue<Node> frontier = new LinkedList<>();
        HashMap<Integer, Node> reached = new HashMap<>();
        if (root.isGoal()) {
            size++;
            double endTime = System.currentTimeMillis();
            ActionPath path = new ActionPath(root, root);
            path.printPath();
            System.out.println("-----------------------");
            System.out.println("Time: " + (endTime - startTime) + " millie seconds");
            System.out.println("Space: " + size);
            return true;
        }
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            for (Node child : expand(node)) {
                if (child.isGoal()) {
                    double endTime = System.currentTimeMillis();
                    size += 1;
                    ActionPath path = new ActionPath(root, child);
                    path.printPath();
                    System.out.println("-----------------------");
                    System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                    System.out.println("Space: " + size);
                    return true;
                }
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    private int misplacedTiles(Node n) {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        int[][] state = n.getState();
        int h = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != goalState[i][j])
                    h++;
            }
        }
        return h;
    }

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

    // Comparator object for Uniform cost search
    private class f1 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getCost() - o2.getCost();
        }
    }

    private class f2 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return manhattanDistance(o1) - manhattanDistance(o2);
        }
    }

    private class f3 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return euclideanDistance(o1) - euclideanDistance(o2);
        }
    }

    private class f4 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return misplacedTiles(o1) - misplacedTiles(o2);
        }
    }

    // Comparator object for A* - using the manhattan distance heuristic
    private class f5 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (manhattanDistance(o1) + o1.getCost()) - (manhattanDistance(o2) + o2.getCost());
        }
    }

    // Comparator object for A* - using the euclidean distance heuristic
    private class f6 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (euclideanDistance(o1) + o1.getCost()) - (euclideanDistance(o2) + o2.getCost());
        }
    }

    // Comparator object for A* - using the misplaced tiles heuristic
    private class f7 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (misplacedTiles(o1) + o1.getCost()) - (misplacedTiles(o2) + o2.getCost());
        }
    }

    public boolean uniformCostSearch() {
        double startTime = System.currentTimeMillis();
        PriorityQueue<Node> frontier;
        frontier = new PriorityQueue<>(new f1());
        int size = 0;
        HashMap<Integer, Node> reached = new HashMap<>();
        if (root.isGoal()) {
            size++;
            double endTime = System.currentTimeMillis();
            ActionPath path = new ActionPath(root, root);
            path.printPath();
            System.out.println("-----------------------");
            System.out.println("Time: " + (endTime - startTime) + " millie seconds");
            System.out.println("Space: " + size);
            return true;
        }
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("-----------------------");
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            for (Node child : expand(node)) {
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    public boolean bestFirstSearch(int i) {
        double startTime = System.currentTimeMillis();
        PriorityQueue<Node> frontier;
        if (i == 1) {
            frontier = new PriorityQueue<>(new f2());
        } else if (i == 2) {
            frontier = new PriorityQueue<>(new f3());
        } else {
            frontier = new PriorityQueue<>(new f4());
        }
        int size = 0;
        HashMap<Integer, Node> reached = new HashMap<>();
        if (root.isGoal()) {
            size++;
            double endTime = System.currentTimeMillis();
            ActionPath path = new ActionPath(root, root);
            path.printPath();
            System.out.println("-----------------------");
            System.out.println("Time: " + (endTime - startTime) + " millie seconds");
            System.out.println("Space: " + size);
            return true;
        }
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                size += 1;
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("-----------------------");
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            for (Node child : expand(node)) {
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    public boolean aStar(int i) {
        double startTime = System.currentTimeMillis();
        PriorityQueue<Node> frontier;
        if (i == 1) {
            frontier = new PriorityQueue<>(new f5());
        } else if (i == 2) {
            frontier = new PriorityQueue<>(new f6());
        } else {
            frontier = new PriorityQueue<>(new f7());
        }
        int size = 0;
        HashMap<Integer, Node> reached = new HashMap<>();
        if (root.isGoal()) {
            size++;
            double endTime = System.currentTimeMillis();
            ActionPath path = new ActionPath(root, root);
            path.printPath();
            System.out.println("-----------------------");
            System.out.println("Time: " + (endTime - startTime) + " millie seconds");
            System.out.println("Space: " + size);
            return true;
        }
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                size += 1;
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("-----------------------");
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            for (Node child : expand(node)) {
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }
}