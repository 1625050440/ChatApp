package com.example.enpit_p13.chatapp.registerlogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.toppage.TopPageActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            val email = email_login_edittext.text.toString()
            val password = password_login_edittext.text.toString()

            if (!email.isEmpty() || !password.isEmpty()) {


                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (!it.isSuccessful) return@addOnCompleteListener

                            //else if successful
                            Log.d("Main", "Successfully login user with uid: ${it.result?.user?.uid}")
                            intent = Intent(this, TopPageActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Log.d("Main", "Failed to login with user: ${it.message}")
                            Toast.makeText(this, "Failed to login with this user:", Toast.LENGTH_SHORT).show()
                        }
            } else {
                Toast.makeText(this, "メールアドレスやパスワードはまだ未入力", Toast.LENGTH_SHORT).show()
            }
        }


        back_to_register.setOnClickListener {
            finish()
        }

    }
}