package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.util.AttributeSet
import android.view.View

class DefaultIndicatorCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(
    context,
    attrs,
    defStyle
) {
    companion object : IndicatorCellFactory<DefaultIndicatorCellView> {
        override fun create(
            context: Context,
            attrs: AttributeSet?,
            defStyle: Int
        ) = DefaultIndicatorCellView(
            context = context,
            attrs = attrs,
            defStyle = defStyle
        )
    }

    init {
        setBackgroundResource(R.drawable.indicator_cell_state)
    }
}
