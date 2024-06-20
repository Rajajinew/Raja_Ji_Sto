package com.example.raja_ji_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.raja_ji_store.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.birthday, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner6, ScaleTypes.FIT))

        imageList.add(SlideModel(R.drawable.dhol, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.djband.setOnClickListener {
            val intent = Intent(this,band::class.java)
            startActivity(intent)
        }
        binding.Singer.setOnClickListener {
            val intent = Intent(this, SingerActivity::class.java)
            startActivity(intent)
        }
        binding.Dancer.setOnClickListener {
            val intent = Intent(this, DancerActivity::class.java)
            startActivity(intent)
        }
        binding.decoration.setOnClickListener {
            val intent = Intent(this, Decoration::class.java)
            startActivity(intent)
        }
        binding.Hotel.setOnClickListener {
            val intent = Intent(this, HotelActivity::class.java)
            startActivity(intent)
        }
        binding.tent.setOnClickListener {
            val intent = Intent(this, carbet::class.java)
            startActivity(intent)
        }
        binding.resturent.setOnClickListener {
            val intent = Intent(this, Resturent::class.java)
            startActivity(intent)
        }
        binding.cook.setOnClickListener {
            val intent = Intent(this, Cook::class.java)
            startActivity(intent)
        }
        binding.bar.setOnClickListener {
            val intent = Intent(this, Bar::class.java)
            startActivity(intent)
        }
        binding.bridemakeup.setOnClickListener {
            val intent = Intent(this, BMakeup::class.java)
            startActivity(intent)
        }
        binding.photographer.setOnClickListener {
            val intent = Intent(this, Photographer::class.java)
            startActivity(intent)
        }
        binding.GMakeup.setOnClickListener {
            val intent = Intent(this, Gmakeup::class.java)
            startActivity(intent)
        }
        binding.imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
binding.textView.setOnClickListener{
    val intent=Intent(this,DetailActivity::class.java)
    startActivity(intent)
}
    }
}