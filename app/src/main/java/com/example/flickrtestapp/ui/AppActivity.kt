package com.example.flickrtestapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flickrtestapp.R
import com.example.flickrtestapp.network.NetworkChangeReceiver
import com.example.flickrtestapp.util.RxUtils
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_app_main.*
import org.koin.android.ext.android.get

class AppActivity : AppCompatActivity() {

    private var networkStateChangeDisposable: Disposable? = null
    private lateinit var snack: Snackbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)
        registerNetworkBroadcast()

        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }

    private fun registerNetworkBroadcast() {
        networkStateChangeDisposable = get<NetworkChangeReceiver>().connectivitySubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::hideToastNoInternetConnection)
    }

    private fun hideToastNoInternetConnection(hide: Boolean){
        if (hide) {
            if(::snack.isInitialized){
                snack.dismiss()
            }
        } else {
            Snackbar.make(root, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
                .also { snack = it }
                .show()
        }
    }

    private fun unregisterNetworkChanges() {
        RxUtils.dispose(networkStateChangeDisposable)
    }

    override fun onDestroy() {
        unregisterNetworkChanges()
        super.onDestroy()
    }

    fun showAppBarAndBackBtn(appBarVisibility: Boolean, visible: Boolean) {
        toolbar.visibility = if (appBarVisibility) View.VISIBLE else View.GONE
        iv_toolbar_back.visibility = if (visible) View.VISIBLE else View.GONE
    }
}