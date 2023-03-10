package com.unava.dia.dotabuff.presentation.features.addPlayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.unava.dia.dotabuff.domain.model.AccInformation
import com.unava.dia.dotabuff.presentation.features.destinations.PlayersDestination
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens
import com.unava.dia.dotabuff.presentation.ui.theme.Dimens.Small

@Destination
@Composable
// Parcelable, Serializable, Enum and classes annotated with
// @kotlinx.serialization.Serializable (as well as Arrays and ArrayLists of these) work out of the box!
fun AddPlayer(
    id: Int,
    navigator: DestinationsNavigator,
) {
    val viewModel: AddPlayerViewModel = hiltViewModel()

    when (val state = viewModel.state.value) {
        AddPlayerViewModel.State.START -> {
            if (id != -1) {
                viewModel.dispatch(AddPlayerViewModel.Action.LoadPlayer(id))
            } else {
                PlayerProfile(id, null, navigator = navigator, viewModel = viewModel)
            }
        }
        AddPlayerViewModel.State.LOADING -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        is AddPlayerViewModel.State.FAILURE -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                val message = state.message
                Text(text = message, fontSize = 16.sp)
            }
        }
        is AddPlayerViewModel.State.SUCCESS -> {
            val player = state.player
            PlayerProfile(id, player, navigator = navigator, viewModel = viewModel)
        }
    }

}

@Composable
fun PlayerProfile(
    id: Int,
    player: AccInformation?,
    navigator: DestinationsNavigator,
    viewModel: AddPlayerViewModel,
) {
    //Text(text = id.toString(), fontSize = 16.sp)
    Row(
        Modifier
            .wrapContentHeight()
            .padding(Dimens.Large),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Player(player)
        }
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.playerId,
                label = { Text("Profile id", style = MaterialTheme.typography.caption) },
                onValueChange = { viewModel.playerId = it },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.dispatch(AddPlayerViewModel.Action.FetchPlayer)
                    }
                )
            )

            OutlinedButton(
                modifier = Modifier
                    .padding(Small)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .fillMaxWidth(),
                onClick = { viewModel.dispatch(AddPlayerViewModel.Action.FetchPlayer) }
            ) {
                Text("Search player",
                    modifier = Modifier.padding(Small),
                    style = MaterialTheme.typography.subtitle2)
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(Small)
                    .fillMaxWidth(),
                onClick = {
                    player?.let { viewModel.dispatch(AddPlayerViewModel.Action.AddPlayer(it)) }
                    navigator.navigate(PlayersDestination())
                }
            ) {
                Text("Submit",
                    modifier = Modifier.padding(Small),
                    style = MaterialTheme.typography.subtitle2)
            }
            OutlinedButton(
                modifier = Modifier
                    .padding(Small)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.dispatch(AddPlayerViewModel.Action.DeletePlayer(id))
                    navigator.navigate(PlayersDestination())
                }
            ) {
                Text("Delete player",
                    modifier = Modifier.padding(Small),
                    style = MaterialTheme.typography.subtitle2)
            }
        }
    }
}

@Composable
fun Player(player: AccInformation?) {
    Text(
        player?.profile?.personaname ?: "Nickname",
        modifier = Modifier.padding(Dimens.Padding),
        style = MaterialTheme.typography.caption
    )
    Image(
        painter = rememberAsyncImagePainter(player?.profile?.avatarmedium),
        contentDescription = null,
        modifier = Modifier
            .size(Dimens.IconSize)
            .padding(Dimens.Padding)
            .clip(CircleShape)
    )
    Text(
        "Rank: " + player?.leaderboard_rank ?: "",
        modifier = Modifier.padding(Dimens.Padding),
        style = MaterialTheme.typography.caption
    )
    Text(
        "Estimated mmr: " + player?.mmr_estimate?.estimate ?: "",
        modifier = Modifier.padding(Dimens.Padding),
        style = MaterialTheme.typography.caption
    )
}