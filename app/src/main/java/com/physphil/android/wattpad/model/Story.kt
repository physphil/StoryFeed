package com.physphil.android.wattpad.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2017 Phil Shadlyn
 */
data class Story(@SerializedName("id") val id: String,
                 @SerializedName("title") val title: String,
                 @SerializedName("user") val user: User,
                 @SerializedName("cover") val cover: String) : Parcelable {


    // region Parcelable implementation
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(User::class.java.classLoader) as User,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeValue(user)
        parcel.writeString(cover)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Story> {
        override fun createFromParcel(parcel: Parcel): Story {
            return Story(parcel)
        }

        override fun newArray(size: Int): Array<Story?> {
            return arrayOfNulls(size)
        }
    }
    // endregion
}