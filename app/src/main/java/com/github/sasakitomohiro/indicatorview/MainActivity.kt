package com.github.sasakitomohiro.indicatorview

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.sasakitomohiro.indicatorview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = (0..10).mapIndexed { index, i ->
            View(this).apply {
                val viewLayoutParams = ViewGroup.LayoutParams(20, 20)
                layoutParams = viewLayoutParams
                if (index % 2 == 0) {
                    setBackgroundColor(Color.RED)
                } else {
                    setBackgroundColor(Color.BLACK)
                }
            }
        }
        with(binding.indicator) {
            items.forEach {
                addView(it, LinearLayout.LayoutParams(20, 20))
            }
        }
    }
}
