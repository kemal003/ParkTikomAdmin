package com.example.parktikom_admin

import java.io.Serializable

data class LokasiParkir(
    val imageUrl : String,
    val kuota : Long,
    val lokasi : String,
    val terpakai : Long,
    val deskripsi : String
) : Serializable

