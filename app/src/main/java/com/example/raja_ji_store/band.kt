package com.example.raja_ji_store

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raja_ji_store.adapter.BandItemAdapter
import com.example.raja_ji_store.adapter.carbetadapter
import com.example.raja_ji_store.databinding.ActivityBandBinding
import com.example.raja_ji_store.model.Banddata
import com.example.raja_ji_store.model.carbetdata
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class band : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var backbutton: ImageButton
    private var nList = ArrayList<Banddata>()
    private lateinit var adapter: BandItemAdapter

    private lateinit var database:FirebaseDatabase
    private lateinit var binding: ActivityBandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("BandActivity", "onCreate: Activity created successfully")

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        backbutton = findViewById(R.id.backbutton)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)


        backbutton.setOnClickListener {
            Log.d("BandActivity", "Back button clicked")
            finish()
        }



        // Retrieve data
        retrivedata()

        // Search view listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun retrivedata() {
        database=FirebaseDatabase.getInstance()
        val menuRef =database.reference.child("Band")
        menuRef.addListenerForSingleValueEvent(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nList.clear()
                    for (Snapshot in snapshot.children) {
                        val carpetd = Snapshot.getValue(Banddata::class.java)
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
        recyclerView.adapter= BandItemAdapter(nList,this)
    }


    private fun filterList(query: String?) {
        Log.d("BandActivity", "filterList: Filtering data with query: $query")
        if (query != null) {
            val filteredList = ArrayList<Banddata>()
            for (i in nList) {
                if (i.companyname.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                    || i.companyAdress.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                ) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Log.d("BandActivity", "filterList: No data found")
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("BandActivity", "filterList: Data filtered successfully")
                adapter.setFilteredList(filteredList)
            }
        }
    }
}
