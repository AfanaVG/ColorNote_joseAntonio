package com.example.colornote_joseantonio

import Auxiliar.Conexion
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorRecycler
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Auxiliar.Utiles.FechaFormato.lanzarToast
import com.example.colornote_joseantonio.Model.Nota
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var miRecyclerView : RecyclerView
    lateinit var listaNotas:ArrayList<Nota>
    lateinit var miAdapter:MiAdaptadorRecycler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



         listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)


        miRecyclerView = findViewById(R.id.listaNotas_mainActivity) as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter

        listaNotas_mainActivity.addOnItemTouchListener(
            Utiles.FechaFormato.RecyclerItemClickListener(
                this,
                listaNotas_mainActivity,
                object : Utiles.FechaFormato.RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        when(listaNotas[position].tipo){

                            "Simple"-> abrirNotaSimple(listaNotas[position])
                            "Lista"-> abrirNotaTarea(listaNotas[position])
                        }

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("Eliminar")
                        builder.setMessage("¿Desea eliminar la nota?")
                        builder.setPositiveButton("Si") { dialogInterface: DialogInterface, i: Int ->
                            when(listaNotas[position].tipo){
                                "Simple"->Conexion.delNotaSimple(this@MainActivity,listaNotas[position].idN)
                                "Lista"->Conexion.delNotaTareaTotal(this@MainActivity,listaNotas[position].idN)
                            }
                            Conexion.delNota(this@MainActivity,listaNotas[position].idN)
                            recargarLista()
                        }
                        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->

                        }

                        builder.show()
                    }
                })
        )




    }

    private  fun  abrirNotaSimple( nota:Nota){
        val intent = Intent(this, NotaSimpleActivity::class.java)
        intent.putExtra("datosNota",nota)
        startActivity(intent)
    }
    private  fun  abrirNotaTarea( nota:Nota){
        val intent = Intent(this, ListaTareaActivity::class.java)
        intent.putExtra("datosNota",nota)
        startActivity(intent)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var input =  EditText(this)
        input.hint = "NOMBRE DE LA NOTA"

        when(item.itemId){

            R.id.icAdd_main->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Crear nueva nota")
                builder.setMessage("¿Que clase de nota quiere crear?")
                builder.setView(input)
                builder.setPositiveButton("Simple") { dialogInterface: DialogInterface, i: Int ->
                    if (!input.text.isEmpty()){
                        Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), "Simple", Date()))

                        recargarLista()

                    val intent = Intent(this, NotaSimpleActivity::class.java)
                        intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
                    startActivity(intent)
                    }else{
                        lanzarToast("La nota necesita un nombre",this@MainActivity)
                    }
                }
                builder.setNegativeButton("Lista"){ dialogInterface: DialogInterface, i: Int ->
                    if (!input.text.isEmpty()){
                        Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), "Lista", Date()))

                        recargarLista()

                        val intent = Intent(this, ListaTareaActivity::class.java)
                        intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
                        startActivity(intent)
                    }else{
                        lanzarToast("La nota necesita un nombre",this)
                    }

                }

                builder.show()
            }

            R.id.icHora_main->{
                var lista = Auxiliar.Conexion.obtenerListaNotas(this)
                lista.sortByDescending {it.fechaHora }
                var miAdapter = MiAdaptadorRecycler(lista, this)
                miRecyclerView.adapter = miAdapter



            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun recargarLista(){
        listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)
        var miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter
    }


}