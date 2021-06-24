package com.ani.e_canteen

import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.adapter.MenuViewHolder
import com.ani.e_canteen.adapter.PopularViewHolder
import com.ani.e_canteen.adapter.SearchWarungViewHolder
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor

class SearchActivity : AppCompatActivity(),AnkoLogger {
    var nama:String? = null
    var btnback:ImageView? = null

    var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<MakananModels, MyViewHolder>? = null
    //adapter
    private var mAdapter: SearchWarungViewHolder? = null
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var mRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        firestoreDB = FirebaseFirestore.getInstance()
        mRecyclerView = findViewById(R.id.rv_search)
        btnback = findViewById(R.id.btn_back)

        btnback!!.setOnClickListener {
            finish()
        }
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))


        edt_search.addTextChangeListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (FirebaseRecyclerAdapter!=null){
                    val searchText = edt_search.getText().toString().trim()
                    if (!TextUtils.isEmpty(searchText)){
                        val firstLetterCapital: String = searchText.substring(0, 1)
                            .toUpperCase() + searchText.substring(1)
                        loadFirebaseData(firstLetterCapital)

                    }
                }
                else{
                    val searchText = edt_search.getText().toString().trim()
                    if (!TextUtils.isEmpty(searchText)){
                        val firstLetterCapital: String = searchText.substring(0, 1)
                            .toUpperCase() + searchText.substring(1)
                        loadFirebaseData(firstLetterCapital)

                    }

                }
            }
        } )


    }

    private fun loadFirebaseData(searchText : String) {

        if(searchText.isEmpty()){

            FirebaseRecyclerAdapter?.notifyDataSetChanged()
            mRecyclerView.adapter = FirebaseRecyclerAdapter

        }else {
            val mLayoutManager = LinearLayoutManager(this)
            mRecyclerView.layoutManager = mLayoutManager
            mRecyclerView.itemAnimator = DefaultItemAnimator()

            val docre = firestoreDB!!.collection(Constant.food).orderBy("nama").startAt(searchText).endAt(searchText + "\uf8ff").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in it.result!!.documents) {
                            if (doc.exists()){
                                mRecyclerView.visibility = View.VISIBLE

                                val note = doc.toObject(MakananModels::class.java)
                                info { "dinda ${note!!.nama}" }
                                note!!.uid_kantin = doc.id
                                notesList.add(note)
                                mAdapter =
                                    SearchWarungViewHolder(
                                        notesList,
                                        this,
                                        firestoreDB!!
                                    )

                                mAdapter!!.setDialog(object : SearchWarungViewHolder.Dialog {
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
                                mRecyclerView.adapter = mAdapter

                            }

                        }

                    } else {


                    }

                }

        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var  nama : TextView= itemView.findViewById(R.id.txt_namamakanan)
       var foto  : ImageView= itemView.findViewById(R.id.img_foto)
       var jumlah : TextView= itemView.findViewById(R.id.txt_jumlah)


    }


}