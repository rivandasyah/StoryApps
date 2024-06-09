package com.rivaphy.dicoding.submissionstory

import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.api.response.StoryResponse

object DataDummy {

    fun generateDummyResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "2023-05-23T10:00:00Z",
                "author + $i",
                "story $i",
                -16.002,
                -10.212
            )
            items.add(story)
        }
        return items
    }
}