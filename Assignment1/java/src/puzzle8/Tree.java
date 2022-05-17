package puzzle8;

import java.util.*;

/**
 * Tree data-structure which will be used implement a search tree
 */
public class Tree {
    Node root;

    public Tree(int[][] initialState) {
        root = new Node(initialState);
    }

    /**
     * The expand function expands the nodes and adds the children to the node as well as returns them in a form of list to be used in the Search functions <br>
     * The Priority of the actions are as follows (left,right,up,down)
     *
     * @param node the node which will be expanded
     * @return List that contains the children from the expansion
     */
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

    /**
     * Implements the depth first search, in which we will use graph search where we don't visit the same node twice to prevent loops
     *
     * @return boolean
     */
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
        // if not add it to the frontier and increase the size and put it in the reached nodes
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
        // reaching this means that the search has exhausted all the possible nodes and should return failure.
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    /**
     * Implements the breadth first search with an early goal test.
     * @return true if a solution was found; false if the search exhausted all possible positions
     */
    public boolean breadthFirstSearch() {
        double startTime = System.currentTimeMillis();
        int size = 0;
        Queue<Node> frontier = new LinkedList<>();
        HashMap<Integer, Node> reached = new HashMap<>();
        // used to check if the root itself is the solution
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
        // if not add it to the frontier and increase the size and put it in the reached nodes
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            /* makes use of the for each loop.
             * if the nodes returned from the expand function have been reached previously ignore them
             * ;else add them to the frontier and reached map.
             * also uses the early goal check, so that we don't waste time.
             * */
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
        // reaching this means that the search has exhausted all the possible nodes and should return failure.
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }

    // misplaced tile heuristic function
    private int misplacedTiles(Node n) {
        int[][] goalState = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        int[][] state = n.getState();
        int h = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != goalState[i][j]) h++;
            }
        }
        return h;
    }

    // manhattan heuristic function
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

    // euclidean distance heuristic function
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
    private static class f1 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getSecondaryCost() - o2.getSecondaryCost();
        }
    }

    /*
     * Comparator Object for Best first search
     * f2 uses manhattan distance
     * f3 uses euclidean distance
     * f4 uses misplaced tiles
     * */
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

    // Comparator object for A* search, all object make use of secondary cost not the primary cost.
    /*
     * f5 uses manhattan distance
     * f6 uses euclidean distance
     * f7 uses misplaced tiles
     * */
    private class f5 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (manhattanDistance(o1) + o1.getSecondaryCost()) - (manhattanDistance(o2) + o2.getSecondaryCost());
        }
    }

    // Comparator object for A* - using the euclidean distance heuristic
    private class f6 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (euclideanDistance(o1) + o1.getSecondaryCost()) - (euclideanDistance(o2) + o2.getSecondaryCost());
        }
    }

    // Comparator object for A* - using the misplaced tiles heuristic
    private class f7 implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return (misplacedTiles(o1) + o1.getSecondaryCost()) - (misplacedTiles(o2) + o2.getSecondaryCost());
        }
    }

    public boolean uniformCostSearch() {
        double startTime = System.currentTimeMillis();
        PriorityQueue<Node> frontier;
        frontier = new PriorityQueue<>(new f1());
        int size = 0;
        HashMap<Integer, Node> reached = new HashMap<>();
        // used to check if the root itself is the solution
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
        // if not add it to the frontier and increase the size and put it in the reached nodes
        frontier.add(root);
        size++;
        reached.put(root.hashCode(), root);
        while (!(frontier.isEmpty())) {
            Node node = frontier.poll();
            // checks if the node is the goal
            if (node.isGoal()) {
                double endTime = System.currentTimeMillis();
                ActionPath path = new ActionPath(root, node);
                path.printPath();
                System.out.println("-----------------------");
                System.out.println("Time: " + (endTime - startTime) + " millie seconds");
                System.out.println("Space: " + size);
                return true;
            }
            /* makes use of the for each loop.
             * if the nodes returned from the expand function have been reached previously ignore them;
             * else add them to the frontier and reached map
             * */
            for (Node child : expand(node)) {
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        // reaching this means that the search has exhausted all the possible nodes and should return failure.
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
        // used to check if the root itself is the solution
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
        // if not add it to the frontier and increase the size and put it in the reached nodes
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
        // reaching this means that the search has exhausted all the possible nodes and should return failure.
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
        // used to check if the root itself is the solution
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
        // if not add it to the frontier and increase the size and put it in the reached nodes
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
            /* makes use of the for each loop.
             * if the nodes returned from the expand function have been reached previously ignore them;
             * else add them to the frontier and reached map
             * */
            for (Node child : expand(node)) {
                if (!(reached.containsKey(child.hashCode())) && !(frontier.contains(child))) {
                    frontier.add(child);
                    reached.put(child.hashCode(), child);
                    size += 1;
                }
            }
        }
        // reaching this means that the search has exhausted all the possible nodes and should return failure.
        System.out.println("Time: " + (System.currentTimeMillis() - startTime) + " millie seconds");
        System.out.println("Space: " + size);
        return false;
    }
}