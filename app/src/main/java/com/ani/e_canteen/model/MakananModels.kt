package com.ani.e_canteen.model

import com.google.firebase.Timestamp

 class MakananModels {
     var foto: String? = null
     var harga: Int? = null
     var id_makanan: String? = null
     var kategori: String? = null
     var kalori: String? = null
     var keterangan: String? = null
     var nama: String? = null
     var rating: Float? = null
     var terjual: Int? = null
     var uid_kantin: String? = null
     var nama_kantin: String? = null

     constructor() {}
     constructor(
         foto: String?,
         harga: Int?,
         id_makanan: String?,
         kategori: String?,
         kalori: String?,
         keterangan: String?,
         nama: String?,
         rating: Float?,
         terjual: Int?,
         uid_kantin: String?,
         nama_kantin: String?
     ) {
         this.foto = foto
         this.harga = harga
         this.id_makanan = id_makanan
         this.kategori = kategori
         this.kalori = kalori
         this.keterangan = keterangan
         this.nama = nama
         this.rating = rating
         this.terjual = terjual
         this.uid_kantin = uid_kantin
         this.nama_kantin = nama_kantin
     }

 }