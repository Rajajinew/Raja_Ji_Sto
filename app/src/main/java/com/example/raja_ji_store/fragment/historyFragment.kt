package com.example.raja_ji_store.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.raja_ji_store.adapter.historyadapter
import com.example.raja_ji_store.databinding.FragmentHistoryBinding

import com.example.raja_ji_store.model.orderdetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class historyFragment : Fragment() {
    private lateinit var companynamee:MutableList<String>
    private lateinit var companyadress:MutableList<String>
    private lateinit var companyprice:  MutableList<String>
    private lateinit var adapter:historyadapter
    private lateinit var image:MutableList<String>
private lateinit var binding: FragmentHistoryBinding
private lateinit var database: FirebaseDatabase
private lateinit var auth: FirebaseAuth
private lateinit var userId :String
//private var listOfOrdereItem :MutableList<orderdetail> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    auth=FirebaseAuth.getInstance()
    database=FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        retrivehistorydata()
        setAdapter()
        return binding.root
    }

    private fun retrivehistorydata() {
        userId=auth.currentUser?.uid?:""
        val buyItemRefrence=database.reference.child("user").child(userId).child("BuyHistory")
        companynamee= mutableListOf()
        companyprice= mutableListOf()
        companyadress= mutableListOf()
       companynamee= mutableListOf()
        image= mutableListOf()
         buyItemRefrence?.addListenerForSingleValueEvent (object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistoryItem=buySnapshot.getValue(orderdetail::class.java)
                    Log.d("retrieveHistoryData", "BuyHistoryItem: $buyHistoryItem")
                    buyHistoryItem?.companyname?.let { companynamee.add(it) }
                    buyHistoryItem?.companyaddress?.let {  companyadress.add(it)}
                    buyHistoryItem?.companyprice?.let { companyprice.add(it) }
buyHistoryItem?.companyimage?.let {
    image.add(it).toString() }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter() {
        adapter= historyadapter(requireContext() , companyadress,image,companynamee,companyprice)
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerView.adapter=adapter
    }




}
