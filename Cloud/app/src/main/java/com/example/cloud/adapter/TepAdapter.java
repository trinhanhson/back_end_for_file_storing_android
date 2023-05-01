package com.example.cloud.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloud.R;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;

public class TepAdapter extends
RecyclerView.Adapter<TepAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Tep> mTep;
    private int mLayoutType;
    private IOnClickItem iOnClickItem;

    public TepAdapter(Context mContext, ArrayList<Tep> mTep,int mLayoutType, IOnClickItem iOnClickItem) {
        this.mContext = mContext;
        this.mTep = mTep;
        this.mLayoutType = mLayoutType;
        this.iOnClickItem = iOnClickItem;
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
        int imageType = R.drawable.ic_folder;
        if (tep.getLoai().equals("image")){
           imageType=R.drawable.ic_image;
        }
        else if(tep.getLoai().equals("video")){
            imageType=R.drawable.ic_video_lib;
        }
        else if(tep.getLoai().equals("khac")){
            imageType=R.drawable.ic_file;
        }

        Glide.with(mContext).load(imageType).into(holder.mFileImage);

        holder.layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                iOnClickItem.onClickItem(tep);
            }
        });
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
            mFileImage=(ImageView)itemView.findViewById(R.id.fileImage);
            mFileName=(TextView)itemView.findViewById(R.id.fileName);
            layout=(RelativeLayout) itemView.findViewById(R.id.file);
        }
    }

    public  void release(){
        mContext=null;
    }
}
