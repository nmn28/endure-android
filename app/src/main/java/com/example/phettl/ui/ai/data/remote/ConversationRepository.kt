package com.example.phettl.ui.ai.data.remote

import com.example.phettl.ui.ai.models.ConversationModel

interface ConversationRepository {
    suspend fun fetchConversations() : MutableList<ConversationModel>
    fun newConversation(conversation: ConversationModel) : ConversationModel
    suspend fun deleteConversation(conversationId: String)
}