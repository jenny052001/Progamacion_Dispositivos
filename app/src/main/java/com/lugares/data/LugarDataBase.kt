package com.lugares.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lugares.model.Lugar
import com.lugares.ui.lugar.LugarFragment

@Database(entities = [Lugar::class], version = 1, exportSchema = false) // Lugar::class para llamar las tablas.
// exportSchema exportar el esquema desde otra app.

abstract class LugarDataBase: RoomDatabase() {
    abstract fun lugarDato(): LugarDao

    companion object { // constructor de clase
        @Volatile
        private  var INSTANCE: LugarDataBase? = null // URL conexion a la base de datos
        fun  getDataBase(context: android.content.Context): LugarDataBase {
            val temp= INSTANCE
            if (temp !=null){
                return temp
            }
            synchronized(this){
                val instance = Room.databaseBuilder( //CODIGO PRA CREAR BASE DE DATOS
                context.applicationContext,
                LugarDataBase::class.java,
                "lugar_database"
                ).build()
                INSTANCE=instance
                return instance
            }
        }

    }
}