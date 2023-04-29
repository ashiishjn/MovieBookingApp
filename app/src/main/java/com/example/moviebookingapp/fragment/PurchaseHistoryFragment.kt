package com.example.moviebookingapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebookingapp.MainApplication
import com.example.moviebookingapp.R
import com.example.moviebookingapp.adapter.PurchaseHistoryAdapter
import com.example.moviebookingapp.databinding.FragmentPurchaseHistoryBinding
import com.example.moviebookingapp.viewmodel.MainActivityViewModel
import com.example.moviebookingapp.viewmodel.MainActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseHistoryFragment : Fragment() {

    private var _binding : FragmentPurchaseHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var purchaseHistoryAdapter: PurchaseHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPurchaseHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.purchaseHistoryBackArrow.setOnClickListener {
            findNavController().navigate(R.id.action_purchaseHistoryFragment_to_homeScreenFragment)
        }

        val repository =
            (MainApplication.applicationContext() as MainApplication).movieListRepositry

        mainActivityViewModel = ViewModelProvider(
            requireActivity(),
            MainActivityViewModelFactory(repository)
        )[MainActivityViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            mainActivityViewModel.updateFromDatabase()
        }

        // *************************************************************************************

        purchaseHistoryAdapter = PurchaseHistoryAdapter()

        binding.purchaseHistoryRecyclerView.layoutManager =
            GridLayoutManager(view.context, 1)
        binding.purchaseHistoryRecyclerView.adapter = purchaseHistoryAdapter

        // *************************************************************************************

        mainActivityViewModel.userDetails.observe(viewLifecycleOwner) {
            binding.noDisplay.visibility = View.GONE
            purchaseHistoryAdapter.submitList(it.tickets_Purchased.asReversed())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}