package com.example.colornote_joseantonio

import Auxiliar.Conexion
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorRecycler
import com.example.colornote_joseantonio.Adaptadores.TareaAdapter
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.Model.NotaTareas
import kotlinx.android.synthetic.main.activity_lista_tarea.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_tarea_card.*
import java.util.*
import kotlin.collections.ArrayList

class ListaTareaActivity : AppCompatActivity() {

    var resul:Nota = Nota()
    var ntLista:ArrayList<NotaTareas> = ArrayList()

    private val cameraRequest = 1888
    lateinit var imagenSacada: ImageView

    lateinit var miRecyclerView : RecyclerView
    lateinit var listaTareas:ArrayList<NotaTareas>
    lateinit var miAdapter:TareaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_tarea)

        cargarPrincipal()

        listaTareas_listaTareas.addOnItemTouchListener(
            Utiles.FechaFormato.RecyclerItemClickListener(
                this,
                listaTareas_listaTareas,
                object : Utiles.FechaFormato.RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        when(ntLista[position].tachada){
                            0-> Conexion.modNotaTarea(this@ListaTareaActivity,ntLista[position],1)
                            1-> Conexion.modNotaTarea(this@ListaTareaActivity,ntLista[position],0)
                        }
                        recargarLista()
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        lanzarMenuOpcion(position)
                    }
                })
        )

    }

    fun lanzarMenuOpcion(position: Int){
        val builder = AlertDialog.Builder(this@ListaTareaActivity)
        builder.setTitle("Opciones")
        builder.setMessage("¿Qué desea hacer?")
        builder.setPositiveButton("Eliminar") { dialogInterface: DialogInterface, i: Int ->
            eliminarTarea(position)
        }
        builder.setNegativeButton("Tomar foto") { dialogInterface: DialogInterface, i: Int ->
            tomarFoto()
        }
        builder.show()
    }

    fun cargarPrincipal(){
        resul = intent.getSerializableExtra("datosNota") as Nota
        ntLista = Conexion.obtenerNotaTareas(this,resul)
        txtNombre_notaLista.text = resul.nombre
        listaTareas = Auxiliar.Conexion.obtenerNotaTareas(this,resul)
        miRecyclerView = findViewById(R.id.listaTareas_listaTareas) as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        miAdapter = TareaAdapter(listaTareas, this)
        miRecyclerView.adapter = miAdapter
    }

    fun tomarFoto(){
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imgCheck_itemTarea.setImageBitmap(photo)
        }
    }

    fun eliminarTarea(position: Int){
        val builder = AlertDialog.Builder(this@ListaTareaActivity)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Desea eliminar la tarea?")
        builder.setPositiveButton("Si") { dialogInterface: DialogInterface, i: Int ->
            Conexion.delNotaTarea(this@ListaTareaActivity,ntLista[position].idNT)
            recargarLista()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nota_tarea, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icAdd_main->{
                addTarea()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun addTarea(){
        var input =  EditText(this)
        input.hint = "NOMBRE DE LA TAREA"
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nueva nota")
        builder.setMessage("Introduzca el nombre de la tarea")
        builder.setView(input)
        builder.setPositiveButton("Añadir") { dialogInterface: DialogInterface, i: Int ->
            if (!input.text.isEmpty()){
                Auxiliar.Conexion.addNotaTarea(this,
                    NotaTareas(resul.idN,resul.nombre,resul.tipo,resul.fechaHora,0,input.text.toString(),0))
                recargarLista()
                Utiles.FechaFormato.lanzarToast(
                    "Tarea añadida correctamente",
                    this
                )
            }else{
                Utiles.FechaFormato.lanzarToast(
                    "La tarea necesita un nombre",
                    this
                )
            }
        }
        builder.show()
    }


    fun recargarLista(){
        ntLista = Conexion.obtenerNotaTareas(this,resul)
        var miAdapter = TareaAdapter(ntLista, this)
        miRecyclerView.adapter = miAdapter
    }
}