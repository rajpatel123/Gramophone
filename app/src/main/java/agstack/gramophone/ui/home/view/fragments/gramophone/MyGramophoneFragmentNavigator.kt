package agstack.gramophone.ui.home.view.fragments.gramophone

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.farm.model.FarmResponse
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikeUpdate
import agstack.gramophone.ui.home.view.fragments.community.model.socialhomemodels.CommunityHomeResponseModel
import agstack.gramophone.ui.home.view.fragments.gramophone.model.MyGramophoneResponseModel
import agstack.gramophone.ui.order.model.Data
import agstack.gramophone.ui.order.model.GpApiResponseData
import agstack.gramophone.ui.referandearn.model.GramCashResponseModel
import java.util.ArrayList

interface MyGramophoneFragmentNavigator :BaseNavigator {
    fun setUserName()
    fun setUserAddress()
    fun setUserImage()
    fun updateGramCash(gramCashResponseModel: GramCashResponseModel)
    fun updateCommunity(communityHomeResponseModel: CommunityHomeResponseModel)
    fun updateLike(data: LikeUpdate?)
    fun updateOrderSection(placedList: GpApiResponseData?, past: String)
    fun updateFarms(body: FarmResponse?)
    fun updateMyFavoriteSection(body: MyGramophoneResponseModel)
}