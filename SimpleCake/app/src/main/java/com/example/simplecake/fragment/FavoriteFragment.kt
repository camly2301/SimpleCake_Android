package com.example.simplecake.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.Activity.Cake_Details_Activity
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.R
import com.example.simplecake.adapter.FavoriteAdapter
import com.example.simplecake.adapter.FavoriteListAdapter
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson

class FavoriteFragment : Fragment() {
    lateinit var mView:View
    var mainActivity=MainActivity()
    private val firebaseService = FirebaseService()
    lateinit var rcv_cake:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity=activity as MainActivity
        mView = inflater.inflate(R.layout.fragment_favorite, container, false)
        initUI()
        val user:FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        rcv_cake.layoutManager = linearLayoutManager
        firebaseService.getListFavorite(user!!.uid){listFavorite ->
        val cakeAdapter = FavoriteListAdapter(mainActivity,listFavorite)
        rcv_cake.adapter=cakeAdapter
        cakeAdapter.onItemClick= {
            firebaseService.getCake(it.idCake) { cake ->
                val intent = Intent(context, Cake_Details_Activity::class.java)
                val gson = Gson()
                val json = gson.toJson(cake)
                intent.putExtra("json_cake", json)
                ContextCompat.startActivity(mainActivity, intent, null)

                }
            }
        }

        return mView
    }
    fun initUI(){
        rcv_cake= mView.findViewById(R.id.rcv_favorite)
    }

}