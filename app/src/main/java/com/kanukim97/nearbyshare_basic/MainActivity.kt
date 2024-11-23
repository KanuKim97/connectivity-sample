package com.kanukim97.nearbyshare_basic

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.kanukim97.nearbyshare_basic.constant.Constant
import com.kanukim97.nearbyshare_basic.feature.NearByScreenRoute
import com.kanukim97.nearbyshare_basic.feature.state.ShareActions
import com.kanukim97.nearbyshare_basic.model.Endpoint
import com.kanukim97.nearbyshare_basic.utils.ConnectionOptions
import com.kanukim97.nearbyshare_basic.utils.LogUtils.logD
import com.kanukim97.nearbyshare_basic.utils.LogUtils.logE
import com.kanukim97.nearbyshare_basic.utils.LogUtils.logW

class MainActivity : ComponentActivity() {
    private val mEstablishConnection = HashMap<String, Endpoint>()
    private val mPendingConnection = HashMap<String, Endpoint>()
    private val mDiscoveredEndpoint = HashMap<String, Endpoint>()

    private lateinit var mConnectionsClient: ConnectionsClient

    private val mainViewModel by viewModels<MainViewModel>()

    private val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endPointId: String, connectionInfo: ConnectionInfo) {
            mPendingConnection[endPointId] = Endpoint(endPointId, connectionInfo.endpointName)
        }

        override fun onConnectionResult(endPointId: String, connectionResolution: ConnectionResolution) {
            // When Connection Result is Failed
            if (!connectionResolution.status.isSuccess) {
                mPendingConnection.remove(endPointId)
                return
            }

            mEstablishConnection[endPointId] = mPendingConnection.remove(endPointId) as Endpoint
        }

        override fun onDisconnected(endPointId: String) {
            // When Disconnect
            mEstablishConnection.remove(endPointId)
        }
    }

    private val mPayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endPointId: String, payload: Payload) {
            when (payload.type) {
                Payload.Type.FILE -> {
                    // Payload type is File

                    logD("onPayloadReceived() : Payload is File")
                }
                Payload.Type.STREAM -> {
                    // Payload type is Stream

                    logD("onPayloadReceived() : Payload is Stream")
                }
                Payload.Type.BYTES -> {
                    // Payload type is Bytes

                    logD("onPayloadReceived() : Payload is Bytes")
                }
            }
        }

        override fun onPayloadTransferUpdate(
            endPointId: String,
            transferUpdate: PayloadTransferUpdate
        ) { mainViewModel.updateTransferState(transferUpdate) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mConnectionsClient = Nearby.getConnectionsClient(this)

        setContent {
            val shareUiState by mainViewModel.shareState.collectAsState()
            val transferUiState by mainViewModel.transferUiState.collectAsState()

            val shareActions = remember {
                ShareActions(
                    onAdvertisingStart = { startAdvertising() },
                    onAdvertisingStop = { stopAdvertising() },
                    onDiscoveringStart = { startDiscovering() },
                    onDiscoveringStop = { stopDiscovering() },
                    onSendPayload = {},
                    onAcceptConnection = {},
                    onRejectConnection = {}
                )
            }

            NearByScreenRoute(shareUiState, transferUiState, shareActions)
        }
    }


    private fun startAdvertising() {
        mConnectionsClient.startAdvertising(
            "YOUR_ENDPOIT_NAME",
            Constant.SERVICE_ID,
            mConnectionLifecycleCallback,
            ConnectionOptions.advertisingOptions
        ).addOnSuccessListener {
            logD("Advertising Success")

            mainViewModel.updateAdvertisingShareUiState()
        }.addOnFailureListener {
            logW("Advertising Failed", it)

            mainViewModel.updateFailedShareUiState(it)
        }
    }

    private fun stopAdvertising() {
        mConnectionsClient.stopAdvertising()
        mainViewModel.updateShareUiStateToIdle()
    }

    private fun startDiscovering() {
        mConnectionsClient.startDiscovery(
            "YOUR_ENDPOIT_NAME",
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(
                    endpoint: String,
                    discoveredEndpointInfo: DiscoveredEndpointInfo
                ) {

                }

                override fun onEndpointLost(endpointName: String) {

                }
            },
            ConnectionOptions.discoveryOptions
        ).addOnSuccessListener {
            logD("Discovering Success")

            mainViewModel.updateDiscoveringShareUiState()
        }.addOnFailureListener {
            logW("Discovering Failed", it)

            mainViewModel.updateFailedShareUiState(it)
        }
    }

    private fun stopDiscovering() {
        mConnectionsClient.stopDiscovery()
        mainViewModel.updateShareUiStateToIdle()
    }

    private fun stopAllEndpoints() {
        mConnectionsClient.stopAllEndpoints()

        mEstablishConnection.clear()
        mDiscoveredEndpoint.clear()
        mPendingConnection.clear()
    }

    private fun acceptConnection(endpoint: Endpoint) {
        mConnectionsClient
            .acceptConnection(endpoint.id, mPayloadCallback)
            .addOnFailureListener { exception ->
                exception.printStackTrace()

                logE("acceptConnection: Failed", exception)
            }
    }

    private fun rejectConnection(endpoint: Endpoint) {
        mConnectionsClient
            .rejectConnection(endpoint.id)
            .addOnFailureListener { exception ->
                exception.printStackTrace()

                logE("rejectConnection: Failed", exception)
            }
    }
}

