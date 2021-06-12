package com.example.flickrtestapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.flickrtestapp.R
import com.example.flickrtestapp.extensions.navigateSafe
import com.example.flickrtestapp.ui.FlowFragment

class SplashFragment: FlowFragment()  {
    override val layoutRes = R.layout.fragment_splash
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigateSafe(R.id.action_SplashFragment_to_MainFragment)
        }, 1000)
    }
}