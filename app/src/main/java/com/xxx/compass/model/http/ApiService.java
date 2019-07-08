package com.xxx.compass.model.http;

import com.xxx.compass.model.http.bean.AchievementRecordBean;
import com.xxx.compass.model.http.bean.AppVersionBean;
import com.xxx.compass.model.http.bean.ConversionProfitBean;
import com.xxx.compass.model.http.bean.DoExceptionBean;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.DepositInfoBean;
import com.xxx.compass.model.http.bean.DepositProfitBean;
import com.xxx.compass.model.http.bean.CountyBean;
import com.xxx.compass.model.http.bean.DepositRecordBean;
import com.xxx.compass.model.http.bean.base.BooleanBean;
import com.xxx.compass.model.http.bean.FlashSaleBean;
import com.xxx.compass.model.http.bean.HomeBannerBean;
import com.xxx.compass.model.http.bean.HomeBean;
import com.xxx.compass.model.http.bean.IsSettingPayPswBean;
import com.xxx.compass.model.http.bean.LoginBean;
import com.xxx.compass.model.http.bean.MyLevelBean;
import com.xxx.compass.model.http.bean.NodeGameBean;
import com.xxx.compass.model.http.bean.NodeGameRuleBean;
import com.xxx.compass.model.http.bean.NoticeCenterBean;
import com.xxx.compass.model.http.bean.RechargeRecordBean;
import com.xxx.compass.model.http.bean.ReleaseInfoBean;
import com.xxx.compass.model.http.bean.ReleaseRecordBean;
import com.xxx.compass.model.http.bean.RushInfoBean;
import com.xxx.compass.model.http.bean.RushRecordBean;
import com.xxx.compass.model.http.bean.ShareRecordBean;
import com.xxx.compass.model.http.bean.WalletBean;
import com.xxx.compass.model.http.bean.WithdrawalRecordBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    //获取首页列表
    @POST("/CT/invest/getAllCoinInfo")
    Observable<BaseBean<List<HomeBean>>> getHomeList();

    //获取首页轮播图
    @FormUrlEncoded
    @POST("/uc/ancillary/system/advertise")
    Observable<BaseBean<List<HomeBannerBean>>> getHomeBannerList(
            @Field("sysAdvertiseLocation") String location
    );

    //获取钱包列表
    @FormUrlEncoded
    @POST("/CT/invest/getUserAssetInfo")
    Observable<BaseBean<WalletBean>> getWalletList(
            @Field("userId") String userId
    );

    //获取节点大赛列表
    @FormUrlEncoded
    @POST("/CT/invest/getBonusesInfo")
    Observable<BaseBean<NodeGameBean>> getNodeGameList(
            @Field("userId") String userId
    );

    //获取节点大赛奖励规则列表
    @POST("/CT/invest/getBonusesLevel")
    Observable<BaseBean<List<NodeGameRuleBean>>> getNodeGameRuleList();

    //获取充值记录列表
    @FormUrlEncoded
    @POST("/CT/invest/getDepositLogs")
    Observable<BaseBean<List<RechargeRecordBean>>> getRechargeRecordList(
            @Field("userId") String userId,
            @Field("unit") String unit,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取提现记录列表
    @FormUrlEncoded
    @POST("/CT/invest/getWithdrawLogs")
    Observable<BaseBean<List<WithdrawalRecordBean>>> getWithdrawalRecordList(
            @Field("userId") String userId,
            @Field("unit") String unit,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取公告中心列表
    @FormUrlEncoded
    @POST("/uc/announcement/page")
    Observable<BaseBean<NoticeCenterBean>> getNoticeCenterList(
            @Field("pageNo") int pageNo,
            @Field("pageSize") int pageSize
    );

    //获取分享业绩列表
    @FormUrlEncoded
    @POST("/CT/invest/getCommunityInfo")
    Observable<BaseBean<AchievementRecordBean>> getAchievementRecordList(
            @Field("userId") String userId
    );

    //获取分享收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getInvestedIncomeLogs")
    Observable<BaseBean<ShareRecordBean>> getShareRecordList(
            @Field("userId") String userId,
            @Field("unit") String unit,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取抢购收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getRushRecordList")
    Observable<BaseBean<List<RushRecordBean>>> getRushRecordList(
            @Field("userId") String userId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取释放记录列表
    @FormUrlEncoded
    @POST("/CT/invest/getReleaseIncomeList")
    Observable<BaseBean<List<ReleaseRecordBean>>> getReleaseRecordList(
            @Field("userId") String userId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取理财总收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getAllFinancingsIncomeLogs")
    Observable<BaseBean<List<DepositProfitBean>>> getDepositTotalProfitList(
            @Field("userId") String userId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取转化收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getFlashsaleIncomeLogs")
    Observable<BaseBean<List<ConversionProfitBean>>> getConversionProfitList(
            @Field("userId") String userId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取理财收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getFinancingsIncomeLogs")
    Observable<BaseBean<List<DepositProfitBean>>> getDepositProfitList(
            @Field("userId") String userId,
            @Field("unit") String coinId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取理财转入记录列表
    @FormUrlEncoded
    @POST("/CT/invest/getFinancingsLogs")
    Observable<BaseBean<List<DepositRecordBean>>> getDepositInRecordList(
            @Field("userId") String userId,
            @Field("unit") String coinId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );

    //获取理财转出记录列表
    @FormUrlEncoded
    @POST("/CT/invest/getFinancingsDrawoutLogs")
    Observable<BaseBean<List<DepositRecordBean>>> getDepositOutRecordList(
            @Field("userId") String userId,
            @Field("unit") String coinId,
            @Field("page") int pageNo,
            @Field("row") int pageSize
    );


    //----------------------------------------------------------操作----------------------------------------------------------------------------------------------------------------------------//


    //获取抢购信息
    @POST("/CT/invest/getFlashsaleBaseInfo")
    Observable<BaseBean<RushInfoBean>> getRushInfo();

    //获取用户等级
    @FormUrlEncoded
    @POST("/CT/invest/getUserStar")
    Observable<BaseBean<MyLevelBean>> getUserStar(
            @Field("userId") String userId
    );

    //获取释放记录信息
    @FormUrlEncoded
    @POST("/CT/invest/getFlashsaleInfo")
    Observable<BaseBean<ReleaseInfoBean>> getReleaseRecordInfo(
            @Field("userId") String userId
    );

    //获取理财总收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getAllFinancingsInfo")
    Observable<BaseBean<DepositInfoBean>> getDepositTotalProfitInfo(
            @Field("userId") String userId
    );

    //获取理财收益列表
    @FormUrlEncoded
    @POST("/CT/invest/getFinancingsInfo")
    Observable<BaseBean<DepositInfoBean>> getDepositProfitInfo(
            @Field("userId") String userId,
            @Field("coinId") String coinId
    );

    //提现
    @POST("/uc/withdraw/apply/withoutcode")
    @FormUrlEncoded
    Observable<BaseBean<Object>> withdrawal(
            @Field("userId") String userId,
            @Field("unit") String unit,
            @Field("fee") double fee,
            @Field("amount") double amount,
            @Field("address") String address,
            @Field("jyPassword") String jyPassword
    );

    //抢购
    @POST("/CT/invest/compass/doFlashsale")
    @FormUrlEncoded
    Observable<BaseBean<FlashSaleBean>> flashSale(
            @Field("userId") String userId,
            @Field("coinId") String coinId
    );

    //兑换
    @POST("/CT/invest/compass/doExchange")
    @FormUrlEncoded
    Observable<BaseBean<DoExceptionBean>> exchange(
            @Field("userId") String userId,
            @Field("coinId") String coinId,
            @Field("value") double value
    );

    //理财转入
    @POST("/CT/invest/compass/doFinancings")
    @FormUrlEncoded
    Observable<BaseBean<BooleanBean>> depositIn(
            @Field("userId") String userId,
            @Field("coinId") String coinId,
            @Field("value") double value
    );

    //理财转出
    @POST("/CT/invest/compass/doFinancingsDrawout")
    @FormUrlEncoded
    Observable<BaseBean<BooleanBean>> depositOut(
            @Field("userId") String userId,
            @Field("coinId") String coinId,
            @Field("value") double value,
            @Field("jyPassword") String jyPassword
    );


    //-------------------------------------------------------登录-------------------------------------------------------------------------------------------------------------------------------//


    //登录
    @FormUrlEncoded
    @POST("/uc/login")
    Observable<BaseBean<LoginBean>> login(
            @Field("username") String account,
            @Field("password") String password,
            @Field("challenge") String a,
            @Field("validate") String b,
            @Field("seccode") String c
    );

    //发送短信验证码
    @FormUrlEncoded
    @POST("/uc/mobile/code")
    Observable<BaseBean<Object>> sendSMSCode(
            @Field("phone") String phone,
            @Field("country") String country
    );

    //注册
    @FormUrlEncoded
    @POST("/uc/register/phone_ext")
    Observable<BaseBean<Object>> register(
            @Field("username") String account,
            @Field("phone") String phone,
            @Field("code") String smsCode,
            @Field("password") String password,
            @Field("country") String country,
            @Field("promotion") String promotion,
            @Field("ticket") String a,
            @Field("randStr") String b
    );

    //忘记密码
    @FormUrlEncoded
    @POST("/uc/reset/login/password")
    Observable<BaseBean<Object>> forgetPsw(
            @Field("account") String account,
            @Field("code") String smsCode,
            @Field("password") String password,
            @Field("mode") String mode
    );

    //发送忘记密码短信验证码
    @FormUrlEncoded
    @POST("/uc/mobile/reset/code")
    Observable<BaseBean<Object>> sendForgetSMSCode(
            @Field("account") String phone,
            @Field("geetest_challenge") String a,
            @Field("geetest_validate") String b,
            @Field("geetest_seccode") String c
    );

    //发送修改登录密码短信验证码
    @POST("/uc/mobile/update/password/code")
    Observable<BaseBean<Object>> sendModifyLoginSMSCode();

    //发送修改支付密码短信验证码
    @POST("/uc/mobile/transaction/code")
    Observable<BaseBean<Object>> sendModifyPaySMSCode();

    //修改登录密码
    @FormUrlEncoded
    @POST("/uc/approve/update/password")
    Observable<BaseBean<Object>> modifyLoginPsw(
            @Field("code") String smsCode,
            @Field("oldPassword") String password,
            @Field("newPassword") String mode
    );

    //修改支付密码
    @FormUrlEncoded
    @POST("/uc/approve/reset/transaction/password")
    Observable<BaseBean<Object>> modifyPayPsw(
            @Field("code") String smsCode,
            @Field("newPassword") String password
    );

    //设置支付密码
    @FormUrlEncoded
    @POST("/uc/approve/transaction/password")
    Observable<BaseBean<Object>> settingPayPsw(
            @Field("jyPassword") String smsCode
    );

    //获取城市编码
    @POST("/uc/support/country")
    Observable<BaseBean<List<CountyBean>>> getCounty();

    //检查app版本
    @POST("/uc/ancillary/system/app/version/0")
    Observable<BaseBean<AppVersionBean>> checkAppVersion();

    //退出登录
    @POST("/uc/loginout")
    Observable<BaseBean<Object>> outLogin();

    //检查是否设置过支付密码
    @POST("/uc/approve/security/setting")
    Observable<BaseBean<IsSettingPayPswBean>> checkIsSettingPayPassword();

    //意见反馈
    @POST("/CT/invest/getUsdtFee")
    @FormUrlEncoded
    Observable<BaseBean<Object>> submitFeedback(
            @Field("content") String amount,
            @Field("phone") String address
    );
}