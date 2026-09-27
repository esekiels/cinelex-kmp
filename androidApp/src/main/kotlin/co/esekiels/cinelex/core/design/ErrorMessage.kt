/*
 * Cinelex
 * ErrorMessage
 *
 * Created by Esekiel Surbakti on 27/09/26
 */

package co.esekiels.cinelex.core.design

import androidx.annotation.StringRes
import co.esekiels.cinelex.R
import co.esekiels.cinelex.common.ErrorConstants

@StringRes
fun errorMessage(code: String): Int =
	when (code) {
		ErrorConstants.NETWORK_ERROR -> R.string.error_network
		ErrorConstants.HTTP_UNAUTHORIZED -> R.string.error_unauthorized
		ErrorConstants.HTTP_TIMEOUT -> R.string.error_timeout
		ErrorConstants.HTTP_FORBIDDEN -> R.string.error_forbidden
		else -> R.string.error_unknown
	}
