package com.ani.e_canteen.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ani.e_canteen.HomeActivity
import com.ani.e_canteen.R
import com.ani.e_canteen.database.NoteDB
import com.ani.e_canteen.databinding.FragmentSettingBinding
import com.ani.e_canteen.model.UsersModels
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null

    val db by lazy {
        NoteDB(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = this

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        binding.btnBack.setOnClickListener {
            HomeActivity.navigation.visibility = View.VISIBLE
        }

        getdata()


        return binding.root
    }

    fun getdata() {
        val ref = firestore.collection(Constant.akun).document(userId.toString()).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data = it.toObject(UsersModels::class.java)
                    var namapanggilan = data!!.nama!!.split("\\s".toRegex())[0]
                    binding.txtNamacustomer.text = data.nama.toString()
                    binding.txtPanggilan.text = namapanggilan

                }
            }
    }

}