package com.physphil.android.wattpad.feed.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.physphil.android.wattpad.R
import com.physphil.android.wattpad.api.WattpadApi
import com.physphil.android.wattpad.feed.presenter.MainActivityPresenter
import com.physphil.android.wattpad.model.Story
import com.physphil.android.wattpad.util.setVisibility

class MainActivity : AppCompatActivity(), MainActivityView {

    // Use ButterKnife to get references to all views
    @BindView(R.id.main_story_list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.main_progress_view)
    lateinit var progressView: View

    @BindView(R.id.main_error_text)
    lateinit var errorView: TextView

    // TODO - inject these dependencies with Dagger, instead of through constructor
    /** Presenter for this class. App uses Model-View-Presenter (MVP) architecture */
    private val presenter = MainActivityPresenter(this, WattpadApi.service)

    /** Adapter for the RecyclerView containing the list of stories */
    private val adapter = StoryListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        // Setup recycler view
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Initialize presenter, get list of stories
        presenter.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        // cleanup presenter
        presenter.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        presenter.saveDataOnConfigChange(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Presenter will handle user searches
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setOnQueryTextListener(presenter)

        return true
    }

    // region MainActivityView implementation
    override fun setListVisibility(visible: Boolean) {
        recyclerView.setVisibility(visible)
    }

    override fun setProgressVisibility(visible: Boolean) {
        progressView.setVisibility(visible)
    }

    override fun setErrorVisibility(visible: Boolean) {
        errorView.setVisibility(visible)
    }

    override fun addStories(stories: List<Story>) {
        adapter.addStories(stories)
    }

    override fun filterStories(stories: List<Story>) {
        adapter.filterStories(stories)
    }

    override fun setErrorMessage(message: Int) {
        errorView.setText(message)
    }
    // endregion
}
