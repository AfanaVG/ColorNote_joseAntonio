package com.example.colornote_joseantonio.Adaptadores


import Auxiliar.Conexion
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.NotaSimpleActivity
import com.example.colornote_joseantonio.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MiAdaptadorRecycler(var notas : ArrayList<Nota>, var  context: Context) : RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>() {

    companion object {
        var seleccionado:Int = -1
        var idN:Int = 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notas.get(position)
        holder.bind(item, context, position, this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
        return ViewHolder(layoutInflater.inflate(R.layout.item_nota_card,parent,false))
    }


    override fun getItemCount(): Int {

        return notas.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombreNota = view.findViewById(R.id.txtNombre_itemNota) as TextView
        val constraintFondo = view.findViewById(R.id.clFondo_itemNota) as ConstraintLayout
        val horaNota = view.findViewById(R.id.txtHora_itemNota) as TextView
        val fechaNota = view.findViewById(R.id.txtFecha_itemNota) as TextView
        val tipoNota = view.findViewById(R.id.txtTipo_itemNota) as TextView

        fun bind(nota: Nota, context: Context, pos: Int, miAdaptadorRecycler: MiAdaptadorRecycler){
            nombreNota.text = nota.nombre
            horaNota.text = Utiles.FechaFormato.getFormatoFechaHorMinSec().format(nota.fechaHora)
            fechaNota.text = Utiles.FechaFormato.getFormatoFechaCalendario().format(nota.fechaHora)
            tipoNota.text = nota.tipo


            if (pos == MiAdaptadorRecycler.seleccionado) {
                with(nombreNota) {
                    this.setTextColor(resources.getColor(R.color.azul6))
                }
                with(horaNota) {
                    this.setTextColor(resources.getColor(R.color.azul6))
                }
                with(constraintFondo) {
                    this.setBackgroundColor(resources.getColor(R.color.azul1))
                }
            }
            else {
                with(nombreNota) {
                    this.setTextColor(resources.getColor(R.color.azul1))
                }
                with(horaNota) {
                    this.setTextColor(resources.getColor(R.color.azul1))
                }
                with(constraintFondo) {
                    this.setBackgroundColor(resources.getColor(R.color.azul6))
                }
            }
            itemView.setOnClickListener(View.OnClickListener
                    {
                        if (pos == MiAdaptadorRecycler.seleccionado){
                            MiAdaptadorRecycler.seleccionado = -1
                            MiAdaptadorRecycler.idN = -1
                        }
                        else {
                            MiAdaptadorRecycler.seleccionado = pos
                            MiAdaptadorRecycler.idN = nota.idN
                        }
                        miAdaptadorRecycler.notifyDataSetChanged()
                        Toast.makeText(context, "Valor seleccionado " +  MiAdaptadorRecycler.seleccionado.toString(), Toast.LENGTH_SHORT).show()
                    })
        }
    }
}

