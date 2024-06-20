package com.example.raja_ji_store


import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raja_ji_store.adapter.SingerAdapater
import com.example.raja_ji_store.adapter.carbetadapter
import com.example.raja_ji_store.model.carbetdata
import com.example.raja_ji_store.model.singerdata
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class SingerActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var backbutton: ImageButton
    private var nList = ArrayList<singerdata>()
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: SingerAdapater
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singer)


        recyclerView=findViewById(R.id.singerrecyclerView)
        searchView=findViewById(R.id.singersearchView)
        backbutton=findViewById(R.id.singerbackbutton)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(this)
        backbutton.setOnClickListener{
            finish()
        }
        retrivedata()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }
    private fun filterList(query:String?) {
        if (query != null) {
            val filteredList = ArrayList<singerdata>()
            for (i in nList) {
                if (i.companyname.lowercase(Locale.ROOT)
                        .contains(query) || i.companyAdress.lowercase(Locale.ROOT).contains(query)|| (i.rating.lowercase(
                        Locale.ROOT).contains(query))
                ) {
                    filteredList.add(i)
                }
            }
            if(filteredList.isEmpty())
            {
                Toast.makeText(this,"No data found" , Toast.LENGTH_SHORT).show()
            }else{
                adapter.setFilteredList(filteredList)
            }
        }
    }
    private fun retrivedata() {
        database= FirebaseDatabase.getInstance()
        val menuRef =database.reference.child("Singer")
        menuRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nList.clear()
                    for (Snapshot in snapshot.children) {
                        val carpetd = Snapshot.getValue(singerdata::class.java)
                        if (carpetd != null) {
                            nList.add(carpetd)
                            Log.d("FirebaseData", "Retrieved: $carpetd")
                        } else {
                            Log.e("FirebaseData", "Failed to parse data for Snapshot: $Snapshot")
                        }
                    }
                    setAdapter()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun setAdapter() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter= SingerAdapater(nList,this)
    }


}