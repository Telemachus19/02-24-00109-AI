package puzzle8;

import java.util.Arrays;
import java.util.Stack;

public class ActionPath {
    // Data field
    Stack<Node> path = new Stack<>();
    // Constructor
    public ActionPath(Node initialNode,Node goalNode){
        path = getPath(initialNode,goalNode);
    }
    // Methods
    public Stack<Node> getPath(Node initialNode,Node goalNode){
        Node temp = goalNode;
        Stack<Node> list = new Stack<>();
        while(!temp.equals(initialNode)){
            list.push(temp);
            temp = temp.getParent();
        }
        list.push(initialNode);
        return list;
    }
    public void printPath(){
        int size = path.size();
        while (path.size() > 0) {
            Node node = path.pop();
            System.out.println();
            System.out.println();
            System.out.println("Direction Moved: " + node.getAction());
            System.out.println("Depth: " + node.getDepth());
            System.out.println("Cost: " + node.getCost());
            System.out.println("MaxCost: " + node.getMaxCost());
            System.out.println();
            System.out.println("Current Node: \n");
            System.out.println(Arrays.deepToString(node.getState()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
            System.out.println();
        }
    }
}
