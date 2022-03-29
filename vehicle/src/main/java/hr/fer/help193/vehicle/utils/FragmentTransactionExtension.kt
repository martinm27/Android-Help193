package hr.fer.help193.vehicle.utils

import androidx.fragment.app.FragmentTransaction
import hr.fer.help193.vehicle.R


fun FragmentTransaction.applyBottomSlideEnterAndExitAnimation() {
    setCustomAnimations(R.anim.fragment_bottom_enter, R.anim.nothing, R.anim.nothing, R.anim.fragment_bottom_exit)
}

fun FragmentTransaction.applyFadeInEnterAndFadeOutExitAnimation() {
    setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
}

fun FragmentTransaction.applyFadeInEnterAnimation() {
    setCustomAnimations(R.anim.fade_in_animation, R.anim.nothing, R.anim.nothing, R.anim.nothing)
}

fun FragmentTransaction.applyFadeOutExitAnimation() {
    setCustomAnimations(R.anim.nothing, R.anim.fade_out_animation, R.anim.nothing, R.anim.nothing)
}
