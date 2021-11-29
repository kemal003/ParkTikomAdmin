package com.example.parktikom_admin

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.parktikom_admin.databinding.ActivityBuatPengumumanBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.Exception

class BuatPengumuman : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var storageRef : StorageReference
    private lateinit var binding: ActivityBuatPengumumanBinding
    private lateinit var urlFoto: Uri
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
        storageRef = Firebase.storage.reference

        autoCompleteTextView = binding.autoCompleteTextView
        adapterItem = ArrayAdapter<String>(this, R.layout.item_parkir, testParkir)
        autoCompleteTextView.setAdapter(adapterItem)
        supportActionBar?.title = "Buat Pengumuman"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
    }

    override fun onStart() {
        super.onStart()
        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            try {
                Glide.with(this).load(urlFoto).into(binding.takenPhoto)
            } catch (e: Exception) {
                Toast.makeText(this, "Batal mengambil foto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.unggahFoto.setOnClickListener {
            urlFoto = FileProvider.getUriForFile(
                this,
                "${this.packageName}.provider",
                takenImage())

            takePhoto.launch(urlFoto)
        }
    }

    private fun takenImage() : File{
        return File.createTempFile(
            "IMG_",
            "jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }
}