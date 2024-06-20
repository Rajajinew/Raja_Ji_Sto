package com.example.raja_ji_store.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.raja_ji_store.R
import com.example.raja_ji_store.databinding.FragmentOrderedBinding
import com.example.raja_ji_store.databinding.FragmentProfileBinding
import com.example.raja_ji_store.model.usermodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.combine

class profileFragment : Fragment() {
    private val auth= FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        saveUserdata()
        binding.saveinformation.setOnClickListener {
            val name =binding.nameofuser.text.toString()
            val address=binding.addressofuser.text.toString()
            val phonenumber =binding.contactofuser.text.toString()
            val email=binding.emailofuser.text.toString()
            updateuserdata(name,address,phonenumber,email)
        }
       return binding.root

    }

    private fun updateuserdata(name: String, address: String, phonenumber: String, email: String) {
        val userId=auth.currentUser?.uid
        if(userId !=null){
            val userreference=database.getReference("user").child(userId)
            val userdata = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phonenumber" to phonenumber,

            )
            userreference.setValue(userdata).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile update sucessfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(requireContext(), "Profile update Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun saveUserdata() {

        val userId=auth.currentUser?.uid
        if(userId !=null){
            val userReference =database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userprofile =snapshot.getValue(usermodel::class.java)
                        if(userprofile !=null){
                            binding.nameofuser.setText(userprofile.name)
                            binding.addressofuser.setText(userprofile.address)
                            binding.contactofuser.setText(userprofile.phone)
                            binding.emailofuser.setText(userprofile.email)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


}