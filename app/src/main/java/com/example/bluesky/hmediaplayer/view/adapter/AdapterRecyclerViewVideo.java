package com.example.bluesky.hmediaplayer.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bluesky.hmediaplayer.VideoPlayer;
import com.example.bluesky.hmediaplayer.VideoPlayerStandard;
import com.example.bluesky.hmediaplayer.R;
import com.example.bluesky.hmediaplayer.modle.VideoConstant;
import com.example.bluesky.hmediaplayer.modle.VideoInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 037
 */
public class AdapterRecyclerViewVideo extends RecyclerView.Adapter<AdapterRecyclerViewVideo.MyViewHolder> {

    public static final String TAG = "AdapterRecyclerViewVideo";
    private List<VideoInfo> urlList;
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;
    private OnItemClick onItemClick;

    public AdapterRecyclerViewVideo(Context context) {
        this.context = context;
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.videoPlayerStandard.hashCode() + "] position=" + position);

        holder.videoPlayerStandard.setUp(
                urlList.get(position).getUrl(), VideoPlayer.SCREEN_WINDOW_LIST,
                urlList.get(position).getTitle());
        Picasso.with(holder.videoPlayerStandard.getContext())
                .load(urlList.get(position).getImgUrl())
                .into(holder.videoPlayerStandard.thumbImageView);
        holder.llDetail.setOnClickListener(i ->onItemClick.itemTouch());
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public void setDataChange(List<VideoInfo> urlList) {
        if (null != urlList && urlList.size() > 0) {
            this.urlList = urlList;
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        VideoPlayerStandard videoPlayerStandard;
        LinearLayout llDetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            videoPlayerStandard = itemView.findViewById(R.id.videoplayer);
            llDetail = itemView.findViewById(R.id.llDetail);
        }
    }

    public interface OnItemClick {
        void itemTouch();
    }

    public void setItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}
