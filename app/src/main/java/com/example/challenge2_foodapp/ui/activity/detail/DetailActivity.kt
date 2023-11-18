package com.example.challenge2_foodapp.ui.activity.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.challenge2_foodapp.R
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.proceedWhen
import com.example.challenge2_foodapp.databinding.ActivityDetailBinding
import com.example.challenge2_foodapp.utils.toCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var priceTotal = 0
    private var foodQuantity = 1

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val food = intent.getParcelableExtra<Food>("food") as Food

        detailViewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.apply {
            val foodName = food.name
            val foodPrice = food.priceFormat
            val foodImage = food.image
            val foodDescription = food.description
            val foodLocation = food.location

            tvMakananDetail.text = foodName
            Glide.with(this@DetailActivity)
                .load(foodImage)
                .into(ivMakananDetail)

            tvHargaDetail.text = foodPrice
            tvDesc.text = foodDescription
            lokasiValue.text = foodLocation
            addToCart.text = getString(R.string.tambah_keranjang, foodPrice)
        }

        binding.ibMinus.setOnClickListener {
            if (foodQuantity > 1) {
                foodQuantity--
                priceTotal = foodQuantity * food.price
                binding.tvJumlah.text = foodQuantity.toString()
                binding.addToCart.text =
                    getString(R.string.tambah_keranjang, priceTotal.toCurrencyFormat())

            } else {
                Toast.makeText(this, "Cannot be less than one", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.ibPlus.setOnClickListener {
            foodQuantity++
            priceTotal = foodQuantity * food.price
            binding.tvJumlah.text = foodQuantity.toString()
            binding.addToCart.text =
                getString(R.string.tambah_keranjang, priceTotal.toCurrencyFormat())
        }

        binding.addToCart.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    detailViewModel.insertToCart(
                        Food(
                            food.id,
                            food.name,
                            food.price,
                            food.priceFormat,
                            food.description,
                            food.location,
                            food.image,
                        ),
                        foodQuantity
                    )
                }
                Toast.makeText(
                    this@DetailActivity,
                    "Successfully added your food to cart",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}