package com.example.footballleague.source.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.footballleague.source.local.entity.FavoriteMatchEntity
import com.example.footballleague.source.local.entity.FavoriteTeamEntity
import com.example.footballleague.source.remote.DetailMatch
import org.jetbrains.anko.db.*

class FavoriteDatabaseOpenHelper(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {

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
        createTableFavoriteMatch(db)
        createTableFavoriteTeam(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteMatchEntity.TABLE_FAVORITE_MATCH, true)
        db.dropTable(FavoriteTeamEntity.TABLE_FAVORITE_TEAM, true)
    }

    private fun createTableFavoriteMatch(db: SQLiteDatabase) {
        with(FavoriteMatchEntity) {
            db.createTable(
                TABLE_FAVORITE_MATCH, true,
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

    fun addMatchToFavorite(match: DetailMatch) {
        instance.use {
            with(match) {
                insert(
                    FavoriteMatchEntity.TABLE_FAVORITE_MATCH,
                    FavoriteMatchEntity.ID_EVENT to idEvent,
                    FavoriteMatchEntity.EVENT_NAME to eventName,
                    FavoriteMatchEntity.HOME_TEAM_NAME to homeTeamName,
                    FavoriteMatchEntity.AWAY_TEAM_NAME to awayTeamName,
                    FavoriteMatchEntity.HOME_SCORE to homeScore,
                    FavoriteMatchEntity.AWAY_SCORE to awayScore,
                    FavoriteMatchEntity.TIME_EVENT to timeEvent,
                    FavoriteMatchEntity.DATE_EVENT to dateEvent,
                    FavoriteMatchEntity.ID_HOME_TEAM to idHomeTeam,
                    FavoriteMatchEntity.ID_AWAY_TEAM to idAwayTeam,
                    FavoriteMatchEntity.IS_NEXT_MATCH to if (isNextMatch) 1 else 0
                )
            }
        }
    }

    fun removeMatchFromFavorite(eventId: String) {
        instance.use {
            delete(
                FavoriteMatchEntity.TABLE_FAVORITE_MATCH,
                "(${FavoriteMatchEntity.ID_EVENT} = {idEvent})",
                "idEvent" to eventId
            )
        }
    }

    fun favoriteMatchState(eventId: String): Boolean {
        var isFavorite = false
        instance.use {
            val result = select(FavoriteMatchEntity.TABLE_FAVORITE_MATCH)
                .whereArgs(
                    "(${FavoriteMatchEntity.ID_EVENT} = {idEvent})",
                    "idEvent" to eventId
                )
            val favorite = result.parseList(classParser<FavoriteMatchEntity>())
            isFavorite = favorite.isNotEmpty()
        }

        return isFavorite
    }

    fun getFavoriteNextMatch(): List<FavoriteMatchEntity> {
        val favorite = mutableListOf<FavoriteMatchEntity>()

        instance.use {
            val result = select(FavoriteMatchEntity.TABLE_FAVORITE_MATCH)
                .whereArgs(
                    "${FavoriteMatchEntity.IS_NEXT_MATCH} == {isNextMatch}",
                    "isNextMatch" to 1
                )
            favorite.addAll(result.parseList(classParser()))
        }

        return favorite
    }

    fun getFavoriteLastMatch(): List<FavoriteMatchEntity> {
        val favorite = mutableListOf<FavoriteMatchEntity>()

        instance.use {
            val result = select(FavoriteMatchEntity.TABLE_FAVORITE_MATCH)
                .whereArgs(
                    "${FavoriteMatchEntity.IS_NEXT_MATCH} == {isNextMatch}",
                    "isNextMatch" to 0
                )
            favorite.addAll(result.parseList(classParser()))
        }

        return favorite
    }

    private fun createTableFavoriteTeam(db: SQLiteDatabase) {
        with(FavoriteTeamEntity) {
            db.createTable(
                TABLE_FAVORITE_TEAM, true,
                TEAM_ID to TEXT + PRIMARY_KEY,
                TEAM_NAME to TEXT,
                TEAM_DESC to TEXT,
                TEAM_BADGE to TEXT
            )
        }
    }

    fun addTeamToFavorite(team: FavoriteTeamEntity) {
        instance.use {
            with(team) {
                insert(
                    FavoriteTeamEntity.TABLE_FAVORITE_TEAM,
                    FavoriteTeamEntity.TEAM_ID to teamId,
                    FavoriteTeamEntity.TEAM_NAME to teamName,
                    FavoriteTeamEntity.TEAM_DESC to teamDesc,
                    FavoriteTeamEntity.TEAM_BADGE to teamBadge
                )
            }
        }
    }

    fun removeTeamFromFavorite(teamId: String) {
        instance.use {
            delete(
                FavoriteTeamEntity.TABLE_FAVORITE_TEAM,
                "(${FavoriteTeamEntity.TEAM_ID} = {teamId})",
                "teamId" to teamId
            )
        }
    }

    fun favoriteTeamState(teamId: String): Boolean {
        var isFavorite = false
        instance.use {
            val result = select(FavoriteTeamEntity.TABLE_FAVORITE_TEAM)
                .whereArgs(
                    "(${FavoriteTeamEntity.TEAM_ID} = {teamId})",
                    "teamId" to teamId
                )
            val favorite = result.parseList(classParser<FavoriteTeamEntity>())
            isFavorite = favorite.isNotEmpty()
        }

        return isFavorite
    }

    fun getFavoriteTeam(): List<FavoriteTeamEntity> {
        val favorite = mutableListOf<FavoriteTeamEntity>()

        instance.use {
            val result = select(FavoriteTeamEntity.TABLE_FAVORITE_TEAM)
            favorite.addAll(result.parseList(classParser()))
        }

        return favorite
    }
}

val Context.database: FavoriteDatabaseOpenHelper
    get() = FavoriteDatabaseOpenHelper.getInstance(applicationContext)