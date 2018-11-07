package com.example.enpit_p13.chatapp.messages

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.enpit_p13.chatapp.Activity_chat
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.registerlogin.RegisterActivity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_latest_messages.*
class LatestMessagesActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        verifyUserIsLoggedIn()
        fetchUsers()
        room_create_button.setOnClickListener {
            if (title_edditext.text.toString() != "" && explain_EditText.text.toString() != "") {
                error_textview.text = ""
                sendData()
                Toast.makeText(this@LatestMessagesActivity, "チャットルームは作成されました。", Toast.LENGTH_SHORT).show()
                fetchUsers()
            } else {
                error_textview.text = "課題名や説明まだ未入力"
            }

        }
        delete_button.setOnClickListener {
         /*   room_create_button.text = "ルーム作成"
            title_edditext.setText("")
            explain_EditText.setText("")
            delete_button.visibility = View.INVISIBLE
            delete_button.isClickable = false */
            FirebaseDatabase.getInstance().getReference("/Room_Chat/${FirebaseAuth.getInstance().uid.toString()}").removeValue()
            intent = Intent(this,LatestMessagesActivity::class.java)
            startActivity(intent)
        }


    }
    private fun sendData() {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat/$uid").push()
        reference.setValue(Room_chat_messager(explain_EditText.text.toString(), title_edditext.text.toString(),uid))
    }
    companion object {
        val USER_KE = "USER_KE"
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat/${FirebaseAuth.getInstance().uid.toString()}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(Build.VERSION_CODES.O)
            //val adapter = GroupAdapter<ViewHolder>()
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach() {
                    val user = it.getValue(Room_chat_messager::class.java)
                    Log.d("Chat_Room",user?.kadaimeiText.toString())
                    if (!user?.kadaimeiText.toString().isEmpty()) {
                        var delete_check = user?.kadaimeiText.toString()
                        delete_button.visibility = View.VISIBLE
                        delete_button.isClickable = true
                        room_create_button.text = "チャットルームへ"
                        title_edditext.setText(user?.kadaimeiText.toString())
                        explain_EditText.setText(user?.messageText.toString())
                        title_edditext.isEnabled = false
                        title_edditext.isFocusable = false
                        explain_EditText.isEnabled = false
                        explain_EditText.isFocusable = false

                                        room_create_button.setOnClickListener {
                                            val intent = Intent(it.context, Room_chat_Activity::class.java)
                                            intent.putExtra(USER_KE, user)
                                            startActivity(intent)
                                        }

                                }

                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_chat ->{
                val intent = Intent(this, Activity_chat::class.java)
                startActivity(intent)
            }
            R.id.list_chat -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
