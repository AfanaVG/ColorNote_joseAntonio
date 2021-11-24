package com.example.colornote_joseantonio.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Model.NotaTareas
import com.example.colornote_joseantonio.R

class TareaAdapter(var notas : ArrayList<NotaTareas>, var  context: Context) : RecyclerView.Adapter<TareaAdapter.ViewHolder>() {

    companion object {
        var seleccionado:Int = -1
        var idN:Int = 0
    }

     override fun onBindViewHolder(holder: TareaAdapter.ViewHolder, position: Int) {
        val item = notas.get(position)
        holder.bind(item, context, position, this)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
        return ViewHolder(layoutInflater.inflate(R.layout.item_tarea_card,parent,false))
    }


    override fun getItemCount(): Int {

        return notas.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombreTarea = view.findViewById(R.id.txtNombre_itemTarea) as TextView


        fun bind(nota: NotaTareas, context: Context, pos: Int, miAdaptadorRecycler: TareaAdapter){
            nombreTarea.text = nota.texto




            if (pos == MiAdaptadorRecycler.seleccionado) {

            }
            else {

            }
            itemView.setOnClickListener(
                View.OnClickListener
            {
})
        }
    }


}