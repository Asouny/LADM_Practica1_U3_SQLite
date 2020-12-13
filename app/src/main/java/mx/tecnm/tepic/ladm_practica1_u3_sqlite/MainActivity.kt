package mx.tecnm.tepic.ladm_practica1_u3_sqlite

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import mx.tecnm.tepic.ladm_practica1_u3_sqlite.*
class MainActivity : AppCompatActivity() {

    var nombreBasedatos = "escuela"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnImagen.setOnClickListener {
            cargarImagen()
        }

        btnActividad.setOnClickListener {
            insertarActividad()
        }

        btnBuscar.setOnClickListener {
            consultarPorID()
        }

        btnEliminar.setOnClickListener {
            consutlaParaEliminar()
        }
    }

    fun eliminarPorID(id:String){
        try{
            var baseDatos = BaseDatos(this, nombreBasedatos, null,1)

            var insertar = baseDatos.writableDatabase
            var SQL = "DELETE FROM ACTIVIDADES WHERE IDACTIVIDAD=?"
            var parametros = arrayOf(id)

            insertar.execSQL(SQL,parametros)
            mensaje("se elimimnó correctamente")
            insertar.close()
            baseDatos.close()

        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }

    private fun consutlaParaEliminar() {
        var campoIdbuscar = EditText(this)
        campoIdbuscar.setHint("Numero entero")
        campoIdbuscar.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("ESCRIBA ID A ELIMINAR")
            .setPositiveButton("ELIMINAR"){d,i->
                if(campoIdbuscar.text.toString().isEmpty()){
                    mensaje("ERROR: NO ESCRIBISTE UN ID")
                    return@setPositiveButton
                }
                eliminarPorID(campoIdbuscar.text.toString())
            }
            .setNeutralButton("CANCELAR"){d,i->}
            .setView(campoIdbuscar)
            .show()
    }

    fun consultarPorID(){
        var campoIdbuscar = EditText(this)
        campoIdbuscar.setHint("Numero entero")
        campoIdbuscar.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("ESCRIBA ID A BUSCAR")
            .setPositiveButton("BUSCAR"){d,i->
                if(campoIdbuscar.text.toString().isEmpty()){
                    mensaje("ERROR: NO ESCRIBISTE UN ID")
                    return@setPositiveButton
                }
                buscaPorID(campoIdbuscar.text.toString())
            }
            .setNeutralButton("CANCELAR"){d,i->}
            .setView(campoIdbuscar)
            .show()
    }

    fun buscaPorID(id:String){
        try{
            var baseDatos = BaseDatos(this,nombreBasedatos,null,1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES WHERE IDACTIVIDAD=?"
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

        }catch (error:SQLiteException) {
            mensaje(error.message.toString())
        }
    }

    private fun insertarActividad(){
        try{
            var baseDatos = BaseDatos(this, nombreBasedatos,null,1)
            var insertar = baseDatos.writableDatabase
            val bitmap = (foto.drawable as BitmapDrawable).bitmap

            var SQL = "INSERT INTO ACTIVIDADES VALUES(NULL, '${txtDescripcion.text.toString()}','${txtCaptura.text.toString()}'," +
                    "'${txtEntrega.text.toString()}','${ Utils.getBytes(bitmap)}')"

            insertar.execSQL(SQL)
            insertar.close()
            baseDatos.close()

            mensaje("se insertó correctamente")
            txtDescripcion.setText("")
            txtCaptura.setText("")
            txtEntrega.setText("")
            foto.setImageResource(R.drawable.camara)

        }catch (error:SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(mensaje:String){
        AlertDialog.Builder(this)
            .setMessage(mensaje)
            .show()

    }

    private fun cargarImagen() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            var path = data?.data
            foto.setImageURI(path)
        }
    }

}