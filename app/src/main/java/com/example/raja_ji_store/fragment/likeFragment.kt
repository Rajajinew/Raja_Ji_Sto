package com.example.raja_ji_store.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raja_ji_store.adapter.LikeAdapter
import com.example.raja_ji_store.databinding.FragmentLikeBinding
import com.example.raja_ji_store.model.likedata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class likeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var compayname: MutableList<String>
    private lateinit var companyadress: MutableList<String>
    private lateinit var companyprice: MutableList<String>
    private lateinit var rating: MutableList<String>
    private lateinit var image: MutableList<String>
    private lateinit var adapter: LikeAdapter
    private lateinit var userId: String
    private lateinit var binding: FragmentLikeBinding
    private var likeReference: DatabaseReference? = null
    private var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        retrieveLikeData()
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        valueEventListener?.let { likeReference?.removeEventListener(it) }
    }
    private fun retrieveLikeData() {
        userId = auth.currentUser?.uid ?: ""
        likeReference = database.reference.child("user").child(userId).child("Like")
        compayname = mutableListOf()
        companyadress = mutableListOf()
        companyprice = mutableListOf()
        rating = mutableListOf()
        image = mutableListOf()

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (likeSnapshot in snapshot.children) {
                    val likeItem = likeSnapshot.getValue(likedata::class.java)
                    likeItem?.name?.let { compayname.add(it) }
                    likeItem?.price?.let { companyprice.add(it) }
                    likeItem?.address?.let { companyadress.add(it) }
                    likeItem?.ratting?.let { rating.add(it) }
                    likeItem?.imageuri?.let { image.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetched", Toast.LENGTH_SHORT).show()
            }
        }

        likeReference?.addListenerForSingleValueEvent(valueEventListener!!)
    }

    private fun setAdapter() {
        adapter = LikeAdapter(requireContext(), companyadress, image, compayname, companyprice,rating)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
}
