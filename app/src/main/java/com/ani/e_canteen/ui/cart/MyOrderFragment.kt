package com.ani.e_canteen.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.ani.e_canteen.R
import com.ani.e_canteen.database.NoteDB
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.databinding.FragmentMyOrderBinding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.cart.CartFragment.Companion.paymentsuperbottom
import com.ani.e_canteen.ui.rincian.RincianFragment
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.lang.Exception


class MyOrderFragment : SuperBottomSheetFragment() {

    lateinit var binding: FragmentMyOrderBinding


    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    val db by lazy {
        NoteDB(requireContext().applicationContext)
    }
    var sukses = 0
    var pembayaran = ""
    //1 - > pembayaran ovo
    //2 - > pembayaran dana
    //3 - > pembayaran gopay
    //4 - > pembayaran virtual

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false)
        binding.lifecycleOwner = this

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()


        binding.btnOvo.setOnClickListener {
            pembayaran = "OVO"
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnDana.setOnClickListener {
            pembayaran = "DANA"
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnGopay.setOnClickListener {
            pembayaran = "GOPAY"
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse_hitam)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse)
        }

        binding.btnVirtual.setOnClickListener {
            pembayaran = "VIRTUAL"
            binding.bulatputihOvo.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihDana.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihGopay.setBackgroundResource(R.drawable.ellipse)
            binding.bulatputihVirtual.setBackgroundResource(R.drawable.ellipse_hitam)
        }

        binding.btnCfrpayment.setOnClickListener {
            getdata()
        }
        binding.imageButton.text = "Total order Rp. ${CartFragment.sum}"


        return binding.root
    }


    fun setdatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val kode = kodeorder()
            val data = db.noteDao().getNotes()
            val transaksidata: MutableMap<String, Any?> = HashMap()
            transaksidata["harga_total"] = CartFragment.sum
            transaksidata["nama"] = data[0].nama
            transaksidata["nama_kantin"] = data[0].nama_kantin
            transaksidata["uid_kantin"] = data[0].uid_kantin
            transaksidata["uid_user"] = data[0].uid_user
            transaksidata["pembayaran"] = pembayaran
            transaksidata["status"] = "proses"
            transaksidata["no_pesanan"] = kode

            val transaksi =
                firestoreDB!!.collection(Constant.transaksi).document(userID.toString())
                    .set(transaksidata).addOnCompleteListener {
                        if (it.isSuccessful) {
                            try {
                                paymentsuperbottom.dismiss()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.frame,
                                        RincianFragment()
                                    ).commit()


                            } catch (e: Exception) {
                                return@addOnCompleteListener
                            }

                        } else {
                            toast("Ulangi")
                        }
                    }



            for (i in data) {

                val keymakanan =
                    FirebaseFirestore.getInstance().collection(Constant.pesanan).document().id
                val usermap: MutableMap<String, Any?> = HashMap()
                usermap["foto_makanan"] = i.foto_makanan
                usermap["harga"] = i.harga
                usermap["id_makanan"] = i.id_makanan
                usermap["key_makanan"] = keymakanan
                usermap["jumlah"] = i.jumlah
                usermap["nama"] = i.nama
                usermap["nama_kantin"] = i.nama_kantin
                usermap["uid_kantin"] = i.uid_kantin
                usermap["uid_user"] = i.uid_user
                usermap["pembayaran"] = pembayaran
                db.noteDao().deleteNote(i)
                val ref =
                    firestoreDB!!.collection(Constant.pesanan).document(keymakanan).set(usermap)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                            } else {
                                toast("Ulangi")
                            }
                        }
            }


        }

    }
var logika_hapus = 0
    fun getdata() {
        val ref =
            firestoreDB!!.collection(Constant.transaksi).whereEqualTo("uid_user", userID.toString())
                .get().addOnSuccessListener {
                if (it.isEmpty) {
                    setdatabase()
                } else {
                    val data = it.documents[0]["status"].toString()
                    if (data.equals("selesai")) {
                        val docref = firestoreDB!!.collection(Constant.pesanan)
                            .whereEqualTo("uid_user", userID.toString()).get()
                            .addOnSuccessListener {
                                if (it.isEmpty) {

                                } else {
                                    for (hapus in it.documents) {
                                        val hapus_data = hapus.toObject(MakananModels::class.java)
                                        val hapusref = firestoreDB!!.collection(Constant.pesanan)
                                            .document(hapus_data!!.key_makanan.toString()).delete()
                                        val hapusrefselesai = firestoreDB!!.collection(Constant.pesananselesai)
                                            .document(hapus_data.key_makanan.toString()).delete()

                                    }
                                    setdatabase()

                                }
                            }

                     } else {
                        toast("Anda sudah pesan")
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