package com.example.parktikom_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.example.parktikom_admin.databinding.ActivityPilihLokasiBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PilihLokasi : AppCompatActivity() {
    private lateinit var binding: ActivityPilihLokasiBinding
    private lateinit var databaseRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< Updated upstream
        setContentView(R.layout.activity_pilih_lokasi)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
=======
        binding = ActivityPilihLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseRef = FirebaseDatabase.getInstance().getReference("LokasiParkir")

        supportActionBar?.title = "Pilih Lokasi Parkir"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
    }

    override fun onStart() {
        super.onStart()
        binding.parkir1.setOnClickListener {
            val intent = Intent(this, DetailLokasiActivity::class.java)
            intent.putExtra("lokPar", "parkir1")
            startActivity(intent)
        }

        binding.parkir2.setOnClickListener {
            val intent = Intent(this, DetailLokasiActivity::class.java)
            intent.putExtra("lokPar", "parkir2")
            startActivity(intent)
        }
>>>>>>> Stashed changes
    }
}