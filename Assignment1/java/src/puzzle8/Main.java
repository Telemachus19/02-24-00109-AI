package puzzle8;

import java.util.Arrays;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        String command = args[0].toLowerCase();
        String[] initialState = args[1].split(",");
        int[][] state = new int[3][3];
        for (int i = 0,z = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++,z++) {
                state[i][j] = Integer.parseInt(initialState[z]);
            }
        }
        Tree Board = new Tree(state);
        if(command.equals("bfs")){
            Board.breadthFirstSearch();
        }else if(command.equals("dfs")){
            Board.depthFirstSearch();
        }else{
            Board.AStartManhatten();
        }
    }
}
