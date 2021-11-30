package Auxiliar

import Conexion.AdminSQLIteConexion
import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
import com.example.colornote_joseantonio.Model.NotaSimple
import com.example.colornote_joseantonio.Model.NotaTareas
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


    }

    fun addNotaTarea(contexto: AppCompatActivity,nt:NotaTareas){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registroNotaTarea = ContentValues()

            registroNotaTarea.put("idN", nt.idN)
            registroNotaTarea.put("texto",nt.texto)
            registroNotaTarea.put("tachada",nt.tachada)
            bd.insert("notasTareas", null, registroNotaTarea)

        bd.close()
    }

    fun addNotaSimple(contexto: AppCompatActivity){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registroNotaSimple = ContentValues()
        val selectNota = bd.rawQuery("select idN from notas ORDER BY idN DESC LIMIT 1", null)

        while (selectNota.moveToNext()){
            registroNotaSimple.put("idN", selectNota.getInt(0))
            registroNotaSimple.put("contenido","")
            bd.insert("notasSimples", null, registroNotaSimple)
        }
        bd.close()
    }

    fun obtenerUltimaNota(contexto: AppCompatActivity):Nota{
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val selectNota = bd.rawQuery("select idN,nombre,tipo,fechaHora from notas ORDER BY idN DESC LIMIT 1", null)
        var n:Nota = Nota()

        while (selectNota.moveToNext()){
           n  = Nota(selectNota.getInt(0),selectNota.getString(1),
                selectNota.getString(2),Utiles.FechaFormato.getFormatoFechaCompleta().parse(selectNota.getString(3)))
        }

        return n
    }

    fun obtenerNotaSimple(contexto: AppCompatActivity,nota:Nota):NotaSimple{
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val selectNotaSimple = bd.rawQuery("select contenido from notasSimples where idN=${nota.idN}", null)
        var n:NotaSimple = NotaSimple(0,"null","null",Date(),"")
        while (selectNotaSimple.moveToNext()){
            n = NotaSimple(nota.idN,nota.nombre,nota.tipo,nota.fechaHora,selectNotaSimple.getString(0))
        }
        return n
    }

    fun obtenerNotaTareas(contexto: AppCompatActivity,nota:Nota):ArrayList<NotaTareas>{
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val selectNotaTarea = bd.rawQuery("select idNT,texto,tachada from notasTareas where idN=${nota.idN}", null)
        var n:ArrayList<NotaTareas> = ArrayList()
        while (selectNotaTarea.moveToNext()){
            n.add(NotaTareas(nota.idN,nota.nombre,nota.tipo,nota.fechaHora,selectNotaTarea.getInt(0),selectNotaTarea.getString(1),
                selectNotaTarea.getInt(2)))
        }
        return n
    }

    fun modNotaSimple(contexto:AppCompatActivity, ns:NotaSimple):Int {
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        registro.put("contenido", ns.contenido)
        val cant = bd.update("notasSimples", registro, "idN='${ns.idN}'", null)
        bd.close()
        return cant
    }

    fun modNotaTarea(contexto:AppCompatActivity, nt:NotaTareas,n:Int):Int {
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        registro.put("tachada", n)
        val cant = bd.update("notasTareas", registro, "idNT='${nt.idNT}'", null)
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
                Nota(selectNota.getInt(0),selectNota.getString(1),selectNota.getString(2),
                    fecha ))
        }
        bd.close()
        return not
    }

    fun delNotaTarea(contexto: AppCompatActivity, idNT: Int){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val cant = bd.delete("notasTareas", "idNT='${idNT}'", null)
        bd.close()
    }

    fun delNotaTareaTotal(contexto: AppCompatActivity, idN: Int){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val cant = bd.delete("notasTareas", "idN='${idN}'", null)
        bd.close()
    }

    fun delNotaSimple(contexto: AppCompatActivity, idN: Int){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val cant = bd.delete("notasSimples", "idN='${idN}'", null)
        bd.close()
    }

    fun delNota(contexto: AppCompatActivity, idN: Int){
        val admin = AdminSQLIteConexion(contexto, nombreBD, null, 1)
        val bd = admin.writableDatabase
        val cant = bd.delete("notas", "idN='${idN}'", null)
        bd.close()
    }




}