package com.example.colornote_joseantonio

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import com.example.colornote_joseantonio.Adaptadores.MiAdaptadorVH
import com.example.colornote_joseantonio.Model.Contacto

class ContactosActivity : AppCompatActivity() {

    val REQUEST_READ_CONTACTS = 79
    var seleccionado = -1
    var mobileArray: ArrayList<Contacto> = ArrayList()
    lateinit var lista: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactos)

        var ventanaActual:ContactosActivity = this

        lista= findViewById(R.id.listaContactos_Contacto)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mobileArray = obtenerContactos()
        } else {
            requestPermission();
        }
        var adaptador: ArrayAdapter<Contacto> =
            ArrayAdapter(this, R.layout.item_layout, R.id.txtNombre_ItemContacto, mobileArray)
        lista.adapter = adaptador


        lista.onItemClickListener = object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, vista: View?, pos: Int, idElemento: Long) {
                if (pos == seleccionado){
                    seleccionado = -1
                }
                else{
                    seleccionado = pos
                }

            }
        }

    }



    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_READ_CONTACTS)
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_READ_CONTACTS)
        }
    }

    fun obtenerContactos():ArrayList<Contacto> {
        var listaNombre:ArrayList<Contacto>? = ArrayList()
        var cr = this.contentResolver
        var cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null,null)
        if (cur != null){
            if (cur.count > 0){
                while(cur!=null && cur.moveToNext()){
                    var id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID).toInt())
                    var nombre = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME).toInt())

                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER).toInt()) > 0) {
                        val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                        //Sacamos todos los números de ese contacto.
                        while (pCur!!.moveToNext()) {
                            val phoneNo = pCur!!.getString(pCur!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER).toInt())
                            //Esto son los números asociados a ese contacto. Ahora mismo no hacemos nada con ellos.
                            listaNombre!!.add(Contacto(nombre,phoneNo))
                        }
                        pCur!!.close()
                    }
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return listaNombre!!
    }

    fun volver(view: View){
        finish();
    }

}