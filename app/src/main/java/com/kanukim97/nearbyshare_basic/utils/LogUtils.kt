package com.kanukim97.nearbyshare_basic.utils

import android.util.Log
import com.kanukim97.nearbyshare_basic.constant.Constant

/**
 * 2024.11.09
 *
 * LogUtils
 *
 * @author KanuKim97
 */
object LogUtils {
    fun logD(msg: String) {
        Log.d(Constant.DEBUG_TAG, msg)
    }

    fun logE(msg: String, tr: Throwable? = null) {
        Log.e(Constant.ERROR_TAG, msg, tr)
    }

    fun logW(msg: String, tr: Throwable? = null) {
        Log.w(Constant.WARN_TAG, msg, tr)
    }

    fun logI(msg: String, tr: Throwable? = null) {
        Log.i(Constant.INFO_TAG, msg, tr)
    }
}