package com.github.sasakitomohiro.indicatorview

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
            binding.indicator.count = 5
            binding.indicator.selectedIndex = 1
        }
        binding.prev.setOnClickListener {
            binding.indicator.previous()
        }
        binding.next.setOnClickListener {
            binding.indicator.next()
        }
    }
}
