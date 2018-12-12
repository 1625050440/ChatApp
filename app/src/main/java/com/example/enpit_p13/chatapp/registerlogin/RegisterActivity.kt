package com.example.enpit_p13.chatapp.registerlogin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.toppage.TopPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            performRegister()

        }
        already_textview.setOnClickListener {

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri? = null

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Main", "photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            select_imageview_register.setImageBitmap(bitmap)
            selectphoto_button.alpha = 0f

           // val bitmapDrawable = BitmapDrawable(bitmap)
           // selectphoto_button.setBackgroundDrawable(bitmapDrawable)
        }
    }*/

    private fun performRegister() {
        val email = email_edittext.text.toString()
        val password = password_edittext.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "アカウントの作成には、ユーザーネーム、メールアドレス、パスワードが必要です。", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("RegisterActivity", "email is " + email)
        Log.d("RegisterActivity", "password: $password")

        //Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if cuccsessful
                    Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                   // uploadImageToFirebaseStorage()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "ログインに失敗しました。", Toast.LENGTH_SHORT).show()
                }
    }

   /* private fun uploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        if (selectedPhotoUri == null)
            FirebaseStorage.getInstance().getReference("/images/$filename")
                .putFile(select_imageview_register.image)
        else
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Main", "successfully uploaded image: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Main", "File location : $it")

                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener{

                }
    }
*/
    private fun saveUserToDatabase(profileImageUri: String) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_edittext.text.toString(), profileImageUri)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Main","Finally we saved the user to Firebase Database")
                    val intent = Intent(this, TopPageActivity::class.java)
                    intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }

}

