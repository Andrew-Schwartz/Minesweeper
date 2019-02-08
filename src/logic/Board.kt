package logic

import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import java.util.*

data class Board(val width: Int, val height: Int, private val numMines: Int) {
    var tiles: Array<Array<Tile>> = Array(width) { x ->
        Array(height) { y ->
            Tile(x, y)
        }
    }
    private val rand = Random()

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
    }

    fun addTiles(gridRoot: GridPane) {
        for (row in tiles) {
            for (tile in row) {
                val imageView = ImageView(tile.image)
                val maxHeight = gridRoot.prefHeight
                imageView.fitHeight = maxHeight / height
                imageView.fitWidth = maxHeight / height
                gridRoot.add(imageView, tile.x, tile.y)
            }
        }
    }

    fun resetAllTiles() {
        for (row in tiles) {
            for (tile in row) {
                tile.reset()
            }
        }
    }
}
