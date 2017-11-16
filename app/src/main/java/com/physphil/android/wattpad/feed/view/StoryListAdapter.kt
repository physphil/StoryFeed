package com.physphil.android.wattpad.feed.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.physphil.android.wattpad.R
import com.physphil.android.wattpad.model.Story
import com.squareup.picasso.Picasso

/**
 * RecyclerView Adapter to display list of stories
 * Copyright (c) 2017 Phil Shadlyn
 */
class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    interface StoryListAdapterClickListener {
        /**
         * Triggered when a user clicks on a story from the list
         * @param story the Story the user clicked on
         */
        fun onStoryClicked(story: Story)
    }

    /** List of stories being shown to the user */
    private val storyList = mutableListOf<Story>()
    private var listener: StoryListAdapterClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        // Use ButterKnife to get references to all views
        @BindView(R.id.story_list_cover_image)
        lateinit var cover: ImageView

        @BindView(R.id.story_list_title)
        lateinit var title: TextView

        @BindView(R.id.story_list_author)
        lateinit var author: TextView

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener?.onStoryClicked(storyList[adapterPosition])
        }
    }

    override fun getItemCount() = storyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = storyList[position]

        // Bind Story data to list item.
        // Display the user's full name as the author if it's available, otherwise use their username.
        holder.title.text = story.title
        holder.author.text = if (story.user.fullName.isNotEmpty()) story.user.fullName else story.user.name
        Picasso.with(holder.cover.context)
                .load(story.cover)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.cover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_story_list, parent, false)
        return ViewHolder(view)
    }

    /**
     * Add stories at the bottom of the currently displayed list of stories
     * @param stories list of stories to add
     */
    fun addStories(stories: List<Story>) {
        val start = storyList.size
        storyList.addAll(stories)
        notifyItemRangeInserted(start, stories.size)
    }

    /**
     * Filter the list of stories, only showing the ones in the supplied list
     * @param stories the list of stories to show
     */
    fun filterStories(stories: List<Story>) {
        // Clear the list and notify the adapter to keep data in sync, before adding more stories
        storyList.clear()
        notifyDataSetChanged()
        addStories(stories)
    }

    /**
     * Set an OnClickListener for this adapter, to receive a callback when a user clicks on a story in the list
     * @param listener a StoryListAdapterClickListener to receive callbacks
     */
    fun setOnClickListener(listener: StoryListAdapterClickListener) {
        this.listener = listener
    }
}