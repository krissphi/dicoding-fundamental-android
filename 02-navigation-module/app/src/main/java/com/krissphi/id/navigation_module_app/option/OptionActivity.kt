package com.krissphi.id.navigation_module_app.option

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.krissphi.id.navigation_module_app.R
import com.krissphi.id.navigation_module_app.databinding.ActivityOptionBinding
import com.krissphi.id.navigation_module_app.option.searchbar.MenuActivity

class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MenuFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.menu2 -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


}