package com.kanukim97.nearbyshare_basic

import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.kanukim97.nearbyshare_basic.feature.state.ShareState
import com.kanukim97.nearbyshare_basic.feature.state.TransferState
import com.kanukim97.nearbyshare_basic.utils.LogUtils.logD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    private val _shareUiState = MutableStateFlow<ShareState>(ShareState.IDLE)
    val shareState = _shareUiState.asStateFlow()

    private val _transferUiState = MutableStateFlow<TransferState>(TransferState.IDLE)
    val transferUiState = _transferUiState.asStateFlow()

    fun updateAdvertisingShareUiState() {
        if (_shareUiState.value == ShareState.Advertising) return
        if (_shareUiState.value == ShareState.Discovering) return

        _shareUiState.update { ShareState.Advertising }
    }


    fun updateDiscoveringShareUiState() {
        if (_shareUiState.value == ShareState.Advertising) return
        if (_shareUiState.value == ShareState.Discovering) return

        _shareUiState.update { ShareState.Discovering }
    }

    fun updateShareUiStateToIdle() {
        _shareUiState.update { ShareState.IDLE }
    }

    fun updateFailedShareUiState(e: Exception) {
        _shareUiState.update { ShareState.Failed(e) }
    }

    fun updateTransferState(transferUpdate: PayloadTransferUpdate) {
        when (transferUpdate.status) {
            PayloadTransferUpdate.Status.IN_PROGRESS -> {
                logD("onPayloadTransfer() : Payload Transfer is in Progress")
                _transferUiState.update {
                    TransferState.TransferInProgress(
                        calcTransferProgress(
                            transferUpdate.bytesTransferred,
                            transferUpdate.totalBytes
                        )
                    )
                }
            }
            PayloadTransferUpdate.Status.SUCCESS -> {
                logD("onPayloadTransfer() : Payload Transfer is in Success")
                _transferUiState.update { TransferState.TransferSuccess }
            }
            PayloadTransferUpdate.Status.FAILURE -> {
                logD("onPayloadTransfer() : Payload Transfer is Failed")
                _transferUiState.update { TransferState.TransferFailed }
            }
            PayloadTransferUpdate.Status.CANCELED -> {
                logD("onPayloadTransfer() : Payload Transfer is Canceled")
                _transferUiState.update { TransferState.TransferCanceled }
            }
        }
    }

    private fun calcTransferProgress(transferredBytes: Long, totalBytes: Long): Float {
        return (transferredBytes / totalBytes) * 100f
    }
}