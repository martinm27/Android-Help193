package hr.fer.help193.vehicle.utils

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager

const val DEFAULT_QUICK_ANIMATION_DURATION = 200L
const val ALPHA_OPAQUE = 1f
const val ALPHA_HALF = 0.5f
const val ALPHA_TRANSPARENT = 0f
private const val VIEW_HEIGHT_CHANGE_ANIMATION_DURATION = 300L
private const val HEIGHT_INVISIBLE = 0

/**
 * Clears focus from view and calls [InputMethodManager.hideSoftInputFromWindow]
 */
fun View.hideKeyboard() {
    clearFocus()
    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

/**
 * View's alpha property will be animated to [ALPHA_OPAQUE] based on [duration]
 */
fun View.fadeIn(duration: Long = DEFAULT_QUICK_ANIMATION_DURATION) {
    animate().alpha(ALPHA_OPAQUE).duration = duration
}

/**
 * View's alpha property will be animated to [ALPHA_TRANSPARENT] based on [duration]
 */
fun View.fadeOut(duration: Long = DEFAULT_QUICK_ANIMATION_DURATION) {
    animate().alpha(ALPHA_TRANSPARENT).duration = duration
}

fun View.show(show: Boolean = true) {
    if (visibility == VISIBLE && show) return
    if (visibility == GONE && !show) return
    visibility = if (show) VISIBLE else GONE
}

fun View.animateViewHeight(from: Int, to: Int, duration: Long = VIEW_HEIGHT_CHANGE_ANIMATION_DURATION) {
    ValueAnimator.ofInt(from, to).run {
        addUpdateListener {
            layoutParams = layoutParams.apply {
                height = it.animatedValue as Int
            }
        }

        this.duration = duration
        start()
    }
}

fun View.expandViewHeight() {
    (parent as View).let {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(it.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(it.height, View.MeasureSpec.UNSPECIFIED)
        measure(widthSpec, heightSpec)
    }
    animateViewHeight(HEIGHT_INVISIBLE, measuredHeight)
}

fun View.collapseViewHeight() {
    animateViewHeight(height, HEIGHT_INVISIBLE)
}
