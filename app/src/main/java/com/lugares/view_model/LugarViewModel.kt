package com.lugares.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lugares.data.LugarDao
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LugarViewModel (application: Application) : AndroidViewModel(application) {


    // Atributo para obtener la lista del lugares en un ArrayList especial
    val getAllData: MutableLiveData<List<Lugar>>

    // Atributo para acceder al repositorio lugar
    private val repository: LugarRepository = LugarRepository(LugarDao()) // para que se ejecute el constructor de LugarDao



    // Bloque incialización de los atributos

    init {
        getAllData = repository.getAllData //para que se inialice la información

    }

    fun saveLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {// se lanza un hilo de input output
            repository.saveLugar(lugar)
        }

    }


    fun deletelugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {// se lanza un hilo de input output
            repository.DeleteLugar(lugar)
        }
    }
}

