package com.example.endure.ui.ai.data.remote

import android.content.ContentValues
import android.util.Log
import com.example.endure.ui.ai.constants.conversationCollection
import com.example.endure.ui.ai.helpers.DataHolder
import com.example.endure.ui.ai.models.ConversationModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val fsInstance: FirebaseFirestore,
) : ConversationRepository {

    override suspend fun fetchConversations(): MutableList<ConversationModel> {

        if (getFireBaseSnapShot().documents.isNotEmpty()) {
            val documents = getFireBaseSnapShot().documents

            return documents.map {
                it.toObject(ConversationModel::class.java)
            }.toList() as MutableList<ConversationModel>
        }

        return mutableListOf()
    }

    override fun newConversation(conversation: ConversationModel): ConversationModel {
        fsInstance.collection(conversationCollection).add(conversation)
        return conversation
    }

    override suspend fun deleteConversation(conversationId: String) {
        var desiredKey: String? = null

        getFireBaseSnapShot().documents.map { documentSnapshot ->
            val id = documentSnapshot.getString("id")
            if (id == conversationId) {
                desiredKey = documentSnapshot.id
            } else {
                null
            }
        }
        DataHolder.docPath = desiredKey.toString()

        val docRef = fsInstance
            .collection("conversations")
            .document(DataHolder.docPath)

        // Remove the 'capital' field from the document
        val updates = hashMapOf<String, Any>(
            "id" to FieldValue.delete(),
            "title" to FieldValue.delete(),
            "createdAt" to FieldValue.delete()
        )
        docRef.update(updates)
            .addOnSuccessListener {
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot successfully deleted from message!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    ContentValues.TAG,
                    "Error deleting document", e
                )
            }
    }
    private suspend fun getFireBaseSnapShot() =
        fsInstance.collection(conversationCollection)
            .orderBy("createdAt", Query.Direction.DESCENDING).get().await()

}