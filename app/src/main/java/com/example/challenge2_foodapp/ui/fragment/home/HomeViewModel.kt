package com.example.challenge2_foodapp.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.challenge2_foodapp.core.data.FoodRepository
import com.example.challenge2_foodapp.core.domain.model.Category
import com.example.challenge2_foodapp.core.domain.model.Food
import com.example.challenge2_foodapp.core.utils.AssetWrapper
import com.example.challenge2_foodapp.core.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: FoodRepository, private val assetWrapper: AssetWrapper) : ViewModel() {

    val categories: LiveData<ResultWrapper<List<Category>>>
        get() = repo.getCategories().asLiveData(Dispatchers.IO)

    private val _food = MutableLiveData<ResultWrapper<List<Food>>>()
    val food: LiveData<ResultWrapper<List<Food>>>
        get() = _food

    fun getFoods(category: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoods(category).collect {
                _food.postValue(it)
            }
        }
    }
}