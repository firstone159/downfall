package com.mygdx.game.downfall

import com.badlogic.gdx.graphics.Texture

class Enemy2 (texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val ENEMY2_WIDTH = 4.0f
        val ENEMY2_HEIGHT = 3.0f

        val ENEMY2_VELOCITY = -4.0f
    }

    init {
        setSize(ENEMY2_WIDTH, ENEMY2_HEIGHT)
        velocity.x = ENEMY2_VELOCITY
    }
}