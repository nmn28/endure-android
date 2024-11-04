package com.example.phettl.ui.ai.data.remote

import com.example.phettl.ui.ai.models.TextCompletionsParam
import kotlinx.coroutines.flow.Flow

interface OpenAIRepository {
    fun textCompletionsWithStream(params: TextCompletionsParam): Flow<String>
}