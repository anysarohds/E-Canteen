package com.ani.e_canteen.ui.dashboard.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.MenuViewHolder
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dessert.*
import kotlinx.android.synthetic.main.activity_dessert.btn_back
import kotlinx.android.synthetic.main.activity_food.*
import org.jetbrains.anko.intentFor

class DessertActivity : AppCompatActivity() {
    //adapter
    private var mAdapter: MenuViewHolder? = null

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dessert)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        btn_back.setOnClickListener {
            finish()
        }


        shimmer_dessert.startShimmer()

        getfood()
    }

    fun getfood() {
        rv_dessert.layoutManager = LinearLayoutManager(this)
        rv_dessert.setHasFixedSize(true)
        (rv_dessert.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        val docref = firestore.collection(Constant.food).whereEqualTo("kategori", "Desert").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val notesList = mutableListOf<MakananModels>()
                    for (doc in it.result!!.documents) {
                        if (doc.exists()){
                            shimmer_dessert.stopShimmer()
                            shimmer_dessert.visibility = View.GONE
                            rv_dessert.visibility = View.VISIBLE

                            val note = doc.toObject(MakananModels::class.java)
                            note!!.uid_kantin = doc.id
                            notesList.add(note)
                            mAdapter =
                                MenuViewHolder(
                                    notesList,
                                    this,
                                    firestore
                                )
                            mAdapter!!.setDialog(object : MenuViewHolder.Dialog {
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
                                            "id_makanan" to barang.id_makanan
                                        )
                                    )

                                }

                            })
                            mAdapter!!.notifyDataSetChanged()
                            rv_dessert.adapter = mAdapter

                        }

                    }
                }
            }
    }
}