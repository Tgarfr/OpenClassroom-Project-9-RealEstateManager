package com.openclassrooms.realestatemanager.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = Picture.ROOM_TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Estate::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("estateId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Picture(
    @PrimaryKey val id: Long,
    @ColumnInfo(index = true) val estateId: Long,
    val stringUri: String,
    val description: String
) {

    constructor(id: Long, estateId: Long, uri: Uri, description: String) :
            this (id, estateId, uri.toString(), description)

    companion object {
        const val ROOM_TABLE_NAME: String = "picture"
        private const val fileNameFormat: String = "Picture-%d.jpg"
    }

    fun getUri(): Uri = Uri.parse(this.stringUri)

    fun getFileName(): String {
        return String.format(fileNameFormat, id)
    }

}