package com.example.enpit_p13.chatapp.quetion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import com.example.enpit_p13.chatapp.R
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.activity_qustiontemp2.*


class Qustiontemp2Activity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qustiontemp2)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


        t1_radio1.setOnClickListener(){
            t1_text.text="子供と会話できています。"
        }
        t1_radio2.setOnClickListener(){
            t1_text.text="子供と会話ができていません。"
        }
        t1_radio3.setOnClickListener(){
            t1_text.text = ""
        }

        t2_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItemPosition as? Int
                        item?.let{
                            when (it) {
                                0 -> t2_text.text = ""
                                1 -> t2_text.text = "子供は、外出します。"
                                2 -> t2_text.text = "子供は、家から出ようとしません。"
                                3 -> t2_text.text = "子供は、部屋に引きこもっています。"
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

        t3_radio1.setOnClickListener(){
            t3_text.text="子供からの暴力を振るわれています。"
        }
        t3_radio2.setOnClickListener(){
            t3_text.text="子供からの暴力は振るわれていません。"
        }
        t3_radio3.setOnClickListener(){
            t3_text.text = ""
        }

        t4_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItemPosition as? Int
                        item?.let{
                            when (it) {
                                0 -> t4_text.text = ""
                                1 -> t4_text.text = "支援機関への相談をしたことがあります。"
                                2 -> t4_text.text = "親戚や友達に相談をしたことがあります。"
                                3 -> t4_text.text = "誰も身近に相談する相手がいません。"
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

        var  temptext = ""

        temp_create_button.setOnClickListener(){
            temptext = "私と子供との関係は、以下のような状況です。\n"
            if(t1_text.text != ""){
                temptext = temptext + "・" + t1_text.text + "\n"

            }
            if (t2_text.text != ""){
                temptext = temptext + "・" + t2_text.text + "\n"
            }
            if (t3_text.text != ""){
                temptext = temptext + "・" + t3_text.text + "\n"
            }
            if (t4_text.text != ""){
                temptext = temptext + "現在の状況について" + t4_text.text + "\n"
            }
            temptext += "そこで質問なのですが、どうやって子供と打ち解け現在の状況を脱する事ができますか？\nみなさんの意見をお聞きしたいです。"
            editTemplate.setText(temptext)


        }

        send_button.setOnClickListener(){
            temptext = editTemplate.text.toString()
            Log.d("sendtext",temptext)
        }
    }
}
