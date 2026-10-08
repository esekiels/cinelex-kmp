/*
 * Cinelex
 * Intents
 *
 * Created by Esekiel Surbakti on 30/09/26
 */

package co.esekiels.cinelex.core.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openUrl(url: String) {
	try {
		startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
	} catch (_: ActivityNotFoundException) {
	}
}
