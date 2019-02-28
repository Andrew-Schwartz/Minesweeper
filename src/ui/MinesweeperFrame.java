package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import logic.Board;

public class MinesweeperFrame {
    @FXML
    private Label lblWidth, lblHeight, lblNumMines, lblFlagLabel, lblNumFlags, lblTimer, lblTimerLabel;

    @FXML
    private Button btnReset;

    @FXML
    private TextField txtTileWidth, txtTileHeight, txtNumMines;

    @FXML
    private GridPane gridPane;

    private Timeline timeline;
    private SimpleDoubleProperty timeProp = new SimpleDoubleProperty(0);

    private Board board;

    @FXML
    void initialize() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(10),
                        event -> timeProp.setValue(Math.round(100. * (timeProp.doubleValue() + ((KeyFrame) event.getSource()).getTime().toSeconds())) / 100.)
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        reset();

        AnchorPane stage = (AnchorPane) gridPane.getParent();
        stage.widthProperty().addListener((this::windowWidthListener));
        stage.heightProperty().addListener(this::windowHeightListener);

        lblTimer.textProperty().bind(timeProp.asString());
    }

    @FXML
    private void btnRestart() {
        board.resetAllTiles();
    }

    private void reset() {
        timeProp.setValue(0);
        timeline.stop();

        board = new Board(Integer.valueOf(txtTileWidth.getText()),
                Integer.valueOf(txtTileHeight.getText()),
                Integer.valueOf(txtNumMines.getText()),
                gridPane,
                lblNumFlags,
                timeline,
                this::reset
        );
        board.addTiles();
    }

    private void windowWidthListener(ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) {
        double width = newWidth.doubleValue();

        lblWidth.setLayoutX(width * .13);
        txtTileWidth.setLayoutX(width * .13);

        lblHeight.setLayoutX(width * .2825);
        txtTileHeight.setLayoutX(width * .2825);

        btnReset.setLayoutX(width * .44);

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
