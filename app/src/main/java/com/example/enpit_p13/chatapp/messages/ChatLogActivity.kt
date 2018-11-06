package com.example.enpit_p13.chatapp.messages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.database.FirebaseDatabase
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
class ChatfromItem(val text: String,val user: String) : Item<ViewHolder>(){
  override fun bind(viewHolder: ViewHolder,position: Int){
    viewHolder.itemView.textView.textView.text = text
      viewHolder.itemView.username_textview_from_row.text = user
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
