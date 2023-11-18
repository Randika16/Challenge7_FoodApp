package com.example.challenge2_foodapp.ui.activity.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challenge2_foodapp.core.data.CartRepository
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val cartRepository: CartRepository) :
    ViewModel() {

    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun insertToCart(food: Food, totalQuantity: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            cartRepository.insertIntoCart(food, totalQuantity).collect { result ->
                _addToCartResult.postValue(result)
            }
        }
    }
}