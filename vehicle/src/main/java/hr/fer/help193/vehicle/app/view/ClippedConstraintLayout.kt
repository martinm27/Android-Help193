package hr.fer.help193.vehicle.app.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Wrapper around ConstraintLayout which adds `clipToOutline = true`
 */
class ClippedConstraintLayout : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        clipToOutline = true
    }
}
