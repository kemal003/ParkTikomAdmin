package com.example.parktikom_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.example.parktikom_admin.databinding.ActivityEditKuotaBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditKuotaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditKuotaBinding
    private lateinit var lokPar : String
    private lateinit var detailLokasi : LokasiParkir
    private lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditKuotaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Edit Kuota Parkir"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lokPar = intent.getStringExtra("lokPar").toString()
        detailLokasi = intent.getSerializableExtra("detailLokasi") as LokasiParkir

        databaseRef = FirebaseDatabase.getInstance().getReference("LokasiParkir/$lokPar")

        binding.deskripsiEditKuota.text = detailLokasi.deskripsi
        binding.terpakaiEdit.setText(detailLokasi.terpakai.toString())
        binding.kuotaEdit.setText(detailLokasi.kuota.toString())
        binding.lokasiEditKuota.text = detailLokasi.lokasi
        Glide.with(binding.root).load(detailLokasi.imageUrl).into(binding.fotoEditKuota)

    }

    override fun onStart() {
        super.onStart()

        binding.simpanButtonEditKuota.setOnClickListener {
            databaseRef.child("terpakai").setValue(binding.terpakaiEdit.text.toString().toLong())
            databaseRef.child("kuota").setValue(binding.kuotaEdit.text.toString().toLong())
            Toast.makeText(this, "Kuota Berhasil Diubah", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PilihLokasi::class.java)
            intent.putExtra("option", "editKuota")
            startActivity(intent)
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}