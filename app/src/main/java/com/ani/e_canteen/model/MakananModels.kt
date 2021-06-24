package com.ani.e_canteen.model

class MakananModels {
     var foto: String? = null
     var foto_makanan: String? = null
     var harga: Int? = null
     var jumlah: Int? = null
     var id_makanan: String? = null
     var kategori: String? = null
     var kalori: String? = null
     var keterangan: String? = null
     var nama: String? = null
     var rating: Float? = null
     var terjual: Int? = null
     var uid_kantin: String? = null
     var nama_kantin: String? = null
     var pembayaran: String? = null
     var uid_user: String? = null
     var key_makanan: String? = null

     constructor() {}
    constructor(
        foto: String?,
        foto_makanan: String?,
        harga: Int?,
        jumlah: Int?,
        id_makanan: String?,
        kategori: String?,
        kalori: String?,
        keterangan: String?,
        nama: String?,
        rating: Float?,
        terjual: Int?,
        uid_kantin: String?,
        nama_kantin: String?,
        pembayaran: String?,
        uid_user: String?,
        key_makanan: String?
    ) {
        this.foto = foto
        this.foto_makanan = foto_makanan
        this.harga = harga
        this.jumlah = jumlah
        this.id_makanan = id_makanan
        this.kategori = kategori
        this.kalori = kalori
        this.keterangan = keterangan
        this.nama = nama
        this.rating = rating
        this.terjual = terjual
        this.uid_kantin = uid_kantin
        this.nama_kantin = nama_kantin
        this.pembayaran = pembayaran
        this.uid_user = uid_user
        this.key_makanan = key_makanan
    }


}