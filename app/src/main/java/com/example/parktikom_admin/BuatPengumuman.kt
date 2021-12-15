package com.example.parktikom_admin

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.parktikom_admin.databinding.ActivityBuatPengumumanBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storageRef = Firebase.storage.reference
        pengumumanArrayList = arrayListOf()

        getListPengumuman()
        binding.judulPengumuman.addTextChangedListener(textWatcher)
        binding.isiPengumuman.addTextChangedListener(textWatcher)

        supportActionBar?.title = "Buat Pengumuman"
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupPermission()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQ)
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
            println("=======================MULAI INI GAN==================================")
            urlFoto = FileProvider.getUriForFile(
                this,
                "${this.packageName}.provider",
                takenImage())
            try {
                takePhoto.launch(urlFoto)
            }catch (e: Exception){
                Toast.makeText(this, "Batal mengambil foto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buatPengumuman.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            fotoPengumumanRef = storageRef.child("images/pengumuman/${urlFoto.lastPathSegment}")
            databaseRef = Firebase.database.getReference("Pengumuman")
            val judulPengumuman = binding.judulPengumuman.text.toString()
            val isiPengumuman = binding.isiPengumuman.text.toString()
            val uploadTask = fotoPengumumanRef.putFile(urlFoto)

            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formattedDate = currentDate.format(formatter)

            val newId = pengumumanArrayList.size + 1

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
                        binding.progressBar.visibility = View.GONE
                        val home = Intent(this, HomeActivity::class.java)
                        startActivity(home)
                        this.finish()
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

    override fun onBackPressed() {
        super.onBackPressed()
        val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
        this.finish()
    }

    private fun takenImage() : File{
        return File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val usernameInput = binding.judulPengumuman.text.isEmpty()
            val passwordInput = binding.isiPengumuman.text.toString().isEmpty()

            if (!usernameInput && !passwordInput){
                binding.unggahFoto.isEnabled = true
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}