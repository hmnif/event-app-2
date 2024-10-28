package com.example.eventapps.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.eventapps.R
import com.example.eventapps.database.FavoriteEvent
import com.example.eventapps.databinding.ActivityDetailBinding
import com.example.eventapps.remote.response.DetailEventResponse
import com.example.eventapps.remote.Result
import com.example.eventapps.ui.ViewModelFactory
import com.example.eventapps.ui.settings.SettingsPreferences
import com.example.eventapps.ui.settings.SettingsViewModel
import com.example.eventapps.ui.settings.SettingsViewModelFactory
import com.example.eventapps.ui.settings.dataStore

class DetailActivity : AppCompatActivity() {

    private var _activityDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activityDetailBinding

    private var linkPendaftaran: String? = null

    private var detailFavoriteEvent: FavoriteEvent = FavoriteEvent()

    val factory: ViewModelFactory =ViewModelFactory.getInstance(this)
    val viewModel: DetailViewModel by viewModels {
        factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        var isEventExist = false
        viewModel.findDetailEvents(intent.getIntExtra("EVENT_ID", -1).toString()).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val eventData = result.data
                        setEventDetailsData(eventData)
                    }
                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            viewModel.getFavoriteEventById(intent.getIntExtra("EVENT_ID", -1).toString()).observe(this) { favEvent ->
                if (favEvent == null) {
                    binding?.btnFavorite?.setImageResource(R.drawable.ic_favorite_border)
                    isEventExist = false
                } else {
                    binding?.btnFavorite?.setImageResource(R.drawable.ic_favorite)
                    isEventExist = true
                }
            }
        }

        binding?.btnDaftarEvent?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(linkPendaftaran)
            }
            startActivity(intent)
        }

        setSupportActionBar(binding?.toolbar)

        // Enable the "up" button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding?.btnFavorite?.setOnClickListener {
            if (isEventExist) {
                viewModel.deleteFavoriteEventById(intent.getIntExtra("EVENT_ID", -1).toString())
                Toast.makeText(this, "Berhasil menghapus event dari favorite", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavoriteEvent(this.detailFavoriteEvent)
                Toast.makeText(this, "Berhasil menambahkan event ke favorite", Toast.LENGTH_SHORT).show()
            }
        }

        val pref = SettingsPreferences.getInstance(this.dataStore)
        val settingsViewModel = ViewModelProvider(this,
            SettingsViewModelFactory(pref)
        )[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                binding?.toolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorToolbarBackgroundNightMode))
                binding?.llButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLinearLayoutBackgroundNightMode))
                binding?.timeIcon?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                binding?.quotaIcon?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setEventDetailsData(e: DetailEventResponse) {
        binding?.let {
            Glide.with(this)
                .load(e.event?.mediaCover)
                .into(it.imgMediaCover)
        }
        binding?.tvEventName?.text = e.event?.name
        binding?.tvBeginTime?.text = e.event?.beginTime
        binding?.tvOwnerName?.text = e.event?.ownerName
        binding?.tvDeskripsiKegiatan?.text = HtmlCompat.fromHtml(e.event?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding?.tvQuota?.text  = "Quota Tersedia : ${(e.event?.quota ?: -1) - (e.event?.registrants ?: 0)}"
        this.linkPendaftaran = e.event?.link
        supportActionBar?.title = e.event?.name
        this.detailFavoriteEvent = FavoriteEvent(
            id = e.event?.id.toString(),
            name = e.event?.name ?: "-",
            mediaCover =  e.event?.mediaCover
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailBinding = null
    }
}