package com.example.enpit_p13.chatapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button.setOnClickListener{
            performRegister()

        }
        already_textview.setOnClickListener{

            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
        selectphoto_button.setOnClickListener {
            Log.d("Main","try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

    }
    var selectedPhotoUri: Uri? =null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Main","photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            selectphoto_button.setBackgroundDrawable(bitmapDrawable)
        }
    }
private fun performRegister(){
    val email = email_edittext.text.toString()
    val password = password_edittext.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(this, "please enter text in email or password", Toast.LENGTH_SHORT).show()
        return
    }

    Log.d("RegisterActivity", "email is " + email)
    Log.d("RegisterActivity", "password: $password")

    //Firebase Authentication to create a user with email and password
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if cuccsessful
                Log.d("Main", "Successfully created user with uid: ${it.result.user.uid}")
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user:", Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Main","successfully uploaded image: ${it.metadata?.path}")
                }
    }
}

