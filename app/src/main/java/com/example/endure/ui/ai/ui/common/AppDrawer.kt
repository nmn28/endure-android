package com.example.endure.ui.ai.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.endure.ui.ai.constants.urlToGithub
import com.example.endure.ui.ai.constants.urlToImageAppIcon
import com.example.endure.ui.ai.constants.urlToImageAuthor
import com.example.endure.ui.ai.helpers.UrlLauncher
import com.example.endure.ui.ai.models.ConversationModel
import com.example.endure.ui.ai.ui.conversations.ConversationViewModel
import kotlinx.coroutines.launch


@Composable
fun AppDrawer(
    onChatClicked: (String) -> Unit,
    onNewChatClicked: () -> Unit,
    conversationViewModel: ConversationViewModel = hiltViewModel(),
    onIconClicked: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    AppDrawerIn(
        onChatClicked = onChatClicked,
        onNewChatClicked = onNewChatClicked,
        onIconClicked = onIconClicked,
        conversationViewModel = { conversationViewModel.newConversation() },
        deleteConversation = { text ->
            coroutineScope.launch {
                conversationViewModel.deleteConversation(text)
            }
        },
        deleteMessages = { text ->
            coroutineScope.launch {
                conversationViewModel.deleteMessages(text)
            }
        },
        onConversation = { conversationModel: ConversationModel ->
            coroutineScope.launch { conversationViewModel.onConversation(conversationModel) }
        },
        currentConversationState = conversationViewModel.currentConversationState.collectAsState().value,
        conversationState = conversationViewModel.conversationsState.collectAsState().value
    )
}


@Composable
private fun AppDrawerIn(
    onChatClicked: (String) -> Unit,
    onNewChatClicked: () -> Unit,
    onIconClicked: () -> Unit,
    conversationViewModel: () -> Unit,
    deleteConversation: (String) -> Unit,
    deleteMessages: (String) -> Unit,
    onConversation: (ConversationModel) -> Unit,
    currentConversationState: String,
    conversationState: MutableList<ConversationModel>,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader(clickAction = onIconClicked)
        DividerItem()
        DrawerItemHeader("Chats")
        ChatItem("New Chat", Icons.Outlined.AddComment, false) {
            onNewChatClicked()
            conversationViewModel()
        }
        HistoryConversations(
            onChatClicked,
            deleteConversation,
            onConversation,
            deleteMessages,
            currentConversationState,
            conversationState
        )
        DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
        DrawerItemHeader("Settings")
        ChatItem("Settings", Icons.Filled.Settings, false) { onChatClicked("Settings") }
        ProfileItem(
            "lambiengcode (author)",
            urlToImageAuthor,
        ) {
            UrlLauncher().openUrl(context = context, urlToGithub)
        }
    }
}

@Composable
private fun DrawerHeader(
    clickAction: () -> Unit = {}
) {
    val paddingSizeModifier = Modifier
        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        .size(34.dp)

    Row(verticalAlignment = CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f), verticalAlignment = CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(urlToImageAppIcon),
                modifier = paddingSizeModifier.then(Modifier.clip(RoundedCornerShape(6.dp))),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    "ChatGPT Lite",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    "Powered by OpenAI",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
            }
        }

        IconButton(
            onClick = {
                clickAction.invoke()
            },
        ) {
            Icon(
                Icons.Filled.WbSunny,
                "backIcon",
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ColumnScope.HistoryConversations(
    onChatClicked: (String) -> Unit,
    deleteConversation: (String) -> Unit,
    onConversation: (ConversationModel) -> Unit,
    deleteMessages: (String) -> Unit,
    currentConversationState: String,
    conversationState: List<ConversationModel>
) {
    val scope = rememberCoroutineScope()

    LazyColumn(
        Modifier
            .fillMaxWidth()
            .weight(1f, false),
    ) {
        items(conversationState.size) { index ->
            RecycleChatItem(
                text = conversationState[index].title,
                Icons.Filled.Message,
                selected = conversationState[index].id == currentConversationState,
                onChatClicked = {
                    onChatClicked(conversationState[index].id)

                    scope.launch {
                        onConversation(conversationState[index])
                    }
                },
                onDeleteClicked = {
                    scope.launch {
                        deleteConversation(conversationState[index].id)
                        deleteMessages(conversationState[index].id)
                    }
                }
            )
        }
    }
}

@Composable
private fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 28.dp),
        contentAlignment = CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ChatItem(
    text: String,
    icon: ImageVector = Icons.Filled.Edit,
    selected: Boolean,
    onChatClicked: () -> Unit
) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            icon,
            tint = iconTint,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .size(25.dp),
            contentDescription = null,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RecycleChatItem(
    text: String,
    icon: ImageVector = Icons.Filled.Edit,
    selected: Boolean,
    onChatClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 34.dp)
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            icon,
            tint = iconTint,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .size(25.dp),
            contentDescription = null,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth(0.85f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.weight(0.9f, true))
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete",
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.clickable { onDeleteClicked() }
        )
    }
}

@Composable
private fun ProfileItem(text: String, urlToImage: String?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .clickable(onClick = onProfileClicked),
        verticalAlignment = CenterVertically
    ) {
        val paddingSizeModifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .size(24.dp)
        if (urlToImage != null) {
            Image(
                painter = rememberAsyncImagePainter(urlToImage),
                modifier = paddingSizeModifier.then(Modifier.clip(CircleShape)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = paddingSizeModifier)
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Preview()
@Composable
fun PreviewAppDrawerIn(
) {
    AppDrawerIn(
        onChatClicked = {},
        onNewChatClicked = {},
        onIconClicked = {},
        conversationViewModel = {},
        deleteConversation = {},
        deleteMessages = {},
        conversationState = mutableListOf(),
        currentConversationState = String(),
        onConversation = { _: ConversationModel -> }

    )

}