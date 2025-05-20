package com.krissphi.id.dicoding_events_mid_submission_app.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.krissphi.id.dicoding_events_mid_submission_app.R
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.ActivityDetailEventBinding
import androidx.core.net.toUri
import com.krissphi.id.dicoding_events_mid_submission_app.util.formatTime
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.krissphi.id.dicoding_events_mid_submission_app.data.local.FavoriteEventEntity
import com.krissphi.id.dicoding_events_mid_submission_app.data.repository.ViewModelFactory
import com.krissphi.id.dicoding_events_mid_submission_app.ui.favorites.FavoritesViewModel
import kotlinx.coroutines.launch

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var favoriteViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Event"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event_data", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("event_data")
        }

        fun ListEventsItem.toFavoriteEntity(): FavoriteEventEntity {
            return FavoriteEventEntity(
                id = this.id ?: 0,
                name = this.name ?: "",
                beginTime = this.beginTime ?: "",
                endTime = this.endTime ?: "",
                category = this.category ?: "",
                ownerName = this.ownerName ?: "",
                mediaCover = this.mediaCover ?: "",
                cityName = this.cityName ?: "",
                quota = this.quota ?: 0,
                registrants = this.registrants ?: 0,
                description = this.description ?: "",
                link = this.link ?: "",
                summary = this.summary ?: "",
                imageLogo = this.imageLogo ?: ""
            )
        }

        event?.let {
            Glide.with(this)
                .load(it.mediaCover)
                .into(binding.ivCover)
            binding.tvEventName.text = it.name
            binding.tvEventOwner.text = it.ownerName
            binding.tvEventCity.text = it.cityName
            binding.tvEventTime.text = formatTime(it.beginTime ?: "")
            binding.tvEventQuota.text = (it.quota?.minus(it.registrants!!)).toString()

            binding.tvDescription.text = HtmlCompat.fromHtml(
                it.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            val link = it.link
            binding.btnReg.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, link?.toUri())
                startActivity(intent)
            }

            val entity = it.toFavoriteEntity()

            favoriteViewModel = ViewModelProvider(
                this,
                ViewModelFactory.getInstance(application)
            )[FavoritesViewModel::class.java]

            favoriteViewModel.isFavorite.observe(this) { isFavorited ->
                binding.btnFavorite.setImageResource(
                    if (isFavorited) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
                )
            }

            // Check the initial favorite status
            lifecycleScope.launch {
                favoriteViewModel.checkIfFavorite(entity.id)
            }

            binding.btnFavorite.setOnClickListener {
                val currentlyFavorited = favoriteViewModel.isFavorite.value ?: false
                lifecycleScope.launch {
                    favoriteViewModel.toggleFavorite(entity, currentlyFavorited)
                    favoriteViewModel.setFavoriteStatus(!currentlyFavorited)

                    val message = if (!currentlyFavorited) {
                        "Event ditambahkan ke favorit"
                    } else {
                        "Event dihapus dari favorit"
                    }
                    Toast.makeText(this@DetailEventActivity, message, Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}