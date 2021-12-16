package com.example.parktikom_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.parktikom_admin.databinding.ActivityDetailLokasiBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

class DetailLokasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLokasiBinding
    private lateinit var databaseRef : DatabaseReference
    private lateinit var lokPar : String
    private lateinit var detailLokasi : LokasiParkir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLokasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lokPar = intent.getStringExtra("lokPar").toString()
        detailLokasi = intent.getSerializableExtra("detailLokasi") as LokasiParkir

        binding.deskripsiLokasi.text = detailLokasi.deskripsi
        binding.kuotaDetailLokasi.text = "${detailLokasi.terpakai} / ${detailLokasi.kuota} Motor"
        Glide.with(binding.root).load(detailLokasi.imageUrl).into(binding.gambarDetailParkir)

        databaseRef = FirebaseDatabase.getInstance().getReference("LokasiParkir/$lokPar")
//        val newToken = databaseRef.push()
//        val newTokenID = newToken.key
//        newToken.setValue(Token(newTokenID!!, "Date1", "Date2", "CurrentUser"))
//        println(newTokenID)
    }

    override fun onStart() {
        super.onStart()

        binding.editKuotaParkirButton.setOnClickListener {
            val intent = Intent(this, EditKuotaActivity::class.java)
            intent.putExtra("lokPar", lokPar)
            intent.putExtra("detailLokasi", detailLokasi as Serializable)
            startActivity(intent)
        }
    }
}