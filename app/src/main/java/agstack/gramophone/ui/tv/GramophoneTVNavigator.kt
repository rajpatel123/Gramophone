package agstack.gramophone.ui.tv

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.tv.model.PlayListItemModels
import agstack.gramophone.ui.tv.model.YoutubeChannelItem

interface GramophoneTVNavigator : BaseNavigator {
    fun setToolbarTitle(title:String)

    fun populatePlayLists(playLists: List<YoutubeChannelItem>)

    fun populatePlayListsNextPage(playLists: List<YoutubeChannelItem>)

    fun setNextPageToken(nextPageToken: String?)

    fun populateVideosList(videoIds: List<PlayListItemModels>)

    fun populateVideosNextPage(videoIds: List<PlayListItemModels>)

    fun setVideosNextPageToken(nextPageToken: String?)
}
