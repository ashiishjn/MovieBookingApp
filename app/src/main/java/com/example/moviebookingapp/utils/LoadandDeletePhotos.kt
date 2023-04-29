package com.example.moviebookingapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.example.moviebookingapp.model.InternalStoragePhoto
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.io.File
import java.io.IOException

class LoadandDeletePhotos {

    companion object {

        fun loadImage(url : String, imageView : ImageView) {
                Picasso.get()
                .load(url)
                .into(imageView)



        }

        fun loadImageIntoInternalStorage(context : Context, url : String, filename : String) {

                Picasso.get()
                    .load(url)
                    .into(object : com.squareup.picasso.Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom?) {
                            try {
//                            Log.d("Tester2", "Inside Loaded")
                                context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                                    if(!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                                        Log.d("Tester2", "Failed")
                                        throw IOException("Couldn't save bitmap.")
                                    }
                                    else
                                        Log.d("Tester2", "Success")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                // some action
                            }
                        }

                        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                            if (e != null) {
                                Log.d("Tester2", e.message.toString())
                            }
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                        Log.d("Tester2", "Inside Prepare")
                        }
                    })

//            Log.d("Tester2", "Function called")

        }

        fun loadPhotosFromInternalStorage(context : Context, fileName : String): List<InternalStoragePhoto> {
//            try {
//                val imageFile : File = File("${context.filesDir}$fileName.jpg")
//                if(imageFile.exists()) {
//                    return listOf(BitmapFactory.decodeFile(imageFile.absolutePath))
//                }
//                else
//                    throw IOException("Couldn't find file.")
//            } catch (e: Exception) {
//                Log.d("Tester2", e.toString())
//                // some action
//            }
//            return listOf()

        //            return withContext(Dispatchers.IO) {
//                Log.d("Tester2", "Load called")
                val list : ArrayList<InternalStoragePhoto> = ArrayList()
                val files = context.filesDir.listFiles()
                files?.filter { it.canRead() && it.isFile && it.name.equals("$fileName.jpg") }?.map {
//                    Log.d("Tester2", "filter called")
                    val bytes = it.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    list.add(InternalStoragePhoto(it.name, bmp))
                } ?: listOf()
            return list
//            }
        }

        fun deletePhotoFromInternalStorage(context : Context, filename: String): Boolean {
            return try {
                context.deleteFile(filename)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
