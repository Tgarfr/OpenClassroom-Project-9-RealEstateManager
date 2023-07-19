package com.openclassrooms.realestatemanager.model

import android.net.Uri

data class EstateItem(
    val estate: Estate,
    val pictureUri: Uri?
    )