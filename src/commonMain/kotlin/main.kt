import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

suspend fun main() = Korge(
    width = 800,
    height = 600,
    bgcolor = Colors["#2b2b2b"],
    title = "Bryce Meme: the Game",
) {
    createBackground()

    val directions = text(
        text = "WASD to move, SPACEBAR to thrill",
        textSize = 32.0,
    ).xy(100, 125)

    val (walkAnimation, standAnimation, spinAnimation) = createAnimations()

    val bryce = sprite(standAnimation).scale(.5).xy(400, 200)
    bryce.playAnimationLooped(standAnimation)

    addUpdater {
        checkToRemoveDirections(directions)
        handleKeyPress(bryce, walkAnimation, spinAnimation)
    }
}

private fun Stage.checkToRemoveDirections(directions: Text) {
    if(pressed(Key.D, Key.A, Key.S, Key.W, Key.SPACE)) {
        directions.removeFromParent()
    }
}

private suspend fun Stage.createBackground() {
    val backgroundBitmap = resourcesVfs["background.png"].readBitmap()
    image(backgroundBitmap).xy(0, -200).scale(.80)
}

private suspend fun createAnimations(): Triple<SpriteAnimation, SpriteAnimation, SpriteAnimation> {
    val spriteMap = resourcesVfs["bryce-sprite.png"].readBitmap()
    val spinMap = resourcesVfs["bryce-spin.png"].readBitmap()

    val walkAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 167,
        spriteHeight = 378,
        columns = 2,
    )
    val standAnimation = SpriteAnimation(
        spriteMap = spriteMap,
        spriteWidth = 167,
        spriteHeight = 378,
        columns = 1,
    )
    val spinAnimation = SpriteAnimation(
        spriteMap = spinMap,
        spriteWidth = 167,
        spriteHeight = 378,
        columns = 2
    )
    return Triple(walkAnimation, standAnimation, spinAnimation)
}
