package com.example.leo.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddContractsAdapter extends RecyclerView.Adapter<AddContractsAdapter.ViewHolder>{
    private List<ContactItem> contactItemList;
    private Map<Integer,Boolean> itemMap=new HashMap<>();

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView number;
        private CheckBox checkBox;
        public ViewHolder(View itemView){
                super(itemView);
                itemView.setOnClickListener(this);
                name=(TextView)itemView.findViewById(R.id.add_a_contact_item_name);
                number=(TextView)itemView.findViewById(R.id.add_a_contact_phone_number);
                checkBox=(CheckBox)itemView.findViewById(R.id.add_a_contact_item_checkbox);
        }

        @Override
        public void onClick(View v) {
            if (itemMap.get(getAdapterPosition())){
                itemMap.put(getAdapterPosition(),false);
            }else{
                itemMap.put(getAdapterPosition(),true);
            }
            notifyItemChanged(getAdapterPosition());
        }

    }
    public AddContractsAdapter(List<ContactItem> contactItems){
        this.contactItemList=contactItems;
        initMap();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_contract_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ContactItem contactItem=contactItemList.get(position);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemMap.put(position,isChecked);
            }
        });
        if (itemMap.get(position)==null){
            itemMap.put(position,false);
        }
        holder.checkBox.setChecked(itemMap.get(position));
        if (contactItem.getName()==null)
            holder.name.setText(contactItem.getPhoneNumber());
        else
            holder.name.setText(contactItem.getName());
        holder.number.setText(contactItem.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactItemList.size();
    }
    private void initMap(){
        for(int i=0;i<contactItemList.size();i++){
            itemMap.put(i,false);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return itemMap;
    }
}
