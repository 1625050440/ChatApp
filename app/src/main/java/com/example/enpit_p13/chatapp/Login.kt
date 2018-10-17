package com.example.enpit_p13.chatapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button.setOnClickListener{
            val email = email_login_edittext.text.toString()
            val password = password_login_edittext.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener

                        //else if cuccsessful
                        Log.d("Main", "Successfully login user with uid: ${it.result.user.uid}")
                    }
                    .addOnFailureListener {
                        Log.d("Main", "Failed to create user: ${it.message}")
                        Toast.makeText(this, "Failed to create user:", Toast.LENGTH_SHORT).show()
                    }
        }


        back_to_register.setOnClickListener {
            finish()
        }

    }
}