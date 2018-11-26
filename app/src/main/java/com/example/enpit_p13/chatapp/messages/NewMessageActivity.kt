package com.example.enpit_p13.chatapp.messages

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_from_ListView
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.room_chat.view.*
import org.jetbrains.anko.startActivity

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select Room"

        fetchUsers()
    }

    override fun onBackPressed() {
        startActivity<LatestMessagesActivity>()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }
    val check : ArrayList<Room_chat_messager> = ArrayList()

    private fun fetchUsers() {
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var count = p0.childrenCount
                for (data in p0.children) {
                    val datauser = data.getValue<Room_chat_messager>(Room_chat_messager::class.java)
                    val user = datauser?.let { it } ?: continue
                    Log.d("Main", count.toString())
                    if (user.check == false) {
                        count--
                    } else {
                        if (user.kadaimeiText.toString() != "") {
                            Log.d("Chat", "user from New ${user.uid.toString()}")

                            check.add(user)
                            Log.d("Data", "11 ${check.toString()} ")

                            check.sortByDescending { it -> it.timestamp }
                            Log.d("Data", "11 ${check.toString()} ")
                            Log.d("Data", "11 ${check.size} ")
                        }
                    }
                }
                Log.d("Data", "11 ${check.toString()} ")
                Log.d("Data", "11 ${count} ")

                Log.d("Data", "${check.toString()}")
                if (check.size.toLong() == count) {
                    for (data in check) {
                        Log.d("Data", "${data.kadaimeiText.toString()}")

                        adapter.add(UserItem(data))
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            if(item.user.uid.toString()==FirebaseAuth.getInstance().uid.toString())
            {
                val intent = Intent(view.context, Room_chat_Activity::class.java)
                Log.d("Chat", "user from New b ${item.user.uid.toString()}")
                startActivity(intent)
            }
            else{
            val intent = Intent(view.context, Room_chat_from_ListView::class.java)
            intent.putExtra(USER_KEY, item.user)
            Log.d("Chat", "user from New b ${item.user.uid.toString()}")
            startActivity(intent)
            }
        }

        recyclemessage_chat_list.adapter= adapter

    }

}



class UserItem(val user: Room_chat_messager):Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.kadaimei_textview.text = user.kadaimeiText.toString()
    }

    override fun getLayout(): Int {
        return R.layout.room_chat
    }
}

//this is super tedious
    //class CustomAdapter: RecyclerView.Adapter<ViewModel>{
    //override fun onBindViewHolder(holder: ViewModel, position: Int) {
    //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  //  }
//}
