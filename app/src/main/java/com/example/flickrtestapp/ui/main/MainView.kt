package com.example.flickrtestapp.ui.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.flickrtestapp.data.vo.PhotoVo

interface MainView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateRecent(items: List<PhotoVo>, clearItems: Boolean, hasNextPage: Boolean)
}
