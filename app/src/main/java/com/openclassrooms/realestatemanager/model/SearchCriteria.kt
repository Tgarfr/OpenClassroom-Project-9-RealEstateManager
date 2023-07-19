package com.openclassrooms.realestatemanager.model

sealed class SearchCriteria {

    data class MinPrice(val value: Double): SearchCriteria()
    data class MaxPrice(val value: Double): SearchCriteria()
    data class MinSurface(val value: Int): SearchCriteria()
    data class MaxSurface(val value: Int): SearchCriteria()
    data class MinNumberOfRooms(val value: Int): SearchCriteria()
    data class MaxNumberOfRooms(val value: Int): SearchCriteria()
    data class MinNumberOfBathrooms(val value: Int): SearchCriteria()
    data class MaxNumberOfBathrooms(val value: Int): SearchCriteria()
    data class MinNumberOfBedrooms(val value: Int): SearchCriteria()
    data class MaxNumberOfBedrooms(val value: Int): SearchCriteria()
    data class MinSchoolDistance(val value: Int): SearchCriteria()
    data class MaxSchoolDistance(val value: Int): SearchCriteria()
    data class MinShopDistance(val value: Int): SearchCriteria()
    data class MaxShopDistance(val value: Int): SearchCriteria()
    data class MinParcDistance(val value: Int): SearchCriteria()
    data class MaxParcDistance(val value: Int): SearchCriteria()
    data class MinNumberOfPictures(val value: Int): SearchCriteria()
    data class MaxNumberOfPictures(val value: Int): SearchCriteria()
    data class Type(val value: Estate.Type): SearchCriteria()
    data class PutOnTheMarketSince(val value: Int): SearchCriteria()
    data class SoldSince(val value: Int): SearchCriteria()
    data class Sector(val value: String): SearchCriteria()

}