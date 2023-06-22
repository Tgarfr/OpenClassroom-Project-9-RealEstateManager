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
        fun launchEstateSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_list_estate, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory)[EstateListFragmentViewModel::class.java]

        val recyclerView: RecyclerView = view.findViewById(R.id.main_activity_estate_list_recyclerview)
        val estateListAdapter = EstateListAdapter(this, resources)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = estateListAdapter

        viewModel.getEstateListLiveData().observe(viewLifecycleOwner) { estateList -> estateListAdapter.submitList(estateList)}
        viewModel.getSelectedEstateLiveData().observe(viewLifecycleOwner) { estate -> estateListAdapter.updateSelectedEstateColor(estate)}

        return view
    }

    override fun onEstateItemClick(estate: Estate) {
        viewModel.setSelectedEstateLiveData(estate)
        estateListFragmentListener.launchEstateSheetFragment()
    }

}