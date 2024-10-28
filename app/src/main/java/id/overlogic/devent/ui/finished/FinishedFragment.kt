package id.overlogic.devent.ui.finished

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.overlogic.devent.data.Result
import id.overlogic.devent.data.remote.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FinishedViewModel by viewModels {
            factory
        }


        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            Toast.makeText(requireContext(), "Search for ${binding.searchView.text}", Toast.LENGTH_SHORT).show()
            viewModel.searchEvent(binding.searchView.text.toString()).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            var listItemEvents: List<ListEventsItem> = ArrayList()
                            result.data.forEach {
                                listItemEvents += ListEventsItem(
                                    it.summary,
                                    it.mediaCover,
                                    it.registrants,
                                    it.imageLogo,
                                    it.link,
                                    it.description,
                                    it.ownerName,
                                    it.cityName,
                                    it.quota,
                                    it.name,
                                    it.id,
                                    it.beginTime,
                                    it.endTime,
                                    it.category,
                                )
                            }
                            val upcomingEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            binding.rvFinished.layoutManager = upcomingEventLayoutManager
                            val adapter = FinishedAdapter()
                            adapter.submitList(listItemEvents)
                            binding.rvFinished.adapter = adapter
                            binding.pbLoading.visibility = View.GONE

                        }

                        is Result.Error -> {
                            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }

                    }
                }
            }
            false
        }
        viewModel.getFinishedEvent().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        var listItemEvents: List<ListEventsItem> = ArrayList()
                        result.data.forEach {
                            listItemEvents += ListEventsItem(
                                it.summary,
                                it.mediaCover,
                                it.registrants,
                                it.imageLogo,
                                it.link,
                                it.description,
                                it.ownerName,
                                it.cityName,
                                it.quota,
                                it.name,
                                it.id,
                                it.beginTime,
                                it.endTime,
                                it.category,
                            )
                        }
                        val upcomingEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvFinished.layoutManager = upcomingEventLayoutManager
                        val adapter = FinishedAdapter()
                        adapter.submitList(listItemEvents)
                        binding.rvFinished.adapter = adapter
                        binding.pbLoading.visibility = View.GONE

                    }

                    is Result.Error -> {
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }

                }
            }
        }
//
//        viewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
//            if (isLoading) {
//                binding.pbLoading.visibility = View.VISIBLE
//            } else {
//                binding.pbLoading.visibility = View.GONE
//            }
//        }
//
//        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
//            binding.searchBar.setText(binding.searchView.text)
//            binding.searchView.hide()
//            var query = binding.searchView.text.toString()
//            Toast.makeText(requireContext(), "Searching for ${query}", Toast.LENGTH_SHORT).show()
//            viewModel.searchEvents(query)
//            false
//        }
//
//        val finishedEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.rvFinished.layoutManager = finishedEventLayoutManager
//
//
//        viewModel.listFinishedEvents.observe(viewLifecycleOwner) { events ->
//            setFinishedEvent(events)
//        }
    }

    private fun setFinishedEvent(listeEvent: List<ListEventsItem>) {
        val adapter = FinishedAdapter()
        adapter.submitList(listeEvent)
        binding.rvFinished.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}