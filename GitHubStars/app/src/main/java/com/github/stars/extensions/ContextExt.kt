package com.github.stars.extensions

import android.content.Context
import android.util.DisplayMetrics


val Context.windowWidth: Int
    get() {
        val metrics = DisplayMetrics()
        //windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

val Context.windowHeight: Int
    get() {
        val metrics = DisplayMetrics()
       // windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }