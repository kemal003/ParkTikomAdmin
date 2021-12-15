package com.example.parktikom_admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterPengumuman(
    private var eContext : Context,
    private val listPengumuman: ArrayList<Pengumuman>,
):
    RecyclerView.Adapter<AdapterPengumuman.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pengumKlik : CardView = itemView.findViewById(R.id.pengumKlik)
        val fotoParkiran : ImageView = itemView.findViewById(R.id.parkiran_foto)
        val judul : TextView = itemView.findViewById(R.id.judul)
        val tanggal : TextView = itemView.findViewById(R.id.tanggal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.pengumuman, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (foto, judul, tanggal, isi) = listPengumuman[position]
        Glide.with(holder.itemView).load(foto).into(holder.fotoParkiran)
        holder.judul.text = judul
        holder.tanggal.text = tanggal

        holder.pengumKlik.setOnClickListener {
            val intent = Intent(eContext, DetailPengumuman::class.java)
            intent.putExtra("judul", judul)
            intent.putExtra("foto", foto)
            intent.putExtra("tanggal", tanggal)
            intent.putExtra("isi", isi)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            eContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }
}