package com.example.footballleague.ui.lastmatch

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
class LastMatchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: LastMatchViewModel
    private val matchListDummy = DataDummy.matchList
    private val leagueId = "4570"

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = LastMatchViewModel(gson, apiRepository)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
    }

    @Test
    fun getMatchList() = testCoroutineScope.runBlockingTest {
        val lastMatchResponse = MatchResponse(matchListDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(lastMatchResponse.toString())
        Mockito.`when`(gson.fromJson(lastMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(lastMatchResponse)

        viewModel.getMatchList(leagueId)

        val matchList = LiveDataTestUtil.getValue(viewModel.matchList)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertNotNull(matchList)
        assertEquals(matchList, DataDummy.matchList)
        assertEquals(isShowLoading, false)
    }

    @Test
    fun getMatchList_matchIsNullOrEmpty() = testCoroutineScope.runBlockingTest {
        val lastMatchResponse = MatchResponse(emptyList())

        Mockito.`when`(apiResponse.await())
            .thenReturn(lastMatchResponse.toString())
        Mockito.`when`(gson.fromJson(lastMatchResponse.toString(), MatchResponse::class.java))
            .thenReturn(lastMatchResponse)

        viewModel.getMatchList(leagueId)

        val matchList = LiveDataTestUtil.getValue(viewModel.matchList)
        val isEmptyMatchList = LiveDataTestUtil.getValue(viewModel.isEmptyMatchList)

        assertEquals(matchList, emptyList<Match>())
        assertEquals(isEmptyMatchList, true)

    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}