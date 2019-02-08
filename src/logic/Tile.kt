package logic

import javafx.scene.image.Image

@Suppress("EqualsOrHashCode")
data class Tile(val x: Int, val y: Int) {
    var value: Int = 0
    var image = Image("images/CoveredTile.png")

    fun reset() {
        value = 0
        image = Image("images/CoveredTile.png")
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Tile -> this.x == other.x && this.y == other.y
            else -> false
        }
    }
}