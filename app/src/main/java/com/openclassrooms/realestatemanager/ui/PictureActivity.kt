package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Picture

class PictureActivity: AppCompatActivity() {

    private lateinit var viewModel: PictureActivityViewModel
    private lateinit var pictureImageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var leftButton: ImageView
    private lateinit var rightButton: ImageView
    private lateinit var deleteButton: ImageView

    private lateinit var picture: Picture
    private var pictureList: List<Picture>? = null

    companion object {
        const val BUNDLE_PICTURE_ID_KEY: String = "pictureId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[PictureActivityViewModel::class.java]
        initViews()

        intent.extras?.getLong(BUNDLE_PICTURE_ID_KEY)?.let { pictureId ->
            viewModel.getPictureById(pictureId)?.let { picture ->
                updatePicture(picture) }
        } ?: finish()

        viewModel.getPictureListByEstateIdLiveData(picture.estateId).observe(this) { pictureList ->
            this.pictureList = pictureList
            updateButtons()
        }
        deleteButton.setOnClickListener {
            viewModel.deletePicture(this.picture)
            finish()
        }

        pictureImageView.setOnClickListener { finish() }
    }

    private fun initViews() {
        pictureImageView = findViewById(R.id.picture_background)
        descriptionTextView = findViewById(R.id.picture_description)
        leftButton = findViewById(R.id.picture_icon_left)
        rightButton = findViewById(R.id.picture_icon_right)
        deleteButton = findViewById(R.id.picture_icon_delete)
    }

    private fun updatePicture(picture: Picture) {
        this.picture = picture
        Glide.with(this)
            .load(picture.getUri())
            .centerCrop()
            .into(pictureImageView)
        descriptionTextView.text = picture.description
        updateButtons()
    }

    private fun updateButtons() {
        val notMutablePictureList = this.pictureList
        if (!notMutablePictureList.isNullOrEmpty()) {
            when (notMutablePictureList.indexOf(picture)) {
                0 -> {
                    leftButton.isGone = true
                    rightButton.isGone = false
                    rightButton.setOnClickListener { updatePicture(notMutablePictureList[notMutablePictureList.indexOf(picture) + 1]) }
                }
                1 -> {
                    leftButton.isGone = false
                    rightButton.isGone = true
                    leftButton.setOnClickListener { updatePicture(notMutablePictureList[notMutablePictureList.indexOf(picture) - 1]) }
                }
                else -> {
                    leftButton.isGone = false
                    rightButton.isGone = false
                    leftButton.setOnClickListener { updatePicture(notMutablePictureList[notMutablePictureList.indexOf(picture) - 1]) }
                    rightButton.setOnClickListener { updatePicture(notMutablePictureList[notMutablePictureList.indexOf(picture) + 1]) }
                }
            }
        }
    }

}