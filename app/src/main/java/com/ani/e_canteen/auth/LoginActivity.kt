package com.ani.e_canteen.auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.ani.e_canteen.HomeActivity
import com.ani.e_canteen.R
import com.ani.e_canteen.utils.Constant
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : AppCompatActivity(),AnkoLogger {
    //database
    private lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var sessionManager: SessionManager
    lateinit var progressdialog: ProgressDialog

    //token
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressdialog = ProgressDialog(this)
        sessionManager = SessionManager(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btn_login.setOnClickListener {
            //login username
            login_username()
        }

        btn_signup.setOnClickListener {
            startActivity<RegsiterActivity>()
        }


    }

    fun login_username() {
        progressdialog.setTitle(R.string.wait)
        progressdialog.show()
        var email = edt_email.text.toString().trim()
        var password = edt_password.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        gettoken()
                    } else {
                        progressdialog.dismiss()
                        toast("gagal login")
                    }
                }


        } else {
            toast("Masukkan semua isi kolom")
        }
    }

    private fun gettoken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            if (token != null) {
                val req =
                    firestore.collection(Constant.akun).document(auth.currentUser!!.uid)
                        .update("token_id", token.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                sessionManager.setLogin(true)
                                startActivity(intentFor<HomeActivity>().clearTask().newTask())
                                progressdialog.dismiss()
                                finish()
                            }
                        }
            }else{
                return@OnCompleteListener
            }

        })
    }

    override fun onStart() {
        super.onStart()
        sessionManager = SessionManager(this)
        if (sessionManager.getLogin()!!) {
            startActivity(intentFor<HomeActivity>().clearTask().newTask())
            finish()
        }
    }
}