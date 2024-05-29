@file:OptIn(ExperimentalMaterialApi::class)

package com.unava.dia.dotabuff.presentation.features.players

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.presentation.ScreenAddPlayer
import com.unava.dia.dotabuff.presentation.ScreenPlayersActivity
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.NameWidth
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.Padding
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.ShimmerWidth
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.TextWidth

@Composable
fun Players(
    navController: NavHostController
) {
    val viewModel: PlayersViewModel = hiltViewModel()

    PlayersList(
        viewModel,
        onScreenAddPlayer = {
            navController.navigate(ScreenAddPlayer(it))
        },
        onScreenPlayersActivity = {
            navController.navigate(ScreenPlayersActivity(it))
        },
        onDeletePlayer = {
            viewModel.dispatch(PlayersViewModel.Action.DeletePlayer(it))
        }
    )
}

@Composable
fun PlayersList(
    viewModel: PlayersViewModel,
    onScreenAddPlayer: (Int) -> Unit,
    onScreenPlayersActivity: (String) -> Unit,
    onDeletePlayer: ((AccInformation) -> Unit)?
) {
    Column(Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextName("Avatar \nName")
            TextItem("Estim")
            TextItem("Rank")
            IconButton(
                modifier = Modifier.padding(start = Padding),
                onClick = { onScreenAddPlayer(-1) }
            ) {
                Icon(Icons.Filled.Add, "add player")
            }
        }
        FillList(viewModel, onScreenAddPlayer, onScreenPlayersActivity, onDeletePlayer)
    }
}

@Composable
fun FillList(
    viewModel: PlayersViewModel,
    onScreenAddPlayer: (Int) -> Unit,
    onScreenPlayersActivity: (String) -> Unit,
    onDeletePlayer: ((AccInformation) -> Unit)?
) {

    when (val state = viewModel.state.value) {
        PlayersViewModel.State.START -> {
            Text("start")
            viewModel.dispatch(PlayersViewModel.Action.GetPlayersList)
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
                List(
                    players,
                    viewModel.isLoading.value,
                    onDeletePlayer = { currentPlayer ->
                        onDeletePlayer?.invoke(currentPlayer)
                    },
                    onScreenAddPlayer,
                    onScreenPlayersActivity
                )
        }
    }
}

@Composable
fun List(
    players: List<AccInformation>,
    isLoading: Boolean,
    onDeletePlayer: ((AccInformation) -> Unit)? = null,
    onScreenAddPlayer: (Int) -> Unit,
    onScreenPlayersActivity: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = players,
            key = { player -> player.id!! }, // Return a stable + unique key for the item
            itemContent = { player ->
                val currentPlayer by rememberUpdatedState(player)

                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            onDeletePlayer?.invoke(currentPlayer)
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
                                .padding(8.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }

                    },
                    dismissContent = {
                        RowContent(
                            modifier = Modifier.fillMaxWidth(),
                            isLoading,
                            player,
                            onScreenAddPlayer,
                            onScreenPlayersActivity
                        )
                    },
                    directions = setOf(DismissDirection.EndToStart)
                )
                Divider()
            }
        )
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
fun RowContent(
    modifier: Modifier,
    isLoading: Boolean,
    player: AccInformation,
    onScreenAddPlayer: (Int) -> Unit,
    onScreenPlayersActivity: (String) -> Unit
) {
    val ctx = LocalContext.current

    ShimmerListItem(
        isLoading,
        contentAfterLoading = {
            Row(
                modifier = modifier.fillMaxWidth()
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
                    .padding(0.dp, Padding, Padding, Padding)

            ) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(player.profile?.avatarmedium),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(Padding, 0.dp)
                            .size(ShimmerWidth)
                            .clip(CircleShape)
                    )
                    TextName(player.profile?.personaname ?: "")
                }
                TextItem(player.mmr_estimate?.estimate ?: "")
                TextItem(player.leaderboard_rank ?: "")

                Column {
                    IconButton(modifier = Modifier
                        .padding(start = Padding),
                        onClick = {
                            var steamid = "-1"
                            if (player.profile?.steamid != null) {
                                steamid = player.profile?.steamid!!
                            }
                            onScreenPlayersActivity(steamid)
                        }) {
                        Icon(Icons.Filled.AccountCircle, "activity")
                    }

                    IconButton(modifier = Modifier
                        .padding(start = Padding),
                        onClick = {
                            onScreenAddPlayer(player.id!!)
                        }
                    ) {
                        Icon(Icons.Filled.Edit, "add player")
                    }

                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding)
    )
}