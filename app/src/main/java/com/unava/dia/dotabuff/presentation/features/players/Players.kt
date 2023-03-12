@file:OptIn(ExperimentalMaterialApi::class)

package com.unava.dia.dotabuff.presentation.features.players

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unava.dia.dotabuff.domain.model.AccInformation
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
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            PlayersList(navigator, configuration.orientation)
        }
        else -> {
            PlayersListLandscape(navigator, configuration.orientation)
        }
    }
}

@Composable
fun PlayersListLandscape(navigator: DestinationsNavigator, orientation: Int) {
    Column(Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextName("Avatar \nName")
            TextItem("Estim \nRank")
            TextItem("Solo \nParty")
            IconButton(
                modifier = Modifier.padding(start = Padding),
                onClick = { navigator.navigate(AddPlayerDestination(-1)) }
            ) {
                Icon(Icons.Filled.Add, "add player")
            }
        }
        FillList(navigator, orientation)
    }
}

@Composable
fun PlayersList(navigator: DestinationsNavigator, orientation: Int) {
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

        FillList(navigator, orientation)

    }
}

@Composable
fun FillList(navigator: DestinationsNavigator, orientation: Int) {
    val viewModel: PlayersViewModel = hiltViewModel()

    when (val state = viewModel.state.value) {
        PlayersViewModel.State.START -> {
            Text("start")
        }
        PlayersViewModel.State.LOADING -> {
            CircularProgressIndicator()
        }
        is PlayersViewModel.State.FAILURE -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                val message = (state).message
                Text(text = message, fontSize = 16.sp)
            }
        }
        is PlayersViewModel.State.SUCCESS -> {
            val players = state.players
            if (players.isEmpty()) Text("list is empty")
            else
                List(players, navigator, viewModel, orientation)
        }
    }
    PlayersViewModel.Action.GetPlayersList
}

@Composable
fun List(
    players: List<AccInformation>,
    navigator: DestinationsNavigator,
    viewModel: PlayersViewModel,
    orientation: Int,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(players.size) {
            val currentPlayer by rememberUpdatedState(players[it])

            val dismissState = rememberDismissState(
                confirmStateChange = { it ->
                    if (it == DismissValue.DismissedToStart) {
                        viewModel.dispatch(PlayersViewModel.Action.DeletePlayer(currentPlayer))
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                background = {
                    val color = when (dismissState.dismissDirection) {
                        DismissDirection.StartToEnd -> Color.Transparent
                        DismissDirection.EndToStart -> Color.Red
                        null -> Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                },
                dismissContent = {
                    when (orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            RowContent(viewModel, players[it], navigator)
                        }
                        else -> {
                            RowContentLandscape(viewModel, players[it], navigator)
                        }
                    }
                },
                directions = setOf(DismissDirection.EndToStart)
            )
            Divider()
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

@Composable
fun RowContentLandscape(
    viewModel: PlayersViewModel,
    player: AccInformation,
    navigator: DestinationsNavigator,
) {
    val ctx = LocalContext.current

    ShimmerListItem(
        isLoading = viewModel.isLoading.value,
        contentAfterLoading = {
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            PlayersViewModel.Action.UpdatePlayer(player)
                            Toast
                                .makeText(
                                    ctx,
                                    player.profile?.personaname,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    )
                    .fillMaxWidth()
                    .padding(Padding)

            ) {
                Column {
                    TextName(player.profile?.personaname ?: "")
                    Image(
                        painter = rememberAsyncImagePainter(player.profile?.avatarmedium),
                        contentDescription = null,
                        modifier = Modifier
                            .size(ShimmerWidth)
                            .clip(CircleShape)
                    )
                }
                Column {
                    TextItem(player.mmr_estimate?.estimate ?: "")
                    TextItem(player.leaderboard_rank ?: "")
                }
                Column {
                    TextItem(player.solo_competitive_rank ?: "")
                    TextItem(player.competitive_rank ?: "")
                }

                IconButton(modifier = Modifier
                    .padding(start = Padding)
                    .align(Alignment.CenterVertically),
                    onClick = {
                        navigator.navigate(AddPlayerDestination(player.id!!))
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


@Composable
fun RowContent(
    viewModel: PlayersViewModel,
    player: AccInformation,
    navigator: DestinationsNavigator,
) {
    val ctx = LocalContext.current

    ShimmerListItem(
        isLoading = viewModel.isLoading.value,
        contentAfterLoading = {
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = true,
                        onClick = {
                            PlayersViewModel.Action.UpdatePlayer(player)
                            Toast
                                .makeText(
                                    ctx,
                                    player.profile?.personaname,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    )
                    .fillMaxWidth()
                    .padding(Padding)

            ) {
                Image(
                    painter = rememberAsyncImagePainter(player.profile?.avatarmedium),
                    contentDescription = null,
                    modifier = Modifier
                        .size(ShimmerWidth)
                        .clip(CircleShape)
                )

                TextName(player.profile?.personaname ?: "")
                TextItem(player.mmr_estimate?.estimate ?: "")
                TextItem(player.solo_competitive_rank ?: "")
                TextItem(player.competitive_rank ?: "")
                TextItem(player.leaderboard_rank ?: "")

                IconButton(modifier = Modifier.padding(start = Padding),
                    onClick = {
                        navigator.navigate(AddPlayerDestination(player.id!!))
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

@Preview
@Composable
fun PreviewPlayers() {
    DotaBuffTheme {
        //PlayersList()
    }
}