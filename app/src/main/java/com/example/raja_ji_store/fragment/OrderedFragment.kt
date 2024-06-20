package com.example.raja_ji_store.fragment


import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.raja_ji_store.R
import com.example.raja_ji_store.databinding.FragmentOrderedBinding
import com.example.raja_ji_store.model.orderdetail
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class OrderedFragment : Fragment() {
    private lateinit var binding: FragmentOrderedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var nameofusers: String
    private lateinit var addressofuser: String
    private lateinit var contactnumber: String
    private lateinit var emailofuser: String
    private lateinit var selectedDate: String
    private lateinit var companyName: String
    private lateinit var companyAddress: String
    private lateinit var companyPrice: String
    private lateinit var companyImage: String
    private lateinit var orderAccepted :String
    private lateinit var paymentrecieve :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderedBinding.inflate(inflater, container, false)

        binding.btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        setUserData()
        binding.order.setOnClickListener {
            gatherUserData()
            if (areInputsValid()) {
                placeOrder()
            } else {
                Toast.makeText(requireContext(), "sab thik hai", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.tvSelectedDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun gatherUserData() {
        nameofusers = binding.nameofuser.text.toString().trim()
        addressofuser = binding.addressofuser.text.toString().trim()
        contactnumber = binding.contactofuser.text.toString().trim()
        emailofuser = binding.emailofuser.text.toString().trim()
        companyName = binding.companyName.text.toString().trim()
        companyImage=binding.Image.toString()
        companyAddress = binding.Address.text.toString().trim()
        companyPrice = binding.Price.text.toString().trim()
        selectedDate = binding.tvSelectedDate.text.toString().trim()
    }

    private fun areInputsValid(): Boolean {
        return nameofusers.isNotBlank() && addressofuser.isNotBlank() && contactnumber.isNotBlank() &&
                emailofuser.isNotBlank() && selectedDate.isNotBlank()
    }

    private fun placeOrder() {

        userId=auth.currentUser?.uid?:""
        val itemPushKey = databaseReference.child("Orderdetail").push().key
        val orderDetail = orderdetail(
                userId, nameofusers, addressofuser, emailofuser,
                companyName, companyAddress, companyPrice, companyImage, contactnumber, selectedDate,itemPushKey,false,false
            )
            val orderReference = databaseReference.child("Orderdetail").child(itemPushKey!!)
            orderReference.setValue(orderDetail).addOnSuccessListener {
                Toast.makeText(requireContext(), "order is accepted", Toast.LENGTH_SHORT).show()
                removeItemFromLike()
                addordertohistory(orderDetail)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "order is not accepted" ,Toast.LENGTH_SHORT).show()
            }
    }

    private fun addordertohistory(orderDetail: orderdetail) {
        databaseReference.child("user").child(userId).child("BuyHistory").child(orderDetail.itemPushKey!!)
            .setValue(orderDetail).addOnSuccessListener {

            }
    }


    private fun removeItemFromLike() {
        val likeItemReference = databaseReference.child("user").child(userId).child("Like")
        likeItemReference.removeValue()
    }

    private fun setUserData() {
        val user = auth.currentUser

        user?.let {
            val userId = it.uid
            if (userId.isEmpty()) {
                Toast.makeText(requireContext(), "User is not authenticated", Toast.LENGTH_SHORT).show()

            }
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java).orEmpty()
                        val address = snapshot.child("address").getValue(String::class.java).orEmpty()
                        val phoneNumber = snapshot.child("phonenumber").getValue(String::class.java).orEmpty()
                        val email = snapshot.child("email").getValue(String::class.java).orEmpty()
                        binding.apply {
                            nameofuser.setText(names)
                            addressofuser.setText(address)
                            contactofuser.setText(phoneNumber)
                            emailofuser.setText(email)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(requireContext(), "something error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.companyName.text = it.getString("name")
            binding.Address.text = it.getString("address")
            binding.Price.text = it.getString("price")
            val image = it.getString("image")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.like) // Add a placeholder image if needed
                .error(R.drawable.banner1) // Add an error image if needed
                .into(binding.Image)
        }
    }

    companion object {
        fun newInstance(name: String, address: String, image: String, price: String): OrderedFragment {
            return OrderedFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("address", address)
                    putString("image", image)
                    putString("price", price)
                }
            }
        }
    }
}
