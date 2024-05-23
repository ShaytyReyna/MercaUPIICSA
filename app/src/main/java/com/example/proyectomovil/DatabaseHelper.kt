package com.example.proyectomovil

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyectomovil.data.model.User
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        //
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //
    }

    fun insertarUsuario(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("idBoleta", user.boleta)
        contentValues.put("Nombre", user.nombre)

        contentValues.put("Telefono", user.celular)
        contentValues.put("Facebook", user.facebook)
        contentValues.put("Instagram", user.instagram)
        contentValues.put("Disponible", user.disponible)
        return db.insert("Vendedor", null, contentValues)
    }


    companion object {
        private const val DATABASE_NAME = "BDmovil.db"
        private const val DATABASE_VERSION = 1
    }
}
