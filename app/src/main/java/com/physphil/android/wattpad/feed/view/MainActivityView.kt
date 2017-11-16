package com.physphil.android.wattpad.feed.view

import android.support.annotation.StringRes
import com.physphil.android.wattpad.model.Story

/**
 * Copyright (c) 2017 Phil Shadlyn
 */
interface MainActivityView {
    fun setListVisibility(visible: Boolean)
    fun setProgressVisibility(visible: Boolean)
    fun setErrorVisibility(visible: Boolean)
    fun addStories(stories: List<Story>)
    fun filterStories(stories: List<Story>)
    fun setErrorMessage(@StringRes message: Int)
}