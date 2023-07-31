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
    private var currentFragmentName: String?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[MainActivityViewModel::class.java]
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (savedInstanceState == null || isLargeAndLandscapeScreen()) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container, EstateListFragment(), null)
                .setReorderingAllowed(false)
                .commit()
        } else {
            currentFragmentName?.let { fragmentName ->
                supportFragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment_container, Class.forName(fragmentName).newInstance() as Fragment, null)
                    .setReorderingAllowed(false)
                    .commit()
            }
        }

        if (isLargeAndLandscapeScreen()) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_right, EstateSheetFragment(), null)
                .setReorderingAllowed(false)
                .commit()
        }
    }

    override fun launchEstateListFragment() {
        replaceFragment(EstateListFragment())
    }

    override fun launchEstateSheetFragment(estate: Estate) {
        viewModel.setSelectedEstate(estate)
        replaceFragment(EstateSheetFragment())
    }

    override fun launchEstateEditFragment(estate: Estate) {
        viewModel.setSelectedEstate(estate)
        val bundle = Bundle()
        bundle.putInt(EstateEditFragment.ESTATE_EDIT_FRAGMENT_SETTING, EstateEditFragment.Setting.EDIT.value)
        val estateEditFragment = EstateEditFragment()
        estateEditFragment.arguments = bundle
        replaceFragment(estateEditFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_add_estate ->  {
                val bundle = Bundle()
                bundle.putInt(EstateEditFragment.ESTATE_EDIT_FRAGMENT_SETTING, EstateEditFragment.Setting.ADD.value)
                val estateEditFragment = EstateEditFragment()
                estateEditFragment.arguments = bundle
                replaceFragment(estateEditFragment)
            }
            R.id.menu_main_loan_simulator -> {
                val intent = Intent(this, LoanSimulatorActivity::class.java)
                ActivityCompat.startActivity(this, intent, null)
            }
            R.id.menu_main_map -> replaceFragment(MapFragment())
            R.id.menu_main_filter_list_estate -> replaceFragment(EstateListFilterFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        currentFragmentName = fragment::class.simpleName
        if (isLargeAndLandscapeScreen() && fragment !is EstateListFragment) {
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
            if (isLargeAndLandscapeScreen()) {
                replaceFragment(EstateSheetFragment())
            } else {
                replaceFragment(EstateListFragment())
            }
        }
    }

    private fun isLargeAndLandscapeScreen(): Boolean {
        val configuration = resources.configuration
        val screenLayoutSize = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && screenLayoutSize >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}