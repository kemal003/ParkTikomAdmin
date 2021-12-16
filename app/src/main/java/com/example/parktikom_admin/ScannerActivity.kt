package com.example.parktikom_admin

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.parktikom_admin.databinding.ActivityScannerBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner : CodeScanner
    private lateinit var binding: ActivityScannerBinding
    private lateinit var databaseReferenceToken: DatabaseReference
    private lateinit var databaseReferenceUser: DatabaseReference
    private lateinit var databaseReferenceLokasiParkir: DatabaseReference
    private lateinit var option: String
    private lateinit var lokPar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))

        databaseReferenceToken = FirebaseDatabase.getInstance().getReference("Token")
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users")
        databaseReferenceLokasiParkir = FirebaseDatabase.getInstance().getReference("LokasiParkir")
        option = intent.getStringExtra("option")!!
        lokPar = intent.getStringExtra("lokPar")!!
        when(option) {
            "konfirmasiMasuk" -> {
                supportActionBar?.title = "Konfirmasi Masuk"
            }
            "konfirmasiKeluar" -> {
                supportActionBar?.title = "Konfirmasi Keluar"
            }
        }
        setupPermission()
        codeScanner()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        binding.btnScan.setOnClickListener {
            val code = binding.qrScannedCode.text.toString()
            when(option){
                "konfirmasiMasuk" -> {
                    databaseReferenceToken.child(code).child("endTime").get().addOnSuccessListener {
                        val endTime = LocalTime.parse(it.value.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        if (LocalTime.now().isAfter(endTime)){
                            Toast.makeText(this, "Maaf, token sudah kedaluarsa", Toast.LENGTH_SHORT).show()
                            databaseReferenceToken.child(code).removeValue()
                        }
                    }
                    databaseReferenceToken.child(code).child("checkedIn").setValue(true).addOnSuccessListener {
                        Toast.makeText(this, "Berhasil Masuk!", Toast.LENGTH_SHORT).show()
                    }

                }
                "konfirmasiKeluar" -> {
                    databaseReferenceLokasiParkir.child(lokPar).child("terpakai").get().addOnSuccessListener {
                        val nowTerpakai = it.value.toString().toLong()
                        val updatedTerpakai : Long = nowTerpakai - 1
                        databaseReferenceLokasiParkir.child(lokPar).child("terpakai").setValue(updatedTerpakai)
                    }
                    databaseReferenceToken.child(code).removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Berhasil Keluar!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scn)
        var idUser = ""

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val code = it.text
                    databaseReferenceToken.child(code).get().addOnSuccessListener {
                        idUser = it.child("owner").value.toString()
                        val tanggal = it.child("startDate").value.toString()
                        val startTime = it.child("startTime").value.toString()
                        binding.tanggalPemesanan.text = "$tanggal $startTime"
                    }

                    databaseReferenceUser.child(idUser).child("nama").get().addOnSuccessListener {
                        binding.namaPemesan.text = it.value.toString()
                    }
                    binding.qrScannedCode.text = code
                    binding.showData.visibility = View.VISIBLE
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            binding.scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}