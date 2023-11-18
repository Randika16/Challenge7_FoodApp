package com.example.challenge2_foodapp.ui.activity.confirmation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.challenge2_foodapp.adapter.CartConfirmationAdapter
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import com.example.challenge2_foodapp.core.utils.proceedWhen
import com.example.challenge2_foodapp.databinding.ActivityConfirmationOrderBinding
import com.example.challenge2_foodapp.ui.activity.MainActivity
import com.example.challenge2_foodapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfirmationOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationOrderBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartConfirmationAdapter
    private var cartList = listOf<Cart>()

    private val confirmationOrderViewModel: ConfirmationOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.cartRecyclerView
        onClick()

        confirmationOrderViewModel.cartList.observe(this) { cart ->
            if (cart != null) {
                when(cart) {
                    is ResultWrapper.Loading -> {
                        binding.cartProgressBar.visibility = VISIBLE
                        binding.cartRecyclerView.visibility = GONE
                    }
                    is ResultWrapper.Success -> {
                        binding.cartProgressBar.visibility = GONE
                        binding.cartRecyclerView.visibility = VISIBLE
                        cart.payload?.let { (carts, totalPrice) ->
                            Log.d("ConfirmationOrderActivity", " : $carts $totalPrice")
                            adapter = CartConfirmationAdapter(carts, this)
                            binding.totalPayment.text = totalPrice.toCurrencyFormat()
                            cartList = carts.map { cartEntity ->
                                Cart(
                                    id = cartEntity.id,
                                    foodItem = cartEntity.foodItem,
                                    foodQuantity = cartEntity.foodQuantity,
                                    foodNote = cartEntity.foodNote,
                                )
                            }
                        }
                        recyclerView.setHasFixedSize(true)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        recyclerView.adapter = adapter
                    }

                    is ResultWrapper.Error -> {
                        Toast.makeText(
                            this,
                            cart.message.toString() + cart.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ResultWrapper.Empty -> {
                        Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        observeCheckoutResult()
    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.orderButton.setOnClickListener {
            lifecycleScope.launch {
                placeOrder(cartList)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this@ConfirmationOrderActivity,
            msg,
            Toast.LENGTH_SHORT
        ).show()

        confirmationOrderViewModel.clearCart()

        if (msg == "Pesanan Berhasil") {
            val intent = Intent(this@ConfirmationOrderActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("order", true) // Kirim informasi bahwa order berhasil
            startActivity(intent)
        }
    }

    private fun observeCheckoutResult() {
        confirmationOrderViewModel.checkoutResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.cartProgressBar.visibility = GONE
                    showToast("Pesanan Berhasil")
                },
                doOnError = {
                    Toast.makeText(this, "Checkout Error", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.cartProgressBar.visibility = VISIBLE
                }
            )
        }
    }

    private suspend fun placeOrder(cart: List<Cart>) {
        confirmationOrderViewModel.order(cart)
    }
}