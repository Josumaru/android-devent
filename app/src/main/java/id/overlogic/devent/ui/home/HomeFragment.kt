package id.overlogic.devent.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.overlogic.devent.R
import id.overlogic.devent.data.local.entity.EventEntity
import id.overlogic.devent.data.remote.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentHomeBinding
import id.overlogic.devent.ui.home.adapter.FinishedAdapter
import id.overlogic.devent.ui.home.adapter.UpcomingAdapter
import id.overlogic.devent.util.SpaceItemDecoration
import id.overlogic.devent.data.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingEventLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcoming.layoutManager = upcomingEventLayoutManager

        val finishedEventLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvFinished.layoutManager = finishedEventLayoutManager
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels {
            factory
        }


        viewModel.getUpcomingEvent()?.observe(viewLifecycleOwner) { result ->
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
                        setUpcomingEvent(listItemEvents)
                        binding.pbUpcoming.visibility = View.GONE

                    }

                    is Result.Error -> {
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Loading -> {
                        binding.pbUpcoming.visibility = View.VISIBLE
                    }

                }
            }
        }


        viewModel.getFinishedEvent()?.observe(viewLifecycleOwner) { result ->
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
                        setFinishedEvent(listItemEvents)
                        binding.pbFinish.visibility = View.GONE

                    }

                    is Result.Error -> {
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Loading -> {
                        binding.pbFinish.visibility = View.VISIBLE
                    }

                }
            }
        }
    }

    private fun setUpcomingEvent(listeEvent: List<ListEventsItem>) {
        val adapter = UpcomingAdapter()
        adapter.submitList(listeEvent)
        binding.rvUpcoming.adapter = adapter
    }

    private fun setFinishedEvent(listeEvent: List<ListEventsItem>) {
        val adapter = FinishedAdapter()
        adapter.submitList(listeEvent.take(5))
        binding.rvFinished.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}