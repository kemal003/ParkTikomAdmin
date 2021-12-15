package com.example.parktikom_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.parktikom_admin.databinding.ActivityDetailPengumumanBinding

class DetailPengumuman : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPengumumanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        val judul = intent.getStringExtra("judul")
        val foto = intent.getStringExtra("foto")
        val tanggal = intent.getStringExtra("tanggal")
        val isi = intent.getStringExtra("isi")
        binding.judulDetailPengumuman.text = judul
        Glide.with(binding.root).load(foto).into(binding.fotoDetailPengumuman)
        binding.isiDetailPengumuman.text = isi
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}