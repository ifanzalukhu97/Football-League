package com.example.footballleague.ui.search.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.SearchTeamResponse
import com.example.footballleague.source.remote.Team
import com.example.footballleague.utils.LiveDataTestUtil
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SearchTeamViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: SearchTeamViewModel
    private val teamsDummy = DataDummy.SearchTeams

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = SearchTeamViewModel(gson, apiRepository)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
    }

    @Test
    fun searchTeams() = testCoroutineScope.runBlockingTest {
        val searchTeamsResponse = SearchTeamResponse(teamsDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(searchTeamsResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                searchTeamsResponse.toString(),
                SearchTeamResponse::class.java
            )
        )
            .thenReturn(searchTeamsResponse)

        viewModel.searchTeamByQuery("example query")

        val teams = LiveDataTestUtil.getValue(viewModel.teams)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertEquals(teams, DataDummy.SearchTeams)
        assertEquals(isShowLoading, false)
    }

    @Test
    fun searchTeams_queryIsBlank() = testCoroutineScope.runBlockingTest {
        viewModel.searchTeamByQuery("")

        val teams = LiveDataTestUtil.getValue(viewModel.teams)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)
        val isEmptyMatchList = LiveDataTestUtil.getValue(viewModel.isEmptyTeams)

        assertEquals(teams, emptyList<Team>())
        assertEquals(isShowLoading, false)
        assertEquals(isEmptyMatchList, true)
    }

    @Test
    fun searchTeams_resultIsEmpty() = testCoroutineScope.runBlockingTest {
        val searchTeamsResponse = SearchTeamResponse(emptyList())

        Mockito.`when`(apiResponse.await())
            .thenReturn(searchTeamsResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                searchTeamsResponse.toString(),
                SearchTeamResponse::class.java
            )
        )
            .thenReturn(searchTeamsResponse)

        viewModel.searchTeamByQuery("example query")

        val teams = LiveDataTestUtil.getValue(viewModel.teams)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)
        val isEmptyTeams = LiveDataTestUtil.getValue(viewModel.isEmptyTeams)

        assertEquals(teams, emptyList<Team>())
        assertEquals(isShowLoading, false)
        assertEquals(isEmptyTeams, true)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}