package com.example.simplecake

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog {
    lateinit var activity: Activity
    lateinit var dialog: AlertDialog
    constructor(myActivity: Activity){
        activity=myActivity
    }
    fun startLoadingDialog(){
        val builder:AlertDialog.Builder=AlertDialog.Builder(activity)
        val inflater:LayoutInflater= activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading_data,null))
        builder.setCancelable(true)
        dialog=builder.create()
        dialog.show()
    }
    fun dismissDialog(){
        dialog.dismiss()
    }
}