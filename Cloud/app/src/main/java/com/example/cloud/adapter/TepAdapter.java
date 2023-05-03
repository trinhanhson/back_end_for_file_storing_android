package com.example.cloud.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cloud.R;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TepAdapter extends
        RecyclerView.Adapter<TepAdapter.ViewHolder> {

    private Context mContext;
    private List<Tep> mTep;
    private int mLayoutType;
    private IOnClickItem iOnClickItem;

    public TepAdapter(Context mContext, List<Tep> mTep, int mLayoutType, IOnClickItem iOnClickItem) {
        this.mContext = mContext;
        this.mTep = mTep;
        this.mLayoutType = mLayoutType;
        this.iOnClickItem = iOnClickItem;
    }
    public void setData(List<Tep> list){
        this.mTep = list;
    }
    public List<Tep> getData(){
        return this.mTep;
    }

    @NonNull
    @Override
    public TepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View fileView = inflater.inflate(mLayoutType, parent, false);
        return new ViewHolder(fileView);
    }

    @Override
    public void onBindViewHolder(@NonNull TepAdapter.ViewHolder holder, int position) {
        Tep tep = mTep.get(position);
        holder.mFileName.setText(tep.getTen());
        Log.e("e",tep.toString());
        switch (tep.getLoai()) {
            case "image":
                String encodedImageName="";
                try {
                    encodedImageName = URLEncoder.encode(tep.getDuongDan(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Glide.with(mContext).load("http://192.168.55.107:8080/getFile?filePath=" + encodedImageName).into(holder.mFileImage);
                break;
            case "video":
                RequestOptions options = new RequestOptions().frame(0);

                String encodedVideoName="";
                try {
                    encodedVideoName = URLEncoder.encode(tep.getDuongDan(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Glide.with(mContext).load("http://192.168.55.107:8080/getFile?filePath=" + encodedVideoName).apply(options).into(holder.mFileImage);
                break;
            case "khac":
                Glide.with(mContext).load(R.drawable.ic_file).into(holder.mFileImage);
                break;
            case "thu muc":
                Glide.with(mContext).load(R.drawable.ic_folder).into(holder.mFileImage);

        }

        holder.layout.setOnClickListener(v -> iOnClickItem.onClickItem(tep));
    }

    @Override
    public int getItemCount() {
        return mTep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mFileImage;
        private TextView mFileName;
        private RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFileImage = itemView.findViewById(R.id.fileImage);
            mFileName = itemView.findViewById(R.id.fileName);
            layout = itemView.findViewById(R.id.file);
        }
    }

    public void release() {
        mContext = null;
    }
}
