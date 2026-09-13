package com.cablemanager;

import android.content.Context;
import android.net.Uri;
import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;
import java.util.*;

public class ExcelImporter {
    public static class Result { public final List<Customer> customers=new ArrayList<>(); public int skipped=0, errors=0; public final List<String> errorMessages=new ArrayList<>(); }

    public static Result read(Context context, Uri uri) {
        Result result=new Result();
        try (InputStream in=context.getContentResolver().openInputStream(uri); Workbook wb=WorkbookFactory.create(in)) {
            DataFormatter fmt=new DataFormatter();
            Sheet sheet=wb.getSheetAt(0);
            int headerRow=-1; Map<String,Integer> h=new HashMap<>();
            for (int r=0;r<=Math.min(sheet.getLastRowNum(),20);r++) {
                Row row=sheet.getRow(r); if(row==null) continue;
                for(Cell cell:row) { String s=norm(fmt.formatCellValue(cell)); if("boxid".equals(s) || "customername".equals(s)) h.put(s,cell.getColumnIndex()); }
                if(h.containsKey("boxid") && h.containsKey("customername")) { headerRow=r; break; }
            }
            if(headerRow<0) throw new IllegalArgumentException("BOX ID / CUSTOMER NAME header not found");
            Map<String,Integer> headers=new HashMap<>(); Row hr=sheet.getRow(headerRow);
            for(Cell cell:hr) headers.put(norm(fmt.formatCellValue(cell)),cell.getColumnIndex());
            for(int r=headerRow+1;r<=sheet.getLastRowNum();r++) {
                Row row=sheet.getRow(r); if(row==null) {result.skipped++; continue;}
                String box=get(row,headers,"boxid",fmt); String name=get(row,headers,"customername",fmt);
                if(box.isEmpty() && name.isEmpty()) { result.skipped++; continue; }
                if(box.isEmpty()) { result.errors++; result.errorMessages.add("Row "+(r+1)+": BOX ID is empty"); continue; }
                Customer c=new Customer(); c.boxId=box; c.cardNo=get(row,headers,"cardno",fmt); c.mso=get(row,headers,"mso",fmt); c.name=name; c.phone=get(row,headers,"phoneno",fmt); c.address=get(row,headers,"address",fmt); c.zone=get(row,headers,"zone",fmt); c.searchCode=get(row,headers,"searchcode",fmt); c.packageName=get(row,headers,"packagename",fmt); c.packageCost=num(row,headers,"packagecost",fmt); c.agentName=get(row,headers,"agentname",fmt); c.agentPhone=get(row,headers,"agentphone",fmt); c.dueAmount=num(row,headers,"dueamount",fmt); c.status=get(row,headers,"status",fmt); c.subscription=get(row,headers,"subscription",fmt); result.customers.add(c);
            }
        } catch(Exception e) { result.errors++; result.errorMessages.add(e.getMessage()==null?e.toString():e.getMessage()); }
        return result;
    }
    private static String norm(String s){return s==null?"":s.replace("\u00A0"," ").trim().toLowerCase(Locale.US).replaceAll("[^a-z0-9]","");}
    private static String get(Row row,Map<String,Integer> h,String key,DataFormatter f){Integer i=h.get(key); if(i==null)return ""; Cell c=row.getCell(i,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL); return c==null?"":f.formatCellValue(c).trim();}
    private static double num(Row row,Map<String,Integer> h,String key,DataFormatter f){String s=get(row,h,key,f).replace(",","").replace("₹","").trim(); if(s.isEmpty())return 0; try{return Double.parseDouble(s);}catch(Exception e){return 0;}}
}
