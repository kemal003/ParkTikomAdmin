package com.example.parktikom_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.example.parktikom_admin.databinding.ActivityPilihLokasiBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class PilihLokasi : AppCompatActivity() {
    private lateinit var binding: ActivityPilihLokasiBinding
    private lateinit var databaseRef : DatabaseReference
    private lateinit var option: String
    private lateinit var lokasi1 : LokasiParkir
    private lateinit var lokasi2 : LokasiParkir


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Pilih Lokasi Parkir"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
        option = intent.getStringExtra("option")!!
        databaseRef = FirebaseDatabase.getInstance().getReference("LokasiParkir")
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        databaseRef.child("parkir1").get().addOnSuccessListener {
            val imageUrl = it.child("foto").value.toString()
            val kuota = it.child("kuota").value as Long
            val lokasi = it.child("lokasi").value.toString()
            val terpakai = it.child("terpakai").value as Long
            val deskripsi = it.child("deskripsi").value.toString()
            lokasi1 = LokasiParkir(imageUrl, kuota, lokasi, terpakai, deskripsi)
            binding.kuotaParkir1.text = "$terpakai / $kuota"
        }

        databaseRef.child("parkir2").get().addOnSuccessListener {
            val imageUrl = it.child("foto").value.toString()
            val kuota = it.child("kuota").value as Long
            val lokasi = it.child("lokasi").value.toString()
            val terpakai = it.child("terpakai").value as Long
            val deskripsi = it.child("deskripsi").value.toString()
            lokasi2 = LokasiParkir(imageUrl, kuota, lokasi, terpakai, deskripsi)
            binding.kuotaParkir2.text = "$terpakai / $kuota"
        }


        binding.parkir1.setOnClickListener {
            if (option == "editKuota"){
                val intent = Intent(this, DetailLokasiActivity::class.java)
                intent.putExtra("lokPar", "parkir1")
                intent.putExtra("detailLokasi", lokasi1 as Serializable)
                startActivity(intent)
            } else {
                val intent = Intent(this, ScannerActivity::class.java)
                intent.putExtra("lokPar", "parkir1")
                intent.putExtra("detailLokasi", lokasi1 as Serializable)
                intent.putExtra("option", option)
                startActivity(intent)
            }
        }

        binding.parkir2.setOnClickListener {
            if (option == "editKuota"){
                val intent = Intent(this, DetailLokasiActivity::class.java)
                intent.putExtra("lokPar", "parkir2")
                intent.putExtra("detailLokasi", lokasi2 as Serializable)
                startActivity(intent)
            } else {
                val intent = Intent(this, ScannerActivity::class.java)
                intent.putExtra("lokPar", "parkir2")
                intent.putExtra("detailLokasi", lokasi2 as Serializable)
                intent.putExtra("option", option)
                startActivity(intent)
            }
        }
    }
}