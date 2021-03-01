package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntRange

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
            if (value < 0 || count < 1 || count - 1 < value) return
            field = value
            selectCell()
        }

    @IntRange(from = 0)
    var count: Int = 0
        set(value) {
            field = value
            addIndicatorCell()
        }

    @IntRange(from = 0)
    var maxVisibleCount = 0
        set(value) {
            field = value
            addIndicatorCell()
        }

    @IntRange(from = 0)
    var cellSize: Int = 0
        get() {
            if (field > 0) return field
            val layoutParams = layoutParams ?: return LayoutParams.WRAP_CONTENT

            val viewWidth = if (layoutParams.width == LayoutParams.WRAP_CONTENT || layoutParams.width == LayoutParams.MATCH_PARENT) (parent as View).width else width
            field = (viewWidth / 2) / count
            return field
        }

    private val cells = mutableListOf<View>()

    init {
        orientation = HORIZONTAL
    }

    override fun setOrientation(orientation: Int) {
        // do nothing
    }

    fun setIndicatorCellFactory(factory: IndicatorCellFactory<View>) {
        this.factory = factory
        addIndicatorCell()
    }

    fun next(): Int {
        val nextIndex = selectedIndex + 1
        if (count - 1 < nextIndex) return selectedIndex
        selectedIndex = nextIndex
        return selectedIndex
    }

    fun previous(): Int {
        val previousIndex = selectedIndex - 1
        if (previousIndex < 0) return selectedIndex
        selectedIndex = previousIndex
        return selectedIndex
    }

    fun getSelectedCell(): View? = cells.firstOrNull { it.isSelected }

    fun refresh() {
        addIndicatorCell()
    }

    private fun initialize() {
        removeAllViews()
        cells.clear()
        if (selectedIndex != -1 && count <= selectedIndex) {
            selectedIndex = -1
        }
    }

    private fun addIndicatorCell() {
        initialize()
        val visibleCellCount = if (maxVisibleCount != 0 && maxVisibleCount < count) maxVisibleCount else count
        for (i in 0 until visibleCellCount) {
            val cellSize =
                if (maxVisibleCount == visibleCellCount && i == visibleCellCount - 1) cellSize / 2 else cellSize
            val cellLayoutParams = LayoutParams(cellSize, cellSize)
            if (i != 0) {
                cellLayoutParams.leftMargin = this.cellSize
                cellLayoutParams.gravity =  Gravity.CENTER_VERTICAL
            }
            val cell = factory.create(context = context).apply {
                layoutParams = cellLayoutParams
            }
            cells.add(cell)
            addView(cell)
        }
        selectCell()
    }

    private fun selectCell() {
        cells.forEachIndexed { cellIndex, view ->
            if (view.isSelected && selectedIndex != cellIndex) view.isSelected = false
        }
        val cellIndex =
            if (selectedIndex < count && maxVisibleCount > 0 && selectedIndex > maxVisibleCount - 1) maxVisibleCount - 1 else selectedIndex
        val cell = cells.getOrNull(cellIndex)
        cell?.isSelected = true
    }
}
