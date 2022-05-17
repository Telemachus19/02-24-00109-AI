package puzzle8;

import java.util.Scanner;

public class Main {
    public static void failure() {
        System.out.println("The puzzle you have entered is unsolvable and resulted in failure");
    }

    public static int chooseHeuristic() {
        Scanner sc = new Scanner(System.in);
        int input;
        boolean inputCorrect = false;
        do {
            System.out.println("Choose the Heuristic function");
            System.out.println();
            System.out.println("1. Manhattan Distance");
            System.out.println("2. Euclidean Distance");
            System.out.println("3. Misplaced Tiles");
            System.out.println();
            System.out.print("Enter your choice: ");
            input = sc.nextInt();
            if (input <= 3 && input >= 1) {
                inputCorrect = true;
            } else {
                System.out.println("Incorrect Choice. Please Try again!");
            }
        } while (!inputCorrect);
        return input;

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] initialState = new int[3][3];
        int input1, input2;
        System.out.println("Welcome to 8 puzzle Solver");
        System.out.print("Enter the puzzle : ");
        boolean inputCorrect = false;
        do {
            int[] freq = new int[9];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    initialState[i][j] = sc.nextInt();
                    if (initialState[i][j] > 8)
                        break;
                    freq[initialState[i][j]]++;
                }
            }
            sc.nextLine();
            for (int i = 0; i < 9; i++) {
                if (freq[i] != 1) {
                    inputCorrect = false;
                    System.out.println("The input you have entered is incorrect. Please try again!");
                    System.out.print("Enter the puzzle : ");
                    break;
                }
                inputCorrect = true;
            }
        } while (!inputCorrect);
        Tree Board = new Tree(initialState);
        inputCorrect = false;
        do {
            System.out.println("Choose the Algorithm");
            System.out.println();
            System.out.println("1. Breadth First Search");
            System.out.println("2. Depth First Search");
            System.out.println("3. Uniform Cost  Search");
            System.out.println("4. Best First Search");
            System.out.println("5. A*");
            System.out.println();
            System.out.print("Enter your choice: ");
            input1 = sc.nextInt();
            if (input1 <= 5 && input1 >= 1) {
                inputCorrect = true;
            } else {
                System.out.println("Incorrect Choice. Please Try again!");
            }
        } while (!inputCorrect);
        switch (input1) {
            case 1 -> {
                if (!(Board.breadthFirstSearch())) failure();
            }
            case 2 -> {
                if (!Board.depthFirstSearch()) failure();
            }
            case 3 -> {
                if (!Board.uniformCostSearch()) failure();
            }
            case 4 -> {
                input2 = chooseHeuristic();
                if (!Board.bestFirstSearch(input2)) failure();
            }
            default -> {
                input2 = chooseHeuristic();
                if (!Board.aStar(input2)) failure();
            }
        }
    }
}