package com.example.footballleague.ui.matchlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.Match
import com.example.footballleague.source.remote.MatchResponse
import com.example.footballleague.utils.LiveDataTestUtil
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MatchListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: LeagueDetailMatchViewModel
    private val matchListDummy = DataDummy.matchList
    private val leagueId = "4570"

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = LeagueDetailMatchViewModel(gson, apiRepository)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
    }

    @Test
    fun getLastMatchList() = testCoroutineScope.runBlockingTest {
        val lastMatchResponse = MatchResponse(matchListDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(lastMatchResponse.toString())
        Mockito.`when`(gson.fromJson(lastMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(lastMatchResponse)

        viewModel.getLastMatchList(leagueId)

        val lastMatchList = LiveDataTestUtil.getValue(viewModel.lastMatchList)
        val isShowLoadingLastMatch = LiveDataTestUtil.getValue(viewModel.isShowLoadingLastMatch)

        assertNotNull(lastMatchList)
        assertEquals(lastMatchList, DataDummy.matchList)
        assertEquals(isShowLoadingLastMatch, false)
    }

    @Test
    fun getLastMatchList_matchIsNullOrEmpty() = testCoroutineScope.runBlockingTest {
        val lastMatchResponse = MatchResponse(emptyList())

        Mockito.`when`(apiResponse.await())
            .thenReturn(lastMatchResponse.toString())
        Mockito.`when`(gson.fromJson(lastMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(lastMatchResponse)

        viewModel.getLastMatchList(leagueId)

        val lastMatchList = LiveDataTestUtil.getValue(viewModel.lastMatchList)
        val isEmptyLastMatch = LiveDataTestUtil.getValue(viewModel.isEmptyLastMatch)

        assertEquals(lastMatchList, emptyList<Match>())
        assertEquals(isEmptyLastMatch, true)

    }

    @Test
    fun getNextMatchList() = testCoroutineScope.runBlockingTest {
        val nextMatchResponse = MatchResponse(matchListDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(nextMatchResponse.toString())
        Mockito.`when`(gson.fromJson(nextMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(nextMatchResponse)

        viewModel.getNextMatchList(leagueId)

        val nextMatchList = LiveDataTestUtil.getValue(viewModel.nextMatchList)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoadingNextMatch)

        assertNotNull(nextMatchList)
        assertEquals(nextMatchList, DataDummy.matchList)
        assertEquals(isShowLoading, false)
    }

    @Test
    fun getNextMatchList_matchIsNullOrEmpty() = testCoroutineScope.runBlockingTest {
        val nextMatchResponse = MatchResponse(emptyList())

        Mockito.`when`(apiResponse.await())
            .thenReturn(nextMatchResponse.toString())
        Mockito.`when`(gson.fromJson(nextMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(nextMatchResponse)

        viewModel.getNextMatchList(leagueId)

        val nextMatchList = LiveDataTestUtil.getValue(viewModel.nextMatchList)
        val isEmptyNextMatch = LiveDataTestUtil.getValue(viewModel.isEmptyNextMatch)

        assertEquals(nextMatchList, emptyList<Match>())
        assertEquals(isEmptyNextMatch, true)

    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}