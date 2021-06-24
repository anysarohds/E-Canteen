package com.ani.e_canteen.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.e_canteen.ChatActivity
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.ChatNotifViewHolder
import com.ani.e_canteen.adapter.ChatViewHolder
import com.ani.e_canteen.adapter.PopularViewHolder
import com.ani.e_canteen.databinding.FragmentChat2Binding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.jetbrains.anko.support.v4.intentFor


class ChatFragment : Fragment() {

    //adapter
    private var mAdapter: ChatViewHolder? = null
    //adapter
    private var notifadapter: ChatNotifViewHolder? = null

    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    lateinit var binding: FragmentChat2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat2, container, false)
        binding.lifecycleOwner = this

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()

        getchat()
        getnotif()
        return binding.root

    }

    fun getchat() {
        binding.rvFoto.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvFoto.setHasFixedSize(true)
        (binding.rvFoto.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.HORIZONTAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.akun_kantin).limit(10).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
                                try {
                                    binding.shimmerLayout.stopShimmer()
                                    binding.shimmerLayout.visibility = View.GONE
                                    binding.rvFoto.visibility = View.VISIBLE
                                    val note = doc.toObject(MakananModels::class.java)

                                    note!!.uid_kantin = doc.id
                                    notesList.add(note)
                                    mAdapter =
                                        ChatViewHolder(
                                            notesList,
                                            requireContext().applicationContext,
                                            firestoreDB!!
                                        )
                                    mAdapter!!.setDialog(object : ChatViewHolder.Dialog {
                                        override fun onClick(position: Int) {
                                            val barang: MakananModels = notesList.get(position)
                                            startActivity(
                                                intentFor<ChatActivity>(
                                                    "uid_kantin" to barang.uid_kantin,
                                                    "nama_kantin" to barang.nama_kantin
                                                )
                                            )

                                        }

                                    })
                                    mAdapter!!.notifyDataSetChanged()
                                    binding.rvFoto.adapter = mAdapter

                                } catch (e: Exception) {
                                    return@addOnCompleteListener
                                }

                            }
                        }

                    }
                }


    }

    fun getnotif(){
        binding.rvKantin.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvKantin.setHasFixedSize(true)
        (binding.rvKantin.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.akun_kantin).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
                                try {
                                    binding.shimmerLayoutnotif.stopShimmer()
                                    binding.shimmerLayoutnotif.visibility = View.GONE
                                    binding.rvKantin.visibility = View.VISIBLE
                                    val note = doc.toObject(MakananModels::class.java)

                                    note!!.uid_kantin = doc.id
                                    notesList.add(note)
                                    notifadapter =
                                        ChatNotifViewHolder(
                                            notesList,
                                            requireContext().applicationContext,
                                            firestoreDB!!
                                        )
                                    notifadapter!!.setDialog(object : ChatNotifViewHolder.Dialog {
                                        override fun onClick(position: Int) {
                                            val barang: MakananModels = notesList.get(position)
                                            startActivity(
                                                intentFor<ChatActivity>(
                                                    "uid_kantin" to barang.uid_kantin,
                                                    "nama_kantin" to barang.nama_kantin
                                                )
                                            )

                                        }

                                    })
                                    notifadapter!!.notifyDataSetChanged()
                                    binding.rvKantin.adapter = notifadapter

                                } catch (e: Exception) {
                                    return@addOnCompleteListener
                                }

                            }
                        }

                    }
                }


    }

}