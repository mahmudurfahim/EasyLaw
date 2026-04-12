package com.example.easylaw.explorescreens.newsheadline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easylaw.explorescreens.HeadlineNews
import com.example.easylaw.explorescreens.sampleBanglaHeadlines
import com.example.easylaw.explorescreens.sampleEnglishHeadlines
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HeadlinesState(
    val englishHeadlines: List<HeadlineNews> = sampleEnglishHeadlines,
    val banglaHeadlines:  List<HeadlineNews> = sampleBanglaHeadlines,
    val isLoading: Boolean = false,
    val error: String?     = null
)

class HomeViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _headlinesState = MutableStateFlow(HeadlinesState())
    val headlinesState: StateFlow<HeadlinesState> = _headlinesState

    init { refreshHeadlines() }

    fun refreshHeadlines() {
        viewModelScope.launch {
            _headlinesState.value = _headlinesState.value.copy(isLoading = true, error = null)

            val englishDeferred = async { repository.fetchEnglishHeadlines(limit = 5) }
            val banglaDeferred  = async { repository.fetchBanglaHeadlines(limit = 5) }

            val english = englishDeferred.await()
            val bangla  = banglaDeferred.await()

            _headlinesState.value = HeadlinesState(
                englishHeadlines = english.ifEmpty { sampleEnglishHeadlines },
                banglaHeadlines  = bangla.ifEmpty  { sampleBanglaHeadlines  },
                isLoading = false,
                error = if (english.isEmpty() && bangla.isEmpty())
                    "Could not load live headlines" else null
            )
        }
    }
}