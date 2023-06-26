package com.openclassrooms.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity(), EstateListFragment.EstateListFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.activity_main_fragment_container, EstateListFragment(this), null)
            .setReorderingAllowed(false)
            .commit()

        if (resources.getBoolean(R.bool.isLandscapeMode)) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_right, EstateSheetFragment(), null)
                .setReorderingAllowed(false)
                .commit()
        }
    }

    override fun launchEstateSheetFragment() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_right, EstateSheetFragment::class.java, null)
                .setReorderingAllowed(false)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, EstateSheetFragment::class.java, null)
                .setReorderingAllowed(false)
                .addToBackStack(null)
                .commit()
        }
    }

}