package com.zhuandanbao.app.mvp.callback;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.utils.AppUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.Sign;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by BFTECH on 2016/12/5.
 */
public abstract class MJsonCallback<T> extends AbsCallback<T> {

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
//        request.headers("header1", "HeaderValue1")//
//                .params("params1", "ParamsValue1")//
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");

        //主要用于在所有请求之前添加公共的请求头或请求参数，例如登录授权的 token,使用的设备信息等,可以随意添加,也可以什么都不传
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.APPID_NAME, Constants.APPID);
        map.put(Constants.DEVICEID, PerfHelper.getStringData(Constants.DEVICEID));
        map.put(Constants.ACCESS_TOKEN, PerfHelper.getStringData(Constants.ACCESS_TOKEN));
        map.put("app_type", "1");
        map.put("app_version", AppUtils.getVersionName());
        map.put("format", "JSON");
        map.put(Constants.APP_SIGN, Sign.createSign(map));
        request.params(map);
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     * <pre>
     * OkGo.get(Urls.URL_METHOD)//
     *     .tag(this)//
     *     .execute(new DialogCallback<BaseResponse<ServerModel>>(this) {
     *          @Override
     *          public void onSuccess(BaseResponse<ServerModel> responseData, Call call, Response response) {
     *              handleResponse(responseData.data, call, response);
     *          }
     *     });
     * </pre>
     */
    @Override
    public T convertSuccess(Response response) throws Exception {
        //以下代码是通过泛型解析实际参数,泛型必须传
        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.Login> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.Login
        Type type = params[0];
//        if (type == BaseResponse.class) {
//            BaseResponse baseResponse = JSON.parseObject(response.body().string(), type);
//            MLog.e("convertSuccess:code=" + baseResponse.code + "message=" + baseResponse.message);
//        }
        T data=null;
        try{
             data = JSON.parseObject(response.body().string(), type);
        }catch (Exception e){
            new IllegalStateException("网络错误");//這句話你看怎麼寫
        }
        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
        //有数据类型，表示有data
//        T data = Convert.fromJson(jsonReader, type);
//        MLog.e("type="+type.toString());
        response.close();
        return data;
    }
}
