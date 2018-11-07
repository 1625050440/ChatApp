package com.example.enpit_p13.chatapp.quetion

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.enpit_p13.chatapp.R
import kotlinx.android.synthetic.main.activity_question.*


class QuestiontempActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply{
            val editText1 = getString("TEXT1", "")
            val editText2 = getString("TEXT2", "")
            val editText3 = getString("TEXT3", "")

            text1.setText(editText1)
            text2.setText(editText2)
         //   text3.setText(editText3)
        }
        spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItem as? String
                        item?.let{
                            if (it.isNotEmpty()) spin.text = it
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

       // save.setOnClickListener{ onSaveTapped()}


    }

    private fun onSaveTapped(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString("Text1", texttemplate1.text.toString())
                .putString("Text2", text2.text.toString())
        //        .putString("Text3", text3.text.toString())
                .apply()

    }
   // private fun send(val text :String){
    //}
}
