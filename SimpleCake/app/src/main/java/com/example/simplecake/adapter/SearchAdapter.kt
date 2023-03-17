package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(var context: Context, var listCake:ArrayList<Cake1>):RecyclerView.Adapter<SearchAdapter.ViewHolder>(),Filterable {
    var onItemClick : ((Cake1)-> Unit)? =null
    var listCakeSearchOld:ArrayList<Cake1> = listCake
    private val firebaseService = FirebaseService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getOnlyImage(listCake[position].id!!) {
            Glide.with(context).load(it).into(holder.img_cake_item)
        }
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        holder.tv_name_cake.text = listCake[position].name
        val dcf = DecimalFormat("#,###")
        val dcfs = DecimalFormatSymbols(Locale.getDefault())
        dcfs.groupingSeparator='.'
        dcf.decimalFormatSymbols=dcfs
        holder.tv_price_cake.text =dcf.format(listCake[position].price) + " đ"


    }

    override fun getItemCount(): Int {
        return listCake.size
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val img_cake_item = view.findViewById<ImageView>(R.id.img_cake_itemt)
        val tv_name_cake = view.findViewById<TextView>(R.id.tv_name_cake)
        val tv_price_cake = view.findViewById<TextView>(R.id.tv_price_item)


        init {
            view.setOnClickListener {
                onItemClick?.invoke(listCake[adapterPosition])

            }
        }
    }


        private val customFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val strSearch:String = constraint.toString()
            if(strSearch.isEmpty()){
                listCake=listCakeSearchOld
            }
            else{
                var listCakeFound=ArrayList<Cake1>()
                for(cake:Cake1 in listCakeSearchOld){
                    if(cake.name!!.toLowerCase().contains(strSearch.toLowerCase())){
                        listCakeFound.add(cake)
                    }
                }
                listCake=listCakeFound
            }
            val filterResults=FilterResults()
            filterResults.values=listCake
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            listCake= filterResults!!.values as ArrayList<Cake1>
            notifyDataSetChanged()
        }

    }
    override fun getFilter(): Filter {
        return customFilter
    }
}