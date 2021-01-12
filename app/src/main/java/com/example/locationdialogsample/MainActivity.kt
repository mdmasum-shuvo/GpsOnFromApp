package com.example.locationdialogsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.masum.gpson.GpsOnUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click(view: View) {
        GpsOnUtils.openLocationDialog(this)

    }
}