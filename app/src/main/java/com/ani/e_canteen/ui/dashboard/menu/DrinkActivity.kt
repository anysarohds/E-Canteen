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
import kotlinx.android.synthetic.main.activity_drink.*
import kotlinx.android.synthetic.main.activity_drink.btn_back
import kotlinx.android.synthetic.main.activity_food.*
import org.jetbrains.anko.intentFor

class DrinkActivity : AppCompatActivity() {

    //adapter
    private var mAdapter: MenuViewHolder? = null

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)


        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        btn_back.setOnClickListener {
            finish()
        }


        shimmer_drink.startShimmer()

        getfood()
    }


    fun getfood() {
        rv_drink.layoutManager = LinearLayoutManager(this)
        rv_drink.setHasFixedSize(true)
        (rv_drink.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        val docref = firestore.collection(Constant.food).whereEqualTo("kategori", "Drink").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val notesList = mutableListOf<MakananModels>()
                    for (doc in it.result!!.documents) {
                        if (doc.exists()){
                            shimmer_drink.stopShimmer()
                            shimmer_drink.visibility = View.GONE
                            rv_drink.visibility = View.VISIBLE

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
                            rv_drink.adapter = mAdapter

                        }

                    }
                }
            }
    }
}