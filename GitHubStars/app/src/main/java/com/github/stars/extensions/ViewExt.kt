package com.github.stars.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.setDimensions(x: Int, y: Int) {
    this.layoutParams.width = x
    this.layoutParams.height = y

    this.requestLayout()
}

fun updateDataList(dataList: MutableList<Int>) : List<Int> {
    kotlin.repeat(30) {
        dataList.add(dataList.size + 1)
    }
    return dataList
}