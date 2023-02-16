package com.vinay.catch_the_ball.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vinay.catch_the_ball.R
import com.vinay.catch_the_ball.R.id
import com.vinay.catch_the_ball.R.layout
import com.vinay.catch_the_ball.R.string
import com.vinay.catch_the_ball.databinding.ActivityResultBinding
import com.vinay.catch_the_ball.databinding.ActivityStartBinding
import com.vinay.catch_the_ball.util.AppConstant

class ResultActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityResultBinding>(this,R.layout.activity_result)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val score = intent.getIntExtra(AppConstant.SCORE_KEY, 0)
        binding.scoreLabel.text = getString(string.result_score, score)

        //High Score
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        val highScore = sharedPreferences.getInt(AppConstant.HIGH_SCORE_KEY, 0)
        if (score > highScore) {
            //update High Score
            val editor = sharedPreferences.edit()
            editor.putInt(AppConstant.HIGH_SCORE_KEY, score)
            editor.apply()
            binding.highScoreLabel.text = getString(string.high_score, score)
        }
        binding.highScoreLabel.text = getString(string.high_score, highScore)


        binding.btnTryAgain.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}