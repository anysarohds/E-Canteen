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
import com.ani.e_canteen.databinding.FragmentCartBinding
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.ui.dashboard.order.DetailfoodActivity
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.intentFor

class CartFragment : Fragment(), AnkoLogger {

    lateinit var binding: FragmentCartBinding

    //adapter
    private var mAdapter: OrderViewHolder? = null

    val db by lazy {
        NoteDB(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.lifecycleOwner = this

        getfoodpopular()

        return binding.root
    }


    fun getfoodpopular() {

        mAdapter = OrderViewHolder(arrayListOf(), requireContext().applicationContext)
        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(requireContext().applicationContext)
            adapter = mAdapter
        }


    }


    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val note = db.noteDao().getNotes()
            info { "dinda $note" }

            withContext(Dispatchers.Main){
                mAdapter!!.setdata(note)
            }

        }
    }

}