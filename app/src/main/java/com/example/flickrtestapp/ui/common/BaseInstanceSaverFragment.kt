package com.example.flickrtestapp.ui.common

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.flickrtestapp.network.NetworkChangeReceiver
import com.example.flickrtestapp.util.RxUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.android.ext.android.get

abstract class BaseInstanceSaverFragment : MvpAppCompatFragment() {
    private var tempBundle: Bundle? = null
    private var networkStateChangeDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tempBundle = savedInstanceState?.clone() as Bundle?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tempBundle = null
        super.onViewCreated(view, savedInstanceState)
        registerNetworkBroadcast()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        if (tempBundle != null) {
            outState.putAll(tempBundle)
        }
        super.onSaveInstanceState(outState)
    }

    protected open fun onNetworkStateChanged(networkAvailable: Boolean){}

    private fun registerNetworkBroadcast() {
        networkStateChangeDisposable = get<NetworkChangeReceiver>().connectivitySubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::onNetworkStateChanged)
    }

    private fun unregisterNetworkChanges() {
        RxUtils.dispose(networkStateChangeDisposable)
    }


    override fun onDestroyView() {
        unregisterNetworkChanges()
        super.onDestroyView()
    }
}