package com.example.raja_ji_store

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.raja_ji_store.databinding.ActivityDetailBinding
import com.example.raja_ji_store.model.likedata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class DetailActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var companynamee:String ?=null
    private var location:String ?=null
    private var price :String ?=null
    private var rating :String ?=null
    private var image :String?=null
    private lateinit var binding: ActivityDetailBinding
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //intialize firebase
        auth=FirebaseAuth.getInstance()
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.birthday, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner6, ScaleTypes.FIT))

        imageList.add(SlideModel(R.drawable.dhol, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        val imageSlider = binding.imageSlider1
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.backbutto.setOnClickListener{
            finish()
        }

     val imageList1=ArrayList<SlideModel>()
        imageList1.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))
        imageList1.add(SlideModel(R.drawable.birthday, ScaleTypes.FIT))
        imageList1.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))
        imageList1.add(SlideModel(R.drawable.banner6, ScaleTypes.FIT))
        imageList1.add(SlideModel(R.drawable.dhol, ScaleTypes.FIT))
        imageList1.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        val imageSlider1 = binding.imageSlider2
        imageSlider1.setImageList(imageList1)
        imageSlider1.setImageList(imageList1, ScaleTypes.FIT)
        val companynamee = intent.getStringExtra("name")
        val location = intent.getStringExtra("location")
        val price = intent.getStringExtra("price")
        val rating = intent.getStringExtra("rating")
        val image = intent.getStringExtra("image")

        Log.d("check", "location is coming $location")

        // Set data to views
        binding.location.text = location
        binding.price.text = price
        binding.companyname.text = companynamee
        binding.rating.text = rating

        // Load image using Glide
        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.like) // Add a placeholder image if needed
            .error(R.drawable.banner1) // Add an error image if needed
            .into(binding.pic)
    binding.addlike.setOnClickListener{
            addItemtoLike(location.toString(),image.toString(),companynamee.toString(),price.toString(),rating.toString())
        }
//        binding.btnSelectDate.setOnClickListener {
//            showDatePickerDialog()
//        }
    }

    private fun addItemtoLike(location:String ,image:String ,companynamee:String ,price:String ,rating:String) {
        val database=FirebaseDatabase.getInstance().reference
        val userId =auth.currentUser?.uid?:""
 val likeItem =likedata(location,image,companynamee,price,rating)
        database.child("user").child(userId).child("Like").push().setValue(likeItem).addOnSuccessListener {
            Toast.makeText(this ,"Item Added Sucessfully" ,Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Item is not added" ,Toast.LENGTH_SHORT).show()
        }
    }

    }







