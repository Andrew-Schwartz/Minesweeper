package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import logic.Board;

public class MinesweeperFrame {
    @FXML
    private TextField txtTileWidth, txtTileHeight, txtNumMines;

    @FXML
    private GridPane gridPane;

    private Board board;
    private static boolean gameStarted = false;

    @FXML
    void initialize() {
        reset();
    }

    private void gridClickReleased(MouseEvent event) {
        gameStarted = true;
        double x = event.getX();
        double y = event.getY();
        System.out.println(x + "  " + y);

        x /= gridPane.getWidth();
        y /= gridPane.getHeight();
//        System.out.println(x + "  " + y);

        if (!gameStarted) {
            reset();
            board.initialize((int) x, (int) y);
        }
    }

    @FXML
    private void btnRestart() {
        gameStarted = false;
        board.resetAllTiles(gridPane);
        reset();
    }

    private void reset() {
        board = new Board(Integer.valueOf(txtTileWidth.getText()),
                Integer.valueOf(txtTileHeight.getText()),
                Integer.valueOf(txtNumMines.getText())
        );
        board.addTiles(gridPane);
    }

//    private double bottomRightX() {
//        return gridPane.getChildren().get(gridPane.getChildren().size() - 1).getLayoutX();
//    }
//    private double bottomRightY() {
//        return gridPane.getChildren().get(gridPane.getChildren().size() - 1).getLayoutY();
//    }
}
