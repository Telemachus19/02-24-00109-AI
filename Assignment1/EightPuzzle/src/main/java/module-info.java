module puzzleEight.eightpuzzle {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens puzzleEight.eightpuzzle to javafx.fxml;
    exports puzzleEight.eightpuzzle;
}