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
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.activity_snack.*
import kotlinx.android.synthetic.main.activity_snack.btn_back
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor

class SnackActivity : AppCompatActivity() {


    //adapter
    private var mAdapter: MenuViewHolder? = null

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snack)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        btn_back.setOnClickListener {
            finish()
        }


        shimmer_snack.startShimmer()

        getfood()
    }

    fun getfood() {
        rv_snack.layoutManager = LinearLayoutManager(this)
        rv_snack.setHasFixedSize(true)
        (rv_snack.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        val docref = firestore.collection(Constant.food).whereEqualTo("kategori", "Snack").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val notesList = mutableListOf<MakananModels>()
                    for (doc in it.result!!.documents) {
                        if (doc.exists()){
                            shimmer_snack.stopShimmer()
                            shimmer_snack.visibility = View.GONE
                            rv_snack.visibility = View.VISIBLE

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
                            rv_snack.adapter = mAdapter

                        }

                    }
                }
            }
    }
}