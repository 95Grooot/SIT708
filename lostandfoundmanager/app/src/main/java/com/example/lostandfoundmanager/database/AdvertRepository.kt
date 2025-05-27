package com.example.lostandfoundmanager.database

import androidx.lifecycle.LiveData

class AdvertRepository(private val advertDao: AdvertDao) {

    fun getAllAdverts(): LiveData<List<AdvertItem>> = advertDao.getAllAdverts()

    suspend fun getAdvertById(id: Long): AdvertItem? = advertDao.getAdvertById(id)

    suspend fun insertAdvert(advert: AdvertItem): Long = advertDao.insertAdvert(advert)

    suspend fun updateAdvert(advert: AdvertItem) = advertDao.updateAdvert(advert)

    suspend fun deleteAdvert(advert: AdvertItem) = advertDao.deleteAdvert(advert)
}