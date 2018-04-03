package com.zhuandanbao.app.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zhuandanbao.app.entity.AlipayEntity;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Alipay {

    //2088501751439568
    private static String PARTNER = "";
    //MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALA+Tpla6Rzmc5Fauq8ovRQpUOC6mL78m0Oj0oHUwCssm5TvZ3vC72wPJkAsRlVXjlxkOIC9XlOIJbQjAQGdGVxKNzBoCgFn/tVsdScg6ms+HfJezer/cipQy+QudnW9OukmnIcgCnMGD9xhTSvoTJDjlMt0qx/90Sq/EE/P6lxtAgMBAAECgYBfv+oJzOc66US+InGr+dEHpA6pmRalJC3iSJ10Jbalfh2kg5BQH67doTlhwewQL3pLXbOI1djqPtxlCmNgkg4VOzgO2nZ67NLftWdb2U2YtZV5ZNo6aKqNuOoSgspceO7Ea44fotJhnhq395palZpOhUnji6XbtGi0lqd5eVWxrQJBAOZrKqm9DwS4mzXkQbECm5p7rSVDd/9WDrjj0jAMDCRW5r702zfmQB/3sOGAhnZ6TzV6/86fyJ8gPihhdxwtW+cCQQDDz2a2DvWDZLzpNynH0IBCodqCJ8WSN9RjLwhU2PxSTClFkDHdNmTAM2BZwToptNWNdIjV0353mfzzUYp57BqLAkEAr8SmSc8fisFsHOMfPXE409JuVVOvUQcLufyIFQDLrljgMmDEbVLmLbyboJmTeQN6Mti+FJeMyd4lrFYVL1hmlwJACbggvAawDw6QBe90BnQF+ci45N7+gox84VNzUWrX02nuXoKSxw0tIRErxii+L1XHh9bx99MgouUk1hsFCZo7kwJBAOYaxEsBho9X1Dsychnn14/1YgjeCk50+ZEHZgEHZbT/cUKkBQTWWsJQxudTMavlZlh5/hW9mKdtMiaL0lRzL8I=
    private static String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Context context = null;
    private String orderNO = null;
    private String orderName = null;
    private Handler handler = null;
    private String orderFEE = "";
    private String notify_url = "";
    private String pay_desc = "";
    private static Alipay externalPartner;

    // http://pay.xiangsidi.com/notify_alipay

    /**
     * 支付宝快捷支付
     *
     * @param context   上下文
     * @param orderNO   订单号
     * @param mHandler  回调响应句柄 "9000", "操作成功"; "4000", "系统异常" "4001","数据格式不正确"
     *                  "4003","该用户绑定的支付宝账户被冻结或不允许支付" "4004", "该用户已解除绑定" "4005",
     *                  "绑定失败或没有绑定" "4006", "订单支付失败" "4010","重新绑定账户" "6000",
     *                  "支付服务正在进行升级操作" "6001", "用户中途取消支付操作" "7001", "网页支付失败"
     * @param orderFEE  价格
     * @param orderName 商品名
     */
    private Alipay(Context context, String orderName, String orderNO,
                   Handler mHandler, String orderFEE, String notify_url,
                   String pay_desc, String partner, String rsa_private) {
        this.context = context;
        this.orderNO = orderNO;
        this.handler = mHandler;
        this.orderFEE = orderFEE;
        this.orderName = orderName;
        this.notify_url = notify_url;
        this.pay_desc = pay_desc;
        PARTNER = partner;
        RSA_PRIVATE = rsa_private;
    }

    /**
     * 支付宝快捷支付调用类
     *
     * @param context  上下文
     * @param mHandler 回调响应句柄
     */
    public static Alipay getInstance(Context context, Handler mHandler, AlipayEntity alipayEntity) {
        externalPartner = new Alipay(context, alipayEntity.getSubject(), alipayEntity.getPay_sn(), mHandler,
                alipayEntity.getPay_amount(), alipayEntity.getNotify_url(), alipayEntity.getPay_desc(), alipayEntity.getPartner(), alipayEntity.getRsa_private());
        return externalPartner;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
//		check();
        if (StrUtils.isNull(PARTNER) || StrUtils.isNull(RSA_PRIVATE)) {
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(orderName, pay_desc, orderFEE, notify_url);
        // 对订单做RSA 签名
        MLog.d("", "______________________SIGN=" + orderInfo);
        String sign = sign(orderInfo);
        MLog.d("", "______________________SIGN=" + sign);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask((Activity) context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price,
                               String notify_url) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + PARTNER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNO + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
