package com.example.enpit_p13.chatapp.toppage

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.analyzesheet.OwnAnalysisActivity
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_top.*

import kotlinx.android.synthetic.main.activity_top_page.*
import kotlinx.android.synthetic.main.content_top_page.*
import org.jetbrains.anko.startActivity

class TopPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        setSupportActionBar(toolbar)

        verifyUserIsLoggedIn()
        
        goToChatPage.setOnClickListener {
            startActivity<LatestMessagesActivity>()
        }
        goToAnalyzePage.setOnClickListener {
            startActivity<OwnAnalysisActivity>()
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
