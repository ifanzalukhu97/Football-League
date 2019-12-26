package com.example.footballleague.ui.leaguedetail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.LeagueDetailResponse
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
class LeagueDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var application: Application
    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: LeagueDetailViewModel
    private val listLeague = listOf(DataDummy.leagueDetail)
    private val leagueId = 4321


    @Before
    fun setup() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = LeagueDetailViewModel(gson, apiRepository, application)
    }

    @Test
    fun getLeagueDetail() = testCoroutineScope.runBlockingTest {
        val leagueDetailResponse = LeagueDetailResponse(listLeague)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
        Mockito.`when`(apiResponse.await())
            .thenReturn(leagueDetailResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                leagueDetailResponse.toString(),
                LeagueDetailResponse::class.java
            )
        )
            .thenReturn(leagueDetailResponse)

        viewModel.getLeagueDetail(leagueId)

        val detailMovie = LiveDataTestUtil.getValue(viewModel.leagueDetail)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertNotNull(detailMovie)
        assertEquals(detailMovie, DataDummy.leagueDetail)
        assertEquals(isShowLoading, false)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}