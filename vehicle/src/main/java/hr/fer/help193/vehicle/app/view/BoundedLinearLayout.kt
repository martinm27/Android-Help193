package hr.fer.help193.vehicle.app.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.utils.withIntDimension
import hr.fer.help193.vehicle.utils.withTypedArray

private const val NOT_SPECIFIED = -1

/**
 * A LinearLayout that can be constrained to a maximum size.
 *
 * Android doesn't support setting a dimension to match_parent and to bound it to a max value.
 */
class BoundedLinearLayout : LinearLayout {

    private var maxWidth = NOT_SPECIFIED
    private var maxHeight = NOT_SPECIFIED

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        readAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        readAttrs(attrs)
    }

    private fun readAttrs(attrs: AttributeSet?) = attrs?.withTypedArray(context, R.styleable.BoundedLinearLayout) {
        withIntDimension(R.styleable.BoundedLinearLayout_maxWidth, NOT_SPECIFIED) { dimension ->
            maxWidth = dimension
        }

        withIntDimension(R.styleable.BoundedLinearLayout_maxHeight, NOT_SPECIFIED) { dimension ->
            maxHeight = dimension
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(getConstrainedMeasureSpec(maxWidth, widthMeasureSpec), getConstrainedMeasureSpec(maxHeight, heightMeasureSpec))
    }

    private fun getConstrainedMeasureSpec(maxSize: Int, spec: Int): Int {
        if (maxSize == NOT_SPECIFIED) {
            return spec
        }

        if (MeasureSpec.getSize(spec) <= maxSize) { // actualSize <= maSize
            return spec
        }

        val mode = MeasureSpec.getMode(spec).let { if (it == MeasureSpec.UNSPECIFIED) MeasureSpec.AT_MOST else it }
        return MeasureSpec.makeMeasureSpec(maxSize, mode)
    }
}

