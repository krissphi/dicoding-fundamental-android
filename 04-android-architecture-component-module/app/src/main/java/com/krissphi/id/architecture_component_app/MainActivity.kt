package com.krissphi.id.architecture_component_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.krissphi.id.architecture_component_app.databinding.ActivityMainBinding
import com.krissphi.id.architecture_component_app.live_data.LDActivity
import com.krissphi.id.architecture_component_app.live_data_api.RetrofitActivity
import com.krissphi.id.architecture_component_app.view_model.VMActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnViewModel.setOnClickListener {
            val intent = Intent(this@MainActivity, VMActivity::class.java)
            startActivity(intent)
        }
        binding.btnLd.setOnClickListener {
            val intent = Intent(this@MainActivity, LDActivity::class.java)
            startActivity(intent)
        }
        binding.btnLdApi.setOnClickListener{
            val intent = Intent(this@MainActivity, RetrofitActivity::class.java)
            startActivity(intent)

        }



    }
}