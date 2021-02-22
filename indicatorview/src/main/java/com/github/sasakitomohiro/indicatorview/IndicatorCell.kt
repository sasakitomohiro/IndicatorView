package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.util.AttributeSet

interface IndicatorCellFactory<T> {
    fun create(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ): T
}
