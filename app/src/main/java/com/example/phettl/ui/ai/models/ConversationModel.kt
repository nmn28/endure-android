package com.example.phettl.ui.ai.models

import java.util.*

data class ConversationModel(
    var id: String = Date().time.toString(),
    var title: String = "",
    var createdAt: Date = Date(),
)