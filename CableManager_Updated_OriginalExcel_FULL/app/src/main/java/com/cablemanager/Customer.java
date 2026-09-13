package com.cablemanager;

import org.json.JSONObject;

public class Customer {
    String boxId="", cardNo="", mso="", name="", phone="", address="", zone="", searchCode="", packageName="", agentName="", agentPhone="", status="", subscription="";
    double packageCost=0, dueAmount=0;

    JSONObject toJson() {
        JSONObject o=new JSONObject();
        try { o.put("boxId",boxId); o.put("cardNo",cardNo); o.put("mso",mso); o.put("name",name); o.put("phone",phone); o.put("address",address); o.put("zone",zone); o.put("searchCode",searchCode); o.put("packageName",packageName); o.put("packageCost",packageCost); o.put("agentName",agentName); o.put("agentPhone",agentPhone); o.put("dueAmount",dueAmount); o.put("status",status); o.put("subscription",subscription); } catch(Exception ignored) {}
        return o;
    }
    static Customer fromJson(JSONObject o) {
        Customer c=new Customer();
        c.boxId=o.optString("boxId"); c.cardNo=o.optString("cardNo"); c.mso=o.optString("mso"); c.name=o.optString("name"); c.phone=o.optString("phone"); c.address=o.optString("address"); c.zone=o.optString("zone"); c.searchCode=o.optString("searchCode"); c.packageName=o.optString("packageName"); c.packageCost=o.optDouble("packageCost",0); c.agentName=o.optString("agentName"); c.agentPhone=o.optString("agentPhone"); c.dueAmount=o.optDouble("dueAmount",0); c.status=o.optString("status"); c.subscription=o.optString("subscription");
        return c;
    }
    boolean sameAs(Customer x) { return boxId.equals(x.boxId) && cardNo.equals(x.cardNo) && mso.equals(x.mso) && name.equals(x.name) && phone.equals(x.phone) && address.equals(x.address) && zone.equals(x.zone) && searchCode.equals(x.searchCode) && packageName.equals(x.packageName) && Double.compare(packageCost,x.packageCost)==0 && agentName.equals(x.agentName) && agentPhone.equals(x.agentPhone) && Double.compare(dueAmount,x.dueAmount)==0 && status.equals(x.status) && subscription.equals(x.subscription); }
}
