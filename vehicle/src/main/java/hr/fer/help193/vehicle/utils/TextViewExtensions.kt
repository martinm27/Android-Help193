package hr.fer.help193.vehicle.utils

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat

/**
 * Wrapper around TextViewCompat.setTextAppearance.
 * Note: sets only this values (ellipsize/locale/includeFontPadding etc. is not set by this method)
 * textColorHighlight
 * textColor
 * textColorHint
 * textColorLink
 * textSize
 * typeface
 * fontFamily
 * textStyle
 * textFontWeight
 * textAllCaps
 * shadowColor
 * shadowDx
 * shadowDy
 * shadowRadius
 * elegantTextHeight
 * fallbackLineSpacing
 * letterSpacing
 * fontFeatureSettings
 */
fun TextView.setTextAppearanceCompat(@StyleRes styleRes: Int) = TextViewCompat.setTextAppearance(this, styleRes)

/**
 * Wrapper around setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom) which sets only the left drawable and removes others
 */
fun TextView.setLeftDrawable(@DrawableRes drawableRes: Int) = setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)

/**
 * Wrapper around setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom) which sets every drawable to 0
 */
fun TextView.removeDrawables() = setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
