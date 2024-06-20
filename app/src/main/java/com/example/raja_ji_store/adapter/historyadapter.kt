 package com.example.raja_ji_store.adapter

import android.content.Context
import android.graphics.drawable.Drawable


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment

import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.raja_ji_store.R
import com.example.raja_ji_store.databinding.FragmentHistoryBinding
import com.example.raja_ji_store.databinding.HistoryitemBinding
import com.example.raja_ji_store.databinding.LikeitemBinding
import com.example.raja_ji_store.model.orderdetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
 class historyadapter(
     private val context: Context,
     private val addresshistory: MutableList<String>,
     private val imagehistory :MutableList<String>,
     private val namehistory: MutableList<String>,
     private val pricehistory: MutableList<String>
 ) : RecyclerView.Adapter<historyadapter.historyViewHolder>() {


     override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int
     ): historyViewHolder {
         val binding = HistoryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return historyViewHolder(binding)
     }


     override fun onBindViewHolder(holder: historyViewHolder, position: Int) {
         holder.bind(position)
     }

     override fun getItemCount(): Int = namehistory.size
     inner class historyViewHolder(private val binding: HistoryitemBinding) : RecyclerView.ViewHolder(binding.root) {


         fun bind(position: Int) {
             binding.apply {
                 likename.text = namehistory[position]
                 likeprice.text = pricehistory[position]
                 adresslike.text = addresshistory[position]

                 val uriString = imagehistory[position]
                 Log.d("Image", "Food URL: $uriString")
                         try {
                             val uri = if (uriString.startsWith("http") || uriString.startsWith("file")) {
                                 Uri.parse(uriString)
                             } else {
                                 // Assume it's a resource ID
                                 val resourceId = uriString.toIntOrNull()
                                 if (resourceId != null) {
                                     Uri.parse("android.resource://${context.packageName}/$resourceId")
                                 } else {
                                     throw IllegalArgumentException("Invalid image URI: $uriString")
                                 }
                             }
                             Glide.with(context).load(uri).into(likeimage)

                         } catch (e: Exception) {
                             Log.e("Image Load Error", "Invalid URI or Resource ID: $uriString", e)
                             likeimage.setImageResource(R.drawable.heartt) // Fallback image resource
                         }
             }
         }
     }}