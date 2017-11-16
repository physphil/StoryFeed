package com.physphil.android.wattpad.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2017 Phil Shadlyn
 */
data class User(@SerializedName("name") val name: String,
                @SerializedName("avatar") val avatar: String,
                @SerializedName("fullname") val fullName: String) : Parcelable {


    // region Parcelable implementation
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(avatar)
        parcel.writeString(fullName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
    // endregion
}