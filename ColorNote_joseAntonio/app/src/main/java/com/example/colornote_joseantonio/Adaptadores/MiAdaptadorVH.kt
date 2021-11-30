package com.example.colornote_joseantonio.Adaptadores

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.colornote_joseantonio.R


class MiAdaptadorVH : ArrayAdapter<Int> {
    private var context: Activity
    private var resource: Int
    private var valores: ArrayList<String>? = null
    private var seleccionado:Int = 0

    constructor(context: Activity, resource: Int, valores: ArrayList<String>?, seleccionado:Int) : super(context, resource) {
        this.context = context
        this.resource = resource
        this.valores = valores
        this.seleccionado = seleccionado
    }

    override fun getCount(): Int {
        return this.valores?.size!!
    }
/**
    override fun getItem(position: Int): Int? {
        return this.valores?.get(position)
    }**/

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        var holder = ViewHolder()
        if (view == null) {
            if (this.context!=null) {
                view = context.layoutInflater.inflate(this.resource, null)
                holder.txtItem = view.findViewById(R.id.txtNombre_itemTarea)
                view.tag = holder
            }
        }
        else {
            holder = view?.tag as ViewHolder
        }
        var valor: String = this.valores!![position]
        holder.txtItem?.text  = valor
        if (position == seleccionado) {

            with(holder.txtItem) { this?.setTextColor(resources.getColor(R.color.design_default_color_primary_variant))
                this?.setBackgroundResource(R.color.design_default_color_on_primary)}
        }
        return view!!
    }

    class ViewHolder(){
        var txtItem:TextView? = null
    }
}