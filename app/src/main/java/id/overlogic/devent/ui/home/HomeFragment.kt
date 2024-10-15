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
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentHomeBinding
import id.overlogic.devent.ui.home.adapter.FinishedAdapter
import id.overlogic.devent.ui.home.adapter.UpcomingAdapter
import id.overlogic.devent.util.SpaceItemDecoration

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
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

        val upcomingEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcoming.layoutManager = upcomingEventLayoutManager

        val finishedEventLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvFinished.layoutManager = finishedEventLayoutManager

        viewModel.listUpcomingEvents.observe(viewLifecycleOwner) { events ->
            setUpcomingEvent(events)
        }

        viewModel.listFinishedEvents.observe(viewLifecycleOwner) { events ->
            setFinishedEvent(events)
        }
    }

    private fun setUpcomingEvent(listeEvent: List<ListEventsItem>, ) {
        val adapter = UpcomingAdapter()
        adapter.submitList(listeEvent)
        binding.rvUpcoming.adapter = adapter
    }

    private fun setFinishedEvent(listeEvent: List<ListEventsItem>) {
        val adapter = FinishedAdapter()
        adapter.submitList(listeEvent)
        binding.rvFinished.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}