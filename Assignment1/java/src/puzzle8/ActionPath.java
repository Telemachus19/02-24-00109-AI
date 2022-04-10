package puzzle8;

import java.util.Stack;

/**
 * The action Path data-structure is used to get the path taken from the root node to the goal node <br>
 * It makes use of the Stack properties
 */
public class ActionPath {
    // Data field
    Stack<Node> path = new Stack<>();

    // Constructor
    public ActionPath(Node initialNode, Node goalNode) {
        path = getPath(initialNode, goalNode);
    }

    /**
     * @param initialNode the node when reached we stop pushing into the stack
     * @param goalNode    the node where start from. We push its parents into the stack
     * @return Stack that contains the parents of the goal node in the order needed.
     */
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
            System.out.println("-----------------------");
            System.out.println("Current Node: \n");
            System.out.println(node);
            System.out.println("Direction Moved: " + node.getDirection());
            System.out.println("Depth          : " + node.getDepth());
            System.out.println("Primary   Cost : " + node.getCost());
            System.out.println("Secondary Cost : " + node.getSecondaryCost());
        }
    }
}