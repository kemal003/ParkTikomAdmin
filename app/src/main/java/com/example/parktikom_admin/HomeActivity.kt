package com.example.parktikom_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parktikom_admin.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class HomeActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var pengumumanArrayList : ArrayList<Pengumuman>
    private lateinit var pengumumanRecyclerView: RecyclerView
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.header_drawable))
        pengumumanArrayList = arrayListOf()

        pengumumanRecyclerView = findViewById(R.id.recyclerPengumuman)
        pengumumanRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        getListPengumuman()

        binding.logoutButton.setOnClickListener {
            logOut()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.buatPengumuman.setOnClickListener{
            val buatPengumuman = Intent(this, BuatPengumuman::class.java)
            startActivity(buatPengumuman)
            this.finish()
        }
    }

    private fun logOut() {
        Firebase.auth.signOut()
        finish()
    }

    private fun getListPengumuman(){
        database = FirebaseDatabase.getInstance().getReference("Pengumuman")
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(pengumSnapshot in snapshot.children){
                        val pengumuman = pengumSnapshot.getValue(Pengumuman::class.java)
                        pengumumanArrayList.add(pengumuman!!)
                    }
                    pengumumanRecyclerView.adapter = AdapterPengumuman(pengumumanArrayList.reversed() as ArrayList<Pengumuman>)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}