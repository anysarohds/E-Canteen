package com.ani.e_canteen.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.e_canteen.R
import com.ani.e_canteen.adapter.OrderViewHolder
import com.ani.e_canteen.adapter.PopularViewHolder
import com.ani.e_canteen.database.NoteDB
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.databinding.FragmentCartBinding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_cart.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class CartFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentCartBinding

    //adapter
    private var mAdapter: OrderViewHolder? = null

    val db by lazy {
        NoteDB(requireContext().applicationContext)
    }

    //paymentsupertbottom
    var paymentsuperbottom = MyOrderFragment()

    companion object{
        var total_belanja = 0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.lifecycleOwner = this

        binding.btnPayment.setOnClickListener {
            paymentsuperbottom = MyOrderFragment()
            paymentsuperbottom.show(requireActivity().supportFragmentManager,"MyOrderFragment")
        }
        getfoodpopular()
        getprice()
        return binding.root
    }

    fun getprice() {

        CoroutineScope(Dispatchers.IO).launch {
            val data = db.noteDao().getsum()
            info { "dinda $data" }
/*
            for (a in data) {
                var sum = a.harga * a.jumlah
                total_belanja += sum

            }


            var total = total_belanja - 2000


            withContext(Dispatchers.Main) {
                binding.priceItem.text = total_belanja.toString()
                binding.pricePromo.text = "2000"
                binding.priceTotal.text = total.toString()
            }
*/



        }

    }

    fun getfoodpopular() {
        mAdapter = OrderViewHolder(
            arrayListOf(),
            requireContext().applicationContext,
            object : OrderViewHolder.onAdapterListener {
                override fun onClick(note: Note) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.noteDao().deleteNote(note)
                        getprice()
                        loadnote()
                    }
                }

                override fun updatemines(note: Note) {
                    if (note.jumlah < 1) {
                        toast("gagal login")
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            db.noteDao().updateNote(
                                Note(
                                    note.id,
                                    note.id_makanan,
                                    note.nama,
                                    note.nama_kantin,
                                    note.harga,
                                    note.jumlah - 1
                                )
                            )
                            getprice()
                            loadnote()
                        }
                    }
                }

                override fun updateplus(note: Note) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.noteDao().updateNote(
                            Note(
                                note.id,
                                note.id_makanan,
                                note.nama,
                                note.nama_kantin,
                                note.harga,
                                note.jumlah + 1
                            )
                        )
                        getprice()
                        loadnote()
                    }

                }

            })

        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(requireContext().applicationContext)
            adapter = mAdapter
        }


    }

    fun loadnote() {
        CoroutineScope(Dispatchers.IO).launch {
            val note = db.noteDao().getNotes()

            withContext(Dispatchers.Main) {
                mAdapter!!.setdata(note)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val note = db.noteDao().getNotes()

            withContext(Dispatchers.Main) {
                mAdapter!!.setdata(note)
            }

        }
    }

}