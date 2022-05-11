import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import kotlin.random.Random

fun Stage.handleKeyPress(
    sprite: Sprite,
    walkAnimation: SpriteAnimation,
    spinAnimation: SpriteAnimation,
    attackAnimation: SpriteAnimation,
    lazers: MutableList<SolidRect>,
    villians: MutableList<SolidRect>,
    score: Text,
    message: Text,
) {
    val deathMessage =  text(
        text = "",
        color = Colors.RED,
        textSize = 32.0,
    ).xy(75, 125)
    val move: (Int, Boolean) -> Unit = { distance, xAxis ->
        if(xAxis) sprite.x += distance else sprite.y += distance
        sprite.playAnimation(spriteAnimation = walkAnimation, spriteDisplayTime = TimeSpan(120.0))
        reposition(sprite)
    }

    val thrill: () -> Unit = {
        message.text = ""
        sprite.playAnimation(
            spriteAnimation = spinAnimation,
            spriteDisplayTime = TimeSpan(240.0),
        )

        if(shots <= 5*(difficulty)) shots++
    }

    val attack: () -> Unit = {
        if(shots > 0) {
            sprite.playAnimation(spriteAnimation = attackAnimation, spriteDisplayTime = TimeSpan(240.0))

            lazers.add(
                solidRect(width = 20, height = 6, color = Colors.RED).xy(
                    sprite.x + sprite.width,
                    sprite.y + sprite.height / 2
                )
            )
            shots--
        } else message.text = "You have to Thrill, to Kill"
    }

    if(alive) {
        if(input.keys.pressing(Key.SPACE)) thrill()
        if(input.keys.justPressed(Key.ENTER)) attack()
        if(input.keys.pressing(Key.D)) move(5, true)
        if(input.keys.pressing(Key.A)) move(-5, true)
        if(input.keys.pressing(Key.S)) move(5, false)
        if(input.keys.pressing(Key.W)) move(-5, false)
    }

    lazers.forEach {
        it.x += 20
        if(it.x > 800) it.removeFromParent()
    }

    lazers.removeAll { it.parent == null }


    if(villians.size < (difficulty+2)) {
        difficulty++
        score.text = "Score: $difficulty"
        for(i in 1..Random.nextInt(5)*difficulty) {
            val villian = solidRect(width = 30, height = 30, color = Colors.BLUE).xy(
                Random.nextInt(from = 700, until = 800), Random.nextInt(from = 100, until = 300)
            )
            villians.add(villian)
        }
    }

    villians.forEach {
        it.color = Colors.colorsByName.values.random()
        if(it.collidesWith(lazers)) {
            it.removeFromParent()
        } else {
            it.x += Random.nextInt(from = -10, until = 5)
            it.y += Random.nextInt(from = -5, until = 5)
            reposition(it)
        }

        if(it.collidesWith(sprite)) {
           deathMessage.text = "Nice, now Bryce is dead.\n Are you happy with yourself?"
            sprite.removeFromParent()
            alive = false
        }

    }

    villians.removeAll { it.parent == null }


}

fun reposition(sprite: View) {
    val height = sprite.height * sprite.scale
    val width = sprite.width * sprite.scale

    if(sprite.x < 0 - width) sprite.x = 800.0
    if(sprite.x > 800.0) sprite.x = 0.0 - width
    if(sprite.y < 0 - height) sprite.y = 600.0
    if(sprite.y > 600.0) sprite.y = 0.0 - height
}

fun Stage.pressed(vararg keys: Key) = keys.any { this.input.keys.pressing(it) }


