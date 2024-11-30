package com.kanukim97.nearbyshare_basic

import android.os.Bundle
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
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val mEstablishConnection = HashMap<String, Endpoint>()
    private val mPendingConnection = HashMap<String, Endpoint>()
    private val mDiscoveredEndpoint = HashMap<String, Endpoint>()

    private lateinit var mConnectionsClient: ConnectionsClient

    private val mainViewModel by viewModels<MainViewModel>()

    private val myLocalEndPointName by lazy { getMyLocalName() }

    private val mConnectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endPointId: String, connectionInfo: ConnectionInfo) {
            val connectionEndpoint = Endpoint(endPointId, connectionInfo.endpointName)

            logD("onConnectionInit() - $endPointId, ${connectionInfo.endpointName}")

            mPendingConnection[endPointId] = connectionEndpoint
            acceptConnection(connectionEndpoint)
        }

        override fun onConnectionResult(endPointId: String, connectionResolution: ConnectionResolution) {
            // When Connection Result is Failed
            if (!connectionResolution.status.isSuccess) {
                mPendingConnection.remove(endPointId)
                return
            }

            logD("onConnectionResult() - $endPointId, ${connectionResolution.status}")

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
        ) {
            mainViewModel.updateTransferState(transferUpdate)
        }
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
            myLocalEndPointName,
            Constant.SERVICE_ID,
            mConnectionLifecycleCallback,
            ConnectionOptions.advertisingOptions
        ).addOnSuccessListener {
            logD("Advertising Success - Now Advertising EndPoint Name: $myLocalEndPointName")

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
            Constant.SERVICE_ID,
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(
                    endpointId: String,
                    discoveredEndpointInfo: DiscoveredEndpointInfo
                ) {
                    logD("onEndpoint Found: $endpointId: ${discoveredEndpointInfo.serviceId}, ${discoveredEndpointInfo.endpointName}")

                    requestConnection(endpointId)
                }

                override fun onEndpointLost(endpointId: String) {
                    logD("onEndpointLost : $endpointId")
                }
            },
            ConnectionOptions.discoveryOptions
        ).addOnSuccessListener {
            mainViewModel.updateDiscoveringShareUiState()
        }.addOnFailureListener {
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

    private fun requestConnection(endpointId: String) {
        mConnectionsClient.requestConnection(
            myLocalEndPointName,
            endpointId,
            mConnectionLifecycleCallback
        ).addOnSuccessListener {
            logD("SUCCESS")
        }.addOnFailureListener {
            logE("Failed Request Connection", it)
        }
    }


    private fun acceptConnection(endpoint: Endpoint) {
        mConnectionsClient
            .acceptConnection(endpoint.id, mPayloadCallback)
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                logE("acceptConnection() - Failed", exception)
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

    private fun getMyLocalName(): String = Random.nextInt(10000).toString()
}

