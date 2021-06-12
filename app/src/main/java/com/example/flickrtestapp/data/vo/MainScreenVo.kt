package com.example.flickrtestapp.data.vo

import android.os.Parcelable
import androidx.databinding.Bindable
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.util.databinding.BaseBindableObservable
import com.example.flickrtestapp.util.databinding.bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.android.parcel.Parcelize


@Parcelize
class MainScreenVo: BaseBindableObservable(), Parcelable {

    @get:Bindable
    var query: String by bindable(Constants.EMPTY_STRING, BR.query)
}