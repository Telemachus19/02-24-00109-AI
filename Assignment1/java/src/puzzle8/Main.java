package puzzle8;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner console = new Scanner(System.in);

    public static void failure() {
        System.out.println("The puzzle you have entered is unsolvable and resulted in failure");
    }

    public static int chooseHeuristic() {
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
            input = console.nextInt();
            if (input <= 3 && input >= 1) {
                inputCorrect = true;
            } else {
                System.out.println("Incorrect Choice. Please Try again!");
            }
        } while (!inputCorrect);
        return input;
    }
    public static void readInput(int[][] matrix,String message){
        boolean correct = false;
        int dimension = matrix.length;
        int max = (dimension * dimension);
        System.out.print(message);
        do {
            int[] freq = new int[max];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    matrix[i][j] = console.nextInt();
                    if (matrix[i][j] > (max - 1)) break;
                    freq[matrix[i][j]]++;
                }
            }
            console.nextLine();
            for (int i = 0; i < max; i++) {
                if (freq[i] != 1) {
                    correct = false;
                    System.out.println("The input you have entered is incorrect. Please try again!");
                    System.out.print(message);
                    break;
                }
                correct = true;
            }
        } while (!correct);
    }
    public static void printMatrix(int[][] matrix){
        int dim = matrix.length;
        for (int[] ints : matrix) {
            for (int j = 0; j < dim; j++) {
                System.out.print(ints[j] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int dimensions =  3;
        boolean correct = false;
        System.out.println("You will enter the dimensions of the Puzzle");
        System.out.println("3 for 8 Puzzle -- since there are 3 rows and cols");
        boolean inputCorrect;
        do{
            System.out.print("Enter the dimensions of the puzzle : ");
            try{
                dimensions = console.nextInt();
                correct = true;
            }catch (Exception e){
                System.out.println("You have not entered a number.");
                System.out.println("Please Enter a number.");
                console.next();
            }
        }while(!correct);
//        System.out.println(dimensions);
        System.out.println();

        int[][] initialState = new int[dimensions][dimensions];
        int[][] goalState= new int[dimensions][dimensions];
        int input1, input2;
        System.out.println("Welcome to " + ((dimensions * dimensions) - 1) + " puzzle Solver");
        readInput(goalState,"Enter the Goal State\n");
        readInput(initialState,"Enter the puzzle\n");
//        printMatrix(initialState);
//        printMatrix(goalState);


        Tree Board = new Tree(initialState,goalState);
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
            input1 = console.nextInt();
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