package com.example.colornote_joseantonio

import Auxiliar.Conexion
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.colornote_joseantonio.Auxiliar.Utiles.FechaFormato.lanzarToast
import com.example.colornote_joseantonio.Model.Nota
import kotlinx.android.synthetic.main.activity_nota_simple.*

class NotaSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_simple)

        var resul = intent.getSerializableExtra("datosNota") as Nota
        var ns = Conexion.obtenerNotaSimple(this,resul)

        txtNombre_notaSimple.text = resul.nombre
        txtCotenido_notaSimple.setText(ns.contenido)

        btnGuardar_NotaSimple.setOnClickListener(){
            ns.contenido = txtCotenido_notaSimple.text.toString()
            Conexion.modNotaSimple(this,ns)
            lanzarToast("Cambios guardados",this)


        }

    }
}