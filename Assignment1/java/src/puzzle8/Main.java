package puzzle8;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] state = new int[3][3];
        System.out.println("Welcome to 8 puzzle solver game");
        System.out.print("Enter the puzzle state: ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state[i][j] = sc.nextInt();
            }
        }
        Tree board = new Tree(state);
        System.out.println("Choose the algorithm");
        System.out.println("1. Breadth first search");
        System.out.println("2. Depth first search *May take some time to*");
        System.out.println("3. A* Manhattan");
        System.out.print("Enter your choice: ");
        int input = sc.nextInt();
        switch (input) {
            case 1 -> board.breadthFirstSearch();
            case 2 -> board.depthFirstSearch();
            case 3 -> board.ASStartManhattan();
            default -> System.out.println("incorrect choice. Programming exiting");
        }
    }
}
