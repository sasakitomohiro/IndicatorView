package com.github.sasakitomohiro.indicatorview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import java.util.concurrent.atomic.AtomicBoolean

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
            val state =
                if (field > value) State.PREVIOUS else if (field < value) State.NEXT else State.NONE
            field = value
            selectCell(state)
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

    private var typedArray = context.obtainStyledAttributes(
        attrs,
        R.styleable.IndicatorView
    )

    private var cellWidth =
        typedArray.getDimension(R.styleable.IndicatorView_cellWidth, 8.dpToPx()).toInt()
    private var cellHeight =
        typedArray.getDimension(R.styleable.IndicatorView_cellHeight, 8.dpToPx()).toInt()
    private var cellMargin =
        typedArray.getDimension(R.styleable.IndicatorView_cellMargin, 8.dpToPx()).toInt()
    private var selectedCellColor =
        typedArray.getColor(R.styleable.IndicatorView_selectedCellColor, Color.BLACK)
    private var defaultCellColor =
        typedArray.getColor(R.styleable.IndicatorView_defaultCellColor, Color.GRAY)

    private var animationInterpolator: Interpolator = LinearInterpolator()
    private var animationDuration = 0L
    private var cellTranslationX: Float = 0f

    private val cells = mutableListOf<View>()

    private val isAnimating = AtomicBoolean(false)

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
        if (count - 1 < nextIndex || isAnimating.get()) return selectedIndex
        selectedIndex = nextIndex
        return selectedIndex
    }

    fun previous(): Int {
        val previousIndex = selectedIndex - 1
        if (previousIndex < 0 || isAnimating.get()) return selectedIndex
        selectedIndex = previousIndex
        return selectedIndex
    }

    fun getSelectedCell(): View? = cells.firstOrNull { it.isSelected }

    fun getCell(index: Int): View? = cells.getOrNull(index)

    fun refresh() {
        addIndicatorCell()
    }

    fun setCellWidth(cellWidth: Int) {
        this.cellWidth = cellWidth
    }

    fun setCellHeight(cellHeight: Int) {
        this.cellHeight = cellHeight
    }

    fun setCellMargin(cellMargin: Int) {
        this.cellMargin = cellMargin
    }

    fun setSelectedCellColor(@ColorInt color: Int) {
        selectedCellColor = color
        addIndicatorCell()
    }

    fun setDefaultCellColor(@ColorInt color: Int) {
        defaultCellColor = color
        addIndicatorCell()
    }

    fun setInterpolator(interpolator: Interpolator) {
        animationInterpolator = interpolator
        cells.forEach {
            it.animate().interpolator = interpolator
        }
    }

    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
        cells.forEach {
            it.animate().duration = duration
        }
    }

    private fun initialize() {
        removeAllViews()
        cells.clear()
        if (selectedIndex != -1 && count <= selectedIndex) {
            selectedIndex = -1
        }
    }

    private fun addIndicatorCell() {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_selected),
            intArrayOf(android.R.attr.state_selected)
        )
        val colors = intArrayOf(
            defaultCellColor,
            selectedCellColor
        )
        val colorStateList = ColorStateList(states, colors)

        initialize()

        for (i in 0 until count) {
            val cellLayoutParams = LayoutParams(cellWidth, cellHeight)
            if (i != 0) {
                cellLayoutParams.leftMargin = cellMargin
                cellLayoutParams.gravity = Gravity.CENTER_VERTICAL
            }
            val cell = factory.create(context = context).apply {
                layoutParams = cellLayoutParams
                backgroundTintList = colorStateList
                animate().interpolator = animationInterpolator
            }
            cells.add(cell)
            addView(cell)
        }
        selectCell(State.NONE)
    }

    private fun selectCell(state: State) {
        cells.forEach {
            if (it.isSelected) it.isSelected = false
        }

        cells.getOrNull(selectedIndex)?.isSelected = true
        if (selectedIndex == 0) return

        when (state) {
            State.PREVIOUS -> {
                if (maxVisibleCount == 0 || count < maxVisibleCount) return
                cells.forEach {
                    with(it.animate()) {
                        isAnimating.set(true)
                        translationX(it.translationX + cellWidth + cellMargin)
                        withEndAction {
                            isAnimating.set(false)
                        }
                        start()
                    }
                }
            }
            State.NEXT -> {
                if (maxVisibleCount == 0 || count < maxVisibleCount) return
                cells.forEach {
                    with(it.animate()) {
                        isAnimating.set(true)
                        translationX(it.translationX - cellWidth - cellMargin)
                        withEndAction {
                            isAnimating.set(false)
                        }
                        start()
                    }
                }
            }
            State.NONE -> {
                // no-op
            }
        }
    }

    private enum class State {
        PREVIOUS,
        NEXT,
        NONE
    }
}
