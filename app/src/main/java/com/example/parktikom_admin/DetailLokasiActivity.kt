package com.example.parktikom_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parktikom_admin.databinding.ActivityDetailLokasiBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailLokasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLokasiBinding
    private lateinit var databaseRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lokPar = intent.getStringExtra("lokPar")
        databaseRef = FirebaseDatabase.getInstance().getReference("LokasiParkir/$lokPar/token")
        val newToken = databaseRef.push()
        val newTokenID = newToken.key
        newToken.setValue(Token(newTokenID!!, "Date1", "Date2", "CurrentUser"))
        println(newTokenID)
    }
}