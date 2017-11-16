package com.physphil.android.wattpad.feed.presenter

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.SearchView
import com.physphil.android.wattpad.R
import com.physphil.android.wattpad.api.WattpadService
import com.physphil.android.wattpad.api.model.StoriesApiResponse
import com.physphil.android.wattpad.feed.view.MainActivityView
import com.physphil.android.wattpad.model.Story
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val KEY_STORIES = "com.physphil.android.wattpad.KEY_STORIES"

/**
 * Presenter class
 * Copyright (c) 2017 Phil Shadlyn
 */
class MainActivityPresenter(private val view: MainActivityView, private val api: WattpadService) : SearchView.OnQueryTextListener {
    /** Keeps a list of active subscriptions */
    private val subscriptions = CompositeDisposable()
    /** List of stories retrieved from API */
    private val stories = arrayListOf<Story>()

    fun onCreate(savedState: Bundle?) {
        if (savedState != null && savedState.containsKey(KEY_STORIES)) {
            // Display the list of stories if we already have it
            stories.addAll(savedState.getParcelableArrayList(KEY_STORIES))
            showListView()
            view.addStories(stories)
        }
        else {
            // Get story list from API
            loadStories()
        }
    }

    fun onDestroy() {
        // Unsubscribe from any active subscriptions
        subscriptions.clear()
    }

    fun saveDataOnConfigChange(outState: Bundle?) {
        outState?.putParcelableArrayList(KEY_STORIES, stories)
    }

    /**
     * Load list of stories from the API
     */
    private fun loadStories() {
        showProgressView()
        subscriptions.add(api.getStories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: StoriesApiResponse ->
                    // on success - show list of stories, or empty view if no stories are returned
                    stories.addAll(response.stories)
                    if (stories.isNotEmpty()) {
                        showListView()
                        view.addStories(stories)
                    }
                    else {
                        showErrorView(R.string.empty_stories_list)
                    }
                }, {
                    // on error - can implement more informative error handling in future
                    showErrorView(R.string.error_loading_stories)
                }))
    }

    private fun showProgressView() {
        view.setProgressVisibility(true)
        view.setListVisibility(false)
        view.setErrorVisibility(false)
    }

    private fun showListView() {
        view.setListVisibility(true)
        view.setProgressVisibility(false)
        view.setErrorVisibility(false)
    }

    private fun showErrorView(@StringRes message: Int) {
        view.setErrorMessage(message)
        view.setErrorVisibility(true)
        view.setProgressVisibility(false)
        view.setListVisibility(false)
    }

    /**
     * Display list of stories whose title contain the supplied query String.
     * @param query String used to filer list based on title
     */
    private fun filterStoryList(query: String) {
        val filtered = stories.filter { it.title.toLowerCase().contains(query.toLowerCase()) }
        if (filtered.isEmpty()) {
            showErrorView(R.string.empty_stories_list)
        }
        else {
            showListView()
            view.filterStories(filtered)
        }
    }

    // region SearchView.QueryTextListener
    override fun onQueryTextSubmit(query: String): Boolean {
        // Pass handling through to SearchView
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        // Show list of filtered stories, absorb handling of action
        filterStoryList(newText)
        return true
    }
    // endregion
}