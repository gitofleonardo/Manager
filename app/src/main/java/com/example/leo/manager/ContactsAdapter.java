package com.example.leo.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private List<ContactItem> contactItemList;
    private OnRVClickListener onRVClickListener;
    private OnImageClickListener onImageClickListener;

    public void setOnRVClickListener(OnRVClickListener onRVClickListener){
        this.onRVClickListener=onRVClickListener;
    }
    public void setOnImageClickListener(OnImageClickListener onImageClickListener){
        this.onImageClickListener=onImageClickListener;
    }

    /**
     * 为图标点击而创建的监听器
     */
    class ImageClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (onImageClickListener!=null){
                onImageClickListener.onImageClick(v,v.getId());
            }
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            imageView=(ImageView)view.findViewById(R.id.contact_icon);
            imageView.setOnClickListener(new ImageClickListener());
            textView=(TextView)view.findViewById(R.id.name_and_phone);
        }

        @Override
        public void onClick(View v) {
            if (onRVClickListener!=null){
                onRVClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
    public ContactsAdapter(List<ContactItem> list){
        this.contactItemList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return contactItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactItem contactItem=contactItemList.get(position);
        if (contactItem.getName()==null)
            holder.textView.setText(contactItem.getPhoneNumber()+"\n"+contactItem.getPhoneNumber());
        else
            holder.textView.setText(contactItem.getName()+"\n"+contactItem.getPhoneNumber());
        holder.imageView.setImageResource(R.drawable.contracticon);
    }
    public interface OnRVClickListener{
        void onItemClick(View view,int position);
    }
    public interface OnImageClickListener{
        void onImageClick(View view,int position);
    }

}
