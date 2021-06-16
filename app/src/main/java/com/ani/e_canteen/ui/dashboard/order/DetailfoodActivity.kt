package com.ani.e_canteen.ui.dashboard.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ani.e_canteen.R
import com.ani.e_canteen.database.NoteDB
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.databinding.ActivityDetailfoodBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger

class DetailfoodActivity : AppCompatActivity(), AnkoLogger {

    lateinit var binding: ActivityDetailfoodBinding

    var foto: String? = null
    var nama: String? = null
    var nama_kantin: String? = null
    var rating: Float? = null
    var kalori: String? = null
    var harga: Int? = null
    var id_makanan: String? = null

    val db by lazy {
        NoteDB(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailfood)
        binding.lifecycleOwner = this

        binding.fotoShimmer.startShimmer()

        //Bundle
        val bundle: Bundle? = intent.extras
        nama = bundle!!.getString("nama")
        nama_kantin = bundle.getString("nama_kantin")
        foto = bundle.getString("foto")
        rating = bundle.getFloat("rating")
        kalori = bundle.getString("kalori")
        harga = bundle.getInt("harga")
        id_makanan = bundle.getString("id_makanan")
        binding.txtNama.text = nama
        if (foto != null) {
            binding.fotoShimmer.stopShimmer()
            binding.fotoShimmer.visibility = View.GONE
            binding.imgFooddetail.visibility = View.VISIBLE
            Picasso.get().load(foto).centerCrop().fit().into(binding.imgFooddetail)
        }
        binding.txtRating.text = rating.toString()
        binding.txtKalori.text = "$kalori kcal"
        binding.txtHarga.text = "Rp. ${harga.toString()}"

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnTambah.setOnClickListener {
            setuplistener()
        }


    }

    fun setuplistener(){
        CoroutineScope(Dispatchers.IO).launch {
            db.noteDao().addNote(
                Note(0,id_makanan.toString(),nama.toString(),nama_kantin.toString(),harga.toString().toInt(),0)
            )
            finish()
        }
    }

}