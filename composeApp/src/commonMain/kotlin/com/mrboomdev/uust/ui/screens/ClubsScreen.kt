package com.mrboomdev.uust.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrboomdev.uust.LocalBackStack
import com.mrboomdev.uust.resources.Res
import com.mrboomdev.uust.resources.logo_amaterasu
import com.mrboomdev.uust.resources.wallpaper_amaterasu
import com.mrboomdev.uust.resources.wallpaper_bb
import com.mrboomdev.uust.ui.navigation.Routes
import org.jetbrains.compose.resources.painterResource

@Composable
fun ClubsScreen(
    contentPadding: PaddingValues
) {
    val windowInfo = LocalWindowInfo.current
    val backStack = LocalBackStack.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(windowInfo.containerDpSize.height * .6f)
        ) {
            Image(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = .25f,
                painter = painterResource(Res.drawable.wallpaper_amaterasu),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text(
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    text = "Аматэрасу"
                )

                Text(
                    text = "Аниме клуб «Аматэрасу» - это объединение людей, которые увлекаются японской мультипликацией, мангой и всем, что с этим связано. В клубе вы сможете насладиться приятной атмосферой в компании единомышленников, обсудить собственные идеи аниме-мероприятий."
                )

                Button(
                    modifier = Modifier.padding(top = 4.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),

                    onClick = {
                        backStack += Routes.Club
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = "Перейти"
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(windowInfo.containerDpSize.height * .6f)
        ) {
            Image(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = .25f,
                painter = painterResource(Res.drawable.wallpaper_bb),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text(
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    text = "Brain Bricks"
                )

                Text(
                    text = "Brain Bricks – клуб разработчиков игр, где каждый может воплотить свои идеи в жизнь. В нашей лаборатории ты создашь свою настольную игру, протестируешь десятки игр от других авторов, познакомишься с единомышленниками и профессионалами геймдева."
                )
            }
        }
    }
}