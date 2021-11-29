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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BuatPengumuman : AppCompatActivity() {
    private lateinit var storageRef : StorageReference
    private lateinit var binding: ActivityBuatPengumumanBinding
    private lateinit var urlFoto: Uri
    private lateinit var fotoPengumumanRef : StorageReference
    private lateinit var databaseRef : DatabaseReference
    private lateinit var pengumumanArrayList : ArrayList<Pengumuman>
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
        pengumumanArrayList = arrayListOf()

        getListPengumuman()

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
                println(urlFoto)
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

        binding.buatPengumuman.setOnClickListener {
            fotoPengumumanRef = storageRef.child("images/pengumuman/${urlFoto.lastPathSegment}")
            databaseRef = Firebase.database.getReference("Pengumuman")
            val judulPengumuman = binding.judulPengumuman.text.toString()
            val isiPengumuman = binding.isiPengumuman.text.toString()
            val uploadTask = fotoPengumumanRef.putFile(urlFoto)

            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formattedDate = currentDate.format(formatter)

            val newId = pengumumanArrayList.size

            var uploadedFotoUri : Uri?
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful){
                    task.exception?.let{
                        throw it
                    }
                }
                fotoPengumumanRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadedFotoUri = task.result
                    val pengumumanBaru = Pengumuman(
                        uploadedFotoUri.toString(),
                        judulPengumuman,
                        formattedDate,
                        isiPengumuman,
                        newId)
                    databaseRef.child("$newId").setValue(pengumumanBaru).addOnSuccessListener {
                        Toast.makeText(this, "Pengumuman berhasil dibuat", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getListPengumuman(){
        databaseRef = FirebaseDatabase.getInstance().getReference("Pengumuman")
        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(pengumSnapshot in snapshot.children){
                        val pengumuman = pengumSnapshot.getValue(Pengumuman::class.java)
                        pengumumanArrayList.add(pengumuman!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun takenImage() : File{
        return File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }
}