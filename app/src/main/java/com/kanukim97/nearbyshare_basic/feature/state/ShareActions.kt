package com.kanukim97.nearbyshare_basic.feature.state

import androidx.compose.runtime.Immutable

/**
 * 2024.11.09
 *
 * ShareActions
 *
 * @property onAdvertisingStart Advertising Start Event
 * @property onAdvertisingStop Advertising Stop Event
 * @property onDiscoveringStart Discovering Start Event
 * @property onDiscoveringStop Discovering Stop Event
 * @property onSendPayload Send Payload Event
 * @property onAcceptConnection Connection Accept Event
 * @property onRejectConnection Connection Reject Event
 *
 * @author KanuKin97
 */
@Immutable
data class ShareActions(
    val onAdvertisingStart: () -> Unit,
    val onAdvertisingStop: () -> Unit,
    val onDiscoveringStart: () -> Unit,
    val onDiscoveringStop: () -> Unit,
    val onSendPayload: () -> Unit,
    val onAcceptConnection: () -> Unit,
    val onRejectConnection: () -> Unit
)