package com.example.colornote_joseantonio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorRecycler
import com.example.colornote_joseantonio.Model.Nota
import java.sql.Date
import java.sql.Time

class MainActivity : AppCompatActivity() {

    lateinit var miRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var pepe = arrayListOf<Nota>(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))
        pepe.add(Nota("hola","simple", Time(10,10,10), Date(1999,10,1)))


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