package com.example.leo.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHoler>{
    private List<MsgItem> msgItemList;
    private OnClickListener onClickListener;

    public MsgAdapter(List<MsgItem> list){
        this.msgItemList=list;
    }

    class ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView_no;
        TextView textView_content;
        TextView textView_date;
        public ViewHoler(View view){
            super(view);
            view.setOnClickListener(this);
            imageView=(ImageView)view.findViewById(R.id.msg_item_image);
            textView_no=(TextView)view.findViewById(R.id.msg_item_no);
            textView_content=(TextView)view.findViewById(R.id.msg_item_content);
            textView_date=(TextView)view.findViewById(R.id.msg_date);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener!=null)
                onClickListener.onItemClick(v,getAdapterPosition());
        }
    }
    @Override
    public int getItemCount() {
        return msgItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        MsgItem msgItem;
        msgItem=msgItemList.get(position);
        Date date=new Date(msgItem.getDate());
        String dateYYMMDD=simpleDateFormat.format(date);
        holder.textView_no.setText(msgItem.getMsgSender());
        holder.textView_date.setText(dateYYMMDD);
        int count=msgItem.getMsgContent().length();
        String content=(msgItem.getMsgContent()).substring(0,count>=20?20:count)+"...";
        holder.textView_content.setText(content);
        holder.imageView.setImageResource(R.drawable.contracticon);
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        ViewHoler viewHoler=new ViewHoler(view);
        return viewHoler;
    }
    public interface OnClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
}
