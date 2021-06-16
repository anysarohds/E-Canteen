package com.ani.e_canteen.admin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.RadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ani.e_canteen.R
import com.ani.e_canteen.model.MakananModels
import com.ani.e_canteen.utils.Constant
import com.ani.e_canteen.utils.CustomProgressDialog
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_food.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

@Suppress("UNREACHABLE_CODE")
class AddFoodActivity : AppCompatActivity() {
    var nama: String? = null
    var harga: String? = null
    lateinit var radiokategori: RadioButton

    //FOTO
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    private var progressdialog = CustomProgressDialog()
    private var myUrl = ""

    //firebase
    private var storageReference: StorageReference? = null
    lateinit var auth: FirebaseAuth
    var UserId: String? = null
    lateinit var firestore: FirebaseFirestore

    lateinit var mFirebaseStorage: FirebaseStorage

    companion object {
        var kategori_insert: String? = null
        var filePath: Uri? = null
        var data: ByteArray? = null


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        inisialisasifirebase()


        btn_foto.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
            filePath =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            //camera intent
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)

        }

        btn_galery.setOnClickListener {
            pilihfile()
        }

        btnUpload.setOnClickListener {

            val nama = edt_nama.text.toString().trim()
            val harga = edt_harga.text.toString().trim()
            val keterangan = edt_keterangan.text.toString().trim()

            if (nama.isNotEmpty() && harga.isNotEmpty() && keterangan.isNotEmpty() && data != null
                && kategori_insert != null
            ) {
                progressdialog.show(this@AddFoodActivity, Constant.wait)
                val key =
                    FirebaseFirestore.getInstance().collection(Constant.food).document().id
                val fileref =
                    storageReference!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileref.putBytes(data!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw  it
                            val parentLayout = findViewById<View>(android.R.id.content)
                            val snackbar: Snackbar = Snackbar.make(
                                parentLayout,
                                it.message.toString(),
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.show()
                        }
                    }
                    return@Continuation fileref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result

                        myUrl = downloadUrl.toString()
                        val fb = FirebaseFirestore.getInstance().collection(Constant.akun_kantin)
                            .document(UserId.toString()).get().addOnSuccessListener { data ->
                                if (data.exists()) {
                                    val data_objek = data.toObject(MakananModels::class.java)
                                    val usermap: MutableMap<String, Any?> = HashMap()

                                    usermap["foto"] = myUrl
                                    usermap["harga"] = harga.toInt()
                                    usermap["keterangan"] = keterangan
                                    usermap["kategori"] = kategori_insert.toString()
                                    usermap["nama"] = nama
                                    usermap["rating"] = 0
                                    usermap["terjual"] = 0
                                    usermap["id_makanan"] = key
                                    usermap["uid_kantin"] = UserId.toString()
                                    usermap["nama_kantin"] = data_objek!!.nama_kantin.toString()


                                    val docref = firestore.collection(Constant.food).document(key)
                                        .set(usermap)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                progressdialog.dialog.dismiss()
                                                toast("Input Success")
                                                finish()
                                            } else {
                                                progressdialog.dialog.dismiss()
                                                toast("Retry")
                                            }
                                        }

                                }
                            }


                    }

                }


            } else {
                val parentLayout = findViewById<View>(android.R.id.content)
                val snackbar: Snackbar = Snackbar.make(
                    parentLayout,
                    Constant.field,
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
    }

    private fun pilihfile() {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    dialog_permis(
                        "External storage", Manifest.permission.READ_EXTERNAL_STORAGE

                    )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_PICK_IMAGE
                        )
                }

            }
        }
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_PICK_IMAGE)

    }


    fun dialog_permis(
        msg: String,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (this as Activity?)!!, arrayOf(permission),
                    REQUEST_PICK_IMAGE
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Picasso.get().load(filePath).fit().centerCrop().into(gambar_makanan)
                convert()
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                filePath = data?.data
                Picasso.get().load(filePath).fit().centerCrop().into(gambar_makanan)
                convert()
            }
        }

    }

    fun convert() {
        val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        data = baos.toByteArray()

    }

    fun inisialisasifirebase() {
        auth = FirebaseAuth.getInstance()
        UserId = auth.currentUser!!.uid
        storageReference =
            FirebaseStorage.getInstance().reference.child("Warung").child(UserId.toString())
                .child("resep")
        firestore = FirebaseFirestore.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_food ->
                    if (checked) {
                        kategori_insert = getString(R.string.food)
                        // Pirates are the best
                    }
                R.id.radio_drink ->
                    if (checked) {
                        kategori_insert = getString(R.string.drink)
                    }

                R.id.radio_snack ->
                    if (checked) {
                        kategori_insert = getString(R.string.snack)
                    }

                R.id.radio_dessert ->
                    if (checked) {
                        kategori_insert = getString(R.string.dessert)
                    }


            }
        }

    }


}