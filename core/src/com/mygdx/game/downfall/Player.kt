package com.mygdx.game.downfall

import com.badlogic.gdx.graphics.Texture

class Player(texture: Texture, srcX: Int, srcY: Int, srcWidth: Int, srcHeight: Int)
    : GameObject(texture, srcX, srcY, srcWidth, srcHeight) {

    companion object {
        // 横幅、高さ
        val PLAYER_WIDTH = 1.0f
        val PLAYER_HEIGHT = 1.5f

        // 状態（落ちている最中,止まっている状態）
        val PLAYER_STATE_JUMP = 0
        val PLAYER_STATE_FALL = 1

        // 速度
        var PLAYER_JUMP_VELOCITY = 0.0f
        val PLAYER_MOVE_VELOCITY = 10.0f
    }

    private var mState: Int

    init {
        setSize(PLAYER_WIDTH, PLAYER_HEIGHT)
        mState = PLAYER_STATE_FALL
    }

    fun update(delta: Float, accelX: Float) {

        // 重力をプレイヤーの速度に加算し、速度から位置を計算する
        velocity.add(0f, GameScreen.GRAVITY * delta)
        velocity.x = -accelX / 10 * PLAYER_MOVE_VELOCITY
        setPosition(x + velocity.x * delta, y + velocity.y * delta)

        // y方向の速度が下方向のときにSTATEがPLAYER_STATE_FALLでなければPLAYER_STATE_FALLにする
        if (velocity.y < 0) {
            if (mState != PLAYER_STATE_FALL) {
                mState = PLAYER_STATE_FALL
            }
        }

        // y方向の速度がないときはSTATEがPLAYER_STATE_STAYでなければPLAYER_STATE_STAYにする
        if (velocity.y > 0) {
            if (mState != PLAYER_STATE_JUMP) {
                mState = PLAYER_STATE_JUMP
            }
        }

        // 画面の端まで来たら反対側に移動させる
       if (x + PLAYER_WIDTH / 2 < 0) {
            x = GameScreen.WORLD_WIDTH - PLAYER_WIDTH / 2
        } else if (x + PLAYER_WIDTH / 2 > GameScreen.WORLD_WIDTH) {
            x = 0f
            }
        }

        fun hitStep() {
        velocity.y = PLAYER_JUMP_VELOCITY
        mState = PLAYER_STATE_JUMP
        }
}