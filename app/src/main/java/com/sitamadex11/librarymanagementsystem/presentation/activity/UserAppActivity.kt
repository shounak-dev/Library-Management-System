package com.sitamadex11.librarymanagementsystem.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.ActivityUserAppBinding

class UserAppActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_app)
        binding.imgHamburger.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
    }
}