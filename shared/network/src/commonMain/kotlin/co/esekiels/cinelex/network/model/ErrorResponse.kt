/*
 * Cinelex
 * ErrorResponse
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("status_code")
    val code: Int,
    @SerialName("status_message")
    val message: String,
)
