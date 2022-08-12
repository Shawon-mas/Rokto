package com.app.roktoDorkar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.roktoDorkar.R
import com.app.roktoDorkar.databinding.ActivityTestBinding
import com.app.roktoDorkar.view.RequestFrag.MyRequestFrag
import com.app.roktoDorkar.view.RequestFrag.ReceivedFrag
import com.google.android.material.bottomnavigation.BottomNavigationView

class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTestBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        bottomImplement()
    }
    internal lateinit var one:ReceivedFrag
    internal lateinit var two:MyRequestFrag

    fun bottomImplement()
    {


    }
}