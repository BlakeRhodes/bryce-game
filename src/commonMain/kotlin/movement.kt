import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.Stage

fun Stage.handleKeyPress(sprite: Sprite, walkAnimation: SpriteAnimation, spinAnimation: SpriteAnimation) {
    val move: (Int, Boolean) -> Unit = { distance, xAxis ->
        if(xAxis) sprite.x += distance else sprite.y += distance
        sprite.playAnimation(spriteAnimation = walkAnimation, spriteDisplayTime = TimeSpan(120.0))
        reposition(sprite)
    }

    val thrill: () -> Unit = {
        sprite.playAnimation(spriteAnimation = spinAnimation, spriteDisplayTime = TimeSpan(240.0))
    }

    Key.values().filter { pressed(it) }.forEach {
        when(it) {
            Key.D -> move(5, true)
            Key.A -> move(-5, true)
            Key.S -> move(5, false)
            Key.W -> move(-5, false)
            Key.SPACE -> thrill()
            else -> Unit
        }
    }
}

fun reposition(sprite: Sprite) {
    val height = sprite.height * sprite.scale
    val width = sprite.width * sprite.scale

    if(sprite.x < 0 - width) sprite.x = 800.0
    if(sprite.x > 800.0) sprite.x = 0.0 - width
    if(sprite.y < 0 - height) sprite.y = 600.0
    if(sprite.y > 600.0) sprite.y = 0.0 - height
}

fun Stage.pressed(vararg keys: Key) = keys.any { this.input.keys.pressing(it) }


