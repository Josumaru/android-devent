package id.overlogic.devent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.overlogic.devent.data.Result
import id.overlogic.devent.data.remote.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private  var isFirstTime = true

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UpcomingViewModel by viewModels {
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
                            binding.rvUpcoming.layoutManager = upcomingEventLayoutManager
                            val adapter = UpcomingAdapter()
                            adapter.submitList(listItemEvents)
                            binding.rvUpcoming.adapter = adapter
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
        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) { result ->
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
                        binding.rvUpcoming.layoutManager = upcomingEventLayoutManager
                        val adapter = UpcomingAdapter()
                        adapter.submitList(listItemEvents)
                        binding.rvUpcoming.adapter = adapter
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
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        isFirstTime = false
    }
}