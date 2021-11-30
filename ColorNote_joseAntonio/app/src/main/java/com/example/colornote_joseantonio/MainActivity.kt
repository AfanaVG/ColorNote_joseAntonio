package com.example.colornote_joseantonio

import Auxiliar.Conexion
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
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
    var max_EditText = 15

    var orden=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarPrincipal()

        listaNotas_mainActivity.addOnItemTouchListener(
            Utiles.FechaFormato.RecyclerItemClickListener(
                this,
                listaNotas_mainActivity,
                object : Utiles.FechaFormato.RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        when(listaNotas[position].tipo){
                            getString(R.string.notaSimple)-> abrirNotaSimple(listaNotas[position])
                            getString(R.string.notaLista)-> abrirNotaTarea(listaNotas[position])
                        }
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                        eliminarNota(position)
                    }
                })
        )
    }

    fun cargarPrincipal(){
        listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)
        miRecyclerView = findViewById(R.id.listaNotas_mainActivity) as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter
    }

    fun eliminarNota(position:Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(getString(R.string.eliminar))
        builder.setMessage(getString(R.string.eliminarNota))
        builder.setPositiveButton(getString(R.string.si)) { dialogInterface: DialogInterface, i: Int ->
            when(listaNotas[position].tipo){
                getString(R.string.notaSimple)->Conexion.delNotaSimple(this@MainActivity,listaNotas[position].idN)
                getString(R.string.notaLista)->Conexion.delNotaTareaTotal(this@MainActivity,listaNotas[position].idN)
            }
            Conexion.delNota(this@MainActivity,listaNotas[position].idN)
            recargarLista()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
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
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icAdd_main->{
                lanzarMenuAdd()
            }
            R.id.icHora_main->{
                cambiarOrden()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun lanzarMenuAdd(){
        var input =  EditText(this)
        input.hint = getString(R.string.menuNotaNombre)
        input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max_EditText))

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.menuNombre))
        builder.setMessage(getString(R.string.menuNotaPregunta))
        builder.setView(input)
        builder.setPositiveButton(getString(R.string.notaSimple)) { dialogInterface: DialogInterface, i: Int ->
            addNotaSimple(input)
        }
        builder.setNegativeButton(getString(R.string.notaLista)){ dialogInterface: DialogInterface, i: Int ->
            addNotaLista(input)
        }
        builder.show()
    }

    fun addNotaSimple(input:EditText){
        if (!input.text.isEmpty()){
            Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), getString(R.string.notaSimple), Date()))
            Conexion.addNotaSimple(this)
            recargarLista()
            val intent = Intent(this, NotaSimpleActivity::class.java)
            intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
            startActivity(intent)
        }else{
            lanzarToast(getString(R.string.menuNotaError),this@MainActivity)
        }
    }

    fun addNotaLista(input:EditText){
        if (!input.text.isEmpty()){
            Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), getString(R.string.notaLista), Date()))
            recargarLista()
            val intent = Intent(this, ListaTareaActivity::class.java)
            intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
            startActivity(intent)
        }else{
            lanzarToast(getString(R.string.menuNotaError),this)
        }
    }

    fun cambiarOrden(){
        var lista = Auxiliar.Conexion.obtenerListaNotas(this)
        if (orden == 0){
            lista.sortByDescending { it.fechaHora }
            var miAdapter = MiAdaptadorRecycler(lista, this)
            miRecyclerView.adapter = miAdapter
            orden == 1
        }
        if (orden == 1){
            lista.sortBy { it.fechaHora }
            var miAdapter = MiAdaptadorRecycler(lista, this)
            miRecyclerView.adapter = miAdapter
            orden == 0
        }
    }

    fun recargarLista(){
        listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)
        var miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter
    }
}