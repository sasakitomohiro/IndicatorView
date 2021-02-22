package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(
    context,
    attrs,
    defStyle
) {
    init {
        orientation = HORIZONTAL
    }
}
