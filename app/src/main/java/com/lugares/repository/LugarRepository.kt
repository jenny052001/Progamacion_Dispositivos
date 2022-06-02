package com.lugares.repository


import androidx.lifecycle.LiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar


class LugarRepository(private val  LugarDao: LugarDao) {

   val getAllData: LiveData<List<Lugar>> = LugarDao.getAllData()

}
       suspend fun addLugar(lugar: Lugar){
            LugarDao.addLugar(lugar)
        }


    suspend fun UpdateLugar(lugar: Lugar){
        LugarDao.UpdateLugar(lugar)
    }


    suspend fun DeleteLugar(lugar: Lugar){
        LugarDao.DeleteLugar(lugar)
    }



