package com.eslammongy.openfilemanger

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var imageUri:Uri? = null
    private lateinit var filePath:TextView
    private lateinit var imageView:ImageView
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

        if (uri != null){
            imageUri = uri
            val path = uri.path
            filePath.text = path
            imageView.setImageURI(uri)
        }else{
            Toast.makeText(applicationContext, "Selected Noun", Toast.LENGTH_LONG)
                .show()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filePath = findViewById(R.id.tv_FilePath)
        val pickFile = findViewById<Button>(R.id.btn_PickFile)
        val readFile = findViewById<Button>(R.id.btn_ReadFile)
        imageView = findViewById(R.id.imageView)
        val bitmapImage = findViewById<ImageView>(R.id.imageView2)

        readFile.setOnClickListener {

            if (imageUri != null){
                Glide.with(this).load(imageUri).into(bitmapImage)

            }else{
                Toast.makeText(applicationContext, "Selected Noun", Toast.LENGTH_LONG)
                    .show()
            }

        }

        pickFile.setOnClickListener {
           if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
               checkUserPermission(Manifest.permission.READ_EXTERNAL_STORAGE , "Gallery" , 10)
           }else{

               getContent.launch("image/*")
           }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "permission refused", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(applicationContext, " permission granted", Toast.LENGTH_LONG)
                .show()
            //               fileIntent = Intent(Intent.ACTION_GET_CONTENT)
//               fileIntent.type = "*/*"
            getContent.launch("image/*")
        }
    }

    private fun checkUserPermission(permission: String, name: String, requestCode: Int){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "$name Permission Granted.", Toast.LENGTH_LONG).show()

                    //               fileIntent = Intent(Intent.ACTION_GET_CONTENT)
//               fileIntent.type = "*/*"
                    getContent.launch("image/*")
                }
                this.shouldShowRequestPermissionRationale(permission) -> {
                    showRequestPermissionDialog(permission, name, requestCode)
                }
                else -> {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)

                }
            }
        }

    }

    private fun showRequestPermissionDialog(permissions: String, name: String, requestCode: Int) {

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("You need to access your $name permission is required to use this app")
            setTitle("Permission Required")
            setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permissions),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

}