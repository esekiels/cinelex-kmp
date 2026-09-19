/*
 * Cinelex
 * DatabaseFactory
 *
 * Created by Esekiel Surbakti on 19/09/26
 */

package co.esekiels.cinelex.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun databaseBuilder(context: Context): RoomDatabase.Builder<CinelexDatabase> =
	Room.databaseBuilder<CinelexDatabase>(
		context = context.applicationContext,
		name = context.getDatabasePath(DATABASE_NAME).absolutePath,
	)
