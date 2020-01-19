package com.example.footballleague.source

import com.example.footballleague.source.remote.*

object DataDummy {
    val leagueDetail = LeagueDetail(
        "English Premier League",
        "The Premier League (often referred to as the English Premier League (EPL) outside England), is the top level of the English football league system. Contested by 20 clubs, it operates on a system of promotion and relegation with the English Football League (EFL).",
        "1992",
        "England",
        "https://www.thesportsdb.com/images/media/league/badge/i6o0kh1549879062.png",
        "https://www.thesportsdb.com/images/media/league/banner/4m3g4s1520767740.jpg",
        "https://www.thesportsdb.com/images/media/league/fanart/o9c14r1547554186.jpg",
        "www.premierleague.com",
        "en-gb.facebook.com/premierleague/",
        "twitter.com/premierleague",
        "www.youtube.com/channel/UCG5qGWdu8nIRZqJ_GgDwQ-w",
        "https://www.thesportsdb.com/images/media/league/trophy/yrywtr1422246014.png"
    )

    val matchList = listOf(
        Match(
            "672158",
            "Man United vs Colchester",
            "Man United",
            "Colchester",
            "3",
            "0",
            "20:00:00",
            "2019-12-18",
            "133612",
            "133874"

        ),
        Match(
            "672159",
            "Oxford Utd vs Man City",
            "Oxford Utd",
            "Man City",
            "1",
            "3",
            "19:45:00",
            "2019-12-18",
            "134361",
            "133613"
        )
    )

    val detailMatch = DetailMatch(
        "672158",
        "Man United vs Colchester",
        "Man United",
        "Colchester",
        "3",
        "0",
        "20:00:00",
        "2019-12-18",
        "133612",
        "133874",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    val SearchTeams = listOf(
        Team(
            "133604",
            "Arsenal",
            "Arsenal Football Club is a professional football club based in Holloway, London which currently plays in the Premier League, the highest level of English football.",
            "https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png"
        ),
        Team(
            "133612",
            "Man United",
            "Manchester United Football Club is an English professional football club, based in Old Trafford, Greater Manchester that plays in the Premier League.",
            "https://www.thesportsdb.com/images/media/team/badge/xzqdr11517660252.png"
        )
    )

    val standings = listOf(
        Standings(
            "Liverpool",
            20,
            19,
            1,
            0,
            38,
            58,
            "133602"
        ),
        Standings(
            "Leicester",
            21,
            14,
            3,
            4,
            35,
            45,
            "133626"
        )
    )

    val teamDetail = TeamDetail(
        "133612",
        "Man United",
        "Manchester United Football Club is an English professional football club, based in Old Trafford, Greater Manchester that plays in the Premier League.",
        "https://www.thesportsdb.com/images/media/team/badge/xzqdr11517660252.png",
        "Manchester United, Red Devils",
        "Old Trafford",
        "76962",
        "Trafford, Greater Manchester"
    )

    val teams = listOf(
        Team(
            "133604",
            "Arsenal",
            "Arsenal Football Club is a professional football club based in Holloway, London which currently plays in the Premier League, the highest level of English football.",
            "https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png"
        ),
        Team(
            "133612",
            "Man United",
            "Manchester United Football Club is an English professional football club, based in Old Trafford, Greater Manchester that plays in the Premier League.",
            "https://www.thesportsdb.com/images/media/team/badge/xzqdr11517660252.png"
        )
    )
}