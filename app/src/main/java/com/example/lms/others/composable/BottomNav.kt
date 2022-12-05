package com.example.lms.others.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lms.navigation.NavigationScreens

@Composable
fun BottomNav(navController: NavController, currentScreen: NavigationScreens, onMenuClick: ()->Unit){

    val items = listOf(
        NavigationScreens.OverviewScreen,
        NavigationScreens.CertificatesScreen,
        NavigationScreens.CalendarScreen
    )

    Row(){
        Row(Modifier.weight(1F)){
            Box(Modifier.weight(1F)){
                with(NavigationScreens.OverviewScreen){
                    NavigationItem(label = title, icon = icon, selected = currentScreen.route == route) {

                    }
                }
            }
            Box(Modifier.weight(1F)){
                with(NavigationScreens.CertificatesScreen){
                    NavigationItem(label = title, icon = icon, selected = currentScreen.route == route) {

                    }
                }
            }
        }
        Spacer(Modifier.width(60.dp))
        Row(Modifier.weight(1F)){
            Box(Modifier.weight(1F)){
                with(NavigationScreens.CalendarScreen){
                    NavigationItem(label = title, icon = icon, selected = currentScreen.route == route) {

                    }
                }
            }
            Box(Modifier.weight(1F)) {
                NavigationItem(label = "Menu", icon = Icons.Filled.Menu, selected = false) {
                    onMenuClick()
                }
            }
        }
    }
}

@Composable
fun NavigationItemIcon(icon: ImageVector){
    Icon(
        imageVector = icon,
        contentDescription = "",
        modifier = Modifier.size(25.dp),
        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F)
    )
}

@Composable
fun NavigationItemLabel(label: String){
    Text(
        text = label,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F)
    )
}

@Composable
fun NavigationItem(label: String?, icon: ImageVector?, selected: Boolean, onClick: ()->Unit){
    val tint = if(selected){
        MaterialTheme.colorScheme.onPrimary
    }else{
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7F)
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding()
            .clickable { onClick() }

    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = "",
                modifier = Modifier.size(35.dp),
                tint = tint
            )
        }
        label?.let {
            Text(
                text = it,
                fontSize = 9.sp,
                color = tint,
                modifier = Modifier.padding(horizontal = 3.dp)
            )
        }
    }
}