package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout


@Suppress("UNCHECKED_CAST")
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(
    context,
    attrs,
    defStyle
) {
    private var indicatorCellFactory =
        DefaultIndicatorCellView.Companion as IndicatorCellFactory<View>

    var count: Int = 0
        set(value) {
            field = value
            addIndicatorCell()
        }

    init {
        orientation = HORIZONTAL
    }

    fun setIndicatorCellFactory(factory: IndicatorCellFactory<View>) {
        indicatorCellFactory = factory
        addIndicatorCell()
    }

    private fun addIndicatorCell() {
        for (i in 0 until count) {
            addView(indicatorCellFactory.create(context = context))
        }
    }
}
