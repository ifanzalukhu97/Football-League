package com.example.footballleague.ui.matchdetail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.DetailMatchResponse
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
class MatchDetailViewModelTest {

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

    private lateinit var viewModel: MatchDetailViewModel
    private val detailMatchDummy = DataDummy.detailMatch
    private val eventId = "672158"

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = MatchDetailViewModel(gson, apiRepository, application)
    }

    @Test
    fun getDetailMatch() = testCoroutineScope.runBlockingTest {
        val detailMatchResponse = DetailMatchResponse(listOf(detailMatchDummy))

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
        Mockito.`when`(apiResponse.await())
            .thenReturn(detailMatchResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                detailMatchResponse.toString(),
                DetailMatchResponse::class.java
            )
        )
            .thenReturn(detailMatchResponse)

        viewModel.getDetailMatch(eventId)

        val detailMatch = LiveDataTestUtil.getValue(viewModel.detailMatch)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertNotNull(detailMatch)
        assertEquals(detailMatch, DataDummy.detailMatch)
        assertEquals(isShowLoading, false)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}