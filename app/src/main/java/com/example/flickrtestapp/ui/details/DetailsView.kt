package com.example.flickrtestapp.ui.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface DetailsView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateImageUrl(imageUrl: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(exception: Throwable)
}
