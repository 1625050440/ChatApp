package com.example.enpit_p13.chatapp.messages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "Chatlog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user.username
        setupDummyData()
        send_button_chat_log.setOnClickListener{
            Log.d(TAG,"Attemp to send message...")
            performSendMessage()
        }
    }
    class  ChatMessage(val text: String)
    private fun performSendMessage(){
        //how do we actually send a message to firebase...
        val text = editText_chat_log.text.toString()

        val reference = FirebaseDatabase.getInstance().getReference("/chat_messager").push()

        val chatMessage = ChatMessage(text)
        reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d("Main", "saved chat messagers: ${reference.key}")
                }
    }
    private fun setupDummyData(){

            val adapter = GroupAdapter<ViewHolder>()

           // adapter.add(ChatfromItem("From MESSSSSAGE"))
            //adapter.add(ChatToItem("TO MESSAGE\nTOMESSAGE"))

            recycleview_chat_log.adapter= adapter
    }

}
class ChatfromItem(val text: String,val user: String,val check: Boolean,val uid:String) : Item<ViewHolder>(){
  override fun bind(viewHolder: ViewHolder,position: Int){
    viewHolder.itemView.textView.text = text
      viewHolder.itemView.username_textview_from_row.text = user
      var room_check = false
      if (check== true)
      {
          viewHolder.itemView.link_button.visibility = View.VISIBLE
          viewHolder.itemView.link_button.isClickable = true
          viewHolder.itemView.link_button.setOnClickListener{
              FirebaseDatabase.getInstance().getReference().child("/users/")
                      .addValueEventListener(object : ValueEventListener {
                          override fun onDataChange(p0: DataSnapshot) {
                              p0.children.forEach {
                                  val data = it?.getValue(User::class.java)
                                  if (data?.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                                      val reference = FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                                      reference.setValue(Check_online(uid, data?.username.toString(),true))
                                  }
                              }
                          }
                          override fun onCancelled(p0: DatabaseError) {

                          }
                      })
          }
      }
  }

    override fun getLayout(): Int {
    return R.layout.chat_from_row
    }
}
class ChatToItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder,position: Int){
     viewHolder.itemView.textView_chat_to_row.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
