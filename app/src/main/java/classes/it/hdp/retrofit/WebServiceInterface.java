package classes.it.hdp.retrofit;

import com.google.gson.JsonObject;

import classes.it.hdp.models.ReqAeps;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebServiceInterface {

    @FormUrlEncoded
    @POST("Godigital")
    Call<JsonObject> goDigital(@Field("UserKey") String UserKey,
                               @Field("MobileNumber") String MobileNumber,
                               @Field("CustomerName") String CustomerName,
                               @Field("CompanyName") String CompanyName,
                               @Field("EmailId") String EmailId,
                               @Field("Service") String Service,
                               @Field("Remarks") String Remarks);

    @FormUrlEncoded
    @POST("DMTACVarify")
    Call<JsonObject> accountVerify(@Field("UserKey") String UserKey,
                                   @Field("MobileNo") String MobileNo,
                                   @Field("Account") String Account,
                                   @Field("IFSC") String IFSC,
                                   @Field("Bankname") String Bankname,
                                   @Field("BeneName") String BeneName,
                                   @Field("sendername") String sendername);

    @FormUrlEncoded
    @POST("BBPSPay")
    Call<JsonObject> payBill(@Field("UserKey") String UserKey,
                             @Field("BBPSKey") String BBPSKey,
                             @Field("OPCode") String OPCode,
                             @Field("MobileNumber") String MobileNumber,
                             @Field("CustomerName") String CustomerName,
                             @Field("Amount") String Amount,
                             @Field("ClientRefno") String ClientRefno,
                             @Field("mac") String mac,
                             @Field("Canumber") String Canumber,
                             @Field("hfbiller") String hfbiller,
                             @Field("hfinput") String hfinput,
                             @Field("hfainfo") String hfainfo,
                             @Field("Service") String Service);

    @FormUrlEncoded
    @POST("BBPSBillFetch")
    Call<JsonObject> fetchBill(@Field("UserKey") String UserKey,
                               @Field("BBPSKey") String BBPSKey,
                               @Field("OPCode") String OPCode,
                               @Field("mac") String mac,
                               @Field("MobileNo") String MobileNo,
                               @Field("Param1") String Param1,
                               @Field("Param1Value") String Param1Value,
                               @Field("Param2") String Param2,
                               @Field("Param2Value") String Param2Value);

    @FormUrlEncoded
    @POST("GetProfile")
    Call<JsonObject> getProfile(@Field("UserKey") String UserKey);

    @FormUrlEncoded
    @POST("UserKYC")
    Call<JsonObject> doUserKyc(@Field("UserKey") String UserKey,
                               @Field("PancardNo") String PancardNo,
                               @Field("AadharCardNo") String AadharCardNo,
                               @Field("AadharCopy") String AadharCopy,
                               @Field("PanCopy") String PanCopy,
                               @Field("BankCopy") String BankCopy,
                               @Field("SelfyImage") String SelfyImage,
                               @Field("ShopImage") String ShopImage);

    @FormUrlEncoded
    @POST("CheckKycStatus")
    Call<JsonObject> checkKycStatus(@Field("UserKey") String UserKey);

    @FormUrlEncoded
    @POST("EditProfile")
    Call<JsonObject> editProfile(@Field("UserKey") String UserKey,
                                 @Field("ShopName") String ShopName,
                                 @Field("PINCode") String PINCode,
                                 @Field("City") String City,
                                 @Field("State") String State,
                                 @Field("Address") String Address,
                                 @Field("LastName") String LastName,
                                 @Field("FirstName") String FirstName
    );

    @FormUrlEncoded
    @POST("MyCommission")
    Call<JsonObject> getMyCommission(@Field("UserKey") String UserKey);

    @FormUrlEncoded
    @POST("PayoutReport")
    Call<JsonObject> getSettlementReport(@Field("UserKey") String UserKey,
                                         @Field("SearchType") String SearchType,
                                         @Field("From") String From,
                                         @Field("To") String To);

    @FormUrlEncoded
    @POST("SendOTP")
    Call<JsonObject> sendOtp(@Field("UserKey") String UserKey);


    @FormUrlEncoded
    @POST("AddPayoutBeneficiary")
    Call<JsonObject> addPayoutBene(@Field("UserKey") String UserKey,
                                   @Field("BeneName") String BeneName,
                                   @Field("AccountNumber") String AccountNumber,
                                   @Field("IFSCCode") String IFSCCode,
                                   @Field("BankName") String BankName,
                                   @Field("IsVerified") String IsVerified);

    @FormUrlEncoded
    @POST("PayoutBeneList")
    Call<JsonObject> getPayoutBeneList(@Field("UserKey") String UserKey);

    @FormUrlEncoded
    @POST("MovetoDistributor")
    Call<JsonObject> moveToDistributor(@Field("UserKey") String UserKey,
                                       @Field("Amt") String Amt);

    @FormUrlEncoded
    @POST("MovetoWallet")
    Call<JsonObject> moveToWallet(@Field("UserKey") String UserKey,
                                  @Field("Amt") String Amt);

    @FormUrlEncoded
    @POST("MovetoBank")
    Call<JsonObject> moveToBank(@Field("UserKey") String UserKey,
                                @Field("Amt") String Amt,
                                @Field("Account") String Account,
                                @Field("IFSC") String IFSC,
                                @Field("Bankname") String Bankname,
                                @Field("BeneName") String BeneName);

    @FormUrlEncoded
    @POST("AEPSReport")
    Call<JsonObject> getAepsReport(@Field("UserKey") String UserKey,
                                   @Field("SearchType") String SearchType,
                                   @Field("From") String From,
                                   @Field("To") String To);

    @FormUrlEncoded
    @POST("DMTReport")
    Call<JsonObject> getDmtReport(@Field("UserKey") String UserKey,
                                  @Field("SearchType") String SearchType,
                                  @Field("From") String From,
                                  @Field("To") String To);

    @FormUrlEncoded
    @POST("Changepassword")
    Call<JsonObject> changePassword(@Field("UserKey") String UserKey,
                                    @Field("UserPassword") String UserPassword,
                                    @Field("Newpassword") String Newpassword);

    @FormUrlEncoded
    @POST("Forgotpassword")
    Call<JsonObject> forgetPassword(@Field("MobileNo") String MobileNo);

    @FormUrlEncoded
    @POST("BankList")
    Call<JsonObject> getBanks(@Field("UserKey") String UserKey,
                              @Field("Service") String Service);

    @FormUrlEncoded
    @POST("DMTPAY")
    Call<JsonObject> payBeneficiary(
            @Field("UserKey") String UserKey,
            @Field("Amt") String Amt,
            @Field("MobileNo") String MobileNo,
            @Field("Account") String Account,
            @Field("IFSC") String IFSC,
            @Field("Bankname") String Bankname,
            @Field("Benecode") String Benecode,
            @Field("BeneName") String BeneName,
            @Field("sendername") String sendername
    );

    @FormUrlEncoded
    @POST("SenderRegistrations")
    Call<JsonObject> addSender(@Field("UserKey") String UserKey,
                               @Field("fname") String fname,
                               @Field("surname") String surname,
                               @Field("pincode") String pincode,
                               @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("SenderValidate")
    Call<JsonObject> verifySenderOtp(@Field("UserKey") String UserKey,
                                     @Field("senderid") String senderid,
                                     @Field("mobile") String mobile,
                                     @Field("otp") String otp);


    @POST("AEPSTXN")
    Call<JsonObject> getDirectAeps(@Body ReqAeps map);

    @FormUrlEncoded
    @POST("BeneficiaryRegistrations")
    Call<JsonObject> addBeneficiary(@Field("UserKey") String UserKey,
                                    @Field("senderid") String senderid,
                                    @Field("IFSC") String IFSC,
                                    @Field("BankAcNo") String BankAcNo,
                                    @Field("BeneName") String BeneName,
                                    @Field("Mobile") String Mobile);

    @FormUrlEncoded
    @POST("BeneficiaryDeleteCmfirm")
    Call<JsonObject> verifyDeleteBeneOtp(@Field("UserKey") String UserKey,
                                         @Field("senderid") String senderid,
                                         @Field("beniid") String beniid,
                                         @Field("otp") String otp);

    @FormUrlEncoded
    @POST("BeneficiaryDelete")
    Call<JsonObject> deleteBeneficiary(@Field("UserKey") String UserKey,
                                       @Field("senderid") String senderid,
                                       @Field("beniid") String beniid);

    @FormUrlEncoded
    @POST("CheckSender")
    Call<JsonObject> isSenderValidate(@Field("UserKey") String UserKey,
                                      @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("RecentReport")
    Call<JsonObject> getRecentReport(@Header("Authorization") String auth,
                                     @Field("userid") String userid,
                                     @Field("filtertype") String filtertype);

    @FormUrlEncoded
    @POST("UserCreditDebit")
    Call<JsonObject> doCreditBalance(@Header("Authorization") String auth,
                                     @Field("userid") String userid,
                                     @Field("crdruserid") String crdruserid,
                                     @Field("amount") String amount,
                                     @Field("remarks") String remarks,
                                     @Field("crdrtype") String crdrtype);

    @FormUrlEncoded
    @POST("FetchParentUser")
    Call<JsonObject> getUsers(@Header("Authorization") String auth,
                              @Field("userid") String userid);

    @FormUrlEncoded
    @POST("PaymentRequest")
    Call<JsonObject> doPaymentRequest(@Field("UserKey") String UserKey,
                                      @Field("Amount") String Amount,
                                      @Field("RefNo") String RefNo,
                                      @Field("Paymode") String Paymode,
                                      @Field("PaymentSlip") String PaymentSlip,
                                      @Field("Remarks") String Remarks);

    @FormUrlEncoded
    @POST("GetBankList")
    Call<JsonObject> getBankList(@Header("Authorization") String auth,
                                 @Field("userID") String userID);

    @Multipart
    @POST("FileUploading/UploadFile")
    Call<JsonObject> uploadfile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("RechargeReport")
    Call<JsonObject> getRechargeReport(@Field("UserKey") String UserKey,
                                       @Field("SearchType") String SearchType,
                                       @Field("From") String From,
                                       @Field("To") String To);

    @FormUrlEncoded
    @POST("LedgerReport")
    Call<JsonObject> getLedger(@Field("UserKey") String UserKey,
                               @Field("WalletName") String WalletName,
                               @Field("From") String From,
                               @Field("To") String To);

    @FormUrlEncoded
    @POST("UpiCollectionReport")
    Call<JsonObject> getUpiReport(@Header("Authorization") String auth,
                                  @Field("userid") String userid,
                                  @Field("fromdate") String fromdate,
                                  @Field("todate") String todate);

    @FormUrlEncoded
    @POST("WalletSummary")
    Call<JsonObject> getWalletSummary(@Header("Authorization") String auth,
                                      @Field("userid") String userid,
                                      @Field("fromdate") String fromdate,
                                      @Field("todate") String todate);


    @FormUrlEncoded
    @POST("ConfirmUpiPayment")
    Call<JsonObject> insertUpiDetails(@Header("Authorization") String auth,
                                      @Field("userid") String userid,
                                      @Field("uniqueid") String uniqueid,
                                      @Field("status") String status);

    @FormUrlEncoded
    @POST("UpdateUpiPayments")
    Call<JsonObject> updateUpiDetails(@Header("Authorization") String auth,
                                      @Field("userid") String userid,
                                      @Field("amount") String amount);


    @FormUrlEncoded
    @POST("FetchServices")
    Call<JsonObject> getServices(
            @Header("Authorization") String auth,
            @Field("tokenKey") String tokenKey,
            @Field("deviceInfo") String deviceInfo,
            @Field("userID") String userID);

    @FormUrlEncoded
    @POST("GetProduct")
    Call<JsonObject> getOperators(@Field("Service") String Service);

    @FormUrlEncoded
    @POST("GetBBPSOperator")
    Call<JsonObject> getBBPSOperator(@Field("Service") String Service,
                                     @Field("State") String State);


    @FormUrlEncoded
    @POST("Recharge")
    Call<JsonObject> doRecharge(
            @Field("UserKey") String UserKey,
            @Field("Amt") String Amt,
            @Field("ProdKey") String ProdKey,
            @Field("Number") String Number,
            @Field("Service") String Service);

    @FormUrlEncoded
    @POST("UserLogin")
    Call<JsonObject> login(
            @Field("UserName") String UserName,
            @Field("Password") String Password);

    @FormUrlEncoded
    @POST("RTSignUp")
    Call<JsonObject> signUp(@Field("FirstName") String FirstName,
                            @Field("LastName") String LastName,
                            @Field("ShopName") String ShopName,
                            @Field("MobileNo") String MobileNo,
                            @Field("Email") String Email,
                            @Field("Address") String Address,
                            @Field("Pancard") String Pancard,
                            @Field("AadharCard") String AadharCard,
                            @Field("State") String State,
                            @Field("City") String City,
                            @Field("Pincode") String Pincode);

    @FormUrlEncoded
    @POST("MainBalance")
    Call<JsonObject> getBalance(@Field("UserKey") String UserKey);

    @FormUrlEncoded
    @POST("AEPSBalance")
    Call<JsonObject> getAepsBalance(@Field("UserKey") String UserKey);


}
