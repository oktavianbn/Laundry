package com.example.laundry.model_data

import android.os.Parcel
import android.os.Parcelable

class ModelTambahan (
    val idTambahan:String?=null,
    val namaTambahan:String?=null,
    val hargaTambahan:Int?=null,
    val cabangTambahan:String?=null,
    val terdaftar:String?=null,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idTambahan)
        parcel.writeString(namaTambahan)
        parcel.writeValue(hargaTambahan)
        parcel.writeString(cabangTambahan)
        parcel.writeString(terdaftar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelTambahan> {
        override fun createFromParcel(parcel: Parcel): ModelTambahan {
            return ModelTambahan(parcel)
        }

        override fun newArray(size: Int): Array<ModelTambahan?> {
            return arrayOfNulls(size)
        }
    }
}