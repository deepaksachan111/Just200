package com.noida.just200;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.noida.just200.modaldata.ListviewHelper;
import com.noida.just200.modaldata.SendHelpRecordData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ReceivedHelpRecordActivity extends AppCompatActivity {
    ProgressDialog pd;
    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    int year;
    int month;
    int day,no_of_objects;
    EditText fromdate,todate,edt_amout;
    Button btn_sendhelprecord,btn_receiverdhelprecord_reset;
    ArrayList<SendHelpRecordData> receivedHelpRecordDatas ;
    private String APIResponcesenderhelprecord;
    ListView lst_sendhelprecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_help_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year,month,day);
        lst_sendhelprecords=(ListView)findViewById(R.id.lst_receivehelprecords);

        edt_amout=(EditText)findViewById(R.id.edt_amount_receive);
        btn_sendhelprecord=(Button)findViewById(R.id.btn_receivehelprecord) ;
        btn_receiverdhelprecord_reset=(Button)findViewById(R.id.btn_receiverdhelprecord_reset);
        fromdate =(EditText)findViewById(R.id.edt_select_fromdate_receive);
        todate =(EditText)findViewById(R.id.edt_select_todate_receive);

        //fromdate.setText(dateFormatter.format(cal.getTime()));
       // todate.setText(dateFormatter.format(cal.getTime()));

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> map = sessionManager.getUserDetails();
        final   String loginID= map.get(SessionManager.KEY_LOGINID);



        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFromdatedate();
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTodatedate();
            }
        });


        btn_sendhelprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount =edt_amout.getText().toString();
                String fromdates =fromdate.getText().toString();
                String todates =todate.getText().toString();
                if(amount.equals("")){
                    amount= "Null";
                } if(fromdates.equals("")){
                    fromdates= "Null";
                } if(todates.equals("")){
                    todates= "Null";
                }

                String Url ="http://api.just200.in/Just200Panel.svc/ReceivedHelpReports/"+loginID+"/"+fromdates+"/"+todates+"/"+amount+"/1/100";
                showProgressDialog();
                PostSendHelpRecords(Url);
            }
        });

        btn_receiverdhelprecord_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromdate.setText("");
                todate.setText("");
                edt_amout.setText("");
            }
        });


    }



    private void setFromdatedate() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Display Selected date in textbox
                        //  addmeeting_edt_selectdate.setText(dayOfMonth + "-" + dateFormatter.format (monthOfYear + 1) + "-" + year);
                        cal.set(year, monthOfYear, dayOfMonth);
                        fromdate.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }

    private void setTodatedate() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Display Selected date in textbox
                        //  addmeeting_edt_selectdate.setText(dayOfMonth + "-" + dateFormatter.format (monthOfYear + 1) + "-" + year);
                        cal.set(year, monthOfYear, dayOfMonth);
                        todate.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }


    private void PostSendHelpRecords(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;

                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        receivedHelpRecordDatas = new ArrayList<>();
                        receivedHelpRecordDatas.clear();

                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {

                                count++;
                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                APIResponcesenderhelprecord = jsonObject1.getString("APIResponce");
                                String amout = jsonObject1.getString("Amount");
                                String name = jsonObject1.getString("DisplayName");
                                String id = jsonObject1.getString("LoginID");
                                String sno = jsonObject1.getString("RowId");
                                String status = jsonObject1.getString("Status");
                                String TotalCount = jsonObject1.getString("TotalCount");
                                String TransactionDate = jsonObject1.getString("TransactionDate");
                                String TransactionID = jsonObject1.getString("TransactionID");






                                receivedHelpRecordDatas.add(new SendHelpRecordData(sno,id,name, amout,TransactionID,TransactionDate, status)) ;


                            }
                            if (APIResponcesenderhelprecord.equals("Success")) {
                               receivehelpRecordAdapter arrayAdapter = new receivehelpRecordAdapter(ReceivedHelpRecordActivity.this, R.layout.sendhelprecord_activity_adapter, receivedHelpRecordDatas);

                                //  linear_addheader_getsend_record.setVisibility(View.VISIBLE);
                                //  linear_addheader_getsend_record.addView(mHeaderView_sendrecords);
                                lst_sendhelprecords.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                ListviewHelper.getListViewSize(lst_sendhelprecords);

                            }else {
                                receivehelpRecordAdapter arrayAdapter = new receivehelpRecordAdapter(ReceivedHelpRecordActivity.this, R.layout.sendhelprecord_activity_adapter, receivedHelpRecordDatas);

                                //  linear_addheader_getsend_record.setVisibility(View.VISIBLE);
                                //  linear_addheader_getsend_record.addView(mHeaderView_sendrecords);
                                lst_sendhelprecords.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                ListviewHelper.getListViewSize(lst_sendhelprecords);
                                Toast.makeText(getApplicationContext(),"No Records Found",Toast.LENGTH_LONG).show();
                                // setListAdapter(adapter);
                                //  ListViewHelper.getListViewSize(listView);
                                //  String resp = jsonObject.getString("RetSiteResult");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                }) {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cm_name", "sounds");
                params.put("sub_category_name", "test");
                return params;
            }*/

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void showProgressDialog() {
        pd = new ProgressDialog(this, R.style.StyledDialog);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading.........");
        pd.setIndeterminate(false);
        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click

    }

    private class receivehelpRecordAdapter extends ArrayAdapter {
        private ArrayList<SendHelpRecordData> receivedhelpDataArrayList;
        int deepColor = Color.parseColor("#32BBBB");
        int deepColor2 = Color.parseColor("#98BF8E");
        int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2,deepColor3};

        public receivehelpRecordAdapter(Context context, int resource, ArrayList<SendHelpRecordData> getsendhelpDataArrayList) {
            super(context, resource, getsendhelpDataArrayList);

            this.receivedhelpDataArrayList =getsendhelpDataArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                // convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_custom_adapter, parent,false);

                final LayoutInflater sInflater = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                convertView = sInflater.inflate(R.layout.sendhelprecord_activity_adapter, null,false);
                int colorPos = position % colors.length;
                convertView.setBackgroundColor(colors[colorPos]);
            }
            TextView tv_sno=(TextView)convertView.findViewById(R.id.tv_sn_sendrecord);
            TextView  tv_associateid=(TextView)convertView.findViewById(R.id.tv_s_associateid);
            TextView  tv_associatename=(TextView)convertView.findViewById(R.id.tv_s_associatename);
            TextView  tv_sendamount=(TextView)convertView.findViewById(R.id.tv_s_sendamount);
            TextView  tv_transid=(TextView)convertView.findViewById(R.id.tv_s_transid);
            TextView  tv_senddate=(TextView)convertView.findViewById(R.id.tv_s_senddate);
            TextView  tv_staus=(TextView)convertView.findViewById(R.id.tv_s_status);

            SendHelpRecordData sendHelpRecordDatasss = receivedhelpDataArrayList.get(position);
            tv_sno.setText(sendHelpRecordDatasss.getSno());
            tv_associateid.setText(sendHelpRecordDatasss.getAssociateid());
            tv_associatename.setText(sendHelpRecordDatasss.getAssociatename());
            tv_sendamount.setText(sendHelpRecordDatasss.getSendamount());
            tv_transid.setText(sendHelpRecordDatasss.getTransactionid());
            tv_senddate.setText(sendHelpRecordDatasss.getSenddate());
            tv_staus.setText(sendHelpRecordDatasss.getStatus());






            return convertView;
        }
    }

}
