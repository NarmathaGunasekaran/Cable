package com.cablemanager;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;
import java.text.NumberFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_EXCEL=1001;
    private ArrayList<Customer> customers=new ArrayList<>();
    private CustomerAdapter adapter; private TextView summary,paid,due,pending,outstanding; private EditText search;
    private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("en","IN"));

    @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
        summary=findViewById(R.id.txtSummary);paid=findViewById(R.id.txtPaid);due=findViewById(R.id.txtDue);pending=findViewById(R.id.txtPending);outstanding=findViewById(R.id.txtOutstanding);search=findViewById(R.id.searchBox);adapter=new CustomerAdapter(this);((ListView)findViewById(R.id.cableListView)).setAdapter(adapter);
        findViewById(R.id.btnImport).setOnClickListener(v->pickExcel()); findViewById(R.id.btnRefresh).setOnClickListener(v->loadSaved()); search.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){} public void onTextChanged(CharSequence s,int st,int b,int c){adapter.filter(s.toString());} public void afterTextChanged(android.text.Editable e){}});
        loadSaved();
    }
    private void pickExcel(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("*/*");startActivityForResult(i,PICK_EXCEL);}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(request!=PICK_EXCEL||result!=RESULT_OK||data==null||data.getData()==null)return;Uri uri=data.getData();try{getContentResolver().takePersistableUriPermission(uri,data.getFlags()&(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION));}catch(Exception ignored){}
        ExcelImporter.Result r=ExcelImporter.read(this,uri); if(r.customers.isEmpty()&&r.errors>0){toast("Import failed: "+r.errorMessages.get(0));return;} int added=0,updated=0,skipped= r.skipped; Map<String,Customer> map=new LinkedHashMap<>();for(Customer c:customers)map.put(c.boxId,c);for(Customer c:r.customers){Customer old=map.get(c.boxId);if(old==null){map.put(c.boxId,c);added++;}else if(!old.sameAs(c)){map.put(c.boxId,c);updated++;}else skipped++;}customers=new ArrayList<>(map.values());save();adapter.setAll(customers);updateDashboard();summary.setText("Imported: "+r.customers.size()+" rows  •  Added: "+added+"  •  Updated: "+updated+"  •  Skipped: "+skipped+"  •  Errors: "+r.errors);toast("Excel import completed");}
    private void loadSaved(){String s=getPreferences(MODE_PRIVATE).getString("customers","[]");customers.clear();try{JSONArray a=new JSONArray(s);for(int i=0;i<a.length();i++)customers.add(Customer.fromJson(a.getJSONObject(i)));}catch(Exception ignored){}adapter.setAll(customers);updateDashboard();summary.setText(customers.isEmpty()?"No Excel imported yet":"Customers: "+customers.size()+" • Next month's bill is based on package cost");}
    private void save(){JSONArray a=new JSONArray();for(Customer c:customers)a.put(c.toJson());getPreferences(MODE_PRIVATE).edit().putString("customers",a.toString()).apply();}
    private void updateDashboard(){double dueTotal=0,paidTotal=0;int pendingCount=0;for(Customer c:customers){double d=Math.max(0,c.dueAmount);dueTotal+=d;double p=Math.max(0,c.packageCost-d);paidTotal+=p;if(d>0)pendingCount++;}paid.setText("Paid\n"+money.format(paidTotal));due.setText("Due\n"+money.format(dueTotal));pending.setText("Pending\n"+pendingCount);outstanding.setText("Outstanding\n"+money.format(dueTotal));}
    private void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
}
