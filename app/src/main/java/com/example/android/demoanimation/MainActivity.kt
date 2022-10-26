package com.example.android.demoanimation

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.android.demoanimation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            btnBlink.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(applicationContext,
                    R.anim.blink)
                btnBlink.startAnimation(animation)
            }


            btnFadeIn.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(applicationContext,
                    R.anim.fade_in)
                btnFadeIn.startAnimation(animation)
            }

            btnZoomIn.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(applicationContext,
                    R.anim.zoom_in)
                btnZoomIn.startAnimation(animation)
            }

            btnTrans.setOnClickListener {
                btnTrans.animate().apply {
                    translationY(100f)
                    duration = 2000
                }.start()
            }

            btnChangeColor.setOnClickListener {
                val backgroundColorAnimator: ObjectAnimator =
                    ObjectAnimator.ofObject(btnChangeColor,
                        "backgroundColor",
                        ArgbEvaluator(),
                        Color.RED,
                        Color.GRAY,
                        Color.GREEN, Color.YELLOW,
                        Color.BLUE)
                backgroundColorAnimator.duration = 10000
                backgroundColorAnimator.start()
            }

            vectorDrawable.setOnClickListener {
                (vectorDrawable.drawable as AnimatedVectorDrawable).start()
            }
        }
    }
}