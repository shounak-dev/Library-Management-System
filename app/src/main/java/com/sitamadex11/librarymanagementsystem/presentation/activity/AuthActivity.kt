package com.sitamadex11.librarymanagementsystem.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding:ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.btnAsAdmin.setOnClickListener {
            val intent = Intent(this,AdminAppActivity::class.java)
            startActivity(intent)
        }
        binding.btnAsStudent.setOnClickListener {
            val intent = Intent(this,UserAppActivity::class.java)
            startActivity(intent)
        }
    }
}