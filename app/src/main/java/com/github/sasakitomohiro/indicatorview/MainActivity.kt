package com.github.sasakitomohiro.indicatorview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.github.sasakitomohiro.indicatorview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View(this).apply {
            val viewLayoutParams = ViewGroup.LayoutParams(20, 20)
            layoutParams = viewLayoutParams
            setBackgroundColor(Color.BLACK)
        }
        val view2 = View(this).apply {
            val viewLayoutParams = ViewGroup.LayoutParams(20, 20)
            layoutParams = viewLayoutParams
            setBackgroundColor(Color.RED)
        }
        with(binding.indicatorFrame) {
            addView(view, FrameLayout.LayoutParams(20, 20))
            addView(view2, FrameLayout.LayoutParams(20, 20))
        }
    }
}
