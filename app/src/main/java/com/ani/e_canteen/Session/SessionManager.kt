package com.alfanshter.udinlelangfix.Session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context?) {
    val privateMode = 0
    val privateName ="login"
    var Pref : SharedPreferences?=context?.getSharedPreferences(privateName,privateMode)
    var editor : SharedPreferences.Editor?=Pref?.edit()

    private val islogin = "login"
    fun setLogin(check: Boolean){
        editor?.putBoolean(islogin,check)
        editor?.commit()
    }

    fun getLogin():Boolean?
    {
        return Pref?.getBoolean(islogin,false)
    }

    private val isverivikasi = "verifikasi"
    fun setVerifikasi(check: Boolean){
        editor?.putBoolean(isverivikasi,check)
        editor?.commit()
    }

    fun getVerifikasi():Boolean?
    {
        return Pref?.getBoolean(isverivikasi,false)
    }

    private val isuploadutama = "uploadutama"
    fun setuploadutama(check: Boolean){
        editor?.putBoolean(isuploadutama,check)
        editor?.commit()
    }

    fun getuploadutama():Boolean?
    {
        return Pref?.getBoolean(isuploadutama,false)
    }

    private val status = "status"
    fun setstatuslelang(check:String?)
    {
        editor?.putString(status,check)
        editor?.commit()
    }

    fun getstatuslelang():String?
    {
        return Pref?.getString(status,"")
    }


    private val isloginadmin = "loginadmin"
    fun setLoginadmin(check: Boolean){
        editor?.putBoolean(isloginadmin,check)
        editor?.commit()
    }

    fun getLoginadmin():Boolean?
    {
        return Pref?.getBoolean(isloginadmin,false)
    }




}