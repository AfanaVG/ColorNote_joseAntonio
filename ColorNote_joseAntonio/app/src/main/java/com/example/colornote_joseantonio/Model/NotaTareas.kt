package com.example.colornote_joseantonio.Model

import java.sql.Date
import java.sql.Time

class NotaTareas(nombre:String, tipo:String,  fechaHora: java.util.Date, var listaTareas:ArrayList<String>):Nota(nombre,tipo,fechaHora) {
}