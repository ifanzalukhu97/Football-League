package com.example.footballleague.ui.teams

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.TeamsResponse
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
class TeamsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: TeamsViewModel
    private val teamsDummy = DataDummy.teams
    private val leagueId = "4328"

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = TeamsViewModel(gson, apiRepository)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
    }

    @Test
    fun getTeams() = testCoroutineScope.runBlockingTest {
        val teamsResponse = TeamsResponse(teamsDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(teamsResponse.toString())
        Mockito.`when`(gson.fromJson(teamsResponse.toString(), TeamsResponse::class.java))
            .thenReturn(teamsResponse)

        viewModel.getTeams(leagueId)

        val teams = LiveDataTestUtil.getValue(viewModel.teams)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertNotNull(teams)
        assertEquals(teams, DataDummy.teams)
        assertEquals(isShowLoading, false)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}