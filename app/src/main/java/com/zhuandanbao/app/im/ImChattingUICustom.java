package com.zhuandanbao.app.im;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.utils.MLog;

/**
 * Created by BFTECH on 2016/12/16.
 */
public class ImChattingUICustom extends IMChattingPageUI {
    public ImChattingUICustom(Pointcut pointcut) {
        super(pointcut);
    }

    //单聊
    @Override
    public View getCustomTitleView(final Fragment fragment, final Context context, LayoutInflater inflater, final YWConversation conversation) {
        final Activity activity= (Activity) context;
        View view=inflater.inflate(R.layout.nvs_top_layout,null);
        TextView back= (TextView) view.findViewById(R.id.nvs_back);
        back.setVisibility(View.VISIBLE);
        back.setText("返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        TextView textView= (TextView) view.findViewById(R.id.nvs_title);
        textView.setMaxEms(6);
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setEnabled(true);
        textView.setVisibility(View.VISIBLE);
        String title = null;
        if (conversation.getConversationType() == YWConversationType.P2P) {
            YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation.getConversationBody();
            if (!TextUtils.isEmpty(conversationBody.getContact().getShowName())) {
                title = conversationBody.getContact().getShowName();
            } else {
                YWIMKit imKit = ImLoginHelper.getInstance().getInstance().getIMKit();
                IYWContact contact = imKit.getContactService().getContactProfileInfo(conversationBody.getContact().getUserId(), conversationBody.getContact().getAppKey());
                //生成showName，According to id。
                if (contact != null && !TextUtils.isEmpty(contact.getShowName())) {
                    title = contact.getShowName();
                }
            }
            //如果标题为空，那么直接使用Id
            if (TextUtils.isEmpty(title)) {
                title = conversationBody.getContact().getUserId();
            }
        } else {
            if (conversation.getConversationBody() instanceof YWTribeConversationBody) {
                title = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
                if (TextUtils.isEmpty(title)) {
                    title = "自定义";
                }
            } else {
                if (conversation.getConversationType() == YWConversationType.SHOP) { //为OpenIM的官方客服特殊定义了下、
                    title = AccountUtils.getShortUserID(conversation.getConversationId());
                }
            }
        }
        textView.setText(title);
        TextView right= (TextView) view.findViewById(R.id.nvs_right);
        right.setText("好友请求");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //参数为联系人Id、联系人的appKey，好友备注（可选）、验证信息和操作结果回调。
                YWMessage message= conversation.getLastestMessage();
                ImLoginHelper.getInstance().getIMKit().getContactService().addContact(message.getAuthorUserId(), ImLoginHelper.IM_APP_KEY, "", "", new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {
//                        Toast.makeText(fragment.getActivity(),"好友请求发送成功",Toast.LENGTH_LONG).show();
                        IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "好友请求发送成功");
                        MLog.e("onSuccess");
                    }

                    @Override
                    public void onError(int i, String s) {
                        MLog.e("code="+i+";s="+s);
                    }

                    @Override
                    public void onProgress(int i) {

                    }
                });
            }
        });
        return view;
    }

    /**
     * 返回单聊默认头像资源Id
     * @return
     *      0:使用SDK默认提供的
     */
    @Override
    public int getDefaultHeadImageResId() {
        return R.mipmap.img_im_head;
    }

}
