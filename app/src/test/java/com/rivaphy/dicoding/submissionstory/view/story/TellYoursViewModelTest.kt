package com.rivaphy.dicoding.submissionstory.view.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.rivaphy.dicoding.submissionstory.DataDummy
import com.rivaphy.dicoding.submissionstory.MainDispatcherRule
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.repository.PagingRepository
import com.rivaphy.dicoding.submissionstory.getOrAwaitValue
import com.rivaphy.dicoding.submissionstory.view.main.MainAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TellYoursViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var repository: PagingRepository

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<ListStoryItem>>()
        expectedQuote.value = data
        Mockito.`when`(repository.getPaging()).thenReturn(expectedQuote)

        val tellYoursViewModel = TellYoursViewModel(repository)
        val actualQuote: PagingData<ListStoryItem> = tellYoursViewModel.pagingStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        assertNotNull(differ.snapshot())
        assertEquals(dummyQuote.size, differ.snapshot().size)
        assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<ListStoryItem>>()
        expectedQuote.value = data
        Mockito.`when`(repository.getPaging()).thenReturn(expectedQuote)
        val tellYoursViewModel = TellYoursViewModel(repository)
        val actualQuote: PagingData<ListStoryItem> = tellYoursViewModel.pagingStory.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}