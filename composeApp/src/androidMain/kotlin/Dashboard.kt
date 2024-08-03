import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.net.PlacesClient
import swifties.testapp.R

@Composable
fun DashboardScreen(
    prefs: SharedPreferences,
    placesClient: PlacesClient,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GradientImageView(resourceId = R.drawable.dashboard_page)
        ScheduleScreen(prefs, placesClient, navController)
    }
}

@Composable
fun ScheduleScreen(
    prefs: SharedPreferences,
    placesClient: PlacesClient,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val showPopupState = remember { mutableStateOf(false) }
        val showPopup by showPopupState

        // "Today" header and list
        AnimatedVisibility(
            visible = !showPopup,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            RemindersList()
        }

        // Column of spacer and the popup menu
        Column {
            val animatedHeight by animateDpAsState(
                targetValue = if (showPopup) 40.dp else 680.dp
            )

            // Large space keeps menu minimized
            Spacer(modifier = Modifier.height(animatedHeight))

            // Box allows overlapping the
            NewAlertPopup(prefs, placesClient, navController, showPopupState)
        }
    }
}

@Composable
private fun RemindersList() {
    Column(modifier = Modifier.padding(15.dp)) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Today",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alignByBaseline()
            )
        }
        HorizontalDivider(
            color = Color.White,
            thickness = 1.dp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "No new reminders",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
private fun NewAlertPopup(
    prefs: SharedPreferences,
    placesClient: PlacesClient,
    navController: NavController,
    showPopupState: MutableState<Boolean>
) {
    var showPopup by showPopupState

    Box(
        modifier = Modifier
            .shadow(0.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
                .clip(RoundedCornerShape(topStart = 20.dp))
                .background(Color(235, 235, 235))
        ) {
            CreateAlertScreen(prefs, placesClient, navController)
        }
        Row(
            modifier = Modifier
                .shadow(0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = {
                    showPopup = !showPopup
                },
                modifier = Modifier
                    .size(64.dp)
                    .border(6.dp, Color.White, CircleShape),
                colors = buttonColors(Color.White),
                contentPadding = PaddingValues(6.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                shape = CircleShape,
            ) {
                val icon = painterResource(R.drawable.plus_aqua)
                Icon(
                    painter = icon,
                    tint = Color(0xFF009a88),
                    contentDescription = "Add Task",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
