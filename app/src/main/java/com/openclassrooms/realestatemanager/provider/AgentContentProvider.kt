package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.di.Injection
import com.openclassrooms.realestatemanager.repository.AgentRepository

class AgentContentProvider: ContentProvider() {

    private lateinit var agentRepository: AgentRepository

    companion object {
        const val AUTHORITY = "com.openclassrooms.realestatemanager.agent"
        const val TABLE_NAME = "agent"
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        agentRepository = Injection.getInstance(context).agentRepository
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor =
        agentRepository.getAgentListCursor()

    override fun getType(uri: Uri): String = "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0

}