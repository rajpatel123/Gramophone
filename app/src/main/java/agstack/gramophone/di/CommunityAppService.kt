package agstack.gramophone.di

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.cart.model.AddToCartRequest
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.utils.Constants
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


interface CommunityAppService {

/*
    @Multipart
    @PUT("profiles")
    fun uploadDocument(

        @Part content: MultipartBody.Part,

        ): Single<Response<ResponseUploadDocument>>*/


}