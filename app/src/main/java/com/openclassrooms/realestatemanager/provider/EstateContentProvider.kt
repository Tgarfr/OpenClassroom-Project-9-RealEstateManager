package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.di.Injection
import com.openclassrooms.realestatemanager.repository.EstateRepository

class EstateContentProvider: ContentProvider() {

    private lateinit var estateRepository: EstateRepository

    companion object {
        const val AUTHORITY = "com.openclassrooms.realestatemanager"
        const val TABLE_NAME = "estate"
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        estateRepository = Injection.getInstance(context).estateRepository
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor =
        estateRepository.getEstateListCursor()

    override fun getType(uri: Uri): String = "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0

}