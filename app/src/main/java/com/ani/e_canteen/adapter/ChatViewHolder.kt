package com.ani.e_canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.R
import com.ani.e_canteen.model.MakananModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ChatViewHolder(
    private val notesList: MutableList<MakananModels>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore

) : RecyclerView.Adapter<ChatViewHolder.ViewHolder>() {

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

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var foto: ImageView


        init {
            foto = view.findViewById(R.id.img_foto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_chatfoto, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        val note = notesList[position]
        try {
            Picasso.get().load(note.foto).fit().centerCrop().into(holder.foto)
            holder.itemView.setOnClickListener {
                if(dialog != null){
                    dialog!!.onClick(holder.layoutPosition)
                }
            }

        }catch (e: Exception){
            return
        }
    }

}