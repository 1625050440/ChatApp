package com.example.enpit_p13.chatapp.toppage

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.analyzesheet.OwnAnalysisActivity
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.messages.NewMessageActivity
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_top.*

import org.jetbrains.anko.startActivity

class TopPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        setSupportActionBar(toolbar)

        //ログイン認証
        verifyUserIsLoggedIn()

        //登録したユーザー名を表示
        DisplayUsername()
        
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

    private fun DisplayUsername(){
        Log.d("exeDisp","実行")
        val ref = FirebaseDatabase.getInstance().reference.child("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {

                for(data in p0.children)
                {
                    val userdata = data.getValue<User>(User::class.java)
                    val users = userdata?.let { it } ?:continue

                    Log.d("AuthUid","${FirebaseAuth.getInstance().uid.toString()}\n")
                    Log.d("getUid",users.uid.toString())

                    if(users.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                        dis_puserName.text = "現在\"" + users.username + "\"さんでログイン中"
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

    }

}
