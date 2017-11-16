package com.physphil.android.wattpad.api.model

import com.google.gson.annotations.SerializedName
import com.physphil.android.wattpad.model.Story

/**
 * Copyright (c) 2017 Phil Shadlyn
 */
data class StoriesApiResponse(@SerializedName("stories") val stories: List<Story>,
                              @SerializedName("nextUrl") val nextUrl: String)