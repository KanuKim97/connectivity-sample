package com.kanukim97.nearbyshare_basic.feature.state

import androidx.compose.runtime.Stable

/**
 * 2024.11.09
 *
 * ## ShareState
 *
 * This interface is using on NearByShareScreen
 *
 * @author KanuKim97
 */
@Stable
sealed interface ShareState {
    data object IDLE: ShareState

    data object Advertising: ShareState

    data object Discovering: ShareState

    data class Failed(val e: Exception): ShareState
}


/**
 * 2024.11.09
 *
 * ## TransferState
 *
 * This interface is show payload Transfer state
 *
 * @property TransferInProgress Payload Transfer State is in progress
 * @property TransferSuccess Payload Transfer State is Success
 * @property TransferFailed Payload Transfer State is Failed
 * @property TransferCanceled Payload Transfer State is Canceled
 *
 * @author KanuKim97
 */
@Stable
sealed interface TransferState {
    data object IDLE: TransferState

    data class TransferInProgress(val progress: Float): TransferState

    data object TransferSuccess: TransferState

    data object TransferFailed: TransferState

    data object TransferCanceled: TransferState
}