package com.mygdx.game.downfall

import com.badlogic.gdx.graphics.Texture

class Enemy1 (texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val ENEMY1_WIDTH = 4.0f
        val ENEMY1_HEIGHT = 3.0f

        val ENEMY1_VELOCITY = 4.0f
    }

    init {
        setSize(ENEMY1_WIDTH, ENEMY1_HEIGHT)
        velocity.x = ENEMY1_VELOCITY

        // 画面の端まで来たら反対側に移動させる
        //if (x + Enemy1.ENEMY1_WIDTH / 2 < 0) {
         //   x = GameScreen.WORLD_WIDTH - Enemy1.ENEMY1_WIDTH / 2
        //}
        //else if (x + Enemy1.ENEMY1_WIDTH / 2 > GameScreen.WORLD_WIDTH) {
        //    x = 0f
        //}
    }
}
    

