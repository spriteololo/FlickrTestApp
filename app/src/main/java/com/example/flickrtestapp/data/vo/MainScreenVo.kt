package com.example.flickrtestapp.data.vo

import android.content.Context
import android.os.Parcelable
import androidx.databinding.Bindable
import com.example.flickrtestapp.Constants
import com.example.flickrtestapp.util.databinding.BaseBindableObservable
import com.example.flickrtestapp.util.databinding.bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.flickrtestapp.R
import kotlinx.android.parcel.Parcelize


@Parcelize
class MainScreenVo : BaseBindableObservable(), Parcelable {

    @get:Bindable
    var query: String by bindable(Constants.EMPTY_STRING, BR.query)

    @get:Bindable
    var resultCount: Int? by bindable(null, BR.resultCount)

    fun getResult(query: String, resultCount: Int?, context: Context): String {
        return if (resultCount != null) {
            val resultCountStr = context.resources.getQuantityString(
                R.plurals.result_plurals,
                resultCount,
                resultCount
            )
            context.getString(R.string.results_for_with_count, query, resultCountStr)
        } else {
            Constants.EMPTY_STRING
        }
    }
}