package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
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
    @Suppress("UNCHECKED_CAST")
    private var factory = DefaultIndicatorCellView.Companion as IndicatorCellFactory<View>

    var selectedIndex = -1
        set(value) {
            if (value < 0 || cells.size < 1 || cells.size - 1 < value) return
            field = value
            cells.forEachIndexed { cellIndex, view ->
                if (view.isSelected && value != cellIndex) view.isSelected = false
            }
            cells[value].isSelected = true
        }

    var count: Int = 0
        set(value) {
            field = value
            addIndicatorCell()
        }

    private val cells = mutableListOf<View>()

    init {
        orientation = HORIZONTAL
    }

    override fun setOrientation(orientation: Int) {
        super.setOrientation(orientation)
        addIndicatorCell()
    }

    fun setIndicatorCellFactory(factory: IndicatorCellFactory<View>) {
        this.factory = factory
        addIndicatorCell()
    }

    fun next(): Int {
        val nextIndex = selectedIndex + 1
        if (cells.size - 1 < nextIndex) return selectedIndex
        selectedIndex = nextIndex
        return selectedIndex
    }

    fun previous(): Int {
        val previousIndex = selectedIndex - 1
        if (previousIndex < 0) return selectedIndex
        selectedIndex = previousIndex
        return selectedIndex
    }

    private fun initialize() {
        removeAllViews()
        cells.clear()
        selectedIndex = -1
    }

    private fun addIndicatorCell() {
        initialize()
        for (i in 0 until count) {
            val cell = factory.create(context = context)
            cells.add(cell)
            addView(cell)
        }
    }
}
