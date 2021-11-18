package com.example.colornote_joseantonio.Adaptadores


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MiAdaptadorRecycler(var notas : ArrayList<Nota>, var  context: Context) : RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>() {

    companion object {
        //Esta variable estática nos será muy útil para saber cual está marcado o no.
        var seleccionado:Int = -1
        /*
        PAra marcar o desmarcar un elemento de la lista lo haremos diferente a una listView. En la listView el listener
        está en la activity por lo que podemos controlar desde fuera el valor de seleccionado y pasarlo al adapter, asociamos
        el adapter a la listview y resuelto.
        En las RecyclerView usamos para pintar cada elemento la función bind (ver código más abajo, en la clase ViewHolder).
        Esto se carga una vez, solo una vez, de ahí la eficiencia de las RecyclerView. Si queremos que el click que hagamos
        se vea reflejado debemos recargar la lista, para ello forzamos la recarga con el método: notifyDataSetChanged().
         */
    }









    /**
     * onBindViewHolder() se encarga de coger cada una de las posiciones de la lista de personajes y pasarlas a la clase
     * ViewHolder(clase interna, ver abajo) para que esta pinte todos los valores y active el evento onClick en cada uno.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notas.get(position)
        holder.bind(item, context, position, this)
    }

    /**
     *  Como su nombre indica lo que hará será devolvernos un objeto ViewHolder al cual le pasamos la celda que hemos creado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
        return ViewHolder(layoutInflater.inflate(R.layout.item_nota_card,parent,false))
    }

    /**
     * getItemCount() nos devuelve el tamaño de la lista, que lo necesita el RecyclerView.
     */
    override fun getItemCount(): Int {

        return notas.size
    }


    //--------------------------------- Clase interna ViewHolder -----------------------------------
    /**
     * La clase ViewHolder. No es necesaria hacerla dentro del adapter, pero como van tan ligadas
     * se puede declarar aquí.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Esto solo se asocia la primera vez que se llama a la clase, en el método onCreate de la clase que contiene a esta.
        //Por eso no hace falta que hagamos lo que hacíamos en el método getView de los adaptadores para las listsViews.
        //val nombrePersonaje = view.findViewById(R.id.txtNombre) as TextView
        //val tipoPersonaje = view.findViewById(R.id.txtTipo) as TextView
        //val avatar = view.findViewById(R.id.imgImagen) as ImageView

        //Como en el ejemplo general de las listas (ProbandoListas) vemos que se puede inflar cada elemento con una card o con un layout.
        val nombreNota = view.findViewById(R.id.txtNombre_itemNota) as TextView
        val constraintFondo = view.findViewById(R.id.clFondo_itemNota) as ConstraintLayout
        val horaNota = view.findViewById(R.id.txtHora_itemNota) as TextView
        val fechaNota = view.findViewById(R.id.txtFecha_itemNota) as TextView




        /**
         * Éste método se llama desde el método onBindViewHolder de la clase contenedora. Como no vuelve a crear un objeto
         * sino que usa el ya creado en onCreateViewHolder, las asociaciones findViewById no vuelven a hacerse y es más eficiente.
         */
        fun bind(nota: Nota, context: Context, pos: Int, miAdaptadorRecycler: MiAdaptadorRecycler){
            nombreNota.text = nota.nombre
            horaNota.text = Utiles.FechaFormato.getFormatoFechaHorMinSec().format(nota.fechaHora)
            fechaNota.text = Utiles.FechaFormato.getFormatoFechaCalendario().format(nota.fechaHora)



/**
            when(encuesta.sistemaOperativo){
                "LINUX"-> Glide.with(context).load(R.drawable.linux).into(so)
                "WINDOWS"-> Glide.with(context).load(R.drawable.windows).into(so)
                "MAC"-> Glide.with(context).load(R.drawable.apple).into(so)
            }**/

            //Para marcar o desmarcar al seleccionado usamos el siguiente código.
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
            //Se levanta una escucha para cada item. Si pulsamos el seleccionado pondremos la selección a -1, en otro caso será el nuevo sleccionado.
            itemView.setOnClickListener(View.OnClickListener
                    {
                        if (pos == MiAdaptadorRecycler.seleccionado){
                            MiAdaptadorRecycler.seleccionado = -1
                        }
                        else {
                            MiAdaptadorRecycler.seleccionado = pos

                        }
                        //Con la siguiente instrucción forzamos a recargar el viewHolder porque han cambiado los datos. Así pintará al seleccionado.
                        miAdaptadorRecycler.notifyDataSetChanged()

                        Toast.makeText(context, "Valor seleccionado " +  MiAdaptadorRecycler.seleccionado.toString(), Toast.LENGTH_SHORT).show()
                    })
        }
    }
}

