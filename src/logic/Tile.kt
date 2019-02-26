package logic

import javafx.scene.image.Image
import javafx.scene.image.ImageView

@Suppress("EqualsOrHashCode")
class Tile(val x: Int, val y: Int) {
    companion object {
        private val startImage = Image("images/CoveredTile.png")
    }

    var value: Int = 0
    val imageView = ImageView(startImage)
    var isFlagged = false
    var isRevealed = false

    fun reset() {
        value = 0
        isFlagged = false
        isRevealed = false
        if (imageView.image != startImage)
            imageView.image = startImage
    }

    fun setImageMaxSize(length: Double) {
        imageView.fitWidth = length
        imageView.fitHeight = length
    }

    fun reveal() {
        isRevealed = true
        imageView.image = when (value) {
            -1 -> Image("images/Mine.png")
            0 -> Image("images/Empty.png")
            else -> Image("images/$value.png")
        }
    }

    fun flag(): Boolean {
        if (isRevealed) return false
        if (isFlagged) {
            imageView.image = startImage
        } else {
            imageView.image = Image("images/Flag.png")
        }
        isFlagged = !isFlagged
        return isFlagged
    }

    val isBomb get() = value == -1

    val adjacentTiles
        get() = arrayOf(
            x - 1 to y - 1,
            x     to y - 1,
            x + 1 to y - 1,

            x - 1 to y    ,
            x + 1 to y    ,

            x - 1 to y + 1,
            x     to y + 1,
            x + 1 to y + 1
        )

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Tile -> this.x == other.x && this.y == other.y
            else -> false
        }
    }

    override fun toString() = "Tile(x = $x, y = $y, value = $value)"
}