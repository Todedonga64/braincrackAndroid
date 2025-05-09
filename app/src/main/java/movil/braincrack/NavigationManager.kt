package movil.braincrack

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import movil.braincrack.gamemodes.LibreView
import movil.braincrack.gamemodes.SuddenDeathView
import movil.braincrack.topics.ChismesitoHistoricoView
import movil.braincrack.topics.DatoNautaView
import movil.braincrack.topics.ExactaManiacaView
import movil.braincrack.topics.GeobrainView
import movil.braincrack.topics.LombiletrasView

@Composable
fun NavigationManagerView() {

    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable(route = "start") {
            StartView(navController)
        }

        composable(route = "playername") {
            PlayerNameView(navController)
        }

        composable(route = "gamemodes/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            GameModesView(navController, name)
        }

        composable(route = "topics/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            TopicsView(navController, name)
        }

        composable(route = "suddendeath/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            SuddenDeathView(navController, name)
        }

        composable(route = "libre/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            LibreView(navController, name)
        }

        composable(route = "exactamaniaca/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            ExactaManiacaView(navController, name)
        }

        composable(route = "lombiletras/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            LombiletrasView(navController, name)
        }

        composable(route = "chismesito/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            ChismesitoHistoricoView(navController, name)
        }

        composable(route = "geobrain/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            GeobrainView(navController, name)
        }

        composable(route = "datonauta/{username}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("username") ?: ""
            DatoNautaView(navController, name)
        }
    }
}