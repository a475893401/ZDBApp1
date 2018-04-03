package com.zhuandanbao.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by BFTECH on 2016/11/21.
 */
public class ImageApproveAdapter extends RecyclerView.Adapter<ImageApproveAdapter.SelectedPicViewHolder> {

    private int maxImgCount;
    private Context mContext;
    private List<ImageItem> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片
    private boolean isShow;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<ImageItem> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new ImageItem());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public List<ImageItem> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }


    public ImageApproveAdapter(Context mContext, List<ImageItem> data, int maxImgCount, boolean isShow) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        this.isShow = isShow;
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.item_approve_image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_img;
        private int clickPosition;
        private TextView hint_text;
        private TextView layout;
        private Activity activity = (Activity) mContext;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            hint_text = (TextView) itemView.findViewById(R.id.hint_text);
            layout = (TextView) itemView.findViewById(R.id.show_layout);
        }

        public void bind(int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            ImageItem item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.drawable.selector_image_add);
                clickPosition = Constants.IMAGE_ITEM_ADD;
            } else {
                if (item.name.equals("认证公函")){
                    hint_text.setText(item.name);
                }else {
                    hint_text.setText(Html.fromHtml(StrUtils.setHtmlRed(null, "* ", item.name)));
                }
                if (isShow) {
                    if (StrUtils.isNotNull(item.url)) {
                        layout.setVisibility(View.GONE);
                        iv_img.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.url, iv_img, MyImage.deployMemory());
                    } else {
                        iv_img.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        layout.setOnClickListener(this);
                    }
                } else {
                    ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0, false);
                }
                clickPosition = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}
