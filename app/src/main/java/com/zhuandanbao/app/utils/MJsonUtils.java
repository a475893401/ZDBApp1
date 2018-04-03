package com.zhuandanbao.app.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lzy.imagepicker.bean.ImageItem;
import com.zhuandanbao.app.entity.AdInfoEntity;
import com.zhuandanbao.app.entity.AlipayEntity;
import com.zhuandanbao.app.entity.ApproveSuccessEntity;
import com.zhuandanbao.app.entity.BackListEntity;
import com.zhuandanbao.app.entity.BackNameEntity;
import com.zhuandanbao.app.entity.BiddingOrderInfoEntity;
import com.zhuandanbao.app.entity.BiddingShopListEntity;
import com.zhuandanbao.app.entity.BillListEntity;
import com.zhuandanbao.app.entity.BillOrderShopEntity;
import com.zhuandanbao.app.entity.ConditionEntity;
import com.zhuandanbao.app.entity.FinanceEntity;
import com.zhuandanbao.app.entity.GradListEntity;
import com.zhuandanbao.app.entity.HelpListEntity;
import com.zhuandanbao.app.entity.HelpTitleEntity;
import com.zhuandanbao.app.entity.InfoLogEntity;
import com.zhuandanbao.app.entity.NewsEntity;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.OtherInfoEntity;
import com.zhuandanbao.app.entity.OtherInfoZdbInfoEntity;
import com.zhuandanbao.app.entity.OtherListEntity;
import com.zhuandanbao.app.entity.SaleListEntity;
import com.zhuandanbao.app.entity.SalesInfoEntity;
import com.zhuandanbao.app.entity.SalesTypeEntity;
import com.zhuandanbao.app.entity.ShopAuthInfoEntity;
import com.zhuandanbao.app.entity.ShopDetailsEntity;
import com.zhuandanbao.app.entity.ShopImageEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.ShopMsgInfoEntity;
import com.zhuandanbao.app.entity.ShopMsgListEntity;
import com.zhuandanbao.app.entity.ShopTouchItemEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.entity.TakingListEntity;
import com.zhuandanbao.app.entity.UpVersionDataEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.entity.VipRegisterEntity;
import com.zhuandanbao.app.entity.WeixinEntity;
import com.zhuandanbao.app.entity.WorkOrderContentEntity;
import com.zhuandanbao.app.entity.WorkOrderEntity;
import com.zhuandanbao.app.entity.WorkOrderInfoListEntity;
import com.zhuandanbao.app.entity.WorkOrderTypeEntity;
import com.zhuandanbao.app.entity.WuLiuEntity;
import com.zhuandanbao.app.entity.ZhuijiaHonestEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class MJsonUtils {

    /**
     * string的数组转换成map
     *
     * @param content 需要转化的参数
     * @return
     */
    public static HashMap<String, Object> perJson(String content) {
        HashMap<String, Object> map = JSON.parseObject(content, new TypeReference<HashMap<String, Object>>() {
        });
        return map;
    }

    /**
     * 拆分map
     *
     * @param params 需要交换的键值对
     * @return 返回list
     */
    public static List<String> tearOpenMap(HashMap<String, Object> params) {
        Map<String, Object> treeMap = new TreeMap<>(params);
        List<String> list = new ArrayList<>();
        for (Map.Entry entry : treeMap.entrySet()) {
            String k = entry.getKey().toString();
            if (k != null && !k.isEmpty()) {
                list.add(k);
            }
        }
        return list;
    }

    /**
     * 登陸信息解析
     *
     * @param strJson
     * @return
     */
    public static VipLoginInfoEntity josnToVipLoginInfoEntity(String strJson) {
        VipLoginInfoEntity info = new VipLoginInfoEntity();
        info = JSON.parseObject(strJson, VipLoginInfoEntity.class);
        return info;
    }

    /**
     * 註冊信息解析
     *
     * @param strJson
     * @return
     */
    public static VipRegisterEntity josnToVipRegisterEntity(String strJson) {
        VipRegisterEntity info = new VipRegisterEntity();
        info = JSON.parseObject(strJson, VipRegisterEntity.class);
        return info;
    }

    /**
     * 信息解析
     *
     * @param strJson
     * @return
     */
    public static ShopInfoEntity josnToShopInfoEntity(String strJson) {
        ShopInfoEntity info = new ShopInfoEntity();
        info = JSON.parseObject(strJson, ShopInfoEntity.class);
        return info;
    }

    /**
     * 信息解析
     *
     * @param strJson
     * @return
     */
    public static ShopInfoItemEntity josnToShopInfoItemEntity(String strJson) {
        ShopInfoItemEntity info = new ShopInfoItemEntity();
        info = JSON.parseObject(strJson, ShopInfoItemEntity.class);
        return info;
    }

    /**
     * 解析追加信息
     *
     * @return
     */
    public static ZhuijiaHonestEntity jsonToZhuijiaHonestEntity(String jsonStr) {
        ZhuijiaHonestEntity zuijiaEntity = new ZhuijiaHonestEntity();
        try {
            JSONObject data = new JSONObject(jsonStr);
            JSONArray creditLevel = data.optJSONArray("creditLevel");
            JSONObject m = creditLevel.optJSONObject(0);
            zuijiaEntity.amount = m.optString("amount");
            JSONObject storeCreditInfo = data.optJSONObject("storeCreditInfo");
            zuijiaEntity.assure_credit = storeCreditInfo.optString("assure_credit");
            zuijiaEntity.available_balance = storeCreditInfo.optString("available_balance");
            zuijiaEntity.credit_money = storeCreditInfo.optString("credit_money");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return zuijiaEntity;
    }

    /**
     * 成功认证信息
     *
     * @param josnStr
     * @return
     */
    public static ApproveSuccessEntity josnToApproveSuccessEntity(String josnStr) {
        ApproveSuccessEntity info = new ApproveSuccessEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            info = JSON.parseObject(object.optString("authentication_data"), ApproveSuccessEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 上传图片返回数据
     *
     * @param josnStr
     * @return
     */
    public static List<ImageItem> jsonToImageItem(String josnStr) {
        List<ImageItem> list = new ArrayList<>();
        list.addAll(JSON.parseArray(josnStr, ImageItem.class));
        return list;
    }

    /**
     * 店铺联系人
     *
     * @return
     */
    public static List<ShopTouchItemEntity> jsonToShopTouchItemEntity(String jsonStr,ShopInfoItemEntity info) {
        List<ShopTouchItemEntity> list = new ArrayList<>();
        list.addAll(JSON.parseArray(jsonStr, ShopTouchItemEntity.class));
        if (null!=info) {
            ShopTouchItemEntity shopTouchItemEntity = new ShopTouchItemEntity();
            shopTouchItemEntity.setPosition("店主");
            shopTouchItemEntity.setUser(info.getShopkeeper());
            shopTouchItemEntity.setMobile(info.getMobile());
            list.add(0, shopTouchItemEntity);
        }
        return list;
    }

    /**
     * 店鋪形象照
     */
    public static ShopImageEntity jsonToShopImageEntity(String josnStr) {
        ShopImageEntity info = new ShopImageEntity();
        if (StrUtils.isNull(josnStr)){
            info.setPhoto_1("");
            info.setPhoto_2("");
            info.setPhoto_3("");
            info.setPhoto_4("");
            info.setPhoto_5("");
        }else {
            info = JSON.parseObject(josnStr, ShopImageEntity.class);
        }
        return info;
    }

    public static ShopAuthInfoEntity jsonToShopAuthInfoEntity(String jsonStr) {
        ShopAuthInfoEntity info = new ShopAuthInfoEntity();
        info = JSON.parseObject(jsonStr, ShopAuthInfoEntity.class);
        return info;
    }


    public static List<NewsEntity> jsonToNewsEntity(String josnStr) {
        List<NewsEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            list = JSON.parseArray(object.optString("art_lists"), NewsEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<ShopMsgListEntity> jsonToShopMsgListEntity(String josnStr) {
        List<ShopMsgListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            list.addAll(JSON.parseArray(object.optString("art_lists"), ShopMsgListEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ShopMsgInfoEntity jsonToShopMsgInfoEntity(String josnStr) {
        ShopMsgInfoEntity infoEntity = new ShopMsgInfoEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            infoEntity = JSON.parseObject(object.optString("artdata"), ShopMsgInfoEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infoEntity;
    }

    public static AdInfoEntity jsonToAdInfoEntity(String josnStr) {
        AdInfoEntity infoEntity = new AdInfoEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            infoEntity = JSON.parseObject(object.optString("artdata"), AdInfoEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infoEntity;
    }

    public static List<GradListEntity> jsonToGradListEntity(String jsonStr) {
        List<GradListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            list = JSON.parseArray(object.optString("orderlists"), GradListEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<TakingListEntity> jsonToTakingListEntity(String jsonStr) {
        List<TakingListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            list = JSON.parseArray(object.optString("orderlists"), TakingListEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<OrderItemEntity> jsonToOrderItemEntity(String jsonStr) {
        List<OrderItemEntity> list = new ArrayList<>();
        list = JSON.parseArray(jsonStr, OrderItemEntity.class);
        return list;
    }

    public static List<BillListEntity> jsonToBillListEntity(String jsonStr) {
        List<BillListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            list = JSON.parseArray(object.optString("orderlists"), BillListEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<OtherListEntity> jsonToOtherListEntity(String jsonStr) {
        List<OtherListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            list = JSON.parseArray(object.optString("orderlists"), OtherListEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<SaleListEntity> jsonToSaleListEntity(String jsonStr) {
        List<SaleListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            if (object.has("refundLists")) {
                list = JSON.parseArray(object.optString("refundLists"), SaleListEntity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static FinanceEntity jsonToFinanceEntity(String jsonStr) {
        FinanceEntity financeEntity = new FinanceEntity();
        try {
            JSONObject object = new JSONObject(jsonStr);
            if (object.has("basicInfo")) {
                financeEntity = JSON.parseObject(object.optString("basicInfo"), FinanceEntity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return financeEntity;
    }

    public static List<ConditionEntity> jsonToConditionEntity(String josnStr) {
        List<ConditionEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            list.addAll(JSON.parseArray(object.optString("inconformity"), ConditionEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<InfoLogEntity> jsonToInfoLogEntity(String josnStr) {
        List<InfoLogEntity> logEntities = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            logEntities.addAll(JSON.parseArray(object.optString("logistics_tracking"), InfoLogEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return logEntities;

    }

    public static OrderDataEntity jsonToLogOrderDataEntity(String josnStr) {
        OrderDataEntity orderDataEntity = new OrderDataEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            orderDataEntity = JSON.parseObject(object.optString("order_data"), OrderDataEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderDataEntity;

    }

    public static List<WuLiuEntity> jsonToWuLiuEntity(String josnStr) {
        List<WuLiuEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            list = JSON.parseArray(object.optString("logistics_list"), WuLiuEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 獲取第一張圖片
     *
     * @return
     */
    public static String josnToGoodsFirstImg(String josnStr) {
        if (StrUtils.isNull(josnStr)) {
            return null;
        }
        List<String> list = JSON.parseArray(josnStr, String.class);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    public static List<BiddingShopListEntity> jsonToBiddingShopListEntity(String josnStr) {
        List<BiddingShopListEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            list = JSON.parseArray(object.optString("biddingList"), BiddingShopListEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static BiddingOrderInfoEntity jsonToBiddingOrderInfoEntity(String josnStr) {
        BiddingOrderInfoEntity biddingOrderInfoEntity = new BiddingOrderInfoEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            biddingOrderInfoEntity = JSON.parseObject(object.optString("orderInfo"), BiddingOrderInfoEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return biddingOrderInfoEntity;
    }

    public static OtherInfoEntity jsonToOtherInfoEntity(String josnStr) {
        OtherInfoEntity otherInfoEntity = new OtherInfoEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            otherInfoEntity = JSON.parseObject(object.optString("orderInfo"), OtherInfoEntity.class);
            otherInfoEntity.setZhuandanOrder(object.optString("zhuandanOrder"));
            MLog.e("zhuandanOrder==" + object.optString("zhuandanOrder"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return otherInfoEntity;
    }

    public static OtherInfoZdbInfoEntity jsonToOtherInfoZdbInfoEntity(String josnStr) {
        OtherInfoZdbInfoEntity otherInfoEntity = new OtherInfoZdbInfoEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            if (StrUtils.isNotNull(object.optString("zhuandanOrder"))) {
                otherInfoEntity = JSON.parseObject(object.optString("zhuandanOrder"), OtherInfoZdbInfoEntity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return otherInfoEntity;
    }

    public static List<ImageItem> stingToImageItem(String string) {
        List<ImageItem> itemList = new ArrayList<>();
        if (StrUtils.isNotNull(string)) {
            List<String> list = JSON.parseArray(string, String.class);
            if (list.size() > 0) {
                for (String s : list) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.url = s;
                    itemList.add(imageItem);
                }
            }
        }
        return itemList;
    }

    public static ShopDetailsEntity jsonToShopDetailsEntity(String josnStr) {
        ShopDetailsEntity otherInfoEntity = new ShopDetailsEntity();
        try {
            JSONObject object = new JSONObject(josnStr);
            if (StrUtils.isNotNull(object.optString("shopInfo"))) {
                otherInfoEntity = JSON.parseObject(object.optString("shopInfo"), ShopDetailsEntity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return otherInfoEntity;
    }


    public static List<BillOrderShopEntity> jsonToBillOrderShopEntity(String josnStr) {
        List<BillOrderShopEntity> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(josnStr);
            if (StrUtils.isNotNull(object.optString("shopLists"))) {
                list.addAll(JSON.parseArray(object.optString("shopLists"), BillOrderShopEntity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static AlipayEntity jsonToAlipayEntity(String jsonStr) {
        AlipayEntity alipayEntity = new AlipayEntity();
        try {
            alipayEntity = JSON.parseObject(jsonStr, AlipayEntity.class);
            JSONObject object = new JSONObject(alipayEntity.getPayConf());
            alipayEntity.setPartner(object.optString("partner"));
            alipayEntity.setRsa_private(object.optString("rsa_private"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return alipayEntity;
    }

    public static WeixinEntity jsonToWeixinEntity(String jsonStr) {
        WeixinEntity weixinEntity = new WeixinEntity();
        try {
            weixinEntity = JSON.parseObject(jsonStr, WeixinEntity.class);
            JSONObject payConf = new JSONObject(weixinEntity.getPayConf());
            weixinEntity.setApikey(payConf.optString("apikey"));
            weixinEntity.setAppid(payConf.optString("appid"));
            weixinEntity.setMchid(payConf.optString("mchid"));
            JSONObject wechat_data = new JSONObject(weixinEntity.getWechat_data());
            weixinEntity.setAppid(wechat_data.optString("appid"));
            weixinEntity.setMch_id(wechat_data.optString("mch_id"));
            weixinEntity.setNonce_str(wechat_data.optString("nonce_str"));
            weixinEntity.setPrepay_id(wechat_data.optString("prepay_id"));
            weixinEntity.setResult_code(wechat_data.optString("result_code"));
            weixinEntity.setReturn_code(wechat_data.optString("return_code"));
            weixinEntity.setReturn_msg(wechat_data.optString("return_msg"));
            weixinEntity.setSign(wechat_data.optString("sign"));
            weixinEntity.setTimeStamp(wechat_data.optString("timeStamp"));
            weixinEntity.setTrade_type(wechat_data.optString("trade_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weixinEntity;
    }

    public static String jsonToTradeLog(String josnStr) {
        String str = null;
        try {
            JSONObject object = new JSONObject(josnStr);
            str = object.optString("logslists");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static List<BackListEntity> jsonToBackListEntity(String jsonStr) {
        List<BackListEntity> listEntities = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            listEntities.addAll(JSON.parseArray(object.optString("WithdrawCardList"), BackListEntity.class));
            for (int i = 0; i < listEntities.size(); i++) {
                BackListEntity info = listEntities.get(i);
                if (i == 0) {
                    info.setCheck(true);
                }else {
                    info.setCheck(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEntities;
    }

    public static List<BackNameEntity> jsonToBackNameEntity(String jsonStr, String backCode) {
        List<BackNameEntity> listEntities = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(jsonStr);
            listEntities.addAll(JSON.parseArray(object.optString("cardList"), BackNameEntity.class));
            for (int i = 0; i < listEntities.size(); i++) {
                BackNameEntity info = listEntities.get(i);
                if (info.getCode().equals(backCode)) {
                    info.setCheck(true);
                } else {
                    info.setCheck(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEntities;
    }

    public static List<SalesTypeEntity>  josnToSalesTypeEntity(String josnStr){
        List<SalesTypeEntity> list=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(josnStr);
            list.addAll(JSON.parseArray(object.optString("issue_type_list"),SalesTypeEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static SalesInfoEntity josnToSalesInfoEntity(String josnStr){
        SalesInfoEntity salesInfoEntity=new SalesInfoEntity();
        salesInfoEntity=JSON.parseObject(josnStr,SalesInfoEntity.class);
        return salesInfoEntity;
    }

    public static List<WorkOrderEntity>  josnToWorkOrderEntity(String josnStr){
        List<WorkOrderEntity> list=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(josnStr);
            list.addAll(JSON.parseArray(object.optString("TicketLists"),WorkOrderEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<WorkOrderInfoListEntity>  josnToWorkOrderInfoListEntity(String josnStr){
        List<WorkOrderInfoListEntity> list=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(josnStr);
            list.addAll(JSON.parseArray(object.optString("replyLists"),WorkOrderInfoListEntity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static WorkOrderContentEntity josnToWorkOrderContentEntity(String josnStr){
        WorkOrderContentEntity workOrderContentEntity=new WorkOrderContentEntity();
        try {
            JSONObject object=new JSONObject(josnStr);
            workOrderContentEntity=JSON.parseObject(object.optString("ticketData"),WorkOrderContentEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workOrderContentEntity;
    }


    public static List<WorkOrderTypeEntity> josnToWorkOrderTypeEntity(String josnStr,boolean isItem){
        List<WorkOrderTypeEntity> list=new ArrayList<>();
        if (isItem){
            MLog.e(josnStr+"============");
            list.addAll(JSON.parseArray(josnStr,WorkOrderTypeEntity.class));
        }else {
            try {
                JSONObject object=new JSONObject(josnStr);
                JSONArray array=object.optJSONArray("CategoryLists");
                for(int i=0;i<array.length();i++){
                    JSONObject data=array.optJSONObject(i);
                    MLog.e("data.optString()==="+data.optString("_child"));
                    WorkOrderTypeEntity workOrderTypeEntity=JSON.parseObject(array.optString(i),WorkOrderTypeEntity.class);
                    workOrderTypeEntity.setItem(data.optString("_child"));
                    list.add(workOrderTypeEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static SystemSettingEntity jsonToSystemSettingEntity(String josnStr){
        SystemSettingEntity info=new SystemSettingEntity();
        try {
            JSONObject object=new JSONObject(josnStr);
            info=JSON.parseObject(object.optString("gloab_data"),SystemSettingEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static UpVersionDataEntity personUpDataEntity(String jsonStr) {
        UpVersionDataEntity info = new UpVersionDataEntity();
        info=JSON.parseObject(jsonStr,UpVersionDataEntity.class);
        return info;
    }

    public static List<HelpTitleEntity>  personHelpTitleEntity(String josnStr){
        List<HelpTitleEntity> list=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(josnStr);
            if (StrUtils.isNotNull(object.optString("help_category"))) {
                list.addAll(JSON.parseArray(object.optString("help_category"), HelpTitleEntity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<HelpListEntity> personHelpListEntity(String jsonStr){
        List<HelpListEntity> list=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(jsonStr);
            if (StrUtils.isNotNull(object.optString("help_lists"))){
                list.addAll(JSON.parseArray(object.optString("help_lists"),HelpListEntity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
