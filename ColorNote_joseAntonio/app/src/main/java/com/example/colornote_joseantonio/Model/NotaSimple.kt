package com.example.colornote_joseantonio.Model

import java.sql.Date
import java.sql.Time

class NotaSimple(nombre:String, tipo:String, hora: Time, fecha: Date,var descripcion:String):Nota(nombre,tipo,hora,fecha) {
}
