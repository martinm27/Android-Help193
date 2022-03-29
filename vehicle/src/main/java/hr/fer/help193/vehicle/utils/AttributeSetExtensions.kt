package hr.fer.help193.vehicle.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes

inline fun AttributeSet.withTypedArray(context: Context, @StyleableRes attrs: IntArray, block: TypedArray.() -> Unit) {
    with(context.obtainStyledAttributes(this, attrs, 0, 0)) {
        block()
        recycle()
    }
}
