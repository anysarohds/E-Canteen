package com.ani.e_canteen.auth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.ani.e_canteen.HomeActivity
import com.ani.e_canteen.R
import com.ani.e_canteen.utils.Constant
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_regsiter.*
import org.jetbrains.anko.*

class RegsiterActivity : AppCompatActivity() {
    //database
    private lateinit var auth: FirebaseAuth
    private var firestore: FirebaseFirestore? = null

    //loading
    lateinit var progressDialog: ProgressDialog

    //token
    var token: String? = null

    //save data ke sharedpreference
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regsiter)
        sessionManager = SessionManager(this)
        progressDialog = ProgressDialog(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btn_register.setOnClickListener {
            daftar()
        }

        btn_signin.setOnClickListener {
            finish()
        }
    }

    private fun daftar() {
        progressDialog.setTitle("Tunggu...")
        progressDialog.show()
        val fullname = edt_fullname.text.toString().trim()
        val email = edt_email.text.toString().trim()
        val password = edt_password.text.toString().trim()
        val confirm_password = edt_cfmpassword.text.toString().trim()


        if (email.isNotEmpty() && fullname.isNotEmpty() && password.isNotEmpty()
            && fullname.isNotEmpty() && confirm_password.isNotEmpty()
        ) {
            if (password == confirm_password) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user_id = auth.currentUser!!.uid
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        return@OnCompleteListener
                                    }
                                    // Get new FCM registration token
                                    token = task.result
                                    if (token != null) {
                                        val userMap: MutableMap<String, Any?> =
                                            HashMap()
                                        userMap["nama"] = fullname
                                        userMap["email"] = email
                                        userMap["token_id"] = token
                                        userMap["password"] = password
                                        userMap["uid"] = user_id

                                        val req =
                                            firestore!!.collection(Constant.akun)
                                                .document(auth.currentUser!!.uid)
                                                .set(userMap).addOnCompleteListener {
                                                    if (it.isSuccessful){
                                                        sessionManager.setLogin(true)
                                                        startActivity(intentFor<HomeActivity>().clearTask().newTask())
                                                        finish()
                                                    }
                                                }
                                    }

                                })


                        } else {
                            progressDialog.dismiss()
                            toast("Username sudah terdaftar")
                        }
                    }
            }else{
                toast(R.string.wrong_password)
                progressDialog.dismiss()
            }

        } else {
            toast("isi semua from")
            progressDialog.dismiss()
        }


    }
}