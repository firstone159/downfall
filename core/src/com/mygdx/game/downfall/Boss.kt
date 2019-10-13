package com.mygdx.game.downfall

import com.badlogic.gdx.graphics.Texture

class Boss(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val BOSS_WIDTH = 10.0f
        val BOSS_HEIGHT = 6.0f
    }

    init {
        setSize(BOSS_WIDTH, BOSS_HEIGHT)
    }

}