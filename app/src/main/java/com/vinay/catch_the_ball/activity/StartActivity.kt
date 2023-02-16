package com.vinay.catch_the_ball.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vinay.catch_the_ball.R
import com.vinay.catch_the_ball.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_start)

        val binding = DataBindingUtil.setContentView<ActivityStartBinding>(this,R.layout.activity_start)
        binding.btnStartGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }


}