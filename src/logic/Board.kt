package logic

import javafx.animation.Timeline
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import java.util.*
import kotlin.math.min

class Board(
    private val width: Int,
    private val height: Int,
    private var numMines: Int,
    private val gridRoot: GridPane,
    private val lblNumFlags: Label,
    private val timeline: Timeline,
    private val resetFunction: Runnable
) {
    companion object {
        private val rand = Random()
    }

    var tiles = Array(width) { x ->
        Array(height) { y ->
            Tile(x, y)
        }
    }

    var gridWidth = (gridRoot.parent as AnchorPane).prefWidth - gridRoot.layoutX * 2
        set(value) {
            (gridRoot.parent as AnchorPane).prefWidth = value + gridRoot.layoutX

            gridRoot.children.clear()
            addTiles()

            field = value
        }

    var gridHeight = (gridRoot.parent as AnchorPane).prefHeight - gridRoot.layoutY - 20
        set(value) {
            (gridRoot.parent as AnchorPane).prefHeight = value + gridRoot.layoutY + 20

            gridRoot.children.clear()
            addTiles()

            field = value
        }

    private var gameStarted = false

    private var gameOver = false
        set(value) {
            if (value) timeline.stop()
            field = value
        }

    private var flaggedTileCount = 0
        set(value) {
            lblNumFlags.text = value.toString()

            field = value
        }

    private var bombTiles = arrayOf<Tile>()

    /**
     * x,y are location of clicked tile
     */
    private fun initialize(x: Int, y: Int) {
        if (numMines >= width * height) numMines = width * height - 1

        var count = 0
        while (count < numMines) {
            val tryX = rand.nextInt(width)
            val tryY = rand.nextInt(height)
            if (!tiles[tryX][tryY].isBomb) {
                tiles[tryX][tryY].value = -1
                count++
            }
        }

        // ensure that you don't lose right away
        if (tiles[x][y].isBomb) {
            tiles.flatten().filter { !it.isBomb }.random().value = -1
            tiles[x][y].value = 0
        }

        bombTiles = tiles.flatten().filter { it.isBomb }.toTypedArray()

        // each tile's value is the sum of the number of bombs adjacent to it
        for (tile in tiles.flatten()) {
            if (tile.isBomb) continue
            tile.value = tile.adjacentTiles
                .filter { it.first >= 0 }
                .filter { it.first < tiles.size }
                .filter { it.second >= 0 }
                .filter { it.second < tiles[0].size }
                .filter {
                    tiles[it.first][it.second].isBomb
                }
                .count()
        }

        timeline.play()
    }

    fun addTiles() {
        for (tile in tiles.flatten()) {
            val imageSize = min(gridHeight / height, gridWidth / width)
            tile.imageView.fitHeight = imageSize
            tile.imageView.fitWidth = imageSize
            tile.imageView.onMouseReleased = EventHandler { this.handleClick(it, tile) }
            tile.setImageMaxSize(imageSize)

            gridRoot.add(tile.imageView, tile.x, tile.y)
        }
    }

    private fun handleClick(event: MouseEvent, tile: Tile) {
        if (gameOver) resetAllTiles()

        if (event.button == MouseButton.SECONDARY || event.isControlDown) flagTile(tile.x, tile.y)
        else clickTile(tile.x, tile.y)

        checkIfWon()
    }

    private fun checkIfWon() {
        if (bombTiles.any { it.isRevealed }) {
            gameOver = true

            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "game over"
            alert.headerText = "YA LOST"
            alert.contentText = "Donut click mines"
            alert.showAndWait()
        }

        if (tiles.flatten().all { it.isRevealed || it.isBomb }) {
            gameOver = true

            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = ":("
            alert.headerText = "you win"
            alert.contentText = "good job"
            alert.showAndWait()
        }
    }

    private fun clickTile(x: Int, y: Int) {
        if (!gameStarted) {
            initialize(x, y)
            gameStarted = true
        }

        if (tiles[x][y].isFlagged) return

        tiles[x][y].reveal()

        for (coord in tiles[x][y].adjacentTiles) {
            // make sure tile is in bounds
            if (coord.first < 0) continue
            if (coord.first >= tiles.size) continue
            if (coord.second < 0) continue
            if (coord.second >= tiles[0].size) continue

            // only care if the tile is empty
            if (tiles[x][y].value != 0) continue

            // make sure tile isn't already revealed to prevent infinite recursion
            if (tiles[coord.first][coord.second].isRevealed) continue

            clickTile(coord.first, coord.second)
        }
    }

    private fun flagTile(x: Int, y: Int) {
        if (tiles[x][y].isRevealed) return

        if (tiles[x][y].flag()) flaggedTileCount++
        else flaggedTileCount--
    }

    fun resetAllTiles() {
        gameStarted = false
        bombTiles = emptyArray()
        tiles.flatten().forEach { it.reset() }
        gridRoot.children.clear()
        flaggedTileCount = 0
        resetFunction.run()
    }
}
