package com.lugares.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lugares.model.Lugar

@Dao
interface LugarDao {
    @Query ("SELECT * FROM LUGAR")
    fun getAllData(): LiveData <List<Lugar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun UpdateLugar(lugar: Lugar)

    @Delete
    suspend fun DeleteLugar(lugar: Lugar)



}