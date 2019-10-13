package com.mygdx.game.downfall

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music

import java.util.*

//import com.badlogic.gdx.ApplicationListener
//import com.badlogic.gdx.graphics.g2d.TextureAtlas
//import com.badlogic.gdx.scenes.scene2d.Stage
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class GameScreen(private val mGame: downfall) : ScreenAdapter() {

    companion object {
        val CAMERA_WIDTH = 10f
        val CAMERA_HEIGHT = 15f
        val WORLD_WIDTH = 10f
        val WORLD_HEIGHT = 15 * 5  // 20画面おりれば終了
        val GUI_WIDTH = 320f
        val GUI_HEIGHT = 480f

        val GAME_STATE_READY = 0
        val GAME_STATE_PLAYING = 1
        val GAME_STATE_GAMEOVER = 2
        val GAME_STATE_GAMECLEAR = 3

        // 重力
        val GRAVITY = -4

    }

    private val mBg: Sprite
    private val mCamera: OrthographicCamera
    private val mGuiCamera: OrthographicCamera
    private val mViewPort: FitViewport
    private val mGuiViewPort: FitViewport

    private var mRandom: Random
    private var mSteps: ArrayList<Step>
    private lateinit var mPlayer: Player
    private lateinit var mEnemy1: Enemy1
    private lateinit var mEnemy2: Enemy2
    private lateinit var mBoss: Boss

    private var mGameState: Int
    private var mHeightSoFar: Float = 0f
    private var mTouchPoint: Vector3
    //private var mFont: BitmapFont
    //private var mScore: Int
    //private var mHighScore: Int

    // Stageが必要
    //private var mStage: Stage
    // ボタンImage
    //private var mButtonL: Image
    //private var mButtonR: Image
    //private var mButtonJump: Image

    private val mMusic: Music //音

    //ボタン
    //private var mAtlus: TextureAtlas

    init {
        // 背景の準備
        val bgTexture = Texture("fall.png")
        // TextureRegionで切り出す時の原点は左上
        mBg = Sprite(TextureRegion(bgTexture, 0, 0, 292, 230))
        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT)
        mBg.setPosition(0f, 0f)

        // カメラ、ViewPortを生成、設定する
        mCamera = OrthographicCamera()
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT)
        mViewPort = FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, mCamera)

        // GUI用のカメラを設定する
        mGuiCamera = OrthographicCamera()
        mGuiCamera.setToOrtho(false, GUI_WIDTH, GUI_HEIGHT)
        mGuiViewPort = FitViewport(GUI_WIDTH, GUI_HEIGHT, mGuiCamera)


        // プロパティの初期化
        mRandom = Random()
        mSteps = ArrayList()
        mGameState = GAME_STATE_READY
        mTouchPoint = Vector3()

        //mFont = BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false)
        //mFont.data.setScale(0.8f)
        //mScore = 0
        //mHighScore = 0
        //mStage = Stage()
        //mAtlus = TextureAtlas()
        mMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm_maoudamashii_fantasy15.mp3"))

        mMusic.setVolume(0.5f)
        mMusic.setLooping(true)
        mMusic.play()

        //mStage = Stage()
        //mButtonL = Image(Texture("Left.png"))
        //mButtonL.setPosition(10f,10f)
        //mButtonR = Image(Texture("Right.png"))
        //mButtonR.setPosition(60f,10f)
        //mButtonJump = Image(Texture("Up.png"))
        //mButtonJump.setPosition(200f,10f)

        //mStage.addActor(mButtonL)
        //mStage.addActor(mButtonR)
        //mStage.addActor(mButtonJump)
        //Gdx.input.setInputProcessor(mStage)

        //var accel = 0f

        // jumpボタン
        //val listenerJ = object : ClickListener() {//ここがポイント２

            //override fun clicked(event: InputEvent, x: Float, y: Float) {
                // ここでクリックした時の動作
               //accel = 10.0f
            //}

        //}
        //mButtonJump.addListener(listenerJ)//ここがポイント３

        // 左ボタン
        //val listenerL = object : ClickListener() {
            //override fun clicked(event: InputEvent, x: Float, y: Float) {
                // ここでクリックした時の動作
                // left処理

            //}
        //}
        //mButtonL.addListener(listenerL)

        createStage()
    }

    override fun render(delta: Float) {
        // それぞれの状態をアップデートする
        update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        mCamera.position.y = WORLD_HEIGHT + 0f

        if (mPlayer.y < mCamera.position.y) {
            mCamera.position.y = mPlayer.y
        }

        // カメラの座標をアップデート（計算）し、スプライトの表示に反映させる
        mCamera.update()
        mGame.batch.projectionMatrix = mCamera.combined

        mGame.batch.begin()

        // 背景
        // 原点は左下
        mBg.setPosition(mCamera.position.x - CAMERA_WIDTH / 2, mCamera.position.y - CAMERA_HEIGHT / 2)
        mBg.draw(mGame.batch)

        // Step
        for (i in 0 until mSteps.size) {
            mSteps[i].draw(mGame.batch)
        }

        //Enemy
        mEnemy1.draw(mGame.batch)
        mEnemy2.draw(mGame.batch)

        // Boss
        mBoss.draw(mGame.batch)

        //Player
        mPlayer.draw(mGame.batch)

        mGame.batch.end()

        // スコア表示
        //mGuiCamera.update()
        //mGame.batch.projectionMatrix = mGuiCamera.combined
        //mGame.batch.begin()
        //mFont.draw(mGame.batch, "HighScore: $mHighScore", 16f, GUI_HEIGHT - 15)
        //mFont.draw(mGame.batch, "Score: $mScore", 16f, GUI_HEIGHT - 35)
        //mGame.batch.end()
    }

    override fun resize(width: Int, height: Int) {
        mViewPort.update(width, height)
        mGuiViewPort.update(width, height)
    }

    // ステージを作成する
    private fun createStage() {

        // テクスチャの準備
        val stepTexture = Texture("plant06c.png")
        val playerTexture = Texture("WK.png")
        val enemy1Texture = Texture("spirit12.png")
        val enemy2Texture = Texture("spirit12c1.png")
        val bossTexture = Texture("undead71.png")

        // Stepをゴールまで配置していく
        var y = 0f

        while (y < WORLD_HEIGHT - 1) {
            val type = if(mRandom.nextFloat() > 0.8f) Step.STEP_TYPE_MOVING else Step.STEP_TYPE_STATIC
            val x = mRandom.nextFloat() * (WORLD_WIDTH - Step.STEP_WIDTH)

            val step = Step(type, stepTexture, 0, 0, 220, 180)
            step.setPosition(x, y)
            mSteps.add(step)

            y += 5
        }


        // Playerを配置
        mPlayer = Player(playerTexture, 0, 0, 150, 270)
        mPlayer.setPosition(WORLD_WIDTH / 2 - mPlayer.getWidth() / 2, WORLD_HEIGHT + 0f)

        // Enemyを配置
        mEnemy1 = Enemy1(enemy1Texture, 0 ,0, 272, 252)
        mEnemy1.setPosition(0f, WORLD_HEIGHT - 10f)
        //mEnemy1.velocity.x = 4.0f

        mEnemy2 = Enemy2(enemy2Texture, 0, 0, 272, 252)
        mEnemy2.setPosition(WORLD_WIDTH, WORLD_HEIGHT - 8f)
        //mEnemy2.velocity.x = -4.0f

        // Bossを配置
        mBoss = Boss(bossTexture, 0, 0, 772, 408)
        mBoss.setPosition(WORLD_WIDTH / 2 - Boss.BOSS_WIDTH / 2, WORLD_HEIGHT - WORLD_HEIGHT + 0f)
    }

    // それぞれのオブジェクトの状態をアップデートする
    private fun update(delta: Float) {
        when (mGameState) {
            GAME_STATE_READY ->
                updateReady()
            GAME_STATE_PLAYING ->
                updatePlaying(delta)
            GAME_STATE_GAMEOVER ->
                updateGameOver()
            //GAME_STATE_GAMECLEAR ->
                //updateGAMECLAER()
        }
    }

    private fun updateReady() {
        if (Gdx.input.justTouched()) {
            mGameState = GAME_STATE_PLAYING
        }
    }

    private fun updatePlaying(delta: Float) {

        var accel = 0f
        if (Gdx.input.isTouched) {
            mGuiViewPort.unproject(mTouchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
            val left = Rectangle(0f, GUI_HEIGHT / 2, GUI_WIDTH / 2, GUI_HEIGHT / 2)
            val under_left = Rectangle(0f,0f, GUI_WIDTH / 2, GUI_HEIGHT / 2)
            val right = Rectangle(GUI_WIDTH / 2, GUI_HEIGHT / 2, GUI_WIDTH / 2, GUI_HEIGHT / 2)
            val under_right = Rectangle(GUI_WIDTH / 2, 0f, GUI_WIDTH / 2, GUI_HEIGHT / 2)

            if (left.contains(mTouchPoint.x, mTouchPoint.y)) {
                //if (mPlayer.velocity.y < 0) {
                        mPlayer.velocity.y = 5.0f
                //}
                //if (mPlayer.velocity.y > 0) {
                    return
                //}
            }
            if (under_left.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = 5.0f
            }
            if (right.contains(mTouchPoint.x, mTouchPoint.y)) {
                return
            }
            if (under_right.contains(mTouchPoint.x, mTouchPoint.y)) {
                accel = -5.0f
            }


        }

        // Step
        for (i in 0 until mSteps.size) {
            mSteps[i].update(delta)
        }

        // Player
        if (mPlayer.y <= 0.5f) {
            mPlayer.hitStep()
        }
        mPlayer.update(delta, accel)
        mHeightSoFar = Math.max(mPlayer.y, mHeightSoFar)

        // 当たり判定を行う
        checkCollision()

    }

    private fun updateGameOver() {
        if (Gdx.input.justTouched()) {
            mMusic.stop()
            mMusic.dispose()
            mGame.screen = ResultScreen(mGame)
        }
    }

    //private  fun updateGAMECLAER() {
    //    if (Gdx.input.justTouched()) {
    //        mMusic.stop()
    //        mMusic.dispose()
    //        mGame.screen = ResultScreen(mGame,mScore )
    //    }
    //}

    private fun checkCollision() {
        //Enemyとの当たり判定
        if (mPlayer.boundingRectangle.overlaps(mEnemy1.boundingRectangle)) {
            mGameState = GAME_STATE_GAMEOVER
            return
        }

        // Bossとの当たり判定)
        if (mPlayer.boundingRectangle.overlaps(mBoss.boundingRectangle)) {
            mGameState = GAME_STATE_GAMEOVER
            return
        }

        // Stepとの当たり判定
        // 上昇中はStepとの当たり判定を確認しない
        if (mPlayer.velocity.y > 0) {
            return
        }

        for (i in 0 until mSteps.size) {
            val step = mSteps[i]

            //if (step.mState == Step.STEP_STATE_VANISH) {
                //continue
            //}

            if (mPlayer.y > step.y) {
                if (mPlayer.boundingRectangle.overlaps(step.boundingRectangle)) {
                    mPlayer.hitStep()
                    //if (mRandom.nextFloat() > 0.5f) {
                        //step.vanish()
                    //}
                    break
                }
            }
        }
    }
}