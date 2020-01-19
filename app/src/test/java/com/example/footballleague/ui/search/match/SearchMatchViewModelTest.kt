package com.example.footballleague.ui.search.match

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.footballleague.ApiRepository
import com.example.footballleague.source.DataDummy
import com.example.footballleague.source.remote.Match
import com.example.footballleague.source.remote.SearchMatchResponse
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
class SearchMatchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var testCoroutineScope: TestCoroutineScope

    @Mock
    private lateinit var gson: Gson
    @Mock
    private lateinit var apiRepository: ApiRepository
    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var viewModel: SearchMatchViewModel
    private val matchListDummy = DataDummy.matchList

    @Before
    fun setUp() {
        testCoroutineScope = TestCoroutineScope()
        MockitoAnnotations.initMocks(this)
        viewModel = SearchMatchViewModel(gson, apiRepository)

        Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
            .thenReturn(apiResponse)
    }

    @Test
    fun searchMatch() = testCoroutineScope.runBlockingTest {
        val searchMatchResponse = SearchMatchResponse(matchListDummy)

        Mockito.`when`(apiResponse.await())
            .thenReturn(searchMatchResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                searchMatchResponse.toString(),
                SearchMatchResponse::class.java
            )
        )
            .thenReturn(searchMatchResponse)

        viewModel.searchMatchByQuery("example query")

        val matchList = LiveDataTestUtil.getValue(viewModel.matchList)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)

        assertEquals(matchList, DataDummy.matchList)
        assertEquals(isShowLoading, false)
    }

    @Test
    fun searchMatch_queryIsBlank() = testCoroutineScope.runBlockingTest {
        viewModel.searchMatchByQuery("")

        val matchList = LiveDataTestUtil.getValue(viewModel.matchList)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)
        val isEmptyMatchList = LiveDataTestUtil.getValue(viewModel.isEmptyMatchList)

        assertEquals(matchList, emptyList<Match>())
        assertEquals(isShowLoading, false)
        assertEquals(isEmptyMatchList, true)
    }


    @Test
    fun searchMatch_resultIsEmpty() = testCoroutineScope.runBlockingTest {
        val searchMatchResponse = SearchMatchResponse(emptyList())

        Mockito.`when`(apiResponse.await())
            .thenReturn(searchMatchResponse.toString())
        Mockito.`when`(
            gson.fromJson(
                searchMatchResponse.toString(),
                SearchMatchResponse::class.java
            )
        )
            .thenReturn(searchMatchResponse)

        viewModel.searchMatchByQuery("example query")

        val matchList = LiveDataTestUtil.getValue(viewModel.matchList)
        val isShowLoading = LiveDataTestUtil.getValue(viewModel.isShowLoading)
        val isEmptyMatchList = LiveDataTestUtil.getValue(viewModel.isEmptyMatchList)

        assertEquals(matchList, emptyList<Match>())
        assertEquals(isShowLoading, false)
        assertEquals(isEmptyMatchList, true)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}