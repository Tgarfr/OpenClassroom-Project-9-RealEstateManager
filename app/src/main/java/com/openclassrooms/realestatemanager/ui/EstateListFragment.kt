package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.model.Estate


class EstateListFragment(
    private val estateListFragmentListener: EstateListFragmentListener
) : Fragment(), EstateListAdapter.EstateListAdapterListener {

    private lateinit var viewModel: EstateListFragmentViewModel

    interface EstateListFragmentListener {
        fun launchEstateSheetFragment(estate: Estate)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_list_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[EstateListFragmentViewModel::class.java]

        val recyclerView: RecyclerView = view.findViewById(R.id.main_activity_estate_list_recyclerview)
        val estateListAdapter = EstateListAdapter(this, requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = estateListAdapter

        viewModel.getEstateItemListLiveData().observe(viewLifecycleOwner) { estateItemList -> estateListAdapter.submitList(estateItemList) }
        viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner) { estate -> estateListAdapter.selectedEstate(estate.id) }

        return view
    }

    override fun onEstateItemClick(estate: Estate) {
        estateListFragmentListener.launchEstateSheetFragment(estate)
    }

}