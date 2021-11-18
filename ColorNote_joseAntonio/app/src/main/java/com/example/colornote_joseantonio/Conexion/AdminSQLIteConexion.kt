package Conexion

import android.content.ContentValues
import android.content.Context
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.Time
import java.util.*
import kotlin.time.minutes

class AdminSQLIteConexion(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("create table notas(idN Integer primary key autoincrement, nombre text, tipo text,fechaHora text)")
        //db.execSQL("create table notasSimples(idNS Integer primary key autoincrement, idN text references notas(idN),contenido text)")
        //db.execSQL("create table notasTareas(idNT Integer primary key autoincrement, idN text references notas(idN),contenido)")





    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}