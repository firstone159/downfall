package com.mygdx.game.downfall

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class downfall : Game() {
    lateinit var batch: SpriteBatch

    override fun create() {
        batch = SpriteBatch()

        // GameScreenを表示する
        setScreen(GameScreen(this))
    }
}