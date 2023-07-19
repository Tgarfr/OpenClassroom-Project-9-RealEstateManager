package com.openclassrooms.realestatemanager.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.SearchCriteria

class EstateListFilterFragment(
    private val estateListFilterFragmentListener: EstateListFilterFragmentListener
): Fragment() {

    private lateinit var viewModel: EstateListFilterFragmentViewModel
    private lateinit var typeSpinner: Spinner
    private var type: Estate.Type? = null

    private lateinit var minPriceEditText: EditText
    private lateinit var maxPriceEditText: EditText
    private lateinit var minSurfaceEditText: EditText
    private lateinit var maxSurfaceEditText: EditText
    private lateinit var minNumberOfRoomsEditText: EditText
    private lateinit var maxNumberOfRoomsEditText: EditText
    private lateinit var minNumberOfBathroomsEditText: EditText
    private lateinit var maxNumberOfBathroomsEditText: EditText
    private lateinit var minNumberOfBedroomsEditText: EditText
    private lateinit var maxNumberOfBedroomsEditText: EditText
    private lateinit var minSchoolDistanceEditText: EditText
    private lateinit var maxSchoolDistanceEditText: EditText
    private lateinit var minShopDistanceEditText: EditText
    private lateinit var maxShopDistanceEditText: EditText
    private lateinit var minParcDistanceEditText: EditText
    private lateinit var maxParcDistanceEditText: EditText
    private lateinit var minNumberOfPicturesEditText: EditText
    private lateinit var maxNumberOfPicturesEditText: EditText
    private lateinit var putOnTheMarketSinceEditText: EditText
    private lateinit var soldSinceEditText: EditText
    private lateinit var sectorEditText: EditText

    interface EstateListFilterFragmentListener {
        fun launchEstateListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_filter_list_estate, container, false)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateListFilterFragmentViewModel::class.java]
        initView(view)
        setData()

        view.findViewById<Button>(R.id.fragment_filter_list_estate_button_search).setOnClickListener(onClickSearchButton)

        return view
    }

    private fun initView(view: View) {
        minPriceEditText = view.findViewById(R.id.fragment_filter_list_estate_price_min)
        maxPriceEditText = view.findViewById(R.id.fragment_filter_list_estate_price_max)
        minSurfaceEditText = view.findViewById(R.id.fragment_filter_list_estate_surface_min)
        maxSurfaceEditText = view.findViewById(R.id.fragment_filter_list_estate_surface_max)
        minNumberOfRoomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_rooms_min)
        maxNumberOfRoomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_rooms_max)
        minNumberOfBathroomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_bathrooms_min)
        maxNumberOfBathroomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_bathrooms_max)
        minNumberOfBedroomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_bedrooms_min)
        maxNumberOfBedroomsEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_bedrooms_max)
        minSchoolDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_school_min)
        maxSchoolDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_school_max)
        minShopDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_shop_min)
        maxShopDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_shop_max)
        minParcDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_park_min)
        maxParcDistanceEditText = view.findViewById(R.id.fragment_filter_list_estate_distance_park_max)
        minNumberOfPicturesEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_picture_min)
        maxNumberOfPicturesEditText = view.findViewById(R.id.fragment_filter_list_estate_number_of_picture_max)
        putOnTheMarketSinceEditText = view.findViewById(R.id.fragment_filter_list_estate_put_on_the_market_since)
        soldSinceEditText = view.findViewById(R.id.fragment_filter_list_estate_sold_since)
        sectorEditText = view.findViewById(R.id.fragment_filter_list_estate_sector)

        typeSpinner = view.findViewById(R.id.fragment_filter_list_estate_spinner_type)
        val typesAdapter = EstateTypeSpinnerAdapter(resources)
        typeSpinner.adapter = typesAdapter
        typeSpinner.onItemSelectedListener = onSelectedType
    }

    private fun setData() {
        viewModel.getSearchCriteriaLiveData().observe(viewLifecycleOwner) { searchCriteria ->
            searchCriteria.forEach { criteria ->
                when(criteria) {
                    is SearchCriteria.MinPrice -> minPriceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxPrice -> maxPriceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinSurface -> minSurfaceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxSurface -> maxSurfaceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinNumberOfRooms -> minNumberOfRoomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxNumberOfRooms -> maxNumberOfRoomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinNumberOfBathrooms -> minNumberOfBathroomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxNumberOfBathrooms -> maxNumberOfBathroomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinNumberOfBedrooms -> minNumberOfBedroomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxNumberOfBedrooms -> maxNumberOfBedroomsEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinSchoolDistance -> minSchoolDistanceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxSchoolDistance -> maxSchoolDistanceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinShopDistance -> minShopDistanceEditText .setText(criteria.value.toString())
                    is SearchCriteria.MaxShopDistance -> maxShopDistanceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinParcDistance -> minParcDistanceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxParcDistance -> maxParcDistanceEditText.setText(criteria.value.toString())
                    is SearchCriteria.MinNumberOfPictures -> minNumberOfPicturesEditText.setText(criteria.value.toString())
                    is SearchCriteria.MaxNumberOfPictures -> maxNumberOfPicturesEditText.setText(criteria.value.toString())
                    is SearchCriteria.Type -> typeSpinner.setSelection( criteria.value.ordinal + 1)
                    is SearchCriteria.PutOnTheMarketSince -> putOnTheMarketSinceEditText.setText(criteria.value.toString())
                    is SearchCriteria.SoldSince -> soldSinceEditText.setText(criteria.value.toString())
                    is SearchCriteria.Sector -> sectorEditText.setText(criteria.value)
                }
            }
        }
    }

    private val onClickSearchButton = OnClickListener {
        val searchCriteria = mutableListOf<SearchCriteria>()

        if (minPriceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinPrice(minPriceEditText.text.toString().toDouble()))
        if (maxPriceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxPrice(maxPriceEditText.text.toString().toDouble()))
        if (minSurfaceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinSurface(minSurfaceEditText.text.toString().toInt()))
        if (maxSurfaceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxSurface(maxSurfaceEditText.text.toString().toInt()))
        if (minNumberOfRoomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinNumberOfRooms(minNumberOfRoomsEditText.text.toString().toInt()))
        if (maxNumberOfRoomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxNumberOfRooms(maxNumberOfRoomsEditText.text.toString().toInt()))
        if (minNumberOfBathroomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinNumberOfBathrooms(minNumberOfBathroomsEditText.text.toString().toInt()))
        if (maxNumberOfBathroomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxNumberOfBathrooms(maxNumberOfBathroomsEditText.text.toString().toInt()))
        if (minNumberOfBedroomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinNumberOfBedrooms(minNumberOfBedroomsEditText.text.toString().toInt()))
        if (maxNumberOfBedroomsEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxNumberOfBedrooms(maxNumberOfBedroomsEditText.text.toString().toInt()))
        if (minSchoolDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinSchoolDistance(minSchoolDistanceEditText.text.toString().toInt()))
        if (maxSchoolDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxSchoolDistance(maxSchoolDistanceEditText.text.toString().toInt()))
        if (minShopDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinShopDistance(minShopDistanceEditText.text.toString().toInt()))
        if (maxShopDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxShopDistance(maxShopDistanceEditText.text.toString().toInt()))
        if (minParcDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinParcDistance(minParcDistanceEditText.text.toString().toInt()))
        if (maxParcDistanceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxParcDistance(maxParcDistanceEditText.text.toString().toInt()))
        if (minNumberOfPicturesEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MinNumberOfPictures(minNumberOfPicturesEditText.text.toString().toInt()))
        if (maxNumberOfPicturesEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.MaxNumberOfPictures(maxNumberOfPicturesEditText.text.toString().toInt()))
        if (putOnTheMarketSinceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.PutOnTheMarketSince(putOnTheMarketSinceEditText.text.toString().toInt()))
        if (soldSinceEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.SoldSince(soldSinceEditText.text.toString().toInt()))
        if (sectorEditText.text.isNotEmpty())
            searchCriteria.add(SearchCriteria.Sector(sectorEditText.text.toString()))

        type?.let { type -> searchCriteria.add(SearchCriteria.Type(type)) }
        viewModel.setSearchCriteriaLiveData(searchCriteria)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            estateListFilterFragmentListener.launchEstateListFragment()
        }
    }

    private val onSelectedType = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            type = if (position == 0) { null }
            else {
                Estate.Type.values()[position - 1]
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

}