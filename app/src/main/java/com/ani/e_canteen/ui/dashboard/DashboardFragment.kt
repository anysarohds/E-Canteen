package com.ani.e_canteen.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.PopularViewHolder
import com.ani.e_canteen.databinding.FragmentDashboardBinding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.menu.DessertActivity
import com.ani.e_canteen.ui.dashboard.menu.DrinkActivity
import com.ani.e_canteen.ui.dashboard.menu.FoodActivity
import com.ani.e_canteen.ui.dashboard.menu.SnackActivity
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.ani.e_canteen.utils.CustomProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startActivity


class DashboardFragment : Fragment() {

    //adapter
    private var mAdapter: PopularViewHolder? = null

    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    //loading
    lateinit var progressDialog: CustomProgressDialog

    lateinit var sessionManager: SessionManager


    lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = this

        sessionManager = SessionManager(context!!.applicationContext)
        binding.shimmerLayout.startShimmer()

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()


        binding.imgFood.setOnClickListener {
            startActivity<FoodActivity>()
        }

        binding.imgDrink.setOnClickListener {
            startActivity<DrinkActivity>()
        }

        binding.imgSnack.setOnClickListener {
            startActivity<SnackActivity>()
        }

        binding.imgDessert.setOnClickListener {
            startActivity<DessertActivity>()
        }



        getfoodpopular()

        return binding.root
    }

    fun getfoodpopular() {
        binding.rvPopular.layoutManager = LinearLayoutManager(context!!.applicationContext)
        binding.rvPopular.setHasFixedSize(true)
        (binding.rvPopular.layoutManager as LinearLayoutManager).orientation =
            LinearLayoutManager.VERTICAL
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val firestore = FirebaseFirestore.getInstance()
        val setiings = firestore.firestoreSettings.isPersistenceEnabled
        val docref =
            firestore.collection(Constant.food).orderBy("terjual", Query.Direction.DESCENDING)
                .limit(10).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val notesList = mutableListOf<MakananModels>()
                        for (doc in task.result!!.documents) {
                            if (doc.exists()) {
                                binding.shimmerLayout.stopShimmer()
                                binding.shimmerLayout.visibility = View.GONE
                                binding.rvPopular.visibility = View.VISIBLE
                                val note = doc.toObject(MakananModels::class.java)

                                note!!.uid_kantin = doc.id
                                notesList.add(note)
                                mAdapter =
                                    PopularViewHolder(
                                        notesList,
                                        context!!.applicationContext,
                                        firestoreDB!!
                                    )
                                mAdapter!!.setDialog(object : PopularViewHolder.Dialog {
                                    override fun onClick(position: Int) {
                                        val barang: MakananModels = notesList.get(position)
                                        startActivity(
                                            intentFor<DetailfoodActivity>(
                                                "foto" to barang.foto,
                                                "nama" to barang.nama,
                                                "rating" to barang.rating,
                                                "kalori" to barang.kalori,
                                                "harga" to barang.harga,
                                                "id_makanan" to barang.id_makanan
                                            )
                                        )

                                    }

                                })
                                mAdapter!!.notifyDataSetChanged()
                                binding.rvPopular.adapter = mAdapter

                            }
                        }

                    }
                }


    }


}