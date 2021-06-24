package com.ani.e_canteen.model

import com.google.firebase.Timestamp

class TransaksiModels {

    var harga_total : Int? = null
    var nama_kantin : String? = null
    var no_pesanan : String? = null
    var pembayaran : String? = null
    var uid_kantin : String? = null
    var status : String? = null
    var uid_user : String? = null
    var waktu_pemesanan : Timestamp? = null
    var waktu_pembayaran : Timestamp? = null
    var waktu_diterima: Timestamp? = null

     constructor() {}
    constructor(
        harga_total: Int?,
        nama_kantin: String?,
        no_pesanan: String?,
        pembayaran: String?,
        uid_kantin: String?,
        status: String?,
        uid_user: String?,
        waktu_pemesanan: Timestamp?,
        waktu_pembayaran: Timestamp?,
        waktu_diterima: Timestamp?
    ) {
        this.harga_total = harga_total
        this.nama_kantin = nama_kantin
        this.no_pesanan = no_pesanan
        this.pembayaran = pembayaran
        this.uid_kantin = uid_kantin
        this.status = status
        this.uid_user = uid_user
        this.waktu_pemesanan = waktu_pemesanan
        this.waktu_pembayaran = waktu_pembayaran
        this.waktu_diterima = waktu_diterima
    }

}