package com.github.sasakitomohiro.indicatorview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import com.github.sasakitomohiro.indicatorview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.indicator.count = 20
        binding.indicator.maxVisibleCount = 8
        binding.indicator.doOnLayout {
            binding.indicator.selectedIndex = 0
        }
        setCount(0)
        binding.prev.setOnClickListener {
            val selectedIndex = binding.indicator.previous()
            setCount(selectedIndex)
        }
        binding.next.setOnClickListener {
            val selectedIndex = binding.indicator.next()
            setCount(selectedIndex)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCount(count: Int) {
        binding.count.text = "${count}/${binding.indicator.count - 1}"
    }
}
