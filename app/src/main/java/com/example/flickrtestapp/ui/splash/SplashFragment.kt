package com.example.flickrtestapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.flickrtestapp.R
import com.example.flickrtestapp.util.extensions.navigateSafe
import com.example.flickrtestapp.ui.common.BaseInstanceSaverFragment

class SplashFragment: BaseInstanceSaverFragment()  {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigateSafe(R.id.action_SplashFragment_to_MainFragment)
        }, 1000) //TODO replace with some loading if necessary
    }
}