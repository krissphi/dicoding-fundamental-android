package com.krissphi.id.bgnetwork_module_app.networking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krissphi.id.bgnetwork_module_app.R
import com.krissphi.id.bgnetwork_module_app.databinding.ActivityListQuotesBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ListQuotesActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListQuotesBinding

    companion object {
        private val TAG = ListQuotesActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        binding.listQuotes.setLayoutManager(layoutManager)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listQuotes.addItemDecoration(itemDecoration)
        getListQuotes()
    }

    private fun getListQuotes() {
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://quote-api.dicoding.dev/list"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE

                val listQuote = ArrayList<String>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val quote = jsonObject.getString("en")
                        val author = jsonObject.getString("author")
                        listQuote.add("\n$quote\n — $author\n")
                    }
                    val adapter = QuoteAdapter(listQuote)
                    binding.listQuotes.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@ListQuotesActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@ListQuotesActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}