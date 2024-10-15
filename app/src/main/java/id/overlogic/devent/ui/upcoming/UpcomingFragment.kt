package id.overlogic.devent.ui.upcoming

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.overlogic.devent.R
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentHomeBinding
import id.overlogic.devent.databinding.FragmentUpcomingBinding
import id.overlogic.devent.ui.home.HomeViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = UpcomingFragment()
    }

    private val viewModel: UpcomingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }

        val upcomingEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvUpcoming.layoutManager = upcomingEventLayoutManager

        val homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpcomingViewModel::class.java)
        homeViewModel.listUpcomingEvents.observe(viewLifecycleOwner) { events ->
            setUpcomingEvent(events)
        }

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            Toast.makeText(requireContext(), "Search for ${binding.searchView.text}", Toast.LENGTH_SHORT).show()
            homeViewModel.searchEvents(binding.searchView.text.toString())
            false
        }
    }

    private fun setUpcomingEvent(events: List<ListEventsItem>) {
        val adapter = UpcomingAdapter()
        adapter.submitList(events)
        binding.rvUpcoming.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}