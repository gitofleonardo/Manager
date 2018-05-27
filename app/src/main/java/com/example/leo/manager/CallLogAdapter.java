package com.example.leo.manager;

import android.graphics.Color;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder>{
    private List<CallLogItem> callLogItemList;
    private OnClickListener onClickListener;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private String dateString;

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView number;
        private TextView type;
        private TextView date;
        private TextView address;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            number=(TextView)itemView.findViewById(R.id.call_log_item_number);
            type=(TextView)itemView.findViewById(R.id.call_log_type);
            date=(TextView)itemView.findViewById(R.id.call_log_date);
            address=(TextView)itemView.findViewById(R.id.address);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v,getAdapterPosition());
        }
    }
    public CallLogAdapter(List<CallLogItem> callLogItemList){
        this.callLogItemList=callLogItemList;
    }
    @Override
    public int getItemCount() {
        return callLogItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallLogItem callLogItem=callLogItemList.get(position);
        dateString=simpleDateFormat.format(callLogItem.getDate());
        if (callLogItem.getName()!=null)
            holder.number.setText(callLogItem.getName());
        else
            holder.number.setText(callLogItem.getNumber());

        holder.date.setText(dateString);
        holder.address.setText(callLogItem.getAddress());
        switch (callLogItem.getType()){
            case CallLog.Calls.INCOMING_TYPE:
                holder.type.setText("来电");
                holder.type.setTextColor(Color.GREEN);
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                holder.type.setText("拨出");
                holder.type.setTextColor(Color.BLUE);
                break;
            case CallLog.Calls.MISSED_TYPE:
                holder.type.setText("未接来电");
                holder.type.setTextColor(Color.RED);
                break;
            default:
                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    public interface OnClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnClickListener onItemClickListener){
        this.onClickListener=onItemClickListener;
    }
}
