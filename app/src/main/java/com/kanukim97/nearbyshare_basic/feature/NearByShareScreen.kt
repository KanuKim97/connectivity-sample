package com.kanukim97.nearbyshare_basic.feature

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kanukim97.nearbyshare_basic.constant.Constant
import com.kanukim97.nearbyshare_basic.feature.state.ShareActions
import com.kanukim97.nearbyshare_basic.feature.state.ShareState
import com.kanukim97.nearbyshare_basic.feature.state.TransferState
import com.kanukim97.nearbyshare_basic.ui.theme.NearByShare_BasicTheme
import com.kanukim97.nearbyshare_basic.ui.theme.Shapes

@Composable
fun NearByScreenRoute(
    shareUiState: ShareState,
    transferUiState: TransferState,
    shareActions: ShareActions
) {
    val rememberPermissionsContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {  }
    )

    LaunchedEffect(key1 = Unit) {
        rememberPermissionsContract.launch(
            arrayOf(
                Constant.BLUETOOTH_PERMISSION,
                Constant.BLUETOOTH_SCAN_PERMISSION,
                Constant.BLUETOOTH_CONNECT_PERMISSION,
                Constant.BLUETOOTH_ADVERTISE_PERMISSION,
                Constant.NEARBY_WIFI_DEVICES_PERMISSION,
                Constant.ACCESS_FINE_LOCATION_PERMISSION,
                Constant.ACCESS_COARSE_LOCATION_PERMISSION
            )
        )
    }


    NearByScreen(
        shareUiState = shareUiState,
        transferUiState = transferUiState,
        shareActions = shareActions
    )
}

@Composable
fun NearByScreen(
    shareUiState: ShareState,
    transferUiState: TransferState,
    shareActions: ShareActions,
    modifier: Modifier = Modifier
) {
    var isAdvertising by remember { mutableStateOf(false) }
    var isDiscovering by remember { mutableStateOf(false) }

    DisposableEffect(key1 = isAdvertising) {
        if (isAdvertising) {
            shareActions.onAdvertisingStart()
        } else {
            shareActions.onAdvertisingStop()
        }

        onDispose { shareActions.onAdvertisingStop() }
    }

    DisposableEffect(key1 = isDiscovering) {
        if (isDiscovering) {
            shareActions.onDiscoveringStart()
        } else {
            shareActions.onDiscoveringStop()
        }

        onDispose { shareActions.onDiscoveringStop() }
    }

    NearByShare_BasicTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            floatingActionButton = {
                Button(onClick = shareActions.onSendPayload) {
                    Text("Send", color = Color.Transparent)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MenuItems(
                    text = "Start Advertising",
                    checked = shareUiState == ShareState.Advertising,
                    onChecked = { isAdvertising = !isAdvertising }
                )

                HorizontalDivider()

                MenuItems(
                    text = "Start Discovering",
                    checked = shareUiState == ShareState.Discovering,
                    onChecked = { isDiscovering = !isDiscovering }
                )
                AnimatedVisibility(
                    visible = shareUiState == ShareState.Discovering,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = Shapes.large,
                        color = MaterialTheme.colorScheme.background
                    ) {


                    }
                }
            }
        }
    }
}


@Composable
fun MenuItems(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onChecked: ((Boolean) -> Unit)? = {  },
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )

        Switch(
            checked = checked,
            onCheckedChange = onChecked
        )
    }
}