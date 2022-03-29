package hr.fer.help193.vehicle.utils

import android.content.res.TypedArray

inline fun TypedArray.withIntDimension(index: Int, defaultValue: Int = 0, block: (Int) -> Unit) {
    block(getDimension(index, defaultValue.toFloat()).toInt())
}
