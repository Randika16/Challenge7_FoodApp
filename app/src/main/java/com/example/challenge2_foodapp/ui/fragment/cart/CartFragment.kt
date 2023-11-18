package com.example.challenge2_foodapp.ui.fragment.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge2_foodapp.adapter.CartAdapter
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import com.example.challenge2_foodapp.databinding.FragmentCartBinding
import com.example.challenge2_foodapp.ui.activity.confirmation.ConfirmationOrderActivity
import com.example.challenge2_foodapp.ui.activity.detail.DetailActivity
import com.example.challenge2_foodapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    private var totalPriceFood = 0

    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.cartOrderButton.setOnClickListener {
            if (totalPriceFood > 0) {
                val intent = Intent(requireActivity(), ConfirmationOrderActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Cart is empty, please add some items to cart first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.cartRvFood

        cartViewModel.cart.observe(viewLifecycleOwner) { cart ->

            if (cart != null) {
                when(cart) {
                    is ResultWrapper.Loading -> {
                        binding.cartRvFood.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        binding.emptyDataText.visibility = View.GONE
                    }
                    is ResultWrapper.Success -> {
                        binding.cartRvFood.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE
                        binding.emptyDataText.visibility = View.GONE
                        cart.payload?.let { (carts, totalPrice) ->
                            adapter = CartAdapter(carts, requireContext())
                            binding.cartPriceTotal.text = totalPrice.toCurrencyFormat()
                            totalPriceFood = totalPrice
                        }

                        recyclerView.setHasFixedSize(true)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: Cart) {
                                sendSelectedFood(data.foodItem)
                            }

                            override fun onDeleteClicked(data: Cart) {
                                showDeleteConfirmationDialog(data)
                            }

                            override fun onMinusClicked(data: Cart) {
                                if (data.foodQuantity > 1) {
                                    cartViewModel.decreaseCart(data)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Food quantity cannot be less than 1",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onPlusClicked(data: Cart) {
                                cartViewModel.increaseCart(data)
                            }

                            override fun onNotesChanged(data: Cart) {
                                cartViewModel.setCartNotes(data)
                            }
                        })
                    }

                    is ResultWrapper.Error -> {
                        binding.cartRvFood.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        binding.emptyDataText.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), cart.exception.toString() + cart.message.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Error", cart.exception.toString() + cart.message.toString() ?: "Error")
                    }
                    is ResultWrapper.Empty -> {
                        binding.cartRvFood.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        binding.emptyDataText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(data: Cart) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete ${data.foodItem.name}")
            .setMessage("Do you want to delete ${data.foodItem.name} from cart?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                deleteCart(data)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteCart(data: Cart) {
        cartViewModel.removeCart(data)
    }

    private fun sendSelectedFood(data: Food) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra("food", data)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}