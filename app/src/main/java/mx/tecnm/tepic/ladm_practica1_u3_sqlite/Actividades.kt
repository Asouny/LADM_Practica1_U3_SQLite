package mx.tecnm.tepic.ladm_practica1_u3_sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import java.util.*
import kotlin.collections.ArrayList

/*Agenda:
 "Id_actividad INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Descripcion VARCHAR(2000), " +
                "FechaCaptura Date " +
                "FechaEntrega Date */

class Actividades(d:String,fc:String,fe:String) {
    var fechaCaptura = fc
    var fechaEntrega = fe
    var id = 0
    var descrip = d
    var error = -1

    val nombreBaseDatos = "Actividad"
    var puntero : Context ?= null

    fun asignarPuntero(p:Context){
        puntero = p
    }

    fun insertar():Boolean{
        error = -1

        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var insertar = base.writableDatabase
            var datos = ContentValues()

            datos.put("Descripcion",descrip)
            datos.put("FechaCaptura",fechaCaptura)
            datos.put("FechaEntrega",fechaEntrega)


            var respuesta = insertar.insert("ACTIVIDADES","Id_actividad",datos)
            if(respuesta.toInt()== -1){
                error = 2
                return false
            }
        }catch (e: SQLiteException){
            error = 1
            return false
        }
        return true
    }

    fun mostrarTodos():ArrayList<Actividades>{
        var data = ArrayList<Actividades>()
        error = -1
        try{
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1 )
            var select = base.readableDatabase
            var columnas = arrayOf("*")

            var cursor  = select.query("ACTIVIDADES", columnas, null, null, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var trabajadorTemporal = Actividades(cursor.getString(1),cursor.getString(2),cursor.getString(3))

                    trabajadorTemporal.id = cursor.getInt(0)
                    data.add(trabajadorTemporal)
                }while (cursor.moveToNext())
            }else{
                error = 3
            }
        }catch (e:SQLiteException){
            error = 1
        }
        return data
    }

    fun buscar(id:String): Actividades{
        var AgendaEncontrada = Actividades("-1","-1","-1")

        error = -1
        try {
            var base = BaseDatos(puntero!!, nombreBaseDatos, null, 1)
            var select = base.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(id)

            var cursor = select.query("AGENDA", columnas, "IDAGENDA =?",idBuscar, null, null, null)
            if(cursor.moveToFirst()){
                AgendaEncontrada.id = id.toInt()
                AgendaEncontrada.descrip = cursor.getString(1)
                AgendaEncontrada.fechaCaptura = cursor.getString(2)
                AgendaEncontrada.fechaEntrega = cursor.getString(3)

            }else{
                error = 4
            }
        }catch (e:SQLiteException){
            error = 1
        }
        return AgendaEncontrada
    }

    fun eliminar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())

            var respuesta = eliminar.delete("ACTIVIDADES","Id_actividad = ?",idEliminar)
            if(respuesta.toInt()== 0){
                error = 6
                return false
            }
        }catch (e:SQLiteException){
            error = 1
            return false
        }
        return true
    }

    fun actualizar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var actualizar = base.writableDatabase
            var datos = ContentValues()
            var idActualizar = arrayOf(id.toString())


            datos.put("DESCRIPCION",descrip)
            datos.put("FechaCaptura",fechaCaptura)
            datos.put("FechaEntrega",fechaEntrega)


            var respuesta = actualizar.update("ACTIVIDADES",datos,"Id_Actividad = ?", idActualizar)
            if(respuesta.toInt()== 0){
                error = 5
                return false
            }
        }catch (e:SQLiteException){
            error = 1
            return false
        }
        return true
    }

}