package com.zhuandanbao.app.mvp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.classic.common.MultipleStatusView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.ApproveActivity;
import com.zhuandanbao.app.activity.LoginActivity;
import com.zhuandanbao.app.activity.PayPasswordActivity;
import com.zhuandanbao.app.activity.ShopInfoActivity;
import com.zhuandanbao.app.activity.UpdataTouchActivity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * Created by BFTECH on 2017/1/23.
 */

public abstract class BaseDelegate extends AppDelegate {
    private KProgressHUD hud;
    private View view;

    @Override
    public int getBaseLayoutId() {
        return R.layout.base_layout;
    }

    @Override
    public void initWidget() {
//        content.removeAllViews();
        if (0 != getRootLayoutId()) {
            View view = LayoutInflater.from(this.getActivity()).inflate(getRootLayoutId(), null);
            content.addView(view);
        }
    }

    public void goToActivity(SkipInfoEntity infoEntity, Class c, int requestCode) {
        Intent intent = new Intent(this.getActivity(), c);
        if (null != infoEntity) {
            intent.putExtra(Constants.SKIP_INFO_ID, infoEntity);
        }
        getActivity().startActivityForResult(intent, requestCode);
    }

    public void showKprogress(String progressStr) {
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setWindowColor(getActivity().getResources().getColor(R.color.colorPrimary))
                .setAnimationSpeed(2)
                .setLabel(progressStr)
                .show();
    }

    public void hudDismiss() {
        if (null != hud) {
            hud.dismiss();
        }
    }

    /**
     * 接口返回错误
     *
     * @param context
     * @param code
     * @param message
     */
    public void errorHint(final Context context, int code, String message) {
        MLog.e("code=" + code + ";message=" + message);
        if (code == 40015 || code == 40016) {//登录
            goToActivity(null, LoginActivity.class, 0);
            getActivity().finish();
        } else {
            showErrorHint(message, 0, null);
        }
    }

    /**
     * 成功提示
     *
     * @param success
     * @param kPoNClick
     */
    public void showSuccessHint(String success, final KProgressDismissClickLister kPoNClick) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.mipmap.ic_svstatus_success);
        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setCustomView(imageView)
                .setLabel(success)
                .show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                if (null != kPoNClick) {
                    kPoNClick.onDismiss();
                }
            }
        }, 2000);
    }

    /**
     * 错误提示
     *
     * @param error
     * @param type      0:錯誤 1：警告
     * @param kPoNClick
     */
    public void showErrorHint(String error, int type, final KProgressDismissClickLister kPoNClick) {
        ImageView imageView = new ImageView(getActivity());
        if (type == 1) {
            imageView.setImageResource(R.mipmap.ic_error);
        } else {
            imageView.setImageResource(R.mipmap.ic_svstatus_error);
        }
        final KProgressHUD hud = KProgressHUD.create(getActivity())
                .setCustomView(imageView)
                .setLabel(error)
                .show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                if (null != kPoNClick) {
                    kPoNClick.onDismiss();
                }
            }
        }, 2000);
    }

    /**
     * 显示multipleStatusView 的那种试图
     *
     * @param multipleStatusView
     * @param status
     */
    public void showMultipleStatusView(MultipleStatusView multipleStatusView, int status, String emptyStr) {
        if (null == multipleStatusView) {
            MLog.e("multipleStatusView is null");
            return;
        }
        switch (status) {
            case MultipleStatusView.STATUS_CONTENT://內容
                multipleStatusView.showContent();
                break;
            case MultipleStatusView.STATUS_EMPTY://null
                multipleStatusView.showEmpty();
                if (StrUtils.isNotNull(emptyStr)) {
                    TextView textView = (TextView) multipleStatusView.findViewById(R.id.empty_view_tv);
                    textView.setText(emptyStr);
                }
                break;
            case MultipleStatusView.STATUS_ERROR://error
                multipleStatusView.showError();
                break;
            case MultipleStatusView.STATUS_NO_NETWORK://无网络
                multipleStatusView.showNoNetwork();
                break;
            case MultipleStatusView.STATUS_LOADING://zdb_loading
                multipleStatusView.showLoading();
                break;
        }
    }

    /**
     * 设置back
     *
     * @param backText
     * @param resBackImg
     * @param clickListener
     */
    public void setBack(String backText, int resBackImg, View.OnClickListener clickListener) {
        if (null == getToolbar()) {
            return;
        }
        getToolbar().setVisibility(View.VISIBLE);
        TextView back = (TextView) getToolbar().findViewById(R.id.nvs_back);
        if (null != back) {
            if (StrUtils.isNotNull(backText)) {
                back.setVisibility(View.VISIBLE);
                back.setText(backText);
            }
            if (resBackImg != 0) {
                back.setVisibility(View.VISIBLE);
                back.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(resBackImg), null, null, null);
            }
            if (null != clickListener) {
                back.setVisibility(View.VISIBLE);
                back.setOnClickListener(clickListener);
            } else {
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                });
            }
        }
    }

    /**
     * 设置title
     *
     * @param titleText
     */
    public void setTitle(String titleText) {
        if (null == getToolbar()) {
            return;
        }
        getToolbar().setVisibility(View.VISIBLE);
        TextView title = (TextView) getToolbar().findViewById(R.id.nvs_title);
        if (StrUtils.isNotNull(titleText)) {
            title.setVisibility(View.VISIBLE);
            title.setText(titleText);
        }
    }


    /**
     * 设置back
     *
     * @param rightText
     * @param resRightImg
     * @param clickListener
     */
    public void setRight(String rightText, int resRightImg, View.OnClickListener clickListener) {
        if (null == getToolbar()) {
            return;
        }
        getToolbar().setVisibility(View.VISIBLE);
        TextView right = (TextView) getToolbar().findViewById(R.id.nvs_right);
        if (null != right) {
            if (StrUtils.isNotNull(rightText)) {
                right.setVisibility(View.VISIBLE);
                right.setText(rightText);
            }
            if (resRightImg != 0) {
                right.setVisibility(View.VISIBLE);
                right.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(resRightImg), null, null, null);
            }
            if (null != clickListener) {
                right.setVisibility(View.VISIBLE);
                right.setOnClickListener(clickListener);
            }
        }
    }

    public void hintRight() {
        if (null == getToolbar()) {
            return;
        }
        getToolbar().setVisibility(View.VISIBLE);
        TextView right = (TextView) getToolbar().findViewById(R.id.nvs_right);
        right.setVisibility(View.GONE);
    }


    /**
     * 店鋪完善信息提示
     */
    private Class goClass;
    public void showDialog(ShopInfoItemEntity itemEntity, boolean isFadan) {
        if (null != itemEntity && isFadan && itemEntity.getIs_setbaseinfo().equals("1")
                && itemEntity.getIs_setContactUser().equals("1")
                && itemEntity.getIs_set_paypass().equals("1")) {
            return;
        }
        if (null != itemEntity && !isFadan && itemEntity.getIs_setbaseinfo().equals("1")
                && itemEntity.getIs_setContactUser().equals("1")
                && itemEntity.getIs_audit().equals("1")) {
            return;
        }
        String okStr = null;
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_hint, null);
        final Dialog dialog = new Dialog(this.getActivity(), R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        if (itemEntity.getIs_setbaseinfo().equals("0")) {
            content.setText("您还未完善店铺信息");
            goClass = ShopInfoActivity.class;
            okStr = "去完善";
        } else {
            if (itemEntity.getIs_setContactUser().equals("0")) {
                content.setText("您还未添加店铺联系人");
                goClass = UpdataTouchActivity.class;
                okStr = "去添加";
            } else {
                if (isFadan) {
                    if (itemEntity.getIs_set_paypass().equals("0")) {
                        content.setText("您还未设置支付密码");
                        goClass = PayPasswordActivity.class;
                        okStr = "去设置";
                    }
                } else {
                    if (itemEntity.getIs_audit().equals("0")) {
                        content.setText("您还未进行店铺认证");
                        goClass = ApproveActivity.class;
                        okStr = "去认证";
                    }
                }
            }
        }
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        if (StrUtils.isNotNull(okStr)) {
            ok.setText(okStr);
            dialog.show();
        } else {
            return;
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SkipInfoEntity info = new SkipInfoEntity();
                info.index = 1;
                goToActivity(info, goClass, 300);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 店鋪认证提示
     */
    public void showRzDialog(ShopInfoItemEntity itemEntity, String contentStr, final boolean isFinish) {
        if (null == itemEntity || itemEntity.getIs_audit().equals("1") || StrUtils.isNull(contentStr)) {
            return;
        }
        String okStr = null;
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_hint, null);
        final Dialog dialog = new Dialog(this.getActivity(), R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        final TextView content = (TextView) view.findViewById(R.id.dialog_content);
        if (itemEntity.getIs_audit().equals("0")) {
            content.setText(contentStr);
            goClass = ApproveActivity.class;
            okStr = "去认证";
        }
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        if (StrUtils.isNotNull(okStr)) {
            ok.setText(okStr);
            dialog.show();
        } else {
            return;
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SkipInfoEntity info = new SkipInfoEntity();
                info.index = 1;
                goToActivity(info, goClass, 300);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isFinish) {
                    getActivity().finish();
                }
            }
        });
    }

    /**
     * 店鋪完善信息提示
     */
    public void showShopinfoDialog(ShopInfoItemEntity itemEntity, String contentStr) {
        if (null == itemEntity || itemEntity.getIs_setbaseinfo().equals("1")) {
            return;
        }
        String okStr = null;
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_hint, null);
        final Dialog dialog = new Dialog(this.getActivity(), R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        content.setText(contentStr);
        goClass = ShopInfoActivity.class;
        okStr = "去完善";
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        if (StrUtils.isNotNull(okStr)) {
            ok.setText(okStr);
            dialog.show();
        } else {
            return;
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SkipInfoEntity info = new SkipInfoEntity();
                info.index = 1;
                goToActivity(info, goClass, 300);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
    }


    /**
     * 店鋪支付密码
     */
    public void showShopPassDialog(ShopInfoItemEntity itemEntity) {
        if (null == itemEntity || itemEntity.getIs_set_paypass().equals("1")) {
            return;
        }
        String okStr = null;
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_hint, null);
        final Dialog dialog = new Dialog(this.getActivity(), R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        dialog.show();
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        content.setText("您还未设置支付密码");
        goClass = PayPasswordActivity.class;
        okStr = "去设置";
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        if (StrUtils.isNotNull(okStr)) {
            ok.setText(okStr);
            dialog.show();
        } else {
            return;
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SkipInfoEntity info = new SkipInfoEntity();
                info.index = 1;
                goToActivity(info, goClass, 300);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
    }
}
