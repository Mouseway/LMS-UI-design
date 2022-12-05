package com.example.lms.others.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun VerticalMenu(items: List<MenuItem>){
    Column(
        Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .width(65.dp)
    ) {
        if(items.size >= 4){
            items.forEach{
                Box(Modifier.weight(1F)){
                    NavigationItem(label = it.title, icon = it.icon, selected = true) {
                        it.onClick()
                    }
                }
            }
        }else if(items.size == 2){
            with(items[0]){
                Box(Modifier.weight(1F)){
                    NavigationItem(label = title, icon = icon, selected = true) {
                        onClick()
                    }
                }
            }
            Spacer(modifier = Modifier.weight(2F))
            with(items[1]){
                Box(Modifier.weight(1F)){
                    NavigationItem(label = title, icon = icon, selected = true) {
                        onClick()
                    }
                }
            }
        }
    }
}

class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: ()->Unit
)