package com.example.challenge2_foodapp.ui.activity.confirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.challenge2_foodapp.core.data.CartRepository
import com.example.challenge2_foodapp.core.domain.model.Cart
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationOrderViewModel @Inject constructor(private val cartRepository: CartRepository) :
    ViewModel() {

    val cartList = cartRepository.getCart().asLiveData(Dispatchers.IO)

    private val _checkoutResult = MutableLiveData<ResultWrapper<Boolean>>()
    val checkoutResult: LiveData<ResultWrapper<Boolean>>
        get() = _checkoutResult

    suspend fun order(orderRequest: List<Cart>) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.order(orderRequest).collect {
                _checkoutResult.postValue(it)
            }
        }
    }

    fun clearCart() {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.deleteAll()
        }
    }

}