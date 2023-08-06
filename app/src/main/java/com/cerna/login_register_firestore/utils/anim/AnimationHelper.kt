package com.cerna.login_register_firestore.utils.anim

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

object AnimationHelper {

    fun startCircleAnimationWithDelay(
        circleView: View,
        fragment: Fragment,
        animationResourceId: Int,
        duration: Long,
        onAnimationEnd: () -> Unit
    ) {
        val animation = AnimationUtils.loadAnimation(fragment.requireContext(), animationResourceId).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
        }
        circleView.isVisible = true
        circleView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            onAnimationEnd.invoke()
            circleView.isVisible = false
        }, duration)
    }
}
