package mx.tecnm.tepic.ladm_practica1_u3_sqlite

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.lang.Exception

class MainActivity2 : AppCompatActivity() {
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btnBuscar.setOnClickListener{
            buscaPorFechaEntrega(buscartxt.toString())
        }


    }

    fun cargarLista(){
        try {
            var conexion = Actividades("","","")//recuperar data
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()

            if(data.size==0){
                if(conexion.error==3){
                    dialogo("no se pudo realizar consulta / tabla vacía")
                }
                return
            }

            var total = data.size-1
            var vector = Array<String>(data.size,{""})
            listaID = ArrayList<String>()
            (0..total).forEach {
                var agenda = data[it]
                var item = agenda.descrip+"\n"+agenda.fechaCaptura+"\n"+agenda.fechaEntrega+"\n"
                vector[it] = item
                listaID.add(agenda.id.toString())
            }
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,vector)
            lista.setOnItemClickListener { parent, view, position, id ->
                var con = Actividades("", "", "")
                con.asignarPuntero(this)
                var agendaEncontrada = con.buscar(listaID[position])
                if(con.error==4){
                    dialogo("error no se encontro id")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this)
                    .setTitle("¿Que deseas hacer?")
                    .setMessage("ID: ${agendaEncontrada.id}\n"+
                            "Lugar: ${agendaEncontrada.descrip}\nHora: ${agendaEncontrada.fechaEntrega}\nFecha: ${agendaEncontrada.fechaCaptura}\n" +
                            "Descipcion: ${agendaEncontrada.descrip}")
                    //?
                    .setPositiveButton("Editar o Eliminar evento"){ d,i-> otroActivity(agendaEncontrada)
                    }
                    .setNeutralButton("Cancelar"){d,i->}
                    .show()

            }
        }catch (e: Exception){
            dialogo(e.message.toString())
        }
    }

    fun dialogo(s:String){
        AlertDialog.Builder(this)
            .setTitle("Atencion").setMessage(s)
            .setPositiveButton("ok"){d,i->}
            .show()
    }

    private fun otroActivity(a: Actividades){
        var intento = Intent(this, MainActivity2::class.java)

        intento.putExtra("descripcion",a.descrip)
        intento.putExtra("fechaCaptura",a.fechaCaptura)
        intento.putExtra("fechaEntrega",a.fechaEntrega)
        intento.putExtra("ID",a.id)
        startActivityForResult(intento,0)

    }

    fun buscaPorFechaEntrega(id:String){
        try{
            var baseDatos = BaseDatos(this,"escuela",null,1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES WHERE FechaEntrega=?"
            var parametros = arrayOf(id)
            var cursor = select.rawQuery(SQL,parametros)

            if(cursor.moveToFirst()){
                //this.arreglo = byteArrayOf() si
                //val arreglo = arrayOf(cursor.getBlob(4)) //no
                //val arreglo = ByteArray(1)
                //arreglo = cursor.getBlob(4)

                //si hay resultado
                var data = "Actividad : ${cursor.getString(1)}\nCaptura :${cursor.getString(2)}\nEntrega: ${cursor.getString(3)}\nfoto: ${cursor.getBlob(4)}\nesta es la foto pero no la logre convertir:/"
                //val bitmap = Utils.getImage(cursor.getBlob(4))
                //foto.setImageBitmap(bitmap)

                AlertDialog.Builder(this)
                    .setTitle("Detallles de la actividad")
                    .setMessage(data)
                    .setNeutralButton("Atras"){d,i->}
                    .show()
            }else{
                mensaje("no se encontro coincidencia")
            }

            select.close()
            baseDatos.close()

        }catch (error: SQLiteException) {
            mensaje(error.message.toString())
        }
    }


    fun mensaje(mensaje:String){
        AlertDialog.Builder(this)
            .setMessage(mensaje)
            .show()

    }
}