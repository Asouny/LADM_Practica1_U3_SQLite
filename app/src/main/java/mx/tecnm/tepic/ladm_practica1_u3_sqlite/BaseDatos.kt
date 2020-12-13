package mx.tecnm.tepic.ladm_practica1_u3_sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE ACTIVIDADES(IDACTIVIDAD INTEGER PRIMARY KEY AUTOINCREMENT, DESCRIPCION VARCHAR(200), " +
                "FCAPTURA DATE, FENTREGA DATE, EVIDENCIA BLOB)")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}