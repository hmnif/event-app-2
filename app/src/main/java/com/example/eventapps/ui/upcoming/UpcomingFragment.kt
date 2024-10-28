package com.example.eventapps.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapps.adapter.VerticalEventAdapter
import com.example.eventapps.databinding.FragmentUpcomingBinding
import com.example.eventapps.remote.response.ListEventsItem
import com.example.eventapps.remote.Result
import com.example.eventapps.ui.NetworkUtils
import com.example.eventapps.ui.ViewModelFactory

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
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
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels {
            factory
        }

        val verticalEventAdapter = VerticalEventAdapter()
        viewModel.findUpcomingEvents().observe(viewLifecycleOwner) { result ->
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
                        verticalEventAdapter.submitList(items)
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

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = verticalEventAdapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val searchQuery = searchView.text.toString().trim()
                    if (searchQuery.isNotEmpty()) {
                        searchBar.setText(searchView.text)
                        viewModel.findUpcomingEvents(searchQuery).observe(viewLifecycleOwner) { result ->
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
                                        verticalEventAdapter.submitList(items)
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
                        searchBar.setText("")
                        searchView.hide()
                    }
                    true
                }
        }
    }

    override fun onResume() {
        super.onResume()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels {
            factory
        }
        viewModel.findUpcomingEvents()
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
            }
            .create()
            .show()
    }
}