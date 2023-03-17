package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.favorite
import com.example.simplecake.service.FirebaseService
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class FavoriteListAdapter(val context: Context, val favoriteList: ArrayList<favorite>)
    :RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {
    var onItemClick: ((favorite) -> Unit)? = null
    var onItemClickRemove: ((Int,favorite) -> Unit)? = null
    var firebaseService= FirebaseService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        firebaseService.getCake(favoriteList[position].idCake){cake->
            firebaseService.getOnlyImage(cake.image!!) {

                Glide.with(context).load(it).into(holder.cakeImg)
            }
            holder.nameTV.text = cake.name
            holder.desc.text = cake.desc
            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            holder.price.text =dcf.format(cake.price) + " đ"
            holder.dellIcon.setOnClickListener {
                firebaseService.delFavorite(favoriteList[position].id){status->
                    deleteItem(position)
                    if (status){
                        Toast.makeText(context,"${cake.name} is deleted from your favorite list",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(context,"Delete fail",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun deleteItem(index: Int){
        favoriteList.removeAt(index)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return favoriteList.count()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cakeImg = view.findViewById<ImageView>(R.id.cakeImageView)
        val nameTV = view.findViewById<TextView>(R.id.nameTV)
        val desc = view.findViewById<TextView>(R.id.descTV)
        val price = view.findViewById<TextView>(R.id.priceTV)
        val dellIcon = view.findViewById<ImageView>(R.id.delImageView)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(favoriteList[adapterPosition])
            }

        }
    }
}