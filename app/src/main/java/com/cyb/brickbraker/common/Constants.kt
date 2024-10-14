package com.cyb.brickbraker.common

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Constants {
    lateinit var context: Context

    // 화면 세로 길이
    val screenHeight: Int
        get() = context.resources.displayMetrics.heightPixels

    // 화면 가로 길이
    val screenWidth: Int
        get() = context.resources.displayMetrics.widthPixels

    // 초기화
    fun init(context: Context) {
        this.context = context
    }
}