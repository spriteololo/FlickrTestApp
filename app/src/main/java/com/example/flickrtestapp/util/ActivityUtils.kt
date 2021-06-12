package com.example.flickrtestapp.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object ActivityUtils {
    fun closeKeyboard(activity: Activity?) {
        if (activity != null && activity.currentFocus != null) {
            val inputMethodManager = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }
}