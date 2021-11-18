package com.example.colornote_joseantonio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorRecycler
import com.example.colornote_joseantonio.Model.Nota
import java.util.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var miRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        Log.e("jose",currentDate)

        var pepe = arrayListOf<Nota>(Nota("hola","simple", Date()))
        pepe.add(Nota("hola","simple",  Date()))



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
}