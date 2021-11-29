package com.example.parktikom_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.parktikom_admin.databinding.ActivityBuatPengumumanBinding

class BuatPengumuman : AppCompatActivity() {
    private lateinit var binding: ActivityBuatPengumumanBinding
    private val testParkir = arrayOf(
        "parkir1",
        "parkir2"
    )
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItem : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autoCompleteTextView = binding.autoCompleteTextView
        adapterItem = ArrayAdapter<String>(this, R.layout.item_parkir, testParkir)
        autoCompleteTextView.setAdapter(adapterItem)
        supportActionBar?.title = "Buat Pengumuman"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
    }

    override fun onStart() {
        super.onStart()
    }
}