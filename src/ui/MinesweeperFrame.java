package ui;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.Board;

public class MinesweeperFrame {
    @FXML
    private Label lblWidth, lblHeight, lblNumMines, lblFlagLabel, lblNumFlags;

    @FXML
    private Button btnReset;

    @FXML
    private TextField txtTileWidth, txtTileHeight, txtNumMines;

    @FXML
    private GridPane gridPane;

    private Board board;

    @FXML
    void initialize() {
        reset();

        AnchorPane stage = (AnchorPane) gridPane.getParent();
        stage.widthProperty().addListener((this::windowWidthListener));
        stage.heightProperty().addListener(this::windowHeightListener);

        lblNumFlags.textProperty().bind(board.getFlaggedTileCount().asString());
    }

    @FXML
    private void btnRestart() {
        board.resetAllTiles();
        reset();
    }

    private void reset() {
        board = new Board(Integer.valueOf(txtTileWidth.getText()),
                Integer.valueOf(txtTileHeight.getText()),
                Integer.valueOf(txtNumMines.getText()),
                gridPane
        );
        board.addTiles();
    }

    private void windowWidthListener(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
        double width = newWidth.doubleValue();

        lblWidth.setLayoutX(width * .0375);
        txtTileWidth.setLayoutX(width * .0375);

        lblHeight.setLayoutX(width * .2125);
        txtTileHeight.setLayoutX(width * .2125);

        btnReset.setLayoutX(width * .4);

        lblNumMines.setLayoutX(width * .625);
        txtNumMines.setLayoutX(width * .625);

        lblFlagLabel.setLayoutX(width * .8);
        lblNumFlags.setLayoutX(width * .8);

        gridPane.setPrefWidth(width - gridPane.getLayoutX() * 2);

        board.setGridWidth(gridPane.getPrefWidth());
    }

    private void windowHeightListener(ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) {
        double height = newHeight.doubleValue();

        gridPane.setPrefHeight(height - gridPane.getLayoutY() - 20);

        board.setGridHeight(gridPane.getPrefHeight());
    }
}
