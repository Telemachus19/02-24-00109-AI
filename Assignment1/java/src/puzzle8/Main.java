package puzzle8;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] initialState = new int[3][3];
        int input1,input2;
        System.out.println("Welcome to 8 puzzle Solver");
        System.out.print("Enter the puzzle : ");
        for(int i = 0; i < 3;i++){
            for(int j = 0;j < 3;j++)
                initialState[i][j] = sc.nextInt();
        }
        Tree Board = new Tree(initialState);
        System.out.println("Choose the Algorithm");
        System.out.println();
        System.out.println("1. BFS");
        System.out.println("2. DFS");
        System.out.println("3. A*");
        System.out.println();
        System.out.print("Enter your choice: ");
        input1 = sc.nextInt();

        switch (input1) {
            case 1, 2 -> System.out.println("Not yet implemented");
            default -> {
                System.out.println("Choose the Heuristic function");
                System.out.println();
                System.out.println("1. Manhattan Distance");
                System.out.println("2. Euclidean Distance");
                System.out.println();
                System.out.print("Enter your choice: ");
                input2 = sc.nextInt();
                if (input2 == 1)
                    Board.aStar(1);
                else
                    Board.aStar(2);
            }
        }
    }
}
