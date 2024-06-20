package com.example.raja_ji_store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var imageButton2: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var NavController=findNavController(R.id.fragmentContainerView2)
        var botomnav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        botomnav.setupWithNavController(NavController)
        imageButton2=findViewById(R.id.imageButton2)
        imageButton2.setOnClickListener{
            finish()
        }
    }
}