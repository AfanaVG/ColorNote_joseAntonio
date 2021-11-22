package Auxiliar

import Conexion.AdminSQLIteConexion
import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.Model.NotaSimple
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Conexion {
    var nombreBD = "notasBD.db3"

    fun cambiarBD(nombreBD:String){
        this.nombreBD = nombreBD
    }

    fun addNota(contexto: AppCompatActivity, n: Nota){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registroNota = ContentValues()
        registroNota.put("nombre", n.nombre)
        registroNota.put("tipo",n.tipo)
        registroNota.put("fechaHora",Utiles.FechaFormato.getFormatoFechaCompleta().format(n.fechaHora))
        bd.insert("notas", null, registroNota)
        bd.close()
        when(n.tipo){

            "Simple"->{
                addNotaSimple(contexto)

            }
            "Lista"->{
                addNotaSimple(contexto)

            }
        }


    }

    fun addNotaSimple(contexto: AppCompatActivity){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registroNotaSimple = ContentValues()
        val selectNota = bd.rawQuery("select idN from notas ORDER BY idN DESC LIMIT 1", null)

        while (selectNota.moveToNext()){
            registroNotaSimple.put("idN", selectNota.getInt(0))
            registroNotaSimple.put("contenido","hey")
            bd.insert("notasSimples", null, registroNotaSimple)
        }
        bd.close()
    }

    /**
    fun addNotaLista(contexto: AppCompatActivity){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registroNotaSimple = ContentValues()
        val selectNota = bd.rawQuery("select idN from notas ORDER BY idN DESC LIMIT 1", null)

        while (selectNota.moveToNext()){
            registroNotaSimple.put("idN", selectNota.getInt(0))
            registroNotaSimple.put("contenido","")
            bd.insert("notasTareas", null, registroNotaSimple)
        }
        bd.close()
    }*/


    fun modNotaSimple(contexto:AppCompatActivity, ns:NotaSimple):Int {
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        registro.put("nombre", ns.contenido)
        val cant = bd.update("notasSimples", registro, "idE='${ns.contenido}'", null)
        bd.close()
        return cant
    }




    fun obtenerListaNotas(contexto: AppCompatActivity):ArrayList<Nota>{
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val selectNota = bd.rawQuery("select idN,nombre,tipo,fechaHora from notas", null)
        var not:ArrayList<Nota> = arrayListOf()

        while (selectNota.moveToNext()) {
            val fecha = Utiles.FechaFormato.getFormatoFechaCompleta().parse(selectNota.getString(3))

            not.add(
                Nota(selectNota.getString(1),selectNota.getString(2),
                    fecha ))

        }
        bd.close()
        return not
    }






}