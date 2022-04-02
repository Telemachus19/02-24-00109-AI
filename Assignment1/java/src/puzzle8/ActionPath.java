package puzzle8;

import java.util.Arrays;
import java.util.Stack;

public class ActionPath {
    // Data field
    Stack<Node> path = new Stack<>();

    // Constructor
    public ActionPath(Node initialNode, Node goalNode) {
        path = getPath(initialNode, goalNode);
    }

    // Methods
    public Stack<Node> getPath(Node initialNode, Node goalNode) {
        Node temp = goalNode;
        Stack<Node> list = new Stack<>();
        while (!temp.equals(initialNode)) {
            list.push(temp);
            temp = temp.getParent();
        }
        list.push(initialNode);
        return list;
    }

    public void printPath() {
        Node node = path.pop();
        System.out.println("The root node");
        System.out.print(node);
        while (path.size() > 0) {
            node = path.pop();
            System.out.println();
            System.out.println();
            System.out.println("Direction Moved: " + node.getDirection());
            System.out.println("Depth: " + node.getDepth());
            System.out.println("Cost: " + node.getCost());
            System.out.println();
            System.out.println("Current Node: \n");
            System.out.println(node);
            System.out.println();
        }
    }
}