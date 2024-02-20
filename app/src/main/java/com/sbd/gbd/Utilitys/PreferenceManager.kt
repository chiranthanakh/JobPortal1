package com.sbd.gbd.Utilitys

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager constructor(context: Context){
    private val USER_ID="user_id"

    val sharedPreferences:SharedPreferences  = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

    fun saveUserId(id: String){
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID,id)
        editor.apply()
    }

    fun getUserId(): String?{
        return  sharedPreferences.getString(USER_ID,"")
    }

    fun logOut(){
        val editor = sharedPreferences.edit()
        editor?.clear()
        editor?.apply()
    }

    fun saveLoginState(state: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("login",state)
        editor.apply()
    }

    fun getLoginState(): Boolean{
        return  sharedPreferences.getBoolean("login",false)
    }
}