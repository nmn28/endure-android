package com.example.endure.ui.custompicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlin.math.absoluteValue

data class PickerItem(
    val label: String? = null,
    val icon: ImageVector? = null,
    val content: (@Composable () -> Unit)? = null
)

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun CustomPicker(
    items: List<PickerItem>,
    dropdownOptions: List<String>,
    showLabel: Boolean = true,
    showIcon: Boolean = true,
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var selectedLabel by remember { mutableStateOf(items[0].label ?: "") }
    var selectedDropdownOption by remember { mutableStateOf(dropdownOptions[0]) }
    var showMenu by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(initialPage = selectedIndex)
    var rowSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    val itemWidth = with(density) { rowSize.width.toDp() / items.size }
    val animatedOffset by animateDpAsState(targetValue = itemWidth * selectedIndex)
    val animatedWidth by animateDpAsState(targetValue = itemWidth)

    LaunchedEffect(selectedIndex) {
        selectedLabel = items[selectedIndex].label ?: ""
        pagerState.scrollToPage(selectedIndex)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { newSize -> rowSize = newSize }
        ) {
            items.forEachIndexed { index, pickerItem ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    if (showIcon && pickerItem.icon != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = pickerItem.icon, contentDescription = null)
                            if (showLabel) {
                                Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                                Text(text = pickerItem.label ?: "")
                            }
                        }
                    } else if (showLabel) {
                        Text(text = pickerItem.label ?: "")
                    }
                }
            }
        }

        // Animated indicator
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(animatedWidth)
                .height(4.dp)
                .background(Color.Red)
        )

        // Divider
        Divider(color = Color.Gray, thickness = 1.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            // Content that will trigger the DropdownMenu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { showMenu = !showMenu }
            ) {
                Text(
                    text = "$selectedLabel: $selectedDropdownOption",
                    modifier = Modifier.padding(end = 6.dp)
                )
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = if (showMenu) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Menu"
                    )
                }
            }

            // Dropdown menu aligned to the end of the row content
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                dropdownOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedDropdownOption = option
                        showMenu = false
                    }) {
                        Text(option)
                    }
                }
            }
        }

        // Pager content
        HorizontalPager(
            count = items.size,
            state = pagerState,
            modifier = Modifier.weight(1f),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                decayAnimationSpec = rememberSplineBasedDecay()
            )
        ) { page ->
            items[page].content?.invoke() ?: PlaceholderContent()
        }
    }

    // Observe page changes from swipes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedIndex = page
            selectedLabel = items[page].label ?: ""
        }
    }

    // Update the selected index based on the pager scroll state
    LaunchedEffect(pagerState.currentPageOffset) {
        val currentPageOffset = pagerState.currentPageOffset
        val currentPage = pagerState.currentPage
        val targetPage = if (currentPageOffset < 0) currentPage else currentPage + 1
        val progress = (currentPageOffset % 1).absoluteValue
        selectedIndex = (currentPage + progress).toInt().coerceIn(0, items.lastIndex)
    }
}
@Composable
fun PlaceholderContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Content Coming Soon", style = MaterialTheme.typography.subtitle1)
    }
}