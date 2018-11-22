package com.example.enpit_p13.chatapp.analyzesheet


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_own_analysis.*
import kotlinx.android.synthetic.main.content_own_analysis.*
import org.jetbrains.anko.startActivity

class OwnAnalysisActivity : AppCompatActivity() {

    var mDatabase = FirebaseDatabase.getInstance().getReference("/AnalyzeSheet/${FirebaseAuth.getInstance().uid}")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.example.enpit_p13.chatapp.R.layout.activity_own_analysis)
        setSupportActionBar(toolbar)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setToplabel()

        Q2_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}

                }

        save_button.setOnClickListener{


            Toast.makeText(this,"保存しました。",Toast.LENGTH_LONG).show()


            writeNewQetion()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.analyze_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.look_back ->{
                val ref = FirebaseDatabase.getInstance().reference.child("/AnalyzeSheet")

                ref.addListenerForSingleValueEvent(object :ValueEventListener
                {
                    override fun onDataChange(p0: DataSnapshot)
                    {
                        Log.d("Main","実行")
                        if(p0.hasChild(FirebaseAuth.getInstance().uid.toString())) {
                            startActivity<SelectOwnAnalyzeActivity>()
                        }else{
                            Toast.makeText(this@OwnAnalysisActivity,"保存されているデータがありません",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {}
                })
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun writeNewQetion()
    {
        val q1:String = Q1_edit_num.text.toString()
        val q2_1:String = Q2_edit_num.text.toString()
        val q2_2:Int = Q2_spinner.selectedItemPosition
        val q3:String = Q3_edit_num.text.toString()
        val q5:Int = when {
            Q5_noButtom.isChecked -> 0
            Q5_yesButtom.isChecked -> 1
            else -> 3
        }
        val q6_1:Int = when {
            Q6_noButtom.isChecked -> 0
            Q6_yesButtom.isChecked -> 1
            else -> 3
        }
        val q6_2:String =Q6_edit_detail.text.toString()
        val q7:String = Q7_edit_text.text.toString()
        val q8_1:String = Q8Edit_num.text.toString()
        val q8_2:Int = Q2_spinner.selectedItemPosition
        val q8_3:String  = Q8Edit_detail.text.toString()
        val q9:String = Q9_edit_detail.text.toString()

        //データベースに保存
        var quetion = Quetion(q1,q2_1,q2_2,q3,q5,q6_1,q6_2,q7,q8_1,q8_2,q8_3,q9)
        mDatabase.child(quetion.timestamp.toString()).setValue(quetion)

    }

    private fun fetchuser()
    {
        val ref = FirebaseDatabase.getInstance().reference.child("/AnalyzeSheet")

        ref.addValueEventListener(object :ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.key == FirebaseAuth.getInstance().uid) {
                    Log.d("exe", p0.key)
                    Log.d("p0chkey", p0.child("1542701485246").key)

                }
            }

        })

        ref.addListenerForSingleValueEvent(object :ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {
                Log.d("Main","実行")
                for(data in p0.children)
                {
                    val userdata = data.getValue<Quetion>(Quetion::class.java)
                    val read_quetion = userdata?.let { it } ?:continue
                    if(read_quetion.userId.toString() == FirebaseAuth.getInstance().uid.toString()) {
                       ReadQuetion(read_quetion)
                    }

                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun setToplabel(){
        val ref = FirebaseDatabase.getInstance().reference.child("/users")
        ref.addListenerForSingleValueEvent(object :ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot)
            {

                for(data in p0.children)
                {
                    val userdata = data.getValue<User>(User::class.java)
                    val users = userdata?.let { it } ?:continue
                    if(users.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                        Label.text = users.username + " さんの分析シート"
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

    }

    fun ReadQuetion(quetion: Quetion){
        Q1_edit_num.setText(quetion.q1)
        Q2_edit_num.setText(quetion.q2_1)
        Q2_spinner.setSelection(quetion.q2_2)
        Q3_edit_num.setText(quetion.q3)

        when {
            quetion.q5 == 0 -> {
                Q5_yesButtom.isChecked = false
                Q5_noButtom.isChecked = true
            }
            quetion.q5 == 1 -> {
                Q5_yesButtom.isChecked = true
                Q5_noButtom.isChecked = false
            }
            else -> {
                Q5_yesButtom.isChecked = false
                Q5_noButtom.isChecked = false
            }
        }

        when {
            quetion.q6_1 == 0 -> {
                Q6_yesButtom.isChecked = false
                Q6_noButtom.isChecked = true
            }
            quetion.q6_1 == 1 -> {
                Q6_yesButtom.isChecked = true
                Q6_noButtom.isChecked = false
            }
            else -> {
                Q6_yesButtom.isChecked = false
                Q6_noButtom.isChecked = false
            }
        }

        Q6_edit_detail.setText(quetion.q6_2)
        Q7_edit_text.setText(quetion.q7)
        Q8Edit_num.setText(quetion.q8_1)
        Q8_spinner.setSelection(quetion.q8_2)
        Q8Edit_detail.setText(quetion.q8_3)
        Q9_edit_detail.setText(quetion.q9)
    }
}