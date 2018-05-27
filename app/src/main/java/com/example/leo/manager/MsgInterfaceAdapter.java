package com.example.leo.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MsgInterfaceAdapter extends RecyclerView.Adapter<MsgInterfaceAdapter.ViewHolder>{
    private List<MsgItem> msgItemList;

    public MsgInterfaceAdapter(List<MsgItem> msgItems){
        this.msgItemList=msgItems;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView leftImage;
        ImageView rightImage;
        TextView leftMsg;
        TextView rightMsg;
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout=(LinearLayout)itemView.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)itemView.findViewById(R.id.right_layout);
            leftImage=(ImageView)itemView.findViewById(R.id.msg_interface_left_image);
            rightImage=(ImageView)itemView.findViewById(R.id.msg_interface_right_image);
            leftMsg=(TextView)itemView.findViewById(R.id.msg_interface_left_text);
            rightMsg=(TextView)itemView.findViewById(R.id.msg_interface_right_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_interface_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MsgItem msgItem=msgItemList.get(position);
        int TYPE=msgItem.getMSG_TYPE();
        if (TYPE==1){
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(msgItem.getMsgContent());
        }else if (TYPE==2){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msgItem.getMsgContent());
        }
    }

    @Override
    public int getItemCount() {
       return msgItemList.size();
    }
}
