package com.zoho.randomuser.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.zoho.randomuser.data.RandomUser
import com.zoho.randomuser.data.RandomUserMediator
import com.zoho.randomuser.data.RandomUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


@HiltViewModel
class RandomUserViewModel @Inject constructor(
    private val repository: RandomUserRepository
) : ViewModel() {
    private val queryFlow = MutableStateFlow("")

    var randomUserList: Flow<PagingData<RandomUser>> = repository.getRandomUserResultStream()
        .flow.cachedIn(viewModelScope).combine(queryFlow) { pagingData, query ->
            pagingData.filter {
                it.name?.first?.lowercase()?.startsWith(query) ?: false
            }
        }

    fun searchUserByQuery(query: String) {
        queryFlow.value = query.lowercase()
    }

}