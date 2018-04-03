package com.zhuandanbao.app.im;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.zhuandanbao.app.R;

/**
 * Created by BFTECH on 2016/12/14.
 */
public class ImTopTitleUi extends IMConversationListUI {
    public ImTopTitleUi(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 返回会话列表页面自定义标题
     *
     * @param fragment
     * @param context
     * @param inflater
     * @return
     */
    @Override
    public View getCustomConversationListTitle(Fragment fragment, final Context context, LayoutInflater inflater) {
        final Activity activity = (Activity) context;
        View view = inflater.inflate(R.layout.nvs_top_layout, null);
        TextView back = (TextView) view.findViewById(R.id.nvs_back);
        back.setVisibility(View.VISIBLE);
        back.setText("返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        TextView title = (TextView) view.findViewById(R.id.nvs_title);
        title.setText("最近联系人");
        title.setVisibility(View.VISIBLE);
        TextView right = (TextView) view.findViewById(R.id.nvs_right);
        right.setText("好友");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ImLinkManActivity.class));
            }
        });
        return view;
    }

    /**
     * 是否隐藏会话列表标题栏
     *
     * @param fragment
     * @return true: 隐藏标题栏， false：不隐藏标题栏
     */
    @Override
    public boolean needHideTitleView(Fragment fragment) {
        return super.needHideTitleView(fragment);
    }


    /**
     * 是否隐藏无网络提醒View
     *
     * @param fragment
     * @return true: 隐藏无网络提醒，false：不隐藏无网络提醒
     */
    @Override
    public boolean needHideNullNetWarn(Fragment fragment) {
        return super.needHideNullNetWarn(fragment);
    }

    /**
     * 返回自定义置顶回话的背景色(16进制字符串形式)
     *
     * @return
     */
    @Override
    public String getCustomTopConversationColor() {
        return super.getCustomTopConversationColor();
    }

    /**
     * 是否支持下拉刷新
     *
     * @return 反回true，支持下拉刷新，false不支持，默认值为true
     */
    @Override
    public boolean getPullToRefreshEnabled() {
        return true;
    }

    /**
     * 返回true，支持搜索，false不支持，默认值为true
     *
     * @param fragment
     * @return
     */
    @Override
    public boolean enableSearchConversations(Fragment fragment) {
        return true;
    }

    /**
     * 该方法可以构造一个会话列表为空时的展示View
     *
     * @return empty view
     */
    @Override
    public View getCustomEmptyViewInConversationUI(Context context) {
        /** 以下为示例代码，开发者可以按需返回任何view*/
        TextView textView = new TextView(context);
        textView.setText("还没有会话哦，\n快去找人聊聊吧!");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        return textView;
    }

    /**
     * 返回设置最近联系人界面背景的资源Id,返回0则使用默认值
     *
     * @return 资源Id
     */
    @Override
    public int getCustomBackgroundResId() {
        return 0;
    }


//    /**
//     * 返回自定义会话和群会话的默认头像 如返回本地的 R.drawable.test
//     * 该方法只适用设置自定义会话和群会话的头像，设置单聊会话头像请参考{@link com.taobao.openimui.sample.UserProfileSampleHelper}
//     * @param fragment      会话页面fragment
//     * @param conversation  会话对象
//     * @return 头像资源id
//     */
//
//    @Override
//    public int getConversationDefaultHead(Fragment fragment, YWConversation conversation) {
//        if (conversation.getConversationType() == YWConversationType.Custom) {
//            YWCustomConversationBody body = (YWCustomConversationBody) conversation.getConversationBody();
//            String conversationId = body.getIdentity();
//            if(conversationId.equals(FragmentTabs.SYSTEM_TRIBE_CONVERSATION)){
//                return R.drawable.aliwx_tribe_head_default;
//            }else  if(conversationId.equals(FragmentTabs.SYSTEM_FRIEND_REQ_CONVERSATION)){
//                return R.drawable.aliwx_head_default;
//            }else{
//                return R.drawable.aliwx_head_default;
//            }
//        }
//        return 0;
//    }
}
