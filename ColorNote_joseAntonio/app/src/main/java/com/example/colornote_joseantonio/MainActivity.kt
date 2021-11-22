package com.example.colornote_joseantonio

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorRecycler
import com.example.colornote_joseantonio.Model.Nota
import java.lang.StringBuilder
import java.util.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var miRecyclerView : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //var pepe = arrayListOf<Nota>(Nota("hola","simple", Date()))

        var pepe = Auxiliar.Conexion.obtenerListaNotas(this)







        miRecyclerView = findViewById(R.id.listaNotas_mainActivity) as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        var miAdapter = MiAdaptadorRecycler(pepe, this)
        miRecyclerView.adapter = miAdapter

        //var ml: ListView = findViewById(R.id.listVNotas_MainActivity)


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
                    val intent = Intent(this, NotaSimpleActivity::class.java)
                    startActivity(intent)
                    }else{
                        lanzarToast("La nota necesita un nombre")
                    }
                }
                builder.setNegativeButton("Lista"){ dialogInterface: DialogInterface, i: Int ->
                    if (!input.text.isEmpty()){
                        val intent = Intent(this, ListaTareaActivity::class.java)
                        Auxiliar.Conexion.addNota(this, Nota(input.text.toString(), "Lista", Date()))
                        startActivity(intent)
                    }else{
                        lanzarToast("La nota necesita un nombre")
                    }

                }

                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun lanzarToast(mensaje:String){
        val toast = Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT)
        toast.show()
    }
}