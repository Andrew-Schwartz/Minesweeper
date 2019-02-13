package logic

import javafx.event.EventHandler
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import java.util.*
import kotlin.math.min

data class Board(val width: Int, val height: Int, private val numMines: Int) {
    var tiles: Array<Array<Tile>> = Array(width) { x ->
        Array(height) { y ->
            Tile(x, y)
        }
    }

    private val rand = Random()

    var imageSize = 0.0
        private set

    private var clickedTiles = ArrayList<Pair<Int, Int>>()

    /**
     * x,y are location of clicked tile
     */
    fun initialize(x: Int, y: Int) {
        val mineTiles = arrayListOf<Tile>()
        while (mineTiles.size < numMines) {
            val tryTile = Tile(rand.nextInt(width), rand.nextInt(height))
            if (!mineTiles.contains(tryTile) && tryTile != Tile(x, y))
                mineTiles.add(tryTile)
        }

        for (mineTile in mineTiles)
            tiles[mineTile.x][mineTile.y] = mineTile

        for (row in tiles) {
            for (tile in row) {
                tile.value =
            }
        }
    }

    fun addTiles(gridRoot: GridPane) {
        for (row in tiles) {
            for (tile in row) {
                val maxHeight = gridRoot.prefHeight - 20
                val maxWidth = gridRoot.prefWidth - 20
                val maxSize = min(maxHeight / height, maxWidth / width).also { imageSize = it }
                tile.imageView.fitHeight = maxSize
                tile.imageView.fitWidth = maxSize
                tile.imageView.onMouseReleased = EventHandler { clickTile(tile.x, tile.y) }
                tile.setImageMaxSize(maxSize)

                gridRoot.add(tile.imageView, tile.x, tile.y)
            }
        }
    }

    fun clickTile(x: Int, y: Int) {
        tiles[x][y].updateImage()
        clickedTiles.add(x to y)

        fun recursiveClick(newX: Int, newY: Int) {
            if (newX < 0 || newX > tiles.size - 1 || newY < 0 || newY > tiles[0].size - 1) return
            if (tiles[x][y].value == 0 &&
                !clickedTiles.any { it.first == newX && it.second == newY })
                clickTile(newX, newY)
        }

        recursiveClick(x - 1, y - 1)
        recursiveClick(x, y - 1)
        recursiveClick(x + 1, y - 1)

        recursiveClick(x - 1, y)
        recursiveClick(x + 1, y)

        recursiveClick(x - 1, y + 1)
        recursiveClick(x, y + 1)
        recursiveClick(x + 1, y + 1)
    }

    fun resetAllTiles(gridRoot: GridPane) {
        for (row in tiles) {
            for (tile in row) {
                tile.reset()
                gridRoot.children.removeIf { it is ImageView }
            }
        }
    }
}
