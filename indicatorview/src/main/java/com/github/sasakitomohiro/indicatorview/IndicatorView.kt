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

const val NO_INDEX = -1

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

    var currentIndex = NO_INDEX
        set(value) {
            if (value < 0 || count < 1 || count - 1 < value) return
            val state =
                if (field > value) State.PREVIOUS else if (field < value) State.NEXT else State.NONE
            field = value
            selectCell(state)
        }

    private var visibleIndex = NO_INDEX

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
        clipChildren = false
    }

    override fun setOrientation(orientation: Int) {
        // do nothing
    }

    fun setIndicatorCellFactory(factory: IndicatorCellFactory<View>) {
        this.factory = factory
        addIndicatorCell()
    }

    fun next(): Int {
        val nextIndex = currentIndex + 1
        if (count - 1 < nextIndex || isAnimating.get()) return currentIndex
        currentIndex = nextIndex
        return currentIndex
    }

    fun previous(): Int {
        val previousIndex = currentIndex - 1
        if (previousIndex < 0 || isAnimating.get()) return currentIndex
        currentIndex = previousIndex
        return currentIndex
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
        addIndicatorCell()
    }

    fun setAnimationDuration(duration: Long) {
        animationDuration = duration
        addIndicatorCell()
    }

    private fun initialize() {
        removeAllViews()
        cells.clear()
        currentIndex = 0
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

        val calcWidth = if (maxVisibleCount != 0 && maxVisibleCount < count) {
            maxVisibleCount * cellWidth + (maxVisibleCount - 1) * cellMargin
        } else {
            count * cellWidth + (count - 1) * cellMargin
        }
        layoutParams.width = calcWidth
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
                animate().duration = animationDuration
            }
            cells.add(cell)
            addView(cell)
        }
        selectCell(State.NONE)
    }

    private fun selectCell(state: State) {
        val prevIndex = cells.indexOfFirst { it.isSelected }.let {
            if (it > -1) it else 0
        }
        cells.forEach {
            if (it.isSelected) it.isSelected = false
        }

        cells.getOrNull(currentIndex)?.isSelected = true

        when (state) {
            State.PREVIOUS -> {
                if (maxVisibleCount == 0 || count < maxVisibleCount) return
                if (visibleIndex in 0 until maxVisibleCount) {
                    visibleIndex--
                    return
                }
                move(cellWidth + cellMargin)
            }
            State.NEXT -> {
                if (maxVisibleCount == 0 || count < maxVisibleCount) return
                if (maxVisibleCount - 2 > visibleIndex) {
                    visibleIndex++
                    return
                }
                move(-cellWidth - cellMargin)
            }
            State.NONE -> {
                // no-op
            }
        }
    }

//    private fun selectCell(state: State) {
//        val prevIndex = cells.indexOfFirst { it.isSelected }.let {
//            if (it > -1) it else 0
//        }
//        cells.forEach {
//            if (it.isSelected) it.isSelected = false
//        }
//
//        cells.getOrNull(currentIndex)?.isSelected = true
//        if (currentIndex == 0) return
//
//        when (state) {
//            State.PREVIOUS -> {
//                if (maxVisibleCount == 0 || count < maxVisibleCount) return
//                cells.forEach {
//                    with(it.animate()) {
//                        isAnimating.set(true)
//                        translationX(it.translationX + cellWidth + cellMargin)
//                        withEndAction {
//                            isAnimating.set(false)
//                        }
//                        start()
//                    }
//                }
//            }
//            State.NEXT -> {
//                if (maxVisibleCount == 0 || count < maxVisibleCount) return
//                cells.forEach {
//                    with(it.animate()) {
//                        isAnimating.set(true)
//                        translationX(it.translationX - cellWidth - cellMargin)
//                        withEndAction {
//                            isAnimating.set(false)
//                        }
//                        start()
//                    }
//                }
//            }
//            State.NONE -> {
//                // no-op
//            }
//        }
//    }

    private fun move(translationX: Int) {
        cells.forEach {
            with(it.animate()) {
                isAnimating.set(true)
                translationX(it.translationX + translationX)
                withEndAction {
                    isAnimating.set(false)
                }
                start()
            }
        }
    }

    private enum class State {
        PREVIOUS,
        NEXT,
        NONE
    }
}
