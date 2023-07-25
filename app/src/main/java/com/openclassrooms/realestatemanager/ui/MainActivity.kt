package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate

class MainActivity :
    AppCompatActivity(),
    EstateListFragment.EstateListFragmentListener,
    EstateSheetFragment.EstateSheetFragmentListener,
    EstateEditFragment.EstateEditFragmentListener,
    EstateListFilterFragment.EstateListFilterFragmentListener,
    MapFragment.MapFragmentListener {

    private lateinit var viewModel: MainActivityViewModel
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainActivityViewModel::class.java]
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        supportFragmentManager.beginTransaction()
            .add(R.id.activity_main_fragment_container, EstateListFragment(this), null)
            .setReorderingAllowed(false)
            .commit()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_right, EstateSheetFragment(this), null)
                .setReorderingAllowed(false)
                .commit()
        }
    }

    override fun launchEstateListFragment() {
        replaceFragment(EstateListFragment(this))
    }

    override fun launchEstateSheetFragment(estate: Estate) {
        viewModel.setSelectedEstate(estate)
        replaceFragment(EstateSheetFragment(this))
    }

    override fun launchEstateEditFragment(estate: Estate) {
        viewModel.setSelectedEstate(estate)
        replaceFragment(EstateEditFragment(EstateEditFragment.Setting.EDIT, this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_add_estate -> replaceFragment(EstateEditFragment(EstateEditFragment.Setting.ADD,this))
            R.id.menu_main_loan_simulator -> {
                val intent = Intent(this, LoanSimulatorActivity::class.java)
                ActivityCompat.startActivity(this, intent, null)
            }
            R.id.menu_main_map -> replaceFragment(MapFragment(this))
            R.id.menu_main_filter_list_estate -> replaceFragment(EstateListFilterFragment(this))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && fragment !is EstateListFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container_right, fragment, null)
                .setReorderingAllowed(false)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, fragment, null)
                .setReorderingAllowed(false)
                .commit()
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                replaceFragment(EstateSheetFragment(activity))
            } else {
                replaceFragment(EstateListFragment(activity))
            }
        }
    }

}