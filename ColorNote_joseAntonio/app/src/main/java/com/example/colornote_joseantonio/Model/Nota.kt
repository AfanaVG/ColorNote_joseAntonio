package com.example.colornote_joseantonio.Model

import java.io.Serializable
import java.util.Date
import java.sql.Time

open class Nota():Serializable {

    var nombre:String = ""
    var tipo:String = ""
    lateinit var fechaHora:Date

    constructor( nombre:String,  tipo:String,  fechaHora:Date):this(){
        this.nombre = nombre
        this.tipo = tipo
        this.fechaHora = fechaHora
    }
}