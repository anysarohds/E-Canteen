package com.ani.e_canteen.ui.rincian

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.e_canteen.ChatActivity
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.PesananViewHolder
import com.ani.e_canteen.adapter.SelesaiViewHolder
import com.ani.e_canteen.database.NoteDB
import com.ani.e_canteen.databinding.FragmentRincianBinding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.model.TransaksiModels
import com.ani.e_canteen.ui.cart.CartFragment
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.ani.e_canteen.utils.CustomProgressDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class RincianFragment : Fragment(), AnkoLogger {
    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    //adapter
    private var mAdapter: PesananViewHolder? = null

    private var selesaiadapter: SelesaiViewHolder? = null
    val db by lazy {
        NoteDB(requireContext().applicationContext)
    }



    lateinit var binding: FragmentRincianBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rincian, container, false)
        binding.lifecycleOwner = this

        binding.layoutDiterima.visibility = View.INVISIBLE
        binding.layoutPesanan.visibility = View.INVISIBLE

        getdata()
        return binding.root
    }

    fun getdata() {
        val docref = firestoreDB!!.collection(Constant.transaksi).document(userID.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result!!.toObject(TransaksiModels::class.java)
                    try {
                        val status = data!!.status.toString()
                        if (status.equals("selesai")) {
                            binding.layoutDiterima.visibility = View.VISIBLE
                            binding.layoutPesanan.visibility = View.INVISIBLE
                            getpesanan_selesai()
                            gettransaksi_selesai()
                        } else {
                            binding.btnDiterima.setOnClickListener {
                                val detailfirestore = firestoreDB!!.collection(Constant.transaksi)
                                    .document(userID.toString()).get().addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val transaksi =
                                                it.result!!.toObject(TransaksiModels::class.java)
                                            val firestore =
                                                firestoreDB!!.collection(Constant.pesanan).get()
                                                    .addOnSuccessListener {
                                                        if (it.isEmpty) {

                                                        } else {
                                                            for (dat in it.documents) {
                                                                val data =
                                                                    dat.toObject(MakananModels::class.java)
                                                                val pesanan_selesai: MutableMap<String, Any?> =
                                                                    HashMap()
                                                                pesanan_selesai["foto_makanan"] =
                                                                    data!!.foto_makanan
                                                                pesanan_selesai["harga"] =
                                                                    data.harga
                                                                pesanan_selesai["id_makanan"] =
                                                                    data.id_makanan
                                                                pesanan_selesai["key_makanan"] =
                                                                    data.key_makanan
                                                                pesanan_selesai["jumlah"] =
                                                                    data.jumlah
                                                                pesanan_selesai["nama"] = data.nama
                                                                pesanan_selesai["nama_kantin"] =
                                                                    data.nama_kantin
                                                                pesanan_selesai["uid_kantin"] =
                                                                    data.uid_kantin
                                                                pesanan_selesai["uid_user"] =
                                                                    data.uid_user
                                                                pesanan_selesai["pembayaran"] =
                                                                    data.pembayaran

                                                                val kirimterjual =
                                                                    firestoreDB!!.collection(
                                                                        Constant.food
                                                                    )
                                                                        .document(data.id_makanan.toString())
                                                                        .update(
                                                                            "terjual", +1
                                                                        )
                                                                val kirimdata =
                                                                    firestoreDB!!.collection(
                                                                        Constant.pesananselesai
                                                                    )
                                                                        .document(data.key_makanan.toString())
                                                                        .set(
                                                                            pesanan_selesai
                                                                        ).addOnCompleteListener {
                                                                            if (it.isSuccessful) {
                                                                                val transaksidata: MutableMap<String, Any?> =
                                                                                    HashMap()
                                                                                transaksidata["harga_total"] =
                                                                                    transaksi!!.harga_total
                                                                                transaksidata["nama_kantin"] =
                                                                                    transaksi.nama_kantin
                                                                                transaksidata["uid_kantin"] =
                                                                                    transaksi.uid_kantin
                                                                                transaksidata["uid_user"] =
                                                                                    userID.toString()
                                                                                transaksidata["pembayaran"] =
                                                                                    transaksi.pembayaran
                                                                                transaksidata["status"] =
                                                                                    "selesai"
                                                                                transaksidata["no_pesanan"] =
                                                                                    transaksi.no_pesanan
                                                                                transaksidata["waktu_diterima"] =
                                                                                    Timestamp(Date())
                                                                                transaksidata["waktu_pembayaran"] =
                                                                                    Timestamp(Date())
                                                                                transaksidata["waktu_pemesanan"] =
                                                                                    Timestamp(Date())

                                                                                val proses_selesai =
                                                                                    firestoreDB!!.collection(
                                                                                        Constant.transaksi
                                                                                    )
                                                                                        .document(
                                                                                            userID.toString()
                                                                                        )
                                                                                        .update(
                                                                                            "status",
                                                                                            "selesai"
                                                                                        )

                                                                                val docref =
                                                                                    firestoreDB!!.collection(
                                                                                        Constant.transaksi_selesai
                                                                                    ).document()
                                                                                        .set(
                                                                                            transaksidata
                                                                                        )
                                                                                        .addOnCompleteListener {
                                                                                            getpesanan_selesai()
                                                                                            gettransaksi_selesai()

                                                                                            binding.layoutPesanan.visibility =
                                                                                                View.GONE
                                                                                            binding.layoutDiterima.visibility =
                                                                                                View.VISIBLE
                                                                                        }

                                                                            } else {
                                                                            }
                                                                        }
                                                            }
                                                        }
                                                    }

                                        }
                                    }

                            }
                            binding.layoutDiterima.visibility = View.INVISIBLE
                            binding.layoutPesanan.visibility = View.VISIBLE
                            getpesanan()
                            gettransaksi()
                        }

                    } catch (e: Exception) {
                        return@addOnCompleteListener
                    }
                }
            }
    }

    fun gettransaksi() {
        val docref =
            firestoreDB!!.collection(Constant.transaksi).whereEqualTo("uid_user", userID.toString())
                .get().addOnSuccessListener {
                    if (it.isEmpty) {

                    } else {
                        try {
                            val nama_warung = it.documents[0]["nama_kantin"].toString()
                            val harga_pesan = it.documents[0]["harga_total"].toString()
                            val no_pesanan = it.documents[0]["no_pesanan"].toString()
                            val jenis_pembayaran = it.documents[0]["pembayaran"].toString()
                            binding.txtNamawarung.text = nama_warung
                            binding.txtHargapesanan.text = harga_pesan
                            binding.txtNomorpesanan.text = no_pesanan
                            binding.txtJenispembayaran.text = jenis_pembayaran
                            binding.btnChat.setOnClickListener { tas ->
                                requireActivity().supportFragmentManager.beginTransaction().replace(
                                    R.id.frame,
                                    com.ani.e_canteen.ui.chat.ChatFragment()
                                ).commit()
                            }

                        } catch (e: Exception) {
                            return@addOnSuccessListener
                        }
                    }
                }
    }

    fun getpesanan() {
        binding.rvPesanan.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvPesanan.setHasFixedSize(true)
        (binding.rvPesanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.pesanan).whereEqualTo("uid_user", userId.toString()).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
//                                binding.shimmerLayout.stopShimmer()
//                                binding.shimmerLayout.visibility = View.GONE
                                binding.rvPesanan.visibility = View.VISIBLE
                                val note = doc.toObject(MakananModels::class.java)

                                note!!.uid_kantin = doc.id
                                notesList.add(note)
                                mAdapter =
                                    PesananViewHolder(
                                        notesList,
                                        requireContext().applicationContext
                                    )
                                mAdapter!!.setDialog(object : PesananViewHolder.Dialog {
                                    override fun onClick(position: Int) {
                                        val barang: MakananModels = notesList.get(position)
                                        startActivity(
                                            intentFor<DetailfoodActivity>(
                                                "foto" to barang.foto,
                                                "nama" to barang.nama,
                                                "nama_kantin" to barang.nama_kantin,
                                                "rating" to barang.rating,
                                                "kalori" to barang.kalori,
                                                "harga" to barang.harga,
                                                "id_makanan" to barang.id_makanan,
                                                "uid_kantin" to barang.uid_kantin
                                            )
                                        )

                                    }

                                })
                                mAdapter!!.notifyDataSetChanged()
                                binding.rvPesanan.adapter = mAdapter

                            }
                        }

                    }
                }


    }

    fun getpesanan_selesai() {
        binding.rvPesananSelesai.layoutManager =
            LinearLayoutManager(requireContext().applicationContext)
        binding.rvPesananSelesai.setHasFixedSize(true)
        (binding.rvPesananSelesai.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.pesananselesai)
                .whereEqualTo("uid_user", userId.toString()).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
//                                binding.shimmerLayout.stopShimmer()
//                                binding.shimmerLayout.visibility = View.GONE
                                binding.rvPesananSelesai.visibility = View.VISIBLE
                                val note = doc.toObject(MakananModels::class.java)

                                note!!.uid_kantin = doc.id
                                notesList.add(note)
                                mAdapter =
                                    PesananViewHolder(
                                        notesList,
                                        requireContext().applicationContext
                                    )
                                mAdapter!!.setDialog(object : PesananViewHolder.Dialog {
                                    override fun onClick(position: Int) {
                                        val barang: MakananModels = notesList.get(position)
                                        startActivity(
                                            intentFor<DetailfoodActivity>(
                                                "foto" to barang.foto,
                                                "nama" to barang.nama,
                                                "nama_kantin" to barang.nama_kantin,
                                                "rating" to barang.rating,
                                                "kalori" to barang.kalori,
                                                "harga" to barang.harga,
                                                "id_makanan" to barang.id_makanan,
                                                "uid_kantin" to barang.uid_kantin
                                            )
                                        )

                                    }

                                })
                                mAdapter!!.notifyDataSetChanged()
                                binding.rvPesananSelesai.adapter = mAdapter

                            }
                        }

                    }
                }


    }

    fun gettransaksi_selesai() {
        val docref =
            firestoreDB!!.collection(Constant.transaksi_selesai)
                .whereEqualTo("uid_user", userID.toString())
                .orderBy("waktu_pemesanan", Query.Direction.DESCENDING)
                .get().addOnSuccessListener {
                    if (it.isEmpty) {
                        info { "dinda tidak ada" }
                    } else {
                        info { "dinda ada" }

                        val data_riwayat = it.toObjects(TransaksiModels::class.java)
                        val nama_warung = it.documents[0]["nama_kantin"].toString()
                        val harga_pesan = it.documents[0]["harga_total"].toString()
                        val no_pesanan = it.documents[0]["no_pesanan"].toString()
                        val jenis_pembayaran = it.documents[0]["pembayaran"].toString()
                        val jam_pemesanan = it.documents[0]["waktu_pemesanan"] as Timestamp
                        val jam_pembayaran = it.documents[0]["waktu_pembayaran"] as Timestamp
                        val jam_diterima = it.documents[0]["waktu_diterima"] as Timestamp
                        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
                        val netDate = Date(jam_pemesanan.toDate().time)
                        val pemesanan = sdf.format(netDate)
                        val pembayaran = sdf.format(netDate)
                        val diterima = sdf.format(netDate)

                        binding.txtNamawarung2.text = nama_warung
                        binding.txtHargapesanan2.text = harga_pesan
                        binding.txtNomorpesanan2.text = no_pesanan
                        binding.txtJenispembayaran2.text = jenis_pembayaran
                        binding.waktuPemesanan.text = pemesanan.toString()
                        binding.waktuPembayaran.text = pembayaran.toString()
                        binding.waktuSelesai.text = diterima.toString()

                        binding.btnBelilagi.setOnClickListener {
                            val belilagi: MutableMap<String, Any?> = HashMap()
                            belilagi["harga_total"] = data_riwayat[0].harga_total
                            belilagi["nama_kantin"] = data_riwayat[0].nama_kantin
                            belilagi["uid_kantin"] = data_riwayat[0].uid_kantin
                            belilagi["uid_user"] = userID.toString()
                            belilagi["pembayaran"] = data_riwayat[0].pembayaran
                            belilagi["status"] = "proses"
                            belilagi["no_pesanan"] = kodeorder()
                            val transaksi =
                                firestoreDB!!.collection(Constant.transaksi)
                                    .document(userID.toString())
                                    .set(belilagi).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val docref =
                                                firestoreDB!!.collection(Constant.pesananselesai)
                                                    .whereEqualTo("uid_user", userID.toString())
                                                    .get().addOnSuccessListener {
                                                        if (it.isEmpty) {

                                                        } else {
                                                            for (i in it.documents) {

                                                                val data =
                                                                    i.toObject(MakananModels::class.java)
                                                                val keymakanan =
                                                                    FirebaseFirestore.getInstance()
                                                                        .collection(Constant.pesanan)
                                                                        .document().id
                                                                val usermap: MutableMap<String, Any?> =
                                                                    HashMap()
                                                                usermap["foto_makanan"] =
                                                                    data!!.foto_makanan.toString()
                                                                usermap["harga"] = data.harga
                                                                usermap["id_makanan"] =
                                                                    data.id_makanan
                                                                usermap["key_makanan"] = keymakanan
                                                                usermap["jumlah"] = data.jumlah
                                                                usermap["nama"] = data.nama
                                                                usermap["nama_kantin"] =
                                                                    data.nama_kantin
                                                                usermap["uid_kantin"] =
                                                                    data.uid_kantin
                                                                usermap["uid_user"] =
                                                                    userID.toString()
                                                                usermap["pembayaran"] =
                                                                    data.pembayaran
                                                                val deletepesanan_selesai =
                                                                    firestoreDB!!.collection(
                                                                        Constant.pesananselesai
                                                                    )
                                                                        .document(data.key_makanan.toString())
                                                                        .delete()

                                                                val delete_pesanan_proses =
                                                                    firestoreDB!!.collection(
                                                                        Constant.pesanan
                                                                    )
                                                                        .document(data.key_makanan.toString())
                                                                        .delete()

                                                                val ref =
                                                                    firestoreDB!!.collection(
                                                                        Constant.pesanan
                                                                    )
                                                                        .document(keymakanan)
                                                                        .set(usermap)
                                                                        .addOnCompleteListener {
                                                                            if (it.isSuccessful) {
                                                                                getpesanan()
                                                                                gettransaksi()
                                                                                binding.layoutPesanan.visibility =
                                                                                    View.VISIBLE
                                                                                binding.layoutDiterima.visibility =
                                                                                    View.INVISIBLE

                                                                            } else {
                                                                                toast("Ulangi")
                                                                            }
                                                                        }
                                                            }

                                                        }
                                                    }
                                        } else {
                                            toast("Ulangi")
                                        }
                                    }
                        }


                    }
                }
    }

    fun kodeorder(): String {
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
//        val outputStrLength = (0..5).shuffled().first()
        val outputStrLength = (5)

        return (1..outputStrLength)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}

