package com.example.myapplication.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentListBinding
import com.example.myapplication.presentation.ui.adapters.UserListAdapter
import com.example.myapplication.presentation.viewmodels.SharedViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(layoutInflater, container, false)

        adapter = UserListAdapter {
            Navigation.findNavController(binding.root)
                .navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(it))
        }

        bindViews()
        getData()
        setupRecycler()
        handleActions()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        toggleEmptyVisibility()
    }

    private fun setupRecycler() {
        binding.recycler.adapter = adapter
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getData() {
        sharedViewModel.userList.observe(viewLifecycleOwner) {
            it?.let { data -> adapter.setItems(data) }
            toggleEmptyVisibility()
        }
    }

    private fun toggleFabVisibility(scrollFlag: Boolean) {
        if (scrollFlag) {
            binding.fab.hide()
            return
        }
        binding.fab.show()
    }

    private fun toggleEmptyVisibility() {
        binding.recycler.isVisible = adapter.itemCount != 0
        binding.includeEmpty.emptyWrapper.isVisible = adapter.itemCount == 0
    }

    private fun handleActions() {
        binding.nestedScroll.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            toggleFabVisibility(scrollY > oldScrollY)
        }
        binding.fab.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(ListFragmentDirections.actionListFragmentToFormFragment())
        }
    }

}