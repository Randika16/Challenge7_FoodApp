package com.example.challenge2_foodapp.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge2_foodapp.R
import com.example.challenge2_foodapp.adapter.CategoryAdapter
import com.example.challenge2_foodapp.adapter.FoodAdapter
import com.example.challenge2_foodapp.core.data.source.local.datastore.RecyclerViewSettings
import com.example.challenge2_foodapp.core.data.source.local.datastore.RecyclerViewViewModel
import com.example.challenge2_foodapp.core.data.source.local.datastore.RecyclerViewViewModelFactory
import com.example.challenge2_foodapp.core.data.source.local.datastore.dataStore
import com.example.challenge2_foodapp.core.domain.model.Category
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import com.example.challenge2_foodapp.databinding.FragmentHomeBinding
import com.example.challenge2_foodapp.ui.activity.detail.DetailActivity
import com.example.challenge2_foodapp.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private var whatAppearance = "list"

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = RecyclerViewSettings.getInstance(requireActivity().application.dataStore)
        val rvViewModel = ViewModelProvider(this, RecyclerViewViewModelFactory(pref)).get(
            RecyclerViewViewModel::class.java
        )
        homeViewModel.getFoods()

        homeViewModel.categories.observe(viewLifecycleOwner) {
            if (it != null) {
                when(it) {
                    is ResultWrapper.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvCategory.visibility = View.GONE
                        binding.emptyDataText.visibility = View.VISIBLE
                        binding.animationView.visibility = View.VISIBLE
                    }
                    is ResultWrapper.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.animationView.visibility = View.GONE
                        binding.rvCategory.visibility = View.VISIBLE
                        binding.emptyDataText.visibility = View.GONE
                        it.payload.let { data ->
                            if (data != null) {
                                categoryAdapter = CategoryAdapter(data)
                            }
                        }

                        binding.rvCategory.adapter = categoryAdapter
                        binding.rvCategory.setHasFixedSize(true)
                        binding.rvCategory.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                        categoryAdapter.setOnItemClickCallback(object :
                            CategoryAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: Category) {
                                showBasedOnSelectedCategory(data)
                                Toast.makeText(requireContext(), data.name, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                    is ResultWrapper.Error -> {
                        binding.rvCategory.visibility = View.GONE
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        Log.d("Error", it.exception.toString() + it.message.toString() ?: "Error")
                    }

                    is ResultWrapper.Empty -> {
                        binding.emptyDataText.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        binding.rvCategory.visibility = View.GONE
                        Toast.makeText(requireContext(), "Data Kategori Kosong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        rvViewModel.rvAppearance().observe(viewLifecycleOwner) { appearance ->
            whatAppearance = appearance

            recyclerView = binding.rvMakanan
            recyclerView.setHasFixedSize(true)

            // Hapus dekorasi yang ada
            val itemDecorations = recyclerView.itemDecorationCount
            for (i in 0 until itemDecorations) {
                recyclerView.removeItemDecorationAt(0)
            }

            // Hapus adapter lama
            recyclerView.adapter = null

            // Set tampilan yang baru
            if (whatAppearance == "grid") {
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                recyclerView.addItemDecoration(GridSpacingItemDecoration(spacing, true))
            } else {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }

            homeViewModel.food.observe(viewLifecycleOwner) {
                if(it != null) {
                    when(it) {
                        is ResultWrapper.Loading  -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.animationView.visibility = View.VISIBLE
                            binding.emptyDataText.visibility = View.VISIBLE
                            binding.rvMakanan.visibility = View.GONE
                        }
                        is ResultWrapper.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.animationView.visibility = View.GONE
                            binding.emptyDataText.visibility = View.GONE
                            binding.rvMakanan.visibility = View.VISIBLE
                            it.payload.let { data ->
                                if (data != null) {
                                    adapter = FoodAdapter(data, requireContext())
                                }
                            }
                            adapter.isListView = whatAppearance == "list"
                            binding.rvMakanan.adapter = adapter

                            adapter.setOnItemClickCallback(object : FoodAdapter.OnItemClickCallback {
                                override fun onItemClicked(data: Food) {
                                    sendSelectedFood(data)
                                }
                            })
                        }

                        is ResultWrapper.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.animationView.visibility = View.GONE
                            binding.rvMakanan.visibility = View.GONE
                            Log.d("Error", it.exception.toString() + it.message.toString() ?: "Error")
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }

                        is ResultWrapper.Empty -> {
                            binding.emptyDataText.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            binding.animationView.visibility = View.VISIBLE
                            binding.rvMakanan.visibility = View.GONE
                            Toast.makeText(requireContext(), "Data Kosong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            updateListButtonImage()
        }

        binding.listButton.setOnClickListener {
            rvViewModel.setRVAppearance(
                if (whatAppearance == "list") "grid" else "list"
            )

            // Update the adapter without recreating it
            //adapterDummy.setListView(whatAppearance == "list")
            adapter.isListView = whatAppearance == "list"
            adapter.notifyDataSetChanged()

            updateListButtonImage()
        }
    }

    private fun showBasedOnSelectedCategory(data: Category) {
        homeViewModel.getFoods(data.name)
    }

    private fun sendSelectedFood(data: Food) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra("food", data)
        startActivity(intent)
    }

    private fun updateListButtonImage() {
        // Ubah gambar pada listButton berdasarkan status isListView
        val imageResource =
            if (whatAppearance == "list") R.drawable.baseline_list_24 else R.drawable.icons8_grid
        binding.listButton.setImageResource(imageResource)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}