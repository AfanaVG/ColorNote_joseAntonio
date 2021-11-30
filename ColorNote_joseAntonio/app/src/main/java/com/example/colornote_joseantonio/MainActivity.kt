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
import com.example.colornote_joseantonio.Auxiliar.Utiles.lanzarToast
import com.example.colornote_joseantonio.Model.Nota
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.util.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    //Recycler view y adapter que manejara nuestra lista
    lateinit var miRecyclerView : RecyclerView
    lateinit var miAdapter:MiAdaptadorRecycler

    //Lista de Notas sacadas de la base de datos
    lateinit var listaNotas:ArrayList<Nota>

    //Tamaño maximo de caracteres a la hora de introducir el nombre de las Nota
    var max_EditText = 15

    //Controla el tipo de orden de la lista
    var orden=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarPrincipal()
        eventosClick()

    }

    //Metodo que controla si a un item del RecyclerView se le hace un click o un longClick
    fun eventosClick(){
        listaNotas_mainActivity.addOnItemTouchListener(
            Utiles.RecyclerItemClickListener(
                this,
                listaNotas_mainActivity,
                object : Utiles.RecyclerItemClickListener.OnItemClickListener {
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

    //Metodo que carga los datos de la base de datos en la lista
    fun cargarPrincipal(){
        listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)
        miRecyclerView = findViewById(R.id.listaNotas_mainActivity) as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter
    }

    //Añade una nota simple a la base de datos
    fun addNotaSimple(input:EditText){
        //Comprueba que el nombre no este vacio y pasa a crear una Nota y una NotaSimple en la base de datos
        if (!input.text.isEmpty()){
            Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), getString(R.string.notaSimple), Date()))
            Conexion.addNotaSimple(this)
            recargarLista()
            //Se lanza la activity de edicion de la NotaSimple
            val intent = Intent(this, NotaSimpleActivity::class.java)
            intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
            startActivity(intent)
        }else{
            lanzarToast(getString(R.string.menuNotaError),this@MainActivity)
        }
    }

    //Añade una nota simple a la base de datos
    fun addNotaLista(input:EditText){
        //Comprueba que el nombre no este vacio y pasa a crear una Nota, al contrario que con las NotaSimple aqui se crearan las
        //NotaTareas dentro de la Activity ListaTareaActivity, ya que las NotaTareas son elementos de una lista que comienza vacia al crear la nota
        if (!input.text.isEmpty()){
            Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), getString(R.string.notaLista), Date()))
            recargarLista()
            //Se lanza la activity de edicion de la NotaTareas
            val intent = Intent(this, ListaTareaActivity::class.java)
            intent.putExtra("datosNota",Conexion.obtenerUltimaNota(this))
            startActivity(intent)
        }else{
            lanzarToast(getString(R.string.menuNotaError),this)
        }
    }

    //Elimina la nota de la lista y la base de datos
    fun eliminarNota(position:Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(getString(R.string.eliminar))
        builder.setMessage(getString(R.string.eliminarNota))
        builder.setPositiveButton(getString(R.string.si)) { dialogInterface: DialogInterface, i: Int ->
            //Dependiendo del tipo de nota que sea se eliminara la NotaSimple o Tarea que tenga asociada antes
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

    //Se lanza la activity de edicion de la NotaSimple
    private  fun  abrirNotaSimple( nota:Nota){
        val intent = Intent(this, NotaSimpleActivity::class.java)
        intent.putExtra("datosNota",nota)
        startActivity(intent)
    }
    //Se lanza la activity de edicion de la NotaTareas
    private  fun  abrirNotaTarea( nota:Nota){
        val intent = Intent(this, ListaTareaActivity::class.java)
        intent.putExtra("datosNota",nota)
        startActivity(intent)
    }



    //Se carga el ActionBar personalizado
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        //getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu)
    }

    //Se asocian las funciones a los iconos del ActionBar
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

    //Alert dialog que nos pedira que tipo de Nota queremos crear y su nombre
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



    //Cambia el orden de ascendente a descendente
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

    //Recarga la lista en el RecyclerView
    fun recargarLista(){
        listaNotas = Auxiliar.Conexion.obtenerListaNotas(this)
        var miAdapter = MiAdaptadorRecycler(listaNotas, this)
        miRecyclerView.adapter = miAdapter
    }
}