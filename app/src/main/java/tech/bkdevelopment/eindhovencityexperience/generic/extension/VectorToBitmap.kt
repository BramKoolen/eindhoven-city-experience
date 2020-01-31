package tech.bkdevelopment.eindhovencityexperience.generic.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

fun Context.getBitmapFromVectorDrawable(drawable: Drawable): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicHeight,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

fun Context.getBitmapFromVectorDrawableResize(drawable: Drawable,width: Int, height: Int): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicHeight,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return Bitmap.createScaledBitmap(bitmap, width, height, false)
}