package com.donfyy.util

object Calculator {
    @JvmStatic
    fun getScaledDistance(offset: Float, offsetScale: Float, targetScale: Float) = offset * targetScale / offsetScale
}