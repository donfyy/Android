package com.donfyy.skinlib

import android.content.Context

class SkinPreference(context: Context) {
    companion object {
        private const val SKIN_SP = "skin_sp"
        private const val SKIN_PATH = "skin_path"
    }

    val sp = context.getSharedPreferences(SKIN_SP, Context.MODE_PRIVATE)

    var skinPath: String
        set(value) {
            sp.edit().putString(SKIN_PATH, value).apply()
        }
        get() {
            return sp.getString(SKIN_PATH, "") ?: ""
        }

    fun reset() {
        sp.edit().remove(SKIN_PATH).apply()
    }
}