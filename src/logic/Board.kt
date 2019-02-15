package logic

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import java.util.*
import kotlin.math.min

data class Board(val width: Int, val height: Int, private var numMines: Int) {
    companion object {
        private val rand = Random()
        private var gameStarted = false
    }

    var tiles: Array<Array<Tile>> = Array(width) { x ->
        Array(height) { y ->
            Tile(x, y)
        }
    }

    var imageSize = 0.0
        private set

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

        for (column in tiles) {
            for (tile in column) {
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
        }
    }

    fun addTiles(gridRoot: GridPane) {
        for (column in tiles) {
            for (tile in column) {
                val maxHeight = gridRoot.prefHeight - 20
                val maxWidth = gridRoot.prefWidth - 20
                val maxSize = min(maxHeight / height, maxWidth / width).also { imageSize = it }
                tile.imageView.fitHeight = maxSize
                tile.imageView.fitWidth = maxSize
                tile.imageView.onMouseReleased = EventHandler { this.handleClick(it, tile) }
                tile.setImageMaxSize(maxSize)

                gridRoot.add(tile.imageView, tile.x, tile.y)
            }
        }
    }

    private fun handleClick(event: MouseEvent, tile: Tile) {
        if (event.button == MouseButton.SECONDARY || event.isControlDown) {
            flagTile(tile.x, tile.y)
            checkIfWon()
        } else {
            clickTile(tile.x, tile.y)
            checkIfWon()
        }
    }

    private fun checkIfWon() {
    }

    private var revealedTileCount: Int = 0

    private fun clickTile(x: Int, y: Int) {
        if (!gameStarted) {
            initialize(x, y)
            gameStarted = true
        }

        if (tiles[x][y].isFlagged) return

        tiles[x][y].reveal()
        revealedTileCount++

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
        tiles[x][y].flag()
    }

    fun resetAllTiles(gridRoot: GridPane) {
        gameStarted = false
        for (column in tiles) {
            for (tile in column) {
                tile.reset()
                gridRoot.children.clear()
            }
        }
    }
}
