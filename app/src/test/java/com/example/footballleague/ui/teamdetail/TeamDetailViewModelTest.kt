package com.example.footballleague.ui.teamdetail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.TeamDetailResponse
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
class TeamDetailViewModelTest {

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

    private lateinit var viewModel: TeamDetailViewModel
    private val teamDetailDummy = DataDummy.teamDetail
    private val teamId = "133612"

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = TeamDetailViewModel(gson, apiRepository, application)
    }

    @Test
    fun getDetailTeam() = testCoroutineScope.runBlockingTest {
        val teamDetailResponse = TeamDetailResponse(listOf(teamDetailDummy))

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
        Mockito.`when`(apiResponse.await())
            .thenReturn(teamDetailResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                teamDetailResponse.toString(),
                TeamDetailResponse::class.java
            )
        )
            .thenReturn(teamDetailResponse)

        viewModel.getDetailTeam(teamId)

        val teamDetail = LiveDataTestUtil.getValue(viewModel.teamDetail)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertNotNull(teamDetail)
        assertEquals(teamDetail, DataDummy.teamDetail)
        assertEquals(isShowLoading, false)
    }


    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }

}