package com.example.colornote_joseantonio.Adaptadores

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        val nombreTarea = view.findViewById(R.id.txtNombre_itemContacto) as TextView
        val imgTarea = view.findViewById(R.id.imgCheck_itemTarea) as ImageView

        fun bind(nota: NotaTareas, context: Context, pos: Int, miAdaptadorRecycler: TareaAdapter){



            when(nota.tachada){
                0->{
                    nombreTarea.text = nota.texto
                    imgTarea.setImageResource(R.drawable.ic_checkoff)
                }
                1-> {
                    nombreTarea.text = nota.texto
                    nombreTarea.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                    imgTarea.setImageResource(R.drawable.ic_checkon)
                }




            }



            itemView.setOnClickListener(
                View.OnClickListener
            {
})
        }
    }


}