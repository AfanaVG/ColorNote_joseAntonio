package com.example.colornote_joseantonio.Auxiliar

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.example.colornote_joseantonio.R
import java.text.SimpleDateFormat
import android.view.MotionEvent

import androidx.recyclerview.widget.RecyclerView

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener


//Clase con varias utilidades para esta y futuras aplicaciones
object Utiles {


    //Funcion que lanzara un toast en la activity que se la invoque, necesita el mensaje y el contexto
    fun lanzarToast(mensaje:String,context: Context){
        val toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT)
        toast.show()
    }


    //Clase con funciones que devolveran distintos formatos de fecha
    object FechaFormato{

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

    //Clase que permite usar los eventos click y longClick de un RecyclerView
    class RecyclerItemClickListener(
        context: Context?,
        recyclerView: RecyclerView,
        private val mListener: OnItemClickListener?
    ) :
        OnItemTouchListener {
        interface OnItemClickListener {
            fun onItemClick(view: View?, position: Int)
            fun onLongItemClick(view: View?, position: Int)
        }

        var mGestureDetector: GestureDetector
        override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
            val childView: View? = view.findChildViewUnder(e.x, e.y)
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
                return true
            }
            return false
        }

        override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child: View? = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(
                            child,
                            recyclerView.getChildAdapterPosition(child)
                        )
                    }
                }
            })
        }
    }


}