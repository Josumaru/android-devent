package id.overlogic.devent.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.overlogic.devent.data.Result
import id.overlogic.devent.data.remote.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentBlankBinding


class FavouriteFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: FavouriteViewModel by viewModels {
            factory
        }
        viewModel.getBookmark().observe(viewLifecycleOwner) { result ->
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
                        val adapter = FavouriteAdapter()
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
    }
}