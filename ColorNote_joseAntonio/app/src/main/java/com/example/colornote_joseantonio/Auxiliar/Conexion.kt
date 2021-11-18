package Auxiliar

import Conexion.AdminSQLIteConexion
import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import com.example.colornote_joseantonio.Auxiliar.Utiles
import com.example.colornote_joseantonio.Model.Nota
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
        val registro = ContentValues()
        registro.put("nombre", n.nombre)
        registro.put("tipo",n.tipo)
        registro.put("fechaHora",Utiles.FechaFormato.getFormatoFechaCompleta().format(n.fechaHora))
        bd.insert("notas", null, registro)
        bd.close()
    }

    fun obtenerNotas(contexto: AppCompatActivity):ArrayList<Nota>{
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