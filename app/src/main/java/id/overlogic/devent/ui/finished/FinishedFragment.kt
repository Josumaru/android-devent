package id.overlogic.devent.ui.finished

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FinishedFragment()
    }

    private val viewModel: FinishedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
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

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.setText(binding.searchView.text)
            binding.searchView.hide()
            Toast.makeText(requireContext(), binding.searchView.text, Toast.LENGTH_SHORT).show()
            false
        }


        viewModel.listFinishedEvents.observe(viewLifecycleOwner) { events ->
            setFinishedEvent(events)
        }
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