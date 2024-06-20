package com.example.raja_ji_store.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

import android.net.Uri
import android.util.Log

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.raja_ji_store.R
import com.example.raja_ji_store.databinding.LikeitemBinding
import com.example.raja_ji_store.fragment.OrderedFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class LikeAdapter(
    private val context: Context,
    private val addressLike: MutableList<String>,
    private val imageLike: MutableList<String>,
    private val nameLike: MutableList<String>,
    private val priceLike: MutableList<String>,
    private val ratingLike: MutableList<String>
) : RecyclerView.Adapter<LikeAdapter.CardViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val likeItemReference: DatabaseReference

    init {
        val database = FirebaseDatabase.getInstance()
        val userId: String = auth.currentUser?.uid ?: ""
        likeItemReference = database.reference.child("user").child(userId).child("Like")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LikeitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = nameLike.size

    inner class CardViewHolder(private val binding:LikeitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                likename.text = nameLike[position]
                likeprice.text = priceLike[position]
                adresslike.text = addressLike[position]
                ratinglike.text = ratingLike[position]

                val uriString = imageLike[position]
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
                orderkaro.setOnClickListener {
                    Log.d("CardViewHolder", "Ordering: ${likename.text}")

                    val fragment = OrderedFragment.newInstance(
                        nameLike[position],
                        addressLike[position],
                        imageLike[position],
                        priceLike[position]
                    )

                    val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView2, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }


                    deletebutton.setOnClickListener {
                        val itemPosition = adapterPosition
                        if (itemPosition != RecyclerView.NO_POSITION) {
                            deleteItem(itemPosition)
                        }
                    }
                }

        }
    }

    private fun deleteItem(itemPosition: Int) {
        getUniqueKeyForPosition(itemPosition) { uniqueKey ->
            if (uniqueKey != null) {
                removeItem(itemPosition, uniqueKey)
            } else {
                Log.e("Delete Item Error", "Unique key not found for position: $itemPosition")
            }
        }
    }

    private fun removeItem(itemPosition: Int, uniqueKey: String) {
        likeItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
            nameLike.removeAt(itemPosition)
            priceLike.removeAt(itemPosition)
            addressLike.removeAt(itemPosition)
            ratingLike.removeAt(itemPosition)
            imageLike.removeAt(itemPosition)

            Toast.makeText(context, "Delete successful", Toast.LENGTH_SHORT).show()
            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, nameLike.size)
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUniqueKeyForPosition(itemPosition: Int, onComplete: (String?) -> Unit) {
        likeItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey: String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == itemPosition) {
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase Error", "Error retrieving data: ${error.message}")
                onComplete(null)
            }
        })
    }
}

