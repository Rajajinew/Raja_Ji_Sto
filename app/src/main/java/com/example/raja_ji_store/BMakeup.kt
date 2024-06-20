package com.example.raja_ji_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raja_ji_store.adapter.bmakeupAdapter
import com.example.raja_ji_store.adapter.carbetadapter
import com.example.raja_ji_store.model.bmakeupdata
import com.example.raja_ji_store.model.carbetdata
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class BMakeup: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var backbutton: ImageButton
    private var nList = ArrayList<bmakeupdata>()
    private lateinit var adapter: bmakeupAdapter
    private  lateinit var database:FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmakeup)


        recyclerView=findViewById(R.id.recyclerView)
        searchView=findViewById(R.id.searchView)
        backbutton=findViewById(R.id.backbutton)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(this)
        backbutton.setOnClickListener{
            finish()
        }
retrivedata()


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter=bmakeupAdapter(nList,this);
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
            val filteredList = ArrayList<bmakeupdata>()
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
        val menuRef =database.reference.child("BMakeup")
        menuRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    nList.clear()
                    for (Snapshot in snapshot.children) {
                        val carpetd = Snapshot.getValue(bmakeupdata::class.java)
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
        recyclerView.adapter= bmakeupAdapter(nList,this)
    }


}