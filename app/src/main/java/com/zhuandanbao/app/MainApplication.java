package com.zhuandanbao.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhuandanbao.app.adapter.UILImageLoader;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.im.ImChattingOperationCustom;
import com.zhuandanbao.app.im.ImChattingUICustom;
import com.zhuandanbao.app.im.ImConversationListOperation;
import com.zhuandanbao.app.im.ImNotificationInitUI;
import com.zhuandanbao.app.im.ImTopTitleUi;
import com.zhuandanbao.app.im.ImYWSDKGlobalConfig;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.utils.ACache;
import com.zhuandanbao.app.utils.FileUtil;
import com.zhuandanbao.app.utils.JGUtil;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by BFTECH on 2017/2/13.
 */

public class MainApplication extends Application {

    public static MainApplication sInstance;
    public static ACache cache;

    public static synchronized MainApplication getInstance() {
        if (sInstance == null) {
            sInstance = new MainApplication();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initOkGo();
        initImageLoader(this);
        initImagePicker();
        cache = ACache.get(new File(FileUtil.ACACHE_PATH));
        initJG();
        initAli();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //-----------------------------------------------------------------------------------//
        //必须调用初始化
        OkGo.init(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy0216/
                    .setCacheMode(CacheMode.NO_CACHE)
                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)
                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore());      //cookie持久化存储，如果cookie不过期，则一直有效
            if (MLog.isLogEnable) {
                OkGo.getInstance().debug("OkGo");
            }

            //可以设置https的证书,以下几种方案根据需要自己设置
//                    .setCertificates()                                  //方法一：信任所有证书（选一种即可）
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书（选一种即可）
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

            //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上,不需要就不要传
//                    .addCommonHeaders(headers)                                         //设置全局公共头
//                    .addCommonParams(params);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader(Context context) {
        // 缓存目录
        // 有SD卡
        // path=/sdcard/Android/data/com.example.universalimageloadertest/cache
        // 无SD卡 path=/data/data/com.example.universalimageloadertest/cache
//        File cacheDir = StorageUtils.getCacheDirectory(context);
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), FileUtil.IMG_LOADER_PATH);//自定义缓存路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // 加载图片的线程数
                .denyCacheImageMultipleSizesInMemory()
                // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSizePercentage(13)// default
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 设置磁盘缓存文件名称
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(100)//缓存的文件数量
                .tasksProcessingOrder(QueueProcessingType.LIFO) // 设置加载显示图片队列进程
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    /**
     * 硬盘缓存
     */
    private static DisplayImageOptions.Builder baseDiskBuilder = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true);// 载入图片前稍做延时可以提高整体滑动的流畅度;
    /**
     * 硬盘缓存
     */
    public static DisplayImageOptions optionOnDisk = baseDiskBuilder.build();


    public void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UILImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(9);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


    private void initJG() {
        JPushInterface.setDebugMode(MLog.isLogEnable);
        JPushInterface.init(this);
        JPushInterface.setLatestNotificationNumber(this, 3);//设置最近多少条
        int start = 8;
        int end = 22;
        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.REMIND_START_TIME))) {
            start = Integer.parseInt(PerfHelper.getStringData(Constants.REMIND_START_TIME));
        } else {
            PerfHelper.setInfo(Constants.REMIND_START_TIME, start + "");
        }
        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.REMIND_STOP_TIME))) {
            end = Integer.parseInt(PerfHelper.getStringData(Constants.REMIND_STOP_TIME));
        } else {
            PerfHelper.setInfo(Constants.REMIND_STOP_TIME, end + "");
        }
        JGUtil.setPushTime(this, start, end, 0, 1, 2, 3, 4, 5, 6);
    }


    public void initAli() {
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            YWAPI.init(this, ImLoginHelper.IM_APP_KEY);
        }
        //全局
        AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT, ImYWSDKGlobalConfig.class);
//        if (null!=PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME))
//        ImLoginHelper.getInstance().initIMKit(PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME),PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME)+ImLoginHelper.Im_PASSWORD);
        //回话ui
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ImTopTitleUi.class);
        //其中yourclass_2是继承自IMConversationListOperation的自定义类
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ImConversationListOperation.class);
        //通知栏ui
        AdviceBinder.bindAdvice(PointCutEnum.NOTIFICATION_POINTCUT, ImNotificationInitUI.class);
        //聊天底部
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, ImChattingOperationCustom.class);
        //單聊标题
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ImChattingUICustom.class);
    }
}
