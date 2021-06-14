package com.ani.e_canteen.admin

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.MakananViewHolder
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.utils.Constant
import com.ani.e_canteen.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin.*
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.lang.Exception

class AdminActivity : AppCompatActivity() {

    //adapter
    private var mAdapter: MakananViewHolder? = null

    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    //loading
    lateinit var progressDialog: CustomProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()

        rv_menu1.setOnClickListener {
            startActivity<AddFoodActivity>()
        }

        getmakanan()
    }


    //ambil data makanan
    private fun getmakanan() {
        rvMakanan.layoutManager = LinearLayoutManager(this)
        rvMakanan.setHasFixedSize(true)
        (rvMakanan.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.food).whereEqualTo("uid_kantin", userId.toString()).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
                                val note = doc.toObject(MakananModels::class.java)

                                note!!.uid_kantin = doc.id
                                notesList.add(note)
                                mAdapter =
                                    MakananViewHolder(
                                        notesList,
                                        this,
                                        firestoreDB!!
                                    )
                                mAdapter!!.setDialog(object : MakananViewHolder.Dialog {
                                    override fun onClick(position: Int) {

                                    }

                                })
                                mAdapter!!.notifyDataSetChanged()
                                rvMakanan.adapter = mAdapter

                            }
                        }

                    }
                }

    }

}

