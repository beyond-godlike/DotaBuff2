package com.unava.dia.dotabuff.presentation.features.players

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.presentation.features.State
import com.unava.dia.dotabuff.presentation.features.destinations.AddPlayerDestination
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.NameWidth
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.Padding
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.ShimmerWidth
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.TextWidth
import com.unava.dia.dotabuff.presentation.ui.theme.DotaBuffTheme

@Destination(start = true)
@Composable
fun Players(
    navigator: DestinationsNavigator,
) {
    PlayersList(navigator)

}

@Composable
fun PlayersList(navigator: DestinationsNavigator) {
    Column(Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextItem("Avatar")
            TextName("Name")
            TextItem("Estim")
            TextItem("Solo")
            TextItem("Party")
            TextItem("Rank")

            IconButton(
                modifier = Modifier.padding(start = Padding),
                onClick = { navigator.navigate(AddPlayerDestination(-1)) }
            ) {
                Icon(Icons.Filled.Add, "add player")
            }
        }
        //Spacer(modifier = Modifier.size(Dimens.Small))

        FillList(navigator)

    }
}

@Composable
fun FillList(navigator: DestinationsNavigator) {
    val viewModel: PlayersViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    when (state) {
        State.START -> {
            Text("start")
            //List()
        }
        State.LOADING -> {
            CircularProgressIndicator()
        }
        is State.FAILURE -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                val message = (state as State.FAILURE).message
                Text(text = message, fontSize = 16.sp)
            }
        }
        is State.SUCCESSLIST -> {
            val players = (state as State.SUCCESSLIST).players
            if (players.isEmpty()) Text("list is empty")
            else
                List(players, navigator, viewModel)
        }
        else -> {
            Text("ok")
        }
    }
    viewModel.getPlayerList()
}

@Composable
fun List(
    players: List<AccInformation>,
    navigator: DestinationsNavigator,
    viewModel: PlayersViewModel,
) {
    val ctx = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(players.size) {
            ShimmerListItem(
                isLoading = viewModel.isLoading.value,
                contentAfterLoading = {
                    Row(
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = {
                                    viewModel.updatePlayer(players[it])
                                    Toast
                                        .makeText(
                                            ctx,
                                            players[it].profile?.personaname,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            )
                            .fillMaxWidth()
                            .padding(Padding)

                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(players[it].profile?.avatarmedium),
                            contentDescription = null,
                            modifier = Modifier
                                .size(ShimmerWidth)
                                .clip(CircleShape)
                        )

                        TextName(players[it].profile?.personaname ?: "")
                        TextItem(players[it].mmr_estimate?.estimate?: "")
                        TextItem(players[it].solo_competitive_rank ?: "")
                        TextItem(players[it].competitive_rank ?: "")
                        TextItem(players[it].leaderboard_rank ?: "")

                        IconButton(modifier = Modifier.padding(start = Padding),
                            onClick = {
                                navigator.navigate(AddPlayerDestination(players[it].id!!))
                            }
                        ) {
                            Icon(Icons.Filled.Edit, "add player")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding)
            )
        }
    }
}


@Composable
fun TextItem(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .width(TextWidth)
            .padding(Padding),
        style = MaterialTheme.typography.caption
    )
}

@Composable
fun TextName(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .width(NameWidth)
            .padding(Padding),
        style = MaterialTheme.typography.caption
    )
}

@Preview
@Composable
fun PreviewPlayers() {
    DotaBuffTheme {
        //PlayersList()
    }
}