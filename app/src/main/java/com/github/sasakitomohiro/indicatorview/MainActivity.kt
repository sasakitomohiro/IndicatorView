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

        binding.root.doOnLayout {
            binding.indicator.count = 10
            binding.indicator.maxVisibleCount = 6
            binding.indicator.selectedIndex = 0
            setCount(0)
        }
        binding.prev.setOnClickListener {
            val selectedIndex = binding.indicator.previous()
            setCount(selectedIndex)
        }
        binding.next.setOnClickListener {
            val selectedIndex = binding.indicator.next()
            setCount(selectedIndex)
        }
        binding.small.setOnClickListener {
            binding.indicator.cellSize = 20
            binding.indicator.refresh()
            binding.indicator.selectedIndex = 0
            setCount(0)
        }
        binding.large.setOnClickListener {
            binding.indicator.cellSize = 0
            binding.indicator.refresh()
            binding.indicator.selectedIndex = 0
            setCount(0)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCount(count: Int) {
        binding.count.text = "${count}/${binding.indicator.count - 1}"
    }
}
