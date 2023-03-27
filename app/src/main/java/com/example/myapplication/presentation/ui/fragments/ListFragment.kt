package com.example.myapplication.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
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

        adapter = UserListAdapter { image, name, userId ->
            Navigation.findNavController(binding.root)
                .navigate(
                    ListFragmentDirections.actionListFragmentToDetailsFragment(userId),
                    FragmentNavigatorExtras(
                        Pair(image, "image_trans_to"),
                        Pair(name, "name_trans_to")
                    )
                )
        }

        bindViews()
        setupRecycler()
        handleActions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViews() {
        toggleDataChangeVisibility()
    }

    private fun setupRecycler() {
        binding.recycler.adapter = adapter
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
    }

    private fun getData() {

        sharedViewModel.userList.observe(viewLifecycleOwner) { data ->
            adapter.submitData(viewLifecycleOwner.lifecycle, data)
        }
        adapter.addLoadStateListener {
            toggleDataChangeVisibility()
        }
    }

    private fun toggleFabVisibility(scrollFlag: Boolean) {
        if (scrollFlag) {
            binding.fab.hide()
            return
        }
        binding.fab.show()
    }

    private fun toggleDataChangeVisibility() {
        binding.recycler.isVisible = adapter.itemCount != 0
        binding.includeProgress.progressWrapper.isVisible = adapter.itemCount == 0
        binding.includeEmpty.emptyWrapper.isVisible =
            adapter.itemCount == 0 && !binding.includeProgress.progressWrapper.isVisible
    }

    private fun handleActions() {
        binding.recycler.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            toggleFabVisibility(scrollY > oldScrollY)
        }
        binding.fab.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(ListFragmentDirections.actionListFragmentToFormFragment())
        }
    }

}