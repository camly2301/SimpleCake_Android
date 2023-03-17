package com.example.simplecake.Activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.LoadingDialog
import com.example.simplecake.R
import com.example.simplecake.adapter.SearchAdapter
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var rcv_cakeList: RecyclerView
    lateinit var searchAdapter:SearchAdapter
    val firebaseService = FirebaseService()

    lateinit var searchView: androidx.appcompat.widget.SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
        //load Dialog
        val loadingDialog= LoadingDialog(this)
        loadingDialog.startLoadingDialog()
        val handler: Handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                loadingDialog.dismissDialog()
            }
        },4000)

        //end load

        toolbar.title = "Search"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        rcv_cakeList.layoutManager = layoutManager

        firebaseService.getFullListCake{ listCake ->
            listCake

           AdapterSearch(listCake)

        }




    }

    private fun AdapterSearch(listCake: ArrayList<Cake1>) {
        searchAdapter = SearchAdapter(this,listCake)
        if (searchAdapter!= null) {
            searchAdapter.onItemClick={
                val intent = Intent(this, Cake_Details_Activity::class.java)
                val gson = Gson()
                val json = gson.toJson(it)
                intent.putExtra("json_cake",json)
                startActivity(intent)
            }
        }
        val itemDecoration:RecyclerView.ItemDecoration=
            DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        rcv_cakeList.addItemDecoration(itemDecoration)
        rcv_cakeList.adapter=searchAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchManager:SearchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView= menu!!.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth= Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAdapter.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.getFilter().filter(newText)
                return false
            }

        })
        return true
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.cart -> {
//                val intent = Intent(this, CartActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.search->{
//                val intent=Intent(this,SearchActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun initUI(){
        toolbar=findViewById(R.id.tool_bar)
        rcv_cakeList=findViewById(R.id.rcv_cakeList)
        rcv_cakeList.isNestedScrollingEnabled = false
    }


}