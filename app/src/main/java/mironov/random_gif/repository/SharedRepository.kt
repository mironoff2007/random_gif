package mironov.random_gif.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mironov.random_gif.model.GifObject

class SharedRepository {

    val pref: SharedPreferences
    val editor: SharedPreferences.Editor
    val gson: Gson

    constructor(context: Context) {
        pref = context.getSharedPreferences("gifCache", Context.MODE_PRIVATE)
        editor = pref.edit();
        gson = Gson()
    }


    fun saveList(list: ArrayList<GifObject>) {
        editor.putString("GifList", gson.toJson(list).toString()).apply()
    }

    fun getList(): ArrayList<GifObject> {
        return gson.fromJson(pref.getString("GifList", "[]"), object : TypeToken<ArrayList<GifObject>>() {}.type)
    }

    fun clearPrefs(){
        editor.clear().commit()
    }

}