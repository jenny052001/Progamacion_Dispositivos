package com.lugares.repository


import androidx.lifecycle.MutableLiveData
import com.lugares.data.LugarDao
import com.lugares.model.Lugar


class LugarRepository(private val  LugarDao: LugarDao) {

    val getAllData: MutableLiveData<List<Lugar>> = LugarDao.getAllData()


     fun saveLugar(lugar: Lugar) {
        LugarDao.saveLugar(lugar)
    }
     fun DeleteLugar(lugar: Lugar) {
        LugarDao.DeleteLugar(lugar)
    }

}

