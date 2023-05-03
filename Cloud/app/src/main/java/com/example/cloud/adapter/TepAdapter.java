package com.example.cloud.adapter;

import android.content.Context;
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
import com.example.cloud.R;
import com.example.cloud.model.Tep;
import com.example.cloud.onclick.IOnClickItem;

import java.util.ArrayList;
import java.util.List;

public class TepAdapter extends
RecyclerView.Adapter<TepAdapter.ViewHolder> {

    private Context mContext;
    private List<Tep> mTep;
    private List<Tep> mTepFilter;
    private int mLayoutType;
    private IOnClickItem iOnClickItem;

    public TepAdapter(Context mContext, List<Tep> mTep,int mLayoutType, IOnClickItem iOnClickItem) {
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
        switch (tep.getLoai()) {
            case "image":
                imageType = R.drawable.ic_image;
                break;
            case "video":
                imageType = R.drawable.ic_video_lib;
                break;
            case "khac":
                imageType = R.drawable.ic_file;
                break;
        }

        Glide.with(mContext).load(imageType).into(holder.mFileImage);

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
            mFileImage= itemView.findViewById(R.id.fileImage);
            mFileName= itemView.findViewById(R.id.fileName);
            layout= itemView.findViewById(R.id.file);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mTepFilter = mTep;
                } else {
                    ArrayList<Tep> filteredList = new ArrayList<>();
                    for (Tep row :mTep) {
                        if (row.getTen().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mTepFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTepFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTepFilter = (ArrayList<Tep>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public  void release(){
        mContext=null;
    }
}
