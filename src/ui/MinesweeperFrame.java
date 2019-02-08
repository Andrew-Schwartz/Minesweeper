package ui;

import javafx.event.ActionEvent;
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
    private boolean gameStarted = false;

    @FXML
    void initialize() {
        reset();
    }

    @FXML
    private void gridClickReleased(MouseEvent event) {
        gameStarted = true;
        double x = event.getX();
        double y = event.getY();

        x /= board.getWidth();
        y /= board.getHeight();
        System.out.println(x + "  " + y);

        if (!gameStarted) {
            reset();
            board.initialize((int) x, (int) y);
        }
    }

    @FXML
    private void btnRestart() {
        gameStarted = false;
        board.resetAllTiles();
        reset();
    }

    void reset() {
        board = new Board(Integer.valueOf(txtTileWidth.getText()),
                Integer.valueOf(txtTileHeight.getText()),
                Integer.valueOf(txtNumMines.getText())
        );
        board.showTiles(gridPane);
    }
}
