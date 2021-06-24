package com.ani.e_canteen.database.entitas

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val id_makanan: String,
    val nama: String,
    val nama_kantin: String,
    val foto_makanan: String,
    val harga: Int,
    val jumlah: Int,
    val uid_kantin: String,
    val uid_user: String

    )