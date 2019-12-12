package com.example.footballleague.source.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.footballleague.source.local.entity.FavoriteEntity
import com.example.footballleague.source.remote.DetailMatch
import org.jetbrains.anko.db.*

class FavoriteDatabaseOpenHelper(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "FavoriteMatch.db", null, 1) {

    companion object {
        private lateinit var instance: FavoriteDatabaseOpenHelper

        @Synchronized
        fun getInstance(ctx: Context): FavoriteDatabaseOpenHelper {
            if (!::instance.isInitialized) {
                instance = FavoriteDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        with(FavoriteEntity) {
            db.createTable(
                TABLE_FAVORITE, true,
                ID_EVENT to TEXT + PRIMARY_KEY,
                EVENT_NAME to TEXT,
                HOME_TEAM_NAME to TEXT,
                AWAY_TEAM_NAME to TEXT,
                HOME_SCORE to TEXT,
                AWAY_SCORE to TEXT,
                TIME_EVENT to TEXT,
                DATE_EVENT to TEXT,
                ID_HOME_TEAM to TEXT,
                ID_AWAY_TEAM to TEXT,
                IS_NEXT_MATCH to INTEGER
            )
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteEntity.TABLE_FAVORITE, true)
    }

    fun addToFavorite(match: DetailMatch) {
        instance.use {
            with(match) {
                insert(
                    FavoriteEntity.TABLE_FAVORITE,
                    FavoriteEntity.ID_EVENT to idEvent,
                    FavoriteEntity.EVENT_NAME to eventName,
                    FavoriteEntity.HOME_TEAM_NAME to homeTeamName,
                    FavoriteEntity.AWAY_TEAM_NAME to awayTeamName,
                    FavoriteEntity.HOME_SCORE to homeScore,
                    FavoriteEntity.AWAY_SCORE to awayScore,
                    FavoriteEntity.TIME_EVENT to timeEvent,
                    FavoriteEntity.DATE_EVENT to dateEvent,
                    FavoriteEntity.ID_HOME_TEAM to idHomeTeam,
                    FavoriteEntity.ID_AWAY_TEAM to idAwayTeam,
                    FavoriteEntity.IS_NEXT_MATCH to if (isNextMatch) 1 else 0
                )
            }
        }
    }

    fun removeFromFavorite(eventId: String) {
        instance.use {
            delete(
                FavoriteEntity.TABLE_FAVORITE,
                "(${FavoriteEntity.ID_EVENT} = {idEvent})",
                "idEvent" to eventId
            )
        }
    }

    fun favoriteState(eventId: String): Boolean {
        var isFavorite = false
        instance.use {
            val result = select(FavoriteEntity.TABLE_FAVORITE)
                .whereArgs(
                    "(${FavoriteEntity.ID_EVENT} = {idEvent})",
                    "idEvent" to eventId
                )
            val favorite = result.parseList(classParser<FavoriteEntity>())
            isFavorite = favorite.isNotEmpty()
        }

        return isFavorite
    }

    fun getFavoriteNextMatch(): List<FavoriteEntity> {
        val favorite = mutableListOf<FavoriteEntity>()

        instance.use {
            val result = select(FavoriteEntity.TABLE_FAVORITE)
                .whereArgs(
                    "${FavoriteEntity.IS_NEXT_MATCH} == {isNextMatch}",
                    "isNextMatch" to 1
                )
            favorite.addAll(result.parseList(classParser()))
        }

        return favorite
    }

    fun getFavoriteLastMatch(): List<FavoriteEntity> {
        val favorite = mutableListOf<FavoriteEntity>()

        instance.use {
            val result = select(FavoriteEntity.TABLE_FAVORITE)
                .whereArgs(
                    "${FavoriteEntity.IS_NEXT_MATCH} == {isNextMatch}",
                    "isNextMatch" to 0
                )
            favorite.addAll(result.parseList(classParser()))
        }

        return favorite
    }
}

val Context.database: FavoriteDatabaseOpenHelper
    get() = FavoriteDatabaseOpenHelper.getInstance(applicationContext)