package mx.tecnm.tepic.ladm_practica1_u3_sqlite

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object  Utils {
    fun getBytes(bitmap : Bitmap):ByteArray{
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
        return stream.toByteArray()
    }

    fun getImage(image :ByteArray):Bitmap{
        return BitmapFactory.decodeByteArray(image,0,image.size)
    }
}