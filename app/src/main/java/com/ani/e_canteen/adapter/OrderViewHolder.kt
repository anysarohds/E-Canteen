package com.ani.e_canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.R
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.model.MakananModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class OrderViewHolder(
    private val notesList: ArrayList<Note>,
    private val context: Context,
    private val listener : onAdapterListener
) : RecyclerView.Adapter<OrderViewHolder.ViewHolder>() {

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    fun setdata(list: List<Note>) {
        notesList.clear()
        notesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama: TextView
        internal var nama_kantin: TextView
        internal var harga: TextView
        internal var counter: TextView
        internal var foto: CircleImageView
        internal var sampah: ImageButton
        internal var btnmines: RelativeLayout
        internal var btnplus: RelativeLayout


        init {
            nama = view.findViewById(R.id.nama)
            counter = view.findViewById(R.id.counter)
            nama_kantin = view.findViewById(R.id.nama_kantin)
            foto = view.findViewById(R.id.foto)
            harga = view.findViewById(R.id.harga)
            sampah = view.findViewById(R.id.icon_sampah)
            btnmines = view.findViewById(R.id.btn_mines)
            btnplus = view.findViewById(R.id.btn_plus)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        var harga = note.harga * note.jumlah
        holder.nama.text = note.nama
        holder.nama_kantin.text = note.nama_kantin
        holder.counter.text = note.jumlah.toString()
        holder.harga.text = harga.toString()
        Picasso.get().load("https://i.ytimg.com/vi/iHQ5L68G9pg/maxresdefault.jpg").centerCrop()
            .fit().into(holder.foto)
        holder.itemView.setOnClickListener {
            if (dialog != null) {
                dialog!!.onClick(holder.layoutPosition)
            }
        }

        holder.sampah.setOnClickListener {
            listener.onClick(note)
        }

        holder.btnmines.setOnClickListener {
            listener.updatemines(note)
        }

        holder.btnplus.setOnClickListener {
            listener.updateplus(note)
        }





    }

        interface onAdapterListener{
            fun onClick(note  :Note)
            fun updatemines(note : Note)
            fun updateplus(note : Note)
        }

}



