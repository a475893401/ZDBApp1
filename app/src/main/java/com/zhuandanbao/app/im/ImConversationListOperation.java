package com.zhuandanbao.app.im;

import android.support.v4.app.Fragment;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListOperation;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.zhuandanbao.app.utils.MLog;


/**
 * 最近会话界面的定制点(根据需要实现相应的接口来达到自定义会话列表界面)，不设置则使用openIM默认的实现
 * 调用方设置的回调，必须继承BaseAdvice 根据不同的需求实现 不同的 开放的 Advice
 * com.alibaba.mobileim.aop.pointcuts包下开放了不同的Advice.通过实现多个接口，组合成对不同的ui界面的定制
 * 这里设置了自定义会话的定制
 * 1.CustomConversationAdvice 实现自定义会话的ui定制
 * 2.CustomConversationTitleBarAdvice 实现自定义会话列表的标题的ui定制
 * <p/>
 * 另外需要在application中将这个Advice绑定。设置以下代码
 * AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT, CustomChattingAdviceDemo.class);
 *
 * @author jing.huai
 */
public class ImConversationListOperation extends IMConversationListOperation {

    public ImConversationListOperation(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 定制会话点击事件，该方法可以定制所有的会话类型，包括单聊、群聊、自定义会话
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的点击事件  false：使用SDK默认的点击事件
     */
    @Override
    public boolean onItemClick(Fragment fragment, YWConversation conversation) {
        YWConversationType type = conversation.getConversationType();
//        IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了"+type);
        if (type == YWConversationType.P2P){
            //TODO 单聊会话点击事件
            MLog.e("单聊会话点击事件");
            return false;
        } else if (type == YWConversationType.Tribe){
            MLog.e("群会话点击事件");
            //TODO 群会话点击事件
            return true;
        } else if (type == YWConversationType.Custom){
            MLog.e("自定义会话点击事件");
            //TODO 自定义会话点击事件
            return true;
        }else if (type== YWConversationType.SHOP){
            MLog.e("SHOP");
            return true;
        }else if (type== YWConversationType.unknow){
            MLog.e("unknow");
            return true;
        }else if (type== YWConversationType.HJTribe){
            MLog.e("HJTribe");
            return true;
        }else if (type== YWConversationType.CustomViewConversation){
            MLog.e("CustomViewConversation");
            return true;
        }
        return false;
    }


    /**
     * 定制会话长按事件，该方法可以定制所有的会话类型
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的长按事件  false：使用SDK默认的长按事件
     */
    @Override
    public boolean onConversationItemLongClick(Fragment fragment, YWConversation conversation) {
//        YWConversationType type = conversation.getConversationType();
//        if (type == YWConversationType.P2P){
//            //TODO 单聊会话长按事件
//            return true;
//        } else if (type == YWConversationType.Tribe){
//            //TODO 群会话长按事件
//            return true;
//        } else if (type == YWConversationType.Custom){
//            //TODO 自定义会话长按事件
//            return true;
//        }
        return false;
    }

}
