package logic

import javafx.scene.image.Image
import javafx.scene.image.ImageView

@Suppress("EqualsOrHashCode")
data class Tile(val x: Int, val y: Int) {
    var value: Int = 0
    val startImage = Image("images/CoveredTile.png")
    val imageView = ImageView(startImage)

    fun reset() {
        value = 0
        imageView.image = startImage
    }

    fun setImageMaxSize(length: Double) {
        imageView.fitWidth = length
        imageView.fitHeight = length
    }

    fun updateImage() {
        imageView.image = when (value) {
            -1 -> Image("images/Mine.jpg")
            0 -> Image("images/Empty.png") //TODO get this image
            else -> Image("images/$value.png")
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Tile -> this.x == other.x && this.y == other.y
            else -> false
        }
    }
}