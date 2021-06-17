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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor

class FoodActivity : AppCompatActivity(), AnkoLogger {

       //adapter
    private var mAdapter: MenuViewHolder? = null

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid


        shimmer_food.startShimmer()

        getfood()

    }

    fun getfood() {
        rv_food.layoutManager = LinearLayoutManager(this)
        rv_food.setHasFixedSize(true)
        (rv_food.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL


        val docref = firestore.collection(Constant.food).whereEqualTo("kategori", "Food").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val notesList = mutableListOf<MakananModels>()
                    for (doc in it.result!!.documents) {
                        if (doc.exists()){
                            shimmer_food.stopShimmer()
                            shimmer_food.visibility = View.GONE
                            rv_food.visibility = View.VISIBLE

                            val note = doc.toObject(MakananModels::class.java)
                            info { "dinda $note" }
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
                            rv_food.adapter = mAdapter

                        }

                    }
                }
            }
    }
}