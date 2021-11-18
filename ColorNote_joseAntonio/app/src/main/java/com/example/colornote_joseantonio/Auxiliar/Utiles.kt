package com.example.colornote_joseantonio.Auxiliar

import java.text.SimpleDateFormat

object Utiles {


    object FechaFormato{
        /*
        fun getFormatoFecha(estilo:Int): SimpleDateFormat {
            var formatoFecha:SimpleDateFormat
            when(estilo){
                1-> formatoFecha = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                2-> formatoFecha = SimpleDateFormat("HH:mm:ss")
                3-> formatoFecha = SimpleDateFormat("yyyy/MM/dd")
                else-> formatoFecha = SimpleDateFormat()
            }
            return formatoFecha
        }*/

        fun getFormatoFechaCompleta(): SimpleDateFormat {
            return SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        }
        fun getFormatoFechaHorMinSec(): SimpleDateFormat {
            return SimpleDateFormat("HH:mm:ss")
        }
        fun getFormatoFechaCalendario(): SimpleDateFormat {
            return SimpleDateFormat("yyyy/MM/dd")
        }
    }
}