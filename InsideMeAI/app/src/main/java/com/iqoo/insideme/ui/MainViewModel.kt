package com.iqoo.insideme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqoo.insideme.ai.GroundedResponse
import com.iqoo.insideme.integration.InsideMeOrchestrator
import com.iqoo.insideme.integration.SessionContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val response: GroundedResponse? = null
)

class MainViewModel(
    private val orchestrator: InsideMeOrchestrator
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun onSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, query = query)
            
            // Mock context
            val context = SessionContext(
                activeEntityId = null,
                activeProjectId = null,
                lastQuery = null,
                lastMemoryIds = emptyList(),
                updatedAt = System.currentTimeMillis()
            )
            
            val result = orchestrator.answerQuery(query, context)
            _uiState.value = _uiState.value.copy(isLoading = false, response = result)
        }
    }
}
