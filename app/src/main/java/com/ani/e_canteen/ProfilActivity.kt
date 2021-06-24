package com.ani.e_canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.ani.e_canteen.auth.LoginActivity
import com.ani.e_canteen.model.UsersModels
import com.ani.e_canteen.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profil.*
import org.jetbrains.anko.startActivity

class ProfilActivity : AppCompatActivity() {

    //database
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var auth: FirebaseAuth
    var userID: String? = null

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        sessionManager = SessionManager(this)
        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid
        firestoreDB = FirebaseFirestore.getInstance()


        btn_home.setOnClickListener {
            finish()
        }

        getdata()

        btn_logout.setOnClickListener {
            auth.signOut()
            sessionManager.setLogin(false)
            sessionManager.setLoginadmin(false)
            startActivity<LoginActivity>()
            finish()
        }


    }

    fun getdata() {
        val ref = firestoreDB!!.collection(Constant.akun).document(userID.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val data = it.result!!.toObject(UsersModels::class.java)
                    try {
                        txt_nama.text = data!!.nama.toString()

                    } catch (e: Exception) {
                        return@addOnCompleteListener
                    }
                }
            }
    }
}