package com.vinay.catch_the_ball.activity

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vinay.catch_the_ball.R
import com.vinay.catch_the_ball.R.id
import com.vinay.catch_the_ball.R.string
import com.vinay.catch_the_ball.databinding.ActivityGameBinding
import com.vinay.catch_the_ball.util.AppConstant
import com.vinay.catch_the_ball.util.SoundPlayer
import java.util.Timer
import java.util.TimerTask
import kotlin.math.floor
import kotlin.math.roundToInt

class GameActivity : AppCompatActivity() {

    //Position
    private var boxY = 0f
    private var powerX = 0f
    private var powerY = 0f
    private var boostX = 0f
    private var boostY = 0f
    private var crashX = 0f
    private var crashY = 0f

    //size
    private var screenWidth = 0
    private var frameHeight = 0
    private var boxSize = 0

    //score
    private var score = 0

    //timer
    private var timer: Timer? = Timer()
    private val handler = Handler()

    //status
    private var actionFlag = false
    private var startFlag = false

    //sound player
    private var soundPlayer: SoundPlayer? = null

    //speed
    private var boxSpeed = 0
    private var powerSpeed = 0
    private var boostSpeed = 0
    private var crashSpeed = 0

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityGameBinding>(this, R.layout.activity_game)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPlayer = SoundPlayer()

//Screen Size
        val windowManager = windowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        val screenHeight = size.y

        //speed
        boxSpeed = (screenHeight / 60.0f).roundToInt()
        powerSpeed = (screenWidth / 60.0f).roundToInt()
        boostSpeed = (screenWidth / 36.0f).roundToInt()
        crashSpeed = (screenWidth / 45.0f).roundToInt()

        printLog("screenWidth $screenWidth")
        printLog("screenHeight $screenHeight")
        printLog("boxSpeed $boxSpeed")
        printLog("powerSpeed $powerSpeed")
        printLog("boostSpeed $boostSpeed")
        printLog("crashSpeed $crashSpeed")

        //initial position
        binding.apply {
            power.x = -80f
            power.y = -80f
            boost.x = -80f
            boost.y = -80f
            crash.x = -80f
            crash.y = -80f
            scoreLabel.text = getString(string.score, score)
        }


    }

    private fun changePos() {
        hitCheck()
        //power
        powerX -= powerSpeed.toFloat()
        if (powerX < 0) {
            powerX = (screenWidth + 20).toFloat()
            powerY = floor(Math.random() * (frameHeight - binding.power.height)).toFloat()
        }
        binding.power.x = powerX
        binding.power.y = powerY

        //crash
        crashX -= crashSpeed.toFloat()
        if (crashX < 0) {
            crashX = (screenWidth + 10).toFloat()
            crashY = floor(Math.random() * (frameHeight - binding.crash.height)).toFloat()
        }
        binding.crash.x = crashX
        binding.crash.y = crashY

        //boost
        boostX -= boostSpeed.toFloat()
        if (boostX < 0) {
            boostX = (screenWidth + 5000).toFloat()
            boostY = floor(Math.random() * (frameHeight - binding.boost.height)).toFloat()
        }
        binding.boost.x = boostX
        binding.boost.y = boostY

        //box
        if (actionFlag) {
            boxY -= boxSpeed.toFloat()
        } else {
            boxY += boxSpeed.toFloat()
        }
        if (boxY < 0) boxY = 0f
        if (boxY > frameHeight - boxSize) boxY = (frameHeight - boxSize).toFloat()
        binding.box.y = boxY
        binding.scoreLabel.text = getString(string.score, score)
    }

    private fun hitCheck() {

        //power
        val powerCenterX = powerX + binding.power.width / 2.0f
        val powerCenterY = powerY + binding.power.height / 2.0f
        if (0 <= powerCenterX && powerCenterX <= boxSize && boxY <= powerCenterY && powerCenterY <= boxY + boxSize) {
            powerX = -100.0f
            score += 10
            soundPlayer?.playHitSound()
        }

        //boost
        val boostCenterX = boostX + binding.boost.width / 2.0f
        val boostCenterY = boostY + binding.boost.height / 2.0f
        if (0 <= boostCenterX && boostCenterX <= boxSize && boxY <= boostCenterY && boostCenterY <= boxY + boxSize) {
            boostX = -100.0f
            score += 30
            soundPlayer?.playHitSound()
        }

        //crash
        val crashCenterX = crashX + binding.crash.width / 2.0f
        val crashCenterY = crashY + binding.crash.height / 2.0f
        if (0 <= crashCenterX && crashCenterX <= boxSize && boxY <= crashCenterY && crashCenterY <= boxY + boxSize) {
            soundPlayer?.playOverSound()
            //Game Over
            stopTimer()

            //show Result Activity
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(AppConstant.SCORE_KEY, score)
            startActivity(intent)
            finish()
        }
    }

    private fun stopTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!startFlag) {
            startFlag = true

            //FrameHeight
            val frameLayout = findViewById<FrameLayout>(id.frame)
            frameHeight = frameLayout.height
            //Box
            boxY = binding.box.y
            boxSize = binding.box.height
            binding.startLabel.visibility = View.GONE
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    handler.post { changePos() }
                }
            }, 0, 20)
        } else {
            if (event.action == MotionEvent.ACTION_DOWN) {
                actionFlag = true
            } else if (event.action == MotionEvent.ACTION_UP) {
                actionFlag = false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun printLog(message: String) {
        Log.v("vinay", message)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exitByBackKey() {
        val alertDialog = Builder(this)
        alertDialog.setMessage("Do you want to exit from Game?")
        alertDialog.setPositiveButton("Yes") { arg0, arg1 ->
            // do something when the button is clicked
            stopTimer()
            finish()
        }
        alertDialog.setNegativeButton("No") // do something when the button is clicked
        { arg0, arg1 -> }
        alertDialog.show()
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }
}