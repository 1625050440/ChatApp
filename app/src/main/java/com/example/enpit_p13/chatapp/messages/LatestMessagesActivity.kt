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
import android.view.WindowManager
import android.widget.Toast
import com.example.enpit_p13.chatapp.Activity_chat
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.quetion.QuestiontempActivity
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
        //verifyUserIsLoggedIn()
        val reference =FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
        reference.setValue(Check_online("Top_Page"))
        fetchUsers()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        room_create_button.setOnClickListener {
            if(room_create_button.text.toString() == "ルーム作成") {
                if (title_edditext.text.toString() != "" && explain_EditText.text.toString() != "") {
                    val referrence = FirebaseDatabase.getInstance().getReference("/Room_Chat")
                    referrence.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            p0.children.forEach() {
                                val user = it.getValue(Room_chat_messager::class.java)
                                if (it.key.toString() == title_edditext.text.toString() ) {
                                    error_textview.text = "${title_edditext.text.toString()}が存在しました、変更してください"
                                    title_edditext.text.clear()
                                }

                            }
                            if(!title_edditext.text.isEmpty()) {
                                sendData(title_edditext.text.toString(), explain_EditText.text.toString(),true)
                                Toast.makeText(this@LatestMessagesActivity, "チャットルームは作成されました。", Toast.LENGTH_SHORT).show()
                                delete_button.visibility = View.VISIBLE
                                delete_button.isClickable = true
                                room_create_button.text = "チャットルームへ"
                                title_edditext.isEnabled = false
                                title_edditext.isFocusable = false
                                explain_EditText.isEnabled = false
                                explain_EditText.isFocusable = false
                                error_textview.text = " "
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                } else {
                    error_textview.text = "課題名や説明まだ未入力"
                }
            }
            else{
                    val intent = Intent(this, Room_chat_Activity::class.java)
                    startActivity(intent)
            }
        }
        val referrence = FirebaseDatabase.getInstance().getReference("/Room_Chat")
        referrence.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach(){
                    val user = it.getValue(Room_chat_messager::class.java)
                    if((!user?.messageText.toString().isEmpty()) && (user?.uid.toString()==FirebaseAuth.getInstance().uid.toString())){
                            explain_EditText.setText(user?.messageText.toString())
                            create_templates_button.visibility = View.INVISIBLE
                            create_templates_button.isClickable = false

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        create_templates_button.setOnClickListener {
            sendData(title_edditext.text.toString(),explain_EditText.text.toString(),false)
            intent = Intent(this, QuestiontempActivity::class.java)
            //intent.putExtra(USER_KEY,title_edditext.text.toString())
            startActivity(intent)
        }

        delete_button.setOnClickListener {
            val dialog = DeleteConfirmDialog()
            dialog.show(supportFragmentManager,"alert_dialog")
           // startActivity<Room_chat_from_ListView>()


        }


    }
    private fun sendData(title:String,message:String,check:Boolean) {
        val uid = FirebaseAuth.getInstance().uid.toString()
            val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat/$uid/")
            reference.setValue(Room_chat_messager(message, title, uid,check))
    }
    companion object {
        val USER_KE = "USER_KE"
        val USER_KEY ="USER_KEY"
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @TargetApi(Build.VERSION_CODES.O)
            @RequiresApi(Build.VERSION_CODES.O)
            //val adapter = GroupAdapter<ViewHolder>()
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach() {
                    val user = it.getValue(Room_chat_messager::class.java)
                    Log.d("Chat_Room",user?.kadaimeiText.toString())
                    if (user?.uid.toString()==FirebaseAuth.getInstance().uid.toString()) {
                        if (user?.check!=false ) {
                            title_edditext.setText(user?.kadaimeiText.toString())
                            explain_EditText.setText(user?.messageText.toString())
                            delete_button.visibility = View.VISIBLE
                            delete_button.isClickable = true
                            room_create_button.text = "チャットルームへ"
                            title_edditext.isEnabled = false
                            title_edditext.isFocusable = false
                            explain_EditText.isEnabled = false
                            explain_EditText.isFocusable = false
                        }
                        else{
                            title_edditext.setText(user?.kadaimeiText.toString())
                            explain_EditText.setText(user?.messageText.toString())
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
