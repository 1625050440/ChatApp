package com.example.enpit_p13.chatapp.image_slides

import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.enpit_p13.chatapp.R
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
class Chatall_Slider : AppCompatActivity() {

    var imageModelArrayList: ArrayList<ImageModel>? = null

    val myImageList: IntArray = intArrayOf(R.drawable.chatall1,R.drawable.chatall2,R.drawable.chatall3,R.drawable.chatall4,R.drawable.chatall5)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatall__slider)

        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

        init()
    }
    private fun populateList(): ArrayList<ImageModel> {

        val list = ArrayList<ImageModel>()

        for (i in 0..4) {
            val imageModel = ImageModel()
            imageModel.setImage_drawables(myImageList!![i])
            list.add(imageModel)
        }

        return list
    }

    private fun init() {

        mPager = findViewById(R.id.pager_chatall) as ViewPager
        mPager!!.adapter = SlidingImage_Adapter(this@Chatall_Slider, this.imageModelArrayList!!)

        val indicator = findViewById(R.id.indicator) as CirclePageIndicator

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = imageModelArrayList!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        /* val swipeTimer = Timer()
         swipeTimer.schedule(object : TimerTask() {
             override fun run() {
                 handler.post(Update)
             }
         }, 3000, 3000)*/

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }

    companion object {

        private var mPager: ViewPager? = null
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

}
