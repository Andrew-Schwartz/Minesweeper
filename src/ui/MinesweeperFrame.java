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

    @FXML
    void initialize() {
        reset();
    }

    @FXML
    private void btnRestart() {
//        gameStarted = false;
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
