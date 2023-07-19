package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.model.SearchCriteria
import com.openclassrooms.realestatemanager.repository.EstateRepository
import com.openclassrooms.realestatemanager.repository.GeocodingRepository
import com.openclassrooms.realestatemanager.repository.PictureRepository
import com.openclassrooms.realestatemanager.repository.SearchRepository

class EstateListFragmentViewModel(
    private val estateRepository: EstateRepository,
    private val searchRepository: SearchRepository,
    private val pictureRepository: PictureRepository,
    private val geocodingRepository: GeocodingRepository
    ) : ViewModel() {
        
    fun getEstateListLiveData(): LiveData<List<Estate>> {

        var noFilteredEstateList = listOf<Estate>()
        var pictureList = listOf<Picture>()
        var searchCriteria = listOf<SearchCriteria>()

        val mediatorLiveData = MediatorLiveData<List<Estate>>()

        mediatorLiveData.addSource(estateRepository.getEstateListLiveData()) { estateList ->
            noFilteredEstateList = estateList
            mediatorLiveData.value = filterEstateList(estateList, searchCriteria, pictureList)
        }

        mediatorLiveData.addSource(searchRepository.getSearchCriteriaLiveData()) { newSearchCriteria ->
            searchCriteria = newSearchCriteria
            mediatorLiveData.value = filterEstateList(noFilteredEstateList, newSearchCriteria, pictureList)
        }

        mediatorLiveData.addSource(pictureRepository.getPictureListLiveData()) { newPictureList ->
            pictureList = newPictureList
            mediatorLiveData.value = filterEstateList(noFilteredEstateList, searchCriteria, newPictureList)
        }

        return mediatorLiveData
    }

    fun getSelectedEstateLiveData(): LiveData<Estate> = estateRepository.getSelectedEstateLiveData()

    private fun filterEstateList(estateList: List<Estate>, searchCriteria: List<SearchCriteria>, pictureList: List<Picture>): List<Estate> {
        var filteredEstateList: List<Estate> = estateList.toList()
        searchCriteria.forEach { criteria ->
            when (criteria) {
                is SearchCriteria.MinPrice -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.price >= criteria.value }

                is SearchCriteria.MaxPrice -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.price <= criteria.value }

                is SearchCriteria.MinSurface -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.surface >= criteria.value }

                is SearchCriteria.MaxSurface -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.surface <= criteria.value }

                is SearchCriteria.MinNumberOfRooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfRooms >= criteria.value }

                is SearchCriteria.MaxNumberOfRooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfRooms <= criteria.value }

                is SearchCriteria.MinNumberOfBathrooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfBathrooms >= criteria.value }

                is SearchCriteria.MaxNumberOfBathrooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfBathrooms <= criteria.value }

                is SearchCriteria.MinNumberOfBedrooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfBedrooms >= criteria.value }

                is SearchCriteria.MaxNumberOfBedrooms -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.numberOfBedrooms <= criteria.value }

                is SearchCriteria.MinSchoolDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.schoolDistance >= criteria.value }

                is SearchCriteria.MaxSchoolDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.schoolDistance <= criteria.value }

                is SearchCriteria.MinShopDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.shopDistance >= criteria.value }

                is SearchCriteria.MaxShopDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.shopDistance <= criteria.value }

                is SearchCriteria.MinParcDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.parkDistance >= criteria.value }

                is SearchCriteria.MaxParcDistance -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.parkDistance <= criteria.value }

                is SearchCriteria.Type -> filteredEstateList =
                    filteredEstateList.filter { estate -> estate.type == criteria.value }

                is SearchCriteria.PutOnTheMarketSince -> filteredEstateList =
                    filteredEstateList.filter { estate ->
                        estate.entryDate >= getTimeInMillisWithWeekOffset(-criteria.value) }

                is SearchCriteria.SoldSince -> filteredEstateList =
                    filteredEstateList.filter { estate ->
                        estate.saleDate?.let { saleDate ->
                            saleDate >= getTimeInMillisWithMonthOffset(-criteria.value)
                        } ?: false
                    }

                is SearchCriteria.Sector -> {
                    filteredEstateList = filteredEstateList.filter { estate ->
                        if (estate.latitude != null && estate.longitude != null) {
                            geocodingRepository.isInSector(criteria.value, estate.latitude, estate.longitude)
                        } else false
                    }
                }

                is SearchCriteria.MinNumberOfPictures ->  {
                    if (pictureList.isNotEmpty()) {
                        var filteredByPictureEstateList = filteredEstateList.toList()
                        filteredEstateList.forEach { estate ->
                            val numberOfPictures = pictureList.count { picture -> picture.estateId == estate.id }
                            if (numberOfPictures < criteria.value) {
                                filteredByPictureEstateList = filteredByPictureEstateList.filter { it.id != estate.id }
                            }
                        }
                        filteredEstateList = filteredByPictureEstateList
                    }
                }

                is SearchCriteria.MaxNumberOfPictures -> {
                    if (pictureList.isNotEmpty()) {
                        var filteredByPictureEstateList = filteredEstateList.toList()
                        filteredEstateList.forEach { estate ->
                            val numberOfPictures = pictureList.count { picture -> picture.estateId == estate.id }
                            if (numberOfPictures > criteria.value) {
                                filteredByPictureEstateList = filteredByPictureEstateList.filter { it.id != estate.id }
                            }
                        }
                        filteredEstateList = filteredByPictureEstateList
                    }
                }
            }
        }

        return filteredEstateList
    }

    private fun getTimeInMillisWithWeekOffset(weekOffset: Int): Long  {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.WEEK_OF_YEAR, weekOffset)
        return calendar.timeInMillis
    }

    private fun getTimeInMillisWithMonthOffset(monthOffset: Int): Long  {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.MONTH, monthOffset)
        return calendar.timeInMillis
    }

}