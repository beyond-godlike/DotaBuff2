package com.unava.dia.dotabuff.presentation.features.activity

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.unava.dia.dotabuff.domain.model.Player
import com.unava.dia.dotabuff.presentation.ui.theme.Away
import com.unava.dia.dotabuff.presentation.ui.theme.Busy
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens
import com.unava.dia.dotabuff.presentation.ui.theme.Offline
import com.unava.dia.dotabuff.presentation.ui.theme.Online
import com.unava.dia.dotabuff.presentation.ui.theme.Snooze


@Composable
fun PlayersActivity(
    steamId: String
) {

    val viewModel: ActivityViewModel = hiltViewModel()

    when (val state = viewModel.state.value) {
        ActivityViewModel.State.START -> {
            viewModel.dispatch(ActivityViewModel.Action.FetchActivity(steamId))
        }

        ActivityViewModel.State.LOADING -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is ActivityViewModel.State.FAILURE -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                val message = state.message
                Text(text = message, fontSize = 16.sp)
            }
        }

        is ActivityViewModel.State.SUCCESS -> {
            ShowPlayerActivity(state.player)
        }
    }

}

@Composable
fun ShowPlayerActivity(player: Player) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            PortraitPlayer(player)
        }

        else -> {
            LandscapePlayer(player)
        }
    }
}

@Composable
fun PortraitPlayer(player: Player) {
    Column(
        Modifier
            .wrapContentWidth()
            .padding(Dimens.Large)
    ) {
        Text(
            player.personname ?: "",
            modifier = Modifier.padding(Dimens.Padding),
            style = MaterialTheme.typography.caption
        )

        //0 - Offline, 1 - Online, 2 - Busy, 3 - Away, 4 - Snooze, 5 - looking to trade, 6 - looking to play.
        when (player.personastate) {
            0 -> {
                ImageWithBorder(player.avatarmedium, Offline)
            }

            1 -> {
                ImageWithBorder(player.avatarmedium, Online)
            }

            2 -> {
                ImageWithBorder(player.avatarmedium, Busy)
            }

            3 -> {
                ImageWithBorder(player.avatarmedium, Away)
            }

            4 -> {
                ImageWithBorder(player.avatarmedium, Snooze)
            }

            else -> {
                ImageWithBorder(player.avatarmedium, Color.Transparent)
            }
        }
        Text(
            player.gameextrainfo ?: "",
            modifier = Modifier.padding(Dimens.Padding),
            style = MaterialTheme.typography.caption
        )
        Text(
            player.steamid ?: "",
            modifier = Modifier.padding(Dimens.Padding),
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun LandscapePlayer(player: Player) {
    Row(
        Modifier
            .wrapContentHeight()
            .padding(Dimens.Large),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        when (player.personastate) {
            0 -> {
                ImageWithBorder(player.avatarmedium, Offline)
            }

            1 -> {
                ImageWithBorder(player.avatarmedium, Online)
            }

            2 -> {
                ImageWithBorder(player.avatarmedium, Busy)
            }

            3 -> {
                ImageWithBorder(player.avatarmedium, Away)
            }

            4 -> {
                ImageWithBorder(player.avatarmedium, Snooze)
            }

            else -> {
                ImageWithBorder(player.avatarmedium, Color.Transparent)
            }
        }
        Column() {
            Text(
                player.personname ?: "Nickname",
                modifier = Modifier.padding(Dimens.Padding),
                style = MaterialTheme.typography.caption
            )
            Text(
                player.gameextrainfo ?: "",
                modifier = Modifier.padding(Dimens.Padding),
                style = MaterialTheme.typography.caption
            )
            Text(
                player.steamid ?: "",
                modifier = Modifier.padding(Dimens.Padding),
                style = MaterialTheme.typography.caption
            )
        }
    }
}


// todo pass state & color
@Composable
fun ImageWithBorder(avatar: String?, color: Color) {
    Image(
        painter = rememberAsyncImagePainter(avatar),
        contentDescription = null,
        modifier = Modifier
            .border(2.dp, color, CircleShape)
            .size(Dimens.IconSize)
            .clip(CircleShape)
    )
}