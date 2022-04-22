package com.sitamadex11.librarymanagementsystem.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.sitamadex11.librarymanagementsystem.R
import com.sitamadex11.librarymanagementsystem.databinding.ActivityUserAppBinding

class UserAppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityUserAppBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_app)
        binding.imgHamburger.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemHome -> {
                navigate(R.id.homeFragment)
                Toast.makeText(this@UserAppActivity,"${item.title} clicked",Toast.LENGTH_SHORT).show()
            }
            R.id.itemMyBooks -> {
                Toast.makeText(this@UserAppActivity,"${item.title} clicked",Toast.LENGTH_SHORT).show()
            }
            R.id.itemFavourites -> {
                Toast.makeText(this@UserAppActivity,"${item.title} clicked",Toast.LENGTH_SHORT).show()
            }
            R.id.itemLogout -> {
                Toast.makeText(this@UserAppActivity,"${item.title} clicked",Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    /**
     * For handling navigation
     */
    private fun navigate(navFragId: Int) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val id = navController.currentDestination?.id
        id?.let { navController.popBackStack(it, true) }
        navController.navigate(navFragId)
    }
}