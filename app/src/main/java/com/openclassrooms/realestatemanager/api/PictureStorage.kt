package com.openclassrooms.realestatemanager.api

import android.content.Context
import androidx.core.net.toUri
import com.openclassrooms.realestatemanager.model.Picture
import java.io.File
import java.io.IOException

class PictureStorage(val context: Context): PictureStorageApi {

    override fun addPicture(picture: Picture): Picture? {
        try {
            val inputStream = context.contentResolver.openInputStream(picture.getUri())
            val outputFile = File(context.filesDir, picture.getFileName())
            val outputStream = outputFile.outputStream()

            if (inputStream != null) {
                inputStream.use { input ->
                    outputStream.use {output ->
                        input.copyTo(output)
                    }
                }
                inputStream.close()
                outputStream.close()

                if (outputFile.exists()) {
                    return Picture(picture.id, picture.estateId, outputFile.toUri(), picture.description)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun deletePicture(picture: Picture) {
        File(context.filesDir, picture.getFileName()).delete()
    }

}