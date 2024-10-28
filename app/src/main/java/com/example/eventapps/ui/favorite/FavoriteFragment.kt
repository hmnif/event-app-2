package com.example.eventapps.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapps.adapter.VerticalEventAdapter
import com.example.eventapps.databinding.FragmentFavoriteBinding
import com.example.eventapps.remote.response.ListEventsItem
import com.example.eventapps.ui.NetworkUtils
import com.example.eventapps.ui.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // no internet exception handling
        if (!NetworkUtils.isInternetAvailable(requireActivity())) {
            showNoInternetDialog()
        }
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavoriteViewModel by viewModels {
            factory
        }

        val verticalEventAdapter = VerticalEventAdapter()
        viewModel.getAllFavoriteEvent().observe(viewLifecycleOwner) { result ->
            Log.d("FavFragment", "${result.size}")
            if (result.isEmpty()) {
                binding.tvNoEventFound.visibility = View.VISIBLE
            } else {
                binding.tvNoEventFound.visibility = View.INVISIBLE
            }
            val items = arrayListOf<ListEventsItem>()
            result.map {
                val eventId = it.id.toInt()
                val item = ListEventsItem(id = eventId, name = it.name, mediaCover = it.mediaCover)
                items.add(item)
            }
            verticalEventAdapter.submitList(items)
        }

        binding.rvEvents.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = verticalEventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showNoInternetDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No internet")
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