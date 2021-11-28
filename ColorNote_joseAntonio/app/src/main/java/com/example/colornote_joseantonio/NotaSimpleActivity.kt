package com.example.colornote_joseantonio

import Auxiliar.Conexion
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Auxiliar.Utiles.FechaFormato.lanzarToast
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.Model.NotaSimple
import com.example.colornote_joseantonio.Model.NotaTareas
import kotlinx.android.synthetic.main.activity_nota_simple.*

class NotaSimpleActivity : AppCompatActivity() {
    lateinit var ns: NotaSimple
    private val permissionRequest = 101
    var numeroTel:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota_simple)

        var resul = intent.getSerializableExtra("datosNota") as Nota
        ns = Conexion.obtenerNotaSimple(this,resul)

        txtNombre_notaSimple.text = resul.nombre
        txtCotenido_notaSimple.setText(ns.contenido)

        btnGuardar_NotaSimple.setOnClickListener(){
            ns.contenido = txtCotenido_notaSimple.text.toString()
            Conexion.modNotaSimple(this,ns)
            lanzarToast(getString(R.string.guardar),this)
        }

        btnCancelar_NotaSimple.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nota_simple, menu)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var input =  EditText(this)
        input.hint = getString(R.string.numeroTelefono)

        when(item.itemId){

            R.id.icMensaje_notaSimple->{
                /*
                val intent = Intent(this, ContactosActivity::class.java)
                intent.putExtra("mensaje",ns.contenido)
                startActivity(intent)*/

                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.enviarNota))
                builder.setMessage(getString(R.string.introducirNumero))
                builder.setView(input)
                builder.setPositiveButton(getString(R.string.enviar)) { dialogInterface: DialogInterface, i: Int ->
                    if (!input.text.isEmpty()){
                        numeroTel = input.text.toString()
                        enviar()
                    }else{
                        Utiles.FechaFormato.lanzarToast(
                            getString(R.string.telefonoError),
                            this
                        )
                    }
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun enviar(){
        val pm = this.packageManager
        //Esta es una comprobación previa para ver si mi dispositivo puede enviar sms o no.
        if (!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) && !pm.hasSystemFeature(
                PackageManager.FEATURE_TELEPHONY_CDMA)) {
            Toast.makeText(this,getString(R.string.errorSMS),
                Toast.LENGTH_SHORT).show()
        }
        else {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                miMensaje()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), permissionRequest)
            }
        }
    }

    private fun miMensaje() {
        val myNumber: String = numeroTel
        val myMsg: String = txtCotenido_notaSimple.text.toString()
        if (myNumber == "" || myMsg == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(myNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(myNumber, null, myMsg, null, null)
                Toast.makeText(this, getString(R.string.mensajeEnviado), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.telefonoError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                miMensaje();
            } else {
                Toast.makeText(this, getString(R.string.errorPermiso),
                    Toast.LENGTH_SHORT).show();
            }
        }
    }
}