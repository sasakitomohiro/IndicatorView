package com.github.sasakitomohiro.indicatorview

import android.content.Context
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

    var cellSize: Int = 0

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
        val cellWidth = calcCellWidth()
        for (i in 0 until count) {
            val cellLayoutParams = LayoutParams(cellWidth, cellWidth)
            if (i != 0) {
                cellLayoutParams.leftMargin = cellWidth
            }
            val cell = factory.create(context = context).apply {
                layoutParams = cellLayoutParams
            }
            cells.add(cell)
            addView(cell)
        }
    }

    private fun calcCellWidth(): Int {
        val layoutParams = layoutParams ?: return width
        val viewWidth =
            if (layoutParams.width == LayoutParams.WRAP_CONTENT || layoutParams.width == LayoutParams.MATCH_PARENT) {
                (parent as View).width
            } else width
        return (viewWidth / 2) / count
    }

    private fun calcHeight(): Int {
        val layoutParams = layoutParams ?: return height
        return if (layoutParams.width == LayoutParams.WRAP_CONTENT || layoutParams.width == LayoutParams.MATCH_PARENT) {
            LayoutParams.WRAP_CONTENT
        } else height
    }
}
