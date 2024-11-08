package com.example.endure.ui.ai.ui.conversations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.endure.ui.ai.models.MessageModel
import com.example.endure.ui.ai.ui.theme.BackGroundMessageGPT
import com.example.endure.ui.ai.ui.theme.BackGroundMessageHuman
import com.example.endure.ui.ai.ui.theme.ChatGPTLiteTheme
import com.example.endure.ui.ai.ui.theme.ColorTextGPT
import com.example.endure.ui.ai.ui.theme.ColorTextHuman
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import java.util.Date

@Composable
fun MessageCard(message: MessageModel, isHuman: Boolean = false, isLast: Boolean = false) {
    Column(
        horizontalAlignment = if (isHuman) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(top = if (isLast) 120.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .widthIn(0.dp, 260.dp) //mention max width here
                .background(
                    if (isHuman) BackGroundMessageHuman else BackGroundMessageGPT,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            if (isHuman) {
                HumanMessageCard(message = message)
            } else {
                BotMessageCard(message = message)
            }
        }
    }
}

@Composable
fun HumanMessageCard(message: MessageModel) {
    Text(
        text = message.question,
        fontSize = 14.sp,
        color = ColorTextHuman,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
        textAlign = TextAlign.Justify,
    )
}

@Composable
fun BotMessageCard(message: MessageModel) {
    ChatGPTLiteTheme {
        SetupMaterial3RichText {
            RichText(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                style = RichTextStyle(
                    codeBlockStyle = CodeBlockStyle(
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = ColorTextGPT,
                        ),
                        wordWrap = true,
                        modifier = Modifier.background(
                            color = Color.Black,
                            shape = RoundedCornerShape(6.dp)
                        )
                    )
                )
            ) {
                Markdown(
                    message.answer.trimIndent()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageCardPreviewHuman() {

    MessageCard(
        message =  MessageModel(
            id = "",
            conversationId = "",
            question = "question text field by Human ",
            answer = "question text field by Human ",
            createdAt = Date()

        ),
        isHuman = true,
        isLast= false

    )
}

@Preview(showBackground = true)
@Composable
fun MessageCardPreviewBot() {

    MessageCard(
        message =  MessageModel(
            id = "",
            conversationId = "",
            question = "answer text field by Bot ",
            answer = "answer text field by Bot ",
            createdAt = Date()

        ),
        isHuman = false,
        isLast= false

    )
}