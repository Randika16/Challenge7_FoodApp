package com.example.challenge2_foodapp.ui.fragment.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.challenge2_foodapp.core.data.CartRepository
import com.example.challenge2_foodapp.core.domain.model.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    var cart = cartRepository.getCart().asLiveData(Dispatchers.IO)

    fun decreaseCart(item: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.decreaseCart(item).collect {
                Log.d("CartViewModel", " : Decrease Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun increaseCart(item: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.increaseCart(item).collect {
                Log.d("CartViewModel", " : Increase Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }


    fun removeCart(item: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.deleteCart(item).collect {
                Log.d("CartViewModel", " : Remove Cart -> $it ${it.payload} ${it.exception}")
            }
        }
    }

    fun setCartNotes(item: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.setCartNotes(item).collect {
                Log.d("CartViewModel", " : Set Cart Notes -> $it ${it.payload} ${it.exception}")
            }
        }
    }

}