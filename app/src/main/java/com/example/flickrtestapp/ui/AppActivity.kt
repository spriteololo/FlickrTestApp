package com.example.flickrtestapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flickrtestapp.R
import kotlinx.android.synthetic.main.activity_app_main.*

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }

    fun showAppBarAndBackBtn(appBarVisibility: Boolean, visible: Boolean) {
        toolbar.visibility = if (appBarVisibility) View.VISIBLE else View.GONE
        iv_toolbar_back.visibility = if (visible) View.VISIBLE else View.GONE
    }
}