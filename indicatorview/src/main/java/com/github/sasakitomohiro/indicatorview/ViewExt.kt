package com.github.sasakitomohiro.indicatorview

import android.content.res.Resources

private val metrics = Resources.getSystem().displayMetrics

internal fun Int.dpToPx() = this * (metrics.densityDpi / 160f)

internal fun Int.pxToDp() = (this / (metrics.densityDpi / 160f)).toInt()
