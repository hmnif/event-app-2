package com.example.eventapps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapps.adapter.HorizontalEventAdapter
import com.example.eventapps.adapter.VerticalEventAdapter
import com.example.eventapps.databinding.FragmentHomeBinding
import com.example.eventapps.remote.response.ListEventsItem
import com.example.eventapps.remote.Result
import com.example.eventapps.ui.NetworkUtils
import com.example.eventapps.ui.ViewModelFactory
import com.example.eventapps.ui.settings.SettingsPreferences
import com.example.eventapps.ui.settings.SettingsViewModel
import com.example.eventapps.ui.settings.SettingsViewModelFactory
import com.example.eventapps.ui.settings.dataStore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // no internet exception handling
        if (!NetworkUtils.isInternetAvailable(requireActivity())) {
            showNoInternetDialog()
        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingsPreferences.getInstance(requireActivity().dataStore)
        val settingsViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }

        val horizontalEventAdapter = HorizontalEventAdapter()
        viewModel.findFiveUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val items = arrayListOf<ListEventsItem>()
                        result.data.map {
                            val item = ListEventsItem(id = it.id, name = it.name, imageLogo = it.imageLogo)
                            items.add(item)
                        }
                        horizontalEventAdapter.submitList(items)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvEventsHorizontal.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalEventAdapter
        }

        val verticalAdapter = VerticalEventAdapter()
        viewModel.findFiveFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val items = arrayListOf<ListEventsItem>()
                        result.data.map {
                            val item = ListEventsItem(id = it.id, name = it.name, mediaCover = it.mediaCover)
                            items.add(item)
                        }
                        verticalAdapter.submitList(items)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvEventsVertical.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = verticalAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }
        viewModel.findFiveFinishedEvents()
        viewModel.findFiveUpcomingEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No Internet")
            .setMessage("You are not connected to the internet. Please check your connection and try again.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                // Handle aksi jika user memilih cancel, misalnya menutup fragment
            }
            .create()
            .show()
    }
}