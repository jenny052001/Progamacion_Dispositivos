package com.lugares.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lugares.data.LugarDataBase
import com.lugares.model.Lugar
import com.lugares.repository.LugarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LugarViewModel (application: Application) : AndroidViewModel(application) {

    // Atributo para acceder al repositorio lugar
    private val repository: LugarRepository

    // Atributo para obtener la lista del lugares en un ArrayList especial
    val getAllData: LiveData<List<Lugar>>

    // Bloque incialización de los atributos

    init {
        val lugarDao = LugarDataBase.getDataBase(application)
            .lugarDato() // Creando la base de datos, le damos un enlace de conexion
        repository = LugarRepository(lugarDao)
        getAllData = repository.getAllData

    }

    fun addLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {// se lanza un hilo de input output
            repository.addLugar(lugar)
        }

    }

    fun updateLugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {// se lanza un hilo de input output
            repository.addLugar(lugar)
        }

    }

    fun deletelugar(lugar: Lugar) {
        viewModelScope.launch(Dispatchers.IO) {// se lanza un hilo de input output
            repository.addLugar(lugar)
        }
    }
}

