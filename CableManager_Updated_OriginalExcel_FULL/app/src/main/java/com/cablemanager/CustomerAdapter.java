package com.cablemanager;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    private final List<Customer> all=new ArrayList<>();
    public CustomerAdapter(Context c){super(c,0,new ArrayList<>());}
    public void setAll(List<Customer> list){all.clear();all.addAll(list);filter("");}
    public void filter(String q){String x=q==null?q.trim().toLowerCase():q.trim().toLowerCase();clear();for(Customer c:all){String hay=(c.boxId+" "+c.name+" "+c.phone+" "+c.address+" "+c.searchCode).toLowerCase();if(x.isEmpty()||hay.contains(x))add(c);}notifyDataSetChanged();}
    public View getView(int position,View convert,ViewGroup parent){View v=convert;if(v==null)v=LayoutInflater.from(getContext()).inflate(com.cablemanager.R.layout.item_customer,parent,false);Customer c=getItem(position);((TextView)v.findViewById(R.id.name)).setText(c.name.isEmpty()?"Unnamed customer":c.name);((TextView)v.findViewById(R.id.box)).setText("BOX ID: "+c.boxId+"   |   Phone: "+(c.phone.isEmpty()?"-":c.phone));((TextView)v.findViewById(R.id.details)).setText((c.packageName.isEmpty()?"Package -":c.packageName)+"   |   Status: "+(c.status.isEmpty()?"-":c.status));((TextView)v.findViewById(R.id.bill)).setText(String.format(Locale.US,"Due: ₹%.2f   |   Next bill: ₹%.2f",c.dueAmount,c.packageCost));return v;}
}
