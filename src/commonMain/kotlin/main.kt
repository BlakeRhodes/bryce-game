import com.soywiz.klock.Frequency
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import com.soywiz.korma.geom.vector.rect

suspend fun main() = Korge(
    width = 800,
    height = 600,
    bgcolor = Colors["#2b2b2b"],
    title = "Bryce Meme: the Game",
) {
    createBackground()

    val directions = text(
        text = "WASD to move, SPACEBAR to thrill, Enter to Kill",
        textSize = 32.0,
    ).xy(100, 125)
    val score = text(text = "Score: $difficulty", textSize = 32.0).xy(650, 500)
    val message = text("",textSize = 32.0,).xy(100, 125)

    val spriteMap = resourcesVfs["bryce-sprite-sheet.png"].readBitmap()
    val height = 200
    val width = 88
    val lazers = mutableListOf<SolidRect>()
    val villains = mutableListOf<SolidRect>()

    val walkAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = width,
        spriteHeight = height,
        columns = 2,
        marginTop = height,
    )
    val standAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = width,
        spriteHeight = height,
        columns = 1,
    )
    val spinAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = width,
        spriteHeight = height,
        columns = 2,
    )

    val attackAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = width,
        spriteHeight = height,
        columns = 2,
        marginTop = 400,
    )

    val bryce = sprite(standAnimation).xy(100, 200)

    addUpdater {
        checkToRemoveDirections(directions)
        handleKeyPress(bryce, walkAnimation, spinAnimation, attackAnimation, lazers, villains, score, message)
    }
}

var shots = 5
var difficulty = 0
var alive = true

private fun Stage.checkToRemoveDirections(directions: Text) {
    if(pressed(Key.D, Key.A, Key.S, Key.W, Key.SPACE)) {
        directions.text = ""
    }
}

private suspend fun Stage.createBackground() {
    val backgroundBitmap = resourcesVfs["background.png"].readBitmap()
    image(backgroundBitmap).xy(0, -200).scale(.80)
}
