package com.zhuandanbao.app.constant;

import com.zhuandanbao.app.entity.TypeEntity;

/**
 * Created by BFTECH on 2017/2/13.
 */

public class Constants {
    public static final String LOG_TITLE = "ZDB_LOG";

    public static final String APPID = "BZ42541943825993";
    public static final String APPKEY = "nyaoJjIKkQrPkQCiFDW22ZjRWm2LiLXwGdKexQsQOqFSbipKx5jAetEIJP1uaR6f";
    public static final String SECRET = "3FaQTnObuMg78SBrnqMZHKAUPJNZl1kgB5CZ68vuzS35DZjAL1uw150tBt3XcCud";
    public static final String BASE_HTTP_API = "https://appi.zhuandan.com/"; // 正式
    public static final String GRANT_TYPE = "client_credential";
    /*获取TOKEN*/
    public static final String API_GET_TOKEN = BASE_HTTP_API + "Token.client_credential";
    /***
     * 用戶名登录
     */
    public static final String API_ACTION_LOGIN = BASE_HTTP_API + "User.login";
    /**
     * 注册接口
     */
    public static final String API_ACTION_REGISTER = BASE_HTTP_API + "User.register";
    /***
     * 店铺基本信息
     */
    public static final String API_GET_SHOP_INFO = BASE_HTTP_API + "Shop.getinfo";
    /**
     * 店铺更新 ActionUrl
     */
    public static final String API_UPDATA_SHOP_INFO = BASE_HTTP_API + "Shop.modifyStoreInfo";
    /**
     * 加入 店铺诚信保证
     */
    public static final String API_SHOP_ADD_HONESTY = BASE_HTTP_API + "Shop.jionCreditGuarantee";
    /**
     * 追加保證金信息
     */
    public static final String API_ZHUI_JIA_HONEST_INFO = BASE_HTTP_API + "Shop.getCreditGuaranteeLevel";
    /**
     * 追加保證金
     */
    public static final String API_ZHUI_JIA_HONEST_MONEY = BASE_HTTP_API + "Shop.additionalCreditAmount";
    /**
     * 更换店铺LOGO
     */
    public static final String API_UPDATE_SHOP_LOGO = BASE_HTTP_API + "Shop.StoreAvatarUpload";
    /**
     * 获取店铺认证信息
     */
    public static final String API_GET_APPROVE_INFO = BASE_HTTP_API + "Store.getAuthentication";
    /**
     * 批量上传
     */
    public static final String API_BATCH_PICTURE_UPLOAD = BASE_HTTP_API + "Attachment.upPicture";
    /**
     * 店铺认证
     */
    public static final String API_APPROVE_SHOP = BASE_HTTP_API + "Store.modifyAuthentication";
    /**
     * 获取所有地址Area.allarea  getArea
     */
    public static final String API_GET_AREA_ALL = BASE_HTTP_API + "Area.allarea";
    /**
     * 店铺更新 ActionUrl
     */
    public static final String PAI_ACTION_SHOP_UPDAE = BASE_HTTP_API + "Shop.modifyStoreInfo";
    /**
     * 店铺联系人
     */
    public static final String API_ACTION_SHOP_CONTACTS = BASE_HTTP_API + "Shop.getContactsLists";
    /**
     * 店铺添加联系人和修改
     */
    public static final String API_ACTION_SHOP_ADDCONTACTS = BASE_HTTP_API + "Shop.saveContacts";
    /***
     * 店铺 删除联系人
     */
    public static final String API_ACTION_SHOP_DELETECONTACTS = BASE_HTTP_API + "Shop.deltetContacts";
    /***
     * 店铺形象照
     */
    public static final String API_ACTION_SHOP_FACEPHOTO = BASE_HTTP_API + "Shop.getStoreFacePhoto";
    /**
     * 店铺形象照 上传
     */
    public static final String API_ACTION_SHOP_FACE_UPLOAD = BASE_HTTP_API + "Shop.StoreFacePhotoUpload";
    /***
     * 店铺安全设置信息
     */
    public static final String API_ACTION_SHOP_SAFEINFO = BASE_HTTP_API + "Shop.getSafeInfo";
    /**
     * 支付密码
     */
    public static final String API_ACTION_SHOP_PAYPASS = BASE_HTTP_API + "Shop.modifyPaymentPass";

    /***
     * 修改手机绑定号
     */
    public static final String API_ACTION_SHOP_MOBILESAFE = BASE_HTTP_API + "Shop.modifySafeMobile";
    /***
     * 修改登录密码
     */
    public static final String API_ACTION_SHOP_UPLOGIN_PWD = BASE_HTTP_API + "Shop.modifyLoginPass";
    /***
     * 资讯栏目
     */
    public static final String API_ACTION_TYPE_INFOMATION = BASE_HTTP_API + "Article.zixunleimu";
    /**
     * 资讯栏目内容 ActionUrl
     */
    public static final String API_ACTION_INFOMATION = BASE_HTTP_API + "Article.artlist";
    /**
     * 获取转单宝动态
     */
    public static final String API_GET_ZDB_MSG_LIST = BASE_HTTP_API + "Article.getNewsList";
    /**
     * 获取动态详情
     */
    public static final String API_GET_ZDB_MSG_LIST_INFO = BASE_HTTP_API + "Article.getNewsContent";
    /***
     * 资讯详情页 ActionUrl
     */
    public static final String API_ACTION_INFO_CONTENT = BASE_HTTP_API + "Article.artContentByArtId";
    /**
     * 抢单列表
     */
    public static final String API_GRAD_ORDER_LISTS_API = BASE_HTTP_API + "Graborders.lists";
    /**
     * 发单列表
     */
    public static final String API_BILL_ORDER_LIST = BASE_HTTP_API + "Sendorder.orderlist";

    /***
     * 接单
     */
    public static final String API_ACTION_ORDERS_INFO = BASE_HTTP_API + "Receivedorders.search";

    /**
     * 三方订单列表
     */
    public static final String API_OTHER_ORDER_LIST = BASE_HTTP_API + "Tpmall.getOrder";
    /**
     * 售后列表
     */
    public static final String API_ORDER_SALE_LIST = BASE_HTTP_API + "Refundorder.refundList";
    /**
     * 获取店铺财务基本信息
     */
    public static final String API_GET_FINANCE_INFO = BASE_HTTP_API + "Finance.getStoreBasicInfo";
    /**
     * 统一订单详情
     */
    public static final String API_ORDER_INFO = BASE_HTTP_API + "Order.getOrderInfo";
    /**
     * 訂單跟蹤
     */
    public static final String API_ORDER_INFO_TAIL = BASE_HTTP_API + "Order.getLogisticsTracking";
    /**
     * 竞价
     */
    public static final String API_ORDER_BIDDING = BASE_HTTP_API + "Receivedorders.orderBidding";
    /**
     * 确认抢单
     **/
    public static final String API_ACTION_ORDERS_GRAB = BASE_HTTP_API + "Receivedorders.grabOrder";
    /**
     * 上传实物照
     */
    public static final String API_REAL_PIC = BASE_HTTP_API + "Receivedorders.pushRealPhoto";
    /**
     * 确认接单
     */
    public static final String API_ACTION_ORDERS_CONFIRM = BASE_HTTP_API + "Receivedorders.confirmReceiveOrder";
    /**
     * 订单退款订单
     **/
    public static final String API_ACTION_ORDERS_RECEIVED = BASE_HTTP_API + "Refundorder.ReceivedRefundManage";
    /**
     * 获取物流信息
     */
    public static final String API_ACTION_ORDERS_EXPRESS_INFO = BASE_HTTP_API + "Logistics.deliveryLogisticsGet";
    /**
     * 订单物流信息
     **/
    public static final String API_ACTION_ORDERS_ADD_EXPRESS = BASE_HTTP_API + "Receivedorders.confirmOrderLogisticsSend";
    /**
     * 取消订单
     */
    public static final String API_ACTION_ORDERS_CANCEL = BASE_HTTP_API + "Receivedorders.cancelOrder";
    /***
     * 签收订单
     **/
    public static final String API_ACTION_ORDERS_SIGN = BASE_HTTP_API + "Receivedorders.confirmOrderSing";
    /**
     * 发单取消订单
     */
    public static final String API_BILL_ORDER_CANCEL = BASE_HTTP_API + "Sendorder.cancelOrder";
    /**
     * 订单结算
     */
    public static final String API_ORDER_ACCOUNTS = BASE_HTTP_API + "Sendorder.settlementOrder";
    /**
     * 订单支付
     */
    public static final String API_ORDER_PAY = BASE_HTTP_API + "Sendorder.payOrder";
    /**
     * 竞价店铺
     */
    public static final String API_BIDDING_SHOP_LIST = BASE_HTTP_API + "Sendorder.getOrderBiddingInfo";
    /**
     * 确认竞价接单店铺
     */
    public static final String API_CONFIRM_BIDDING_SHOP = BASE_HTTP_API + "Sendorder.confirmBiddingStore";
    /**
     * 三方訂單日誌
     */
    public static final String API_OTHER_INFO_LOG=BASE_HTTP_API+"Tpmall.getLogisticsTracking";
    /**
     * 三方订单信息
     */
    public static final String API_OTHER_ORDER_INFO = BASE_HTTP_API + "Tpmall.getOrderInfo";
    /**
     * 三方订单自处理
     */
    public static final String API_OTHER_ORDER_ZCL = BASE_HTTP_API + "Tpmall.ConfirmSelfProcessing";
    /**
     * 店铺详情
     */
    public static final String API_SHOP_DETATIL_INFO = BASE_HTTP_API + "Store.getStoreInfo";
    /**
     * 加入黑名单
     */
    public static final String API_PUSH_BLACK_SHOP = BASE_HTTP_API + "Store.pushBlackShop";
    /**
     * 移除黑名单
     */
    public static final String API_DELETE_BLACK_SHOP = BASE_HTTP_API + "Store.deleteBlackShop";
    /**
     * 发单
     */
    public static final String API_BILL_ORDER_REQUEST = BASE_HTTP_API + "Sendorder.createOrder";
    /**
     * 检索发单店铺
     */
    public static final String API_SEARCH_SHOP_LIST = BASE_HTTP_API + "Store.searchStoreSimle";
    /**
     * 修改商家备注
     */
    public static final String API_OTHER_SELLER = BASE_HTTP_API + "Tpmall.modifyOrderSellerRemark";
    /**
     * 修改三方订单
     */
    public static final String API_OTHER_AMEND_INFO = BASE_HTTP_API + "Tpmall.modifyOrderInfo";
    /**
     * 三方订单转单
     */
    public static final String API_OTHER_BILL_ORDER = BASE_HTTP_API + "Tpmall.sendToStore";
    /**
     * 店铺充值接口
     */
    public static final String API_FINANCE_BANK_CARD_RECHARGE = BASE_HTTP_API + "Finance.createPayInfo";
    /**
     * 获取店铺账户交易日志
     */
    public static final String API_GET_FINANCE_TRADE_LOG = BASE_HTTP_API + "Finance.tradingLogs";
    /* 获取店铺添加的提现银行卡列表 */
    public static final String API_GET_FINANCE_ADD_BANK_CARD_LISTS = BASE_HTTP_API + "Finance.StoreWithdrawCardList";

    /* 添加/编辑提现银行卡信息 */
    public static final String API_FINANCE_ADD_OR_EDIT_BANK_CARD = BASE_HTTP_API + "Finance.modifyWithdrawCard";
    /* 获取系统支持的提现银行卡列表 */
    public static final String API_GET_FINANCE_BANK_CARD_LISTS = BASE_HTTP_API + "Finance.getSystemSupportCardLists";
    /* 删除当前店铺提现银行卡信息 */
    public static final String API_FINANCE_DETELE_BANK_CARD = BASE_HTTP_API + "Finance.deleteWithdrawCard";
    /* 当前店铺申请提现 */
    public static final String API_FINANCE_BANK_CARD_GET_MONEY = BASE_HTTP_API + "Finance.applyWithdraw";
    /**
     * 售后类型
     */
    public static final String API_ORDER_SALE_KIND = BASE_HTTP_API + "Refundorder.refundIssueTypeList";
    /**
     * 申请售后
     */
    public static final String API_ORDER_SALE_APPLY = BASE_HTTP_API + "Refundorder.applyRefund";
    /**
     * 售后详情
     */
    public static final String API_SALE_ORDER_INFO = BASE_HTTP_API + "Refundorder.getRefundInfo";
    /**
     * 同意
     */
    public static final String API_SALE_AGREE = BASE_HTTP_API + "Refundorder.confirmRefund";

    /**
     * 拒绝
     */
    public static final String API_SALE_DECLINE = BASE_HTTP_API + "Refundorder.refusedRefund";

    /**
     * 申请仲裁
     */
    public static final String API_SALE_ARBITRATION = BASE_HTTP_API + "Refundorder.applyArbitration";

    /**
     * 工单列表
     */
    public static final String API_TICKET_LIST = BASE_HTTP_API + "Ticket.lists";

    /**
     * 工单详情
     */
    public static final String API_WORK_INFO = BASE_HTTP_API + "Ticket.ticketDetails";

    /**
     * 撤销工单
     */
    public static final String API_REPADL_WORK = BASE_HTTP_API + "Ticket.revokeTicket";

    /**
     * 工单补充
     */
    public static final String API_WORK_REPLENISH = BASE_HTTP_API + "Ticket.feedback";

    /**
     * 删除工单
     */
    public static final String API_TICKET_DELETE = BASE_HTTP_API + "Ticket.deleteTicket";
    /**
     * 工单类型
     */
    public static final String API_WORK_TYPE = BASE_HTTP_API + "Ticket.getTicketCategoryLists";
    /**
     * 添加工单
     */
    public static final String API_ADD_WORK = BASE_HTTP_API + "Ticket.create";
    /**
     * 系統設置
     */
    public static final String API_SYSTEMS_SETTING=BASE_HTTP_API+"Systems.getGlobalsettings";

    /**
     * 三方訂單快遞添加物流
     */
    public static final String API_OTHER_ADD_WULIU = BASE_HTTP_API + "Tpmall.confirmOrderLogisticsSend";
    /**
     * 三方订单商品修改
     */
    public static final String API_OTHER_GOODS = BASE_HTTP_API + "Tpmall.modifyOrderWares";
    /**
     * 註冊im
     */
    public static final String REG_IM_USER = BASE_HTTP_API + "Store.RegAliBaiChuanIM";
    /**
     * APP版本升级
     */
    public static final String API_APP_VERSION_UPDATA = BASE_HTTP_API + "Systems.upgrade_check";

    /**
     * 获取店铺三方商城列表
     */
    public static final String API_OTHER_SANFAN_LIST = BASE_HTTP_API + "Tpmall.getStoreList";
    /**
     * 订单点评
     */
    public static final String API_ORDER_COMMENT = BASE_HTTP_API + "Sendorder.commentsOrder";
    /**
     * 修改订单
     */
    public static final String API_AMEND_GRAB_ORDER = BASE_HTTP_API + "Sendorder.modifyGrabOrderInfo";
    /**
     * 取消售後
     */
    public static final String API_CANCLE_SALAS=BASE_HTTP_API+"Refundorder.RevokeRefund";
    /**
     * 更新店铺类型
     */
    public static final String API_UPDATA_SHOP_STYE=BASE_HTTP_API+"Shop.modifyStoreType";
    /**
     * 常见问题列表
     */
    public static final String API_HELP_LIST=BASE_HTTP_API+"Help.lists";
    /**
     * 帮助内容
     */
    public static final String API_HELP_CONTENT=BASE_HTTP_API+"Help.details";

    /**
     * 店铺职位类型
     */
    public static final String[] SHOPTYPE = {"负责人", "运  营", "售  后", "财  务"};
    /*Appid*/
    public static final String APPID_NAME = "appid";
    /*设备ID*/
    public static final String DEVICEID = "device_id";
    /***
     * 登录授权
     */
    public static final String APP_OPENID = "openid";
    public static final String APP_VERSION = "app_version";
    /**
     * TOKEN
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * token过期时间
     */
    public static final String TOKEN_EXPORES = "token_expires";
    /***
     * 签名
     */
    public static final String APP_SIGN = "sign";

    public static final String SKIP_INFO_ID = "skip_info_identification";
    public static final int GRAB_ORDER = 0;//抢单
    public static final int JOIN_ORDER = 1;//接单
    public static final int CUSTOUTER_ODER = 2;//售后
    public static final int BILL_ORDER = 3;//发单
    public static final int OTHER_ORDER = 4;//三方

    public static final String SHOP_INFO = "shop_info";
    public static final String SHOP_LAT_INFO = "shop_lat_info";
    public static final String GD_MAP_ADDRESS_INFO = "gd_map_add_info";
    public static final String ADDRESS_JSON_DATA = "address_json_infos";
    public static final String SHOP_SID = "shop_sid";
    public static final String PAY_INFO="pay_info";
    public static final String SYSTEM_SETTING="system_setting";
    public static final String APK_INFO = "apk_info";


    /**
     * 店铺营业状态
     */
    public static final String[] SHOPBUSINESSSTATE = {"正常营业", "大量接单", "订单饱和", "隐藏店铺"};


    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;


    /**
     * 配送时间
     */
    public static final TypeEntity[] DvTime = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "上午送达（08:00-11:00）"),
            new TypeEntity(3, "中午送达（11:00-14:00）"),
            new TypeEntity(4, "下午送达（14:00-18:00）"),
            new TypeEntity(5, "自定义"),
    };
    public static final TypeEntity[] DvNewTime = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "上午送达（08:00-11:00）"),
            new TypeEntity(3, "中午送达（11:00-14:00）"),
            new TypeEntity(4, "下午送达（14:00-18:00）"),
    };
    /**
     * 配送时间
     */
    public static final TypeEntity[] DvTime1 = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "中午送达（11:00-14:00）"),
            new TypeEntity(3, "下午送达（14:00-18:00）"),
            new TypeEntity(4, "自定义"),
    };
    public static final TypeEntity[] DvNewTime1 = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "中午送达（11:00-14:00）"),
            new TypeEntity(3, "下午送达（14:00-18:00）"),
    };
    /**
     * 配送时间
     */
    public static final TypeEntity[] DvTime2 = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "下午送达（14:00-18:00）"),
            new TypeEntity(3, "自定义"),
    };
    public static final TypeEntity[] DvNewTime2 = {
            new TypeEntity(1, "当天送达（08:00-18:00）"),
            new TypeEntity(2, "下午送达（14:00-18:00）"),
    };

    public static String APP_ID = "wx342a8d33d7c586d8";


    public static final String BIDDING_BUT_HINT = "bidding_but_hint";
    public static final String BIDDING_SHOP_HINT = "bidding_shop_hint";
    public static final String BIDDING_SHOP_LIST_HINT = "bidding_shop_list_hint";
    public static final String SHOP_MSG_HINT = "shop_msg_hint";


    // 关闭
    public static final String REMIND_START_TIME = "REMIND_START_TIME"; // 提醒开始时间
    public static final String REMIND_STOP_TIME = "REMIND_STOP_TIME"; // 提醒结束时间

    public static final String REMIND_VOICE = "REMIND_VOICE"; // 提醒声音: 1开启 0关闭
    public static final String REMIND_LIBRATE = "REMIND_LIBRATE"; // 提醒振动 1开启 0

    public static final String SOUND_NEW="sound_new";
    public static final String SOUND_STATUS="sound_status";
    public static final String SOUND_SALES="sound_sales";
    public static final String SOUND_ONE_BY_ONE="sound_one";

    /**
     * jgpush
     */
    public static final String JG_PUSH = "jg_push";

    public static final String APP_CODE = "code";
    public static final String APP_NAME = "name";

    public static final int SHOP_GUIDE_INFO_SUCCESS=201;
    public static final int SHOP_GUIDE_CONTACT_SUCCESS=202;
    public static final int SHOP_GUIDE_RE_SUCCESS=203;
    public static final int SHOP_GUIDE_PASS_SUCCESS=204;

    //支付时间倒计时
    public static final String PAY_TIME="pay_time";

}
