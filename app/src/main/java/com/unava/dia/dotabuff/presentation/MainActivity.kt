package com.unava.dia.dotabuff.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.unava.dia.dotabuff.presentation.features.activity.PlayersActivity
import com.unava.dia.dotabuff.presentation.features.addPlayer.AddPlayer
import com.unava.dia.dotabuff.presentation.features.players.Players

import com.unava.dia.dotabuff.presentation.ui.theme.DotaBuffTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DotaBuffTheme {
                val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenPlayers
                    ) {
                        composable<ScreenPlayers> {
                            Players(navController)
                        }
                        composable<ScreenPlayersActivity>{
                            val args = it.toRoute<ScreenPlayersActivity>()
                            PlayersActivity(args.steamId)
                        }
                        composable<ScreenAddPlayer> {
                            val args = it.toRoute<ScreenAddPlayer>()
                            AddPlayer(args.id, navController)
                        }
                }
            }
        }
    }
}

@Serializable
data class ScreenPlayersActivity(
    val steamId: String
)

@Serializable
object ScreenPlayers

@Serializable
data class ScreenAddPlayer(
    val id: Int
)