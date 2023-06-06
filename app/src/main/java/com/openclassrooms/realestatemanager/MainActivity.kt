package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.di.ViewModelFactory
import com.openclassrooms.realestatemanager.ui.EstateListAdapter
import com.openclassrooms.realestatemanager.ui.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(this, ViewModelFactory)[MainActivityViewModel::class.java]

        val recyclerView: RecyclerView = findViewById(R.id.main_activity_estate_list_recyclerview)
        val estateListAdapter = EstateListAdapter(resources)
        estateListAdapter.submitList(viewModel.getEstateList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = estateListAdapter
    }

}