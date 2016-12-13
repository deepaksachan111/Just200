package com.noida.just200;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.noida.just200.modaldata.GetReceivehelpData;
import com.noida.just200.modaldata.GetsendhelpData;
import com.noida.just200.modaldata.ListviewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ratan on 7/29/2015.
 */
public class DashbordFragment extends Fragment {
    private ListView listView_dashbord_senderRecords, listView_dashbord_receiverRecords;
    private ImageView image_dsahboard;
    private LinearLayout linear_addheader_getsend_record, linear_addheader_getrecevier_record;
    private SessionManager sessionManager;
    private ProgressDialog pd;
    private View mHeaderView_sendrecords, mHeaderView_receiverecords;
    private String APIResponce_getsenderhelprecord, APIResponce_getreceiverhelprecord;
    private static final int SELECT_PICTURE = 1;

    private ArrayList<GetsendhelpData> getSendhelprecordList = new ArrayList<>();
    private ArrayList<GetReceivehelpData> getReceivehelprecordList = new ArrayList<>();
    private String memid;
    private DatabaseHelper databaseHelper;
    private Button btn_sendhelprecord,btn_receivehelprecord;
    private String Encodedimage_string;
    private TextView tvdashboard_username, dashboard_userid, dasboard_mobile, dasboard_starterplan, dasboard_middle_level_plan, dasboard_registrationDate;
    boolean b  = true;
    int pos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dashboard_layout, null);
        image_dsahboard = (ImageView) v.findViewById(R.id.image_dsahboard);
        listView_dashbord_senderRecords = (ListView) v.findViewById(R.id.listview_dashboard_senderrecords);
        listView_dashbord_receiverRecords = (ListView) v.findViewById(R.id.listview_dashboard_receiverrecords);
        tvdashboard_username = (TextView) v.findViewById(R.id.dashboard_username);
        dashboard_userid = (TextView) v.findViewById(R.id.dashboard_userid);
        dasboard_mobile = (TextView) v.findViewById(R.id.dasboard_mobile);
        dasboard_starterplan = (TextView) v.findViewById(R.id.dasboard_starterplan);
        dasboard_middle_level_plan = (TextView) v.findViewById(R.id.dasboard_middle_level_plan);
        dasboard_registrationDate = (TextView) v.findViewById(R.id.dasboard_registrationDate);

       btn_sendhelprecord =(Button)v.findViewById(R.id.btn_dash_sendrecords);
        btn_sendhelprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(),SendHelpRecordActivity.class));
            }
        });
        btn_receivehelprecord =(Button)v.findViewById(R.id.btn_dash_receiverecords) ;
        btn_receivehelprecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ReceivedHelpRecordActivity.class));
            }
        });


        databaseHelper = new DatabaseHelper(getActivity());

        linear_addheader_getsend_record = (LinearLayout) v.findViewById(R.id.linear_addheader_getsend_record);
        linear_addheader_getrecevier_record = (LinearLayout) v.findViewById(R.id.linear_addheader_getrecevier_record);
        // LayoutInflater inflaterss = LayoutInflater.from(getContext());
        //  mHeaderView_sendrecords = inflater.inflate(R.layout.header_listview_sender_helprecord, null, false);

        mHeaderView_sendrecords = getActivity().getLayoutInflater().inflate(R.layout.header_listview_sender_helprecord, null);
        mHeaderView_receiverecords = getActivity().getLayoutInflater().inflate(R.layout.header_listview_recevier_helprecord, null);


        SessionManager session = new SessionManager(getActivity());
        HashMap<String, String> map = session.getUserDetails();
        memid = map.get(SessionManager.KEY_MEMID);

        String url = "http://api.just200.in/Just200Panel.svc/GetFullDetails/" + memid;
        showProgressDialog();
        registerUserPost(url);
        GetSendHelpRecords("http://api.just200.in/Just200Panel.svc/GetSendHelpRecord/" + memid);
        GetReceiveHelpRecord("http://api.just200.in/Just200Panel.svc/GetReceiveHelpRecord/" + memid);
        return v;
    }


    private void registerUserPost(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;

                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {


                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                String userId = jsonObject1.getString("UserId");
                                String name = jsonObject1.getString("Name");
                                String registrationdate = jsonObject1.getString("RegistrationDate");
                                String activationdate = jsonObject1.getString("ActivationDate");
                                String mobileno = jsonObject1.getString("MobileNo");
                                String startterplan = jsonObject1.getString("StarterPlan");
                                String endPlan = jsonObject1.getString("EndPlan");
                                String middlePlan = jsonObject1.getString("MiddlePlan");
                                String sponserid = jsonObject1.getString("SponsorId");
                                String sponsername = jsonObject1.getString("SponsorName");
                                String parentid = jsonObject1.getString("ParentID");
                                String parentname = jsonObject1.getString("ParentName");
                                String ProfilePic = jsonObject1.getString("ProfilePic");

                                tvdashboard_username.setText(name);
                                dashboard_userid.setText(userId);
                                dasboard_mobile.setText(mobileno);
                                dasboard_starterplan.setText(startterplan);
                                dasboard_middle_level_plan.setText(middlePlan);

                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date myDate = newDateFormat.parse(activationdate);
                                // newDateFormat.applyPattern("yyyy/MM/dd");
                                String myDateString = newDateFormat.format(myDate);
                                dasboard_registrationDate.setText(myDateString);


                                //  Picasso.with(getActivity()).load( ProfilePic).placeholder(R.drawable.user_user).error(R.drawable.profile).resize(100, 100).into(image_dsahboard);
                             /*   Bitmap bitmap = null;
                                byte[] bb =   databaseHelper.retreiveImageFromDB();

                                if(bb != null){
                                    bitmap =  DbBitmapUtility.getImage(bb) ;
                                }
*/


                                new DownloadAsyncTask(image_dsahboard).execute(ProfilePic);


                                // count++;
                                //  arrayList_breathdatas.add(category_name);


                            }

                            //listView.setAdapter(arrayAdapter);

                            //  Toast.makeText(getApplicationContext(),"No of records : "+count+"",Toast.LENGTH_LONG).show();
                            // setListAdapter(adapter);
                            //  ListViewHelper.getListViewSize(listView);
                            //  String resp = jsonObject.getString("RetSiteResult");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void showProgressDialog() {
        pd = new ProgressDialog(getActivity(), R.style.StyledDialog);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading.........");
        pd.setIndeterminate(false);
        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click

    }

    private class DownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ImageView view;

        public DownloadAsyncTask(ImageView view) {
            this.view = view;
        }

        Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            //load image directly

            try {
                URL imageURL = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (IOException e) {
                // TODO: handle exception
                Log.e("error", "Downloading Image Failed");

            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub


            if (result != null) {
                byte[] b = DbBitmapUtility.getBytes(result);

                // databaseHelper.addEntry("profile", b);
                view.setImageBitmap(result);

            }

        }
    }


    private void GetSendHelpRecords(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;

                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
       getSendhelprecordList.clear();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {

                                count++;
                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                APIResponce_getsenderhelprecord = jsonObject1.getString("APIResponce");
                                String AccountName = jsonObject1.getString("AccountName");
                                String Amount = jsonObject1.getString("Amount");
                                String AssignmentNo = jsonObject1.getString("AssignmentNo");
                                String CreatedDate = jsonObject1.getString("CreatedDate");
                                String Email = jsonObject1.getString("Email");
                                String LevelPlan = jsonObject1.getString("LevelPlan");
                                String LoginId = jsonObject1.getString("LoginId");
                                String MemberAccNo = jsonObject1.getString("MemberAccNo");
                                String MemberMobile = jsonObject1.getString("MemberMobile");
                                String MemberName = jsonObject1.getString("MemberName");
                                String Narration = jsonObject1.getString("Narration");
                                String PK_HelpDRCRID = jsonObject1.getString("PK_HelpDRCRID");
                                String PaytmID = jsonObject1.getString("PaytmID");
                                String Receipt = jsonObject1.getString("Receipt");
                                String RemainingTime = jsonObject1.getString("RemainingTime");
                                String helpRemark = jsonObject1.getString("HelpRemark");


                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date myDate = newDateFormat.parse(CreatedDate);
                                //newDateFormat.applyPattern("dd/MM/yyyy");
                                String myDateString = newDateFormat.format(myDate);
                                // dasboard_registrationDate.setText(myDateString);


                                getSendhelprecordList.add(new GetsendhelpData(count, AccountName, Amount, AssignmentNo, myDateString, MemberAccNo, LevelPlan, LoginId, MemberMobile, MemberName, Narration, PK_HelpDRCRID, PaytmID, Receipt, RemainingTime,helpRemark));


                            }
                            if (APIResponce_getsenderhelprecord.equals("Success")) {

                                GetsendhelpRecordAdapter arrayAdapter = new GetsendhelpRecordAdapter(getActivity(), R.layout.dashboard_getsenderhelp_record_adapter, getSendhelprecordList);

                                //  linear_addheader_getsend_record.setVisibility(View.VISIBLE);
                                linear_addheader_getsend_record.addView(mHeaderView_sendrecords);
                                listView_dashbord_senderRecords.setAdapter(arrayAdapter);
                                ListviewHelper.getListViewSize(listView_dashbord_senderRecords);

                            } else {
                                linear_addheader_getsend_record.setVisibility(View.GONE);

                            }

                            //  Toast.makeText(getApplicationContext(),"No of records : "+count+"",Toast.LENGTH_LONG).show();
                            // setListAdapter(adapter);
                            //  ListViewHelper.getListViewSize(listView);
                            //  String resp = jsonObject.getString("RetSiteResult");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void GetReceiveHelpRecord(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;

                    //  String    cancelButtonView ="";
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        getReceivehelprecordList.clear();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {
                                count++;
                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                APIResponce_getreceiverhelprecord = jsonObject1.getString("APIResponce");
                                String accountId = jsonObject1.getString("AccountId");
                                String PK_HelpDRCRID = jsonObject1.getString("PK_HelpDRCRID");
                                String helpRemark = jsonObject1.getString("HelpRemark");
                                String drAmount = jsonObject1.getString("DrAmount");
                                String assignmentNo = jsonObject1.getString("AssignmentNo");
                                String createdDate = jsonObject1.getString("CreatedDate");
                                String cancelButtonView = jsonObject1.getString("CancelButtonView");
                                String levelPlan = jsonObject1.getString("LevelPlan");
                                String loginId = jsonObject1.getString("LoginId");
                                String memberAccNo = jsonObject1.getString("MemberAccNo");
                                String memberName = jsonObject1.getString("MemberName");
                                String memberMobile = jsonObject1.getString("MemberMobile");
                                String memberBankName = jsonObject1.getString("MemberBankName");
                                String memberBranch = jsonObject1.getString("MemberBranch");
                                String ifscCode = jsonObject1.getString("IFSCCode");
                                String userType = jsonObject1.getString("UserType");
                                String sponsorName = jsonObject1.getString("SponsorName");
                                String approvedDate = jsonObject1.getString("ApprovedDate");
                                String remainingTime = jsonObject1.getString("RemainingTime");
                                String receipt = jsonObject1.getString("Receipt");


                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date myDate = null;

                                myDate = newDateFormat.parse(createdDate);

                                //newDateFormat.applyPattern("dd/MM/yyyy");
                                String myDateString = newDateFormat.format(myDate);

                                getReceivehelprecordList.add(new GetReceivehelpData(count, levelPlan, loginId, PK_HelpDRCRID, helpRemark, assignmentNo, drAmount, createdDate, accountId, memberName, cancelButtonView, memberMobile, memberAccNo, memberBankName, memberBranch, ifscCode, approvedDate, sponsorName, userType, remainingTime, receipt));


                            }
                            if (APIResponce_getreceiverhelprecord.equals("Success")) {

                                GetReceiverhelpRecordAdapter arrayAdapter = new GetReceiverhelpRecordAdapter(getActivity(), R.layout.dashboard_getsenderhelp_record_adapter, getReceivehelprecordList);

                                //  linear_addheader_getsend_record.setVisibility(View.VISIBLE);
                                linear_addheader_getrecevier_record.addView(mHeaderView_receiverecords);
                                listView_dashbord_receiverRecords.setAdapter(arrayAdapter);
                                ListviewHelper.getListViewSize(listView_dashbord_receiverRecords);

                            } else {
                                linear_addheader_getrecevier_record.setVisibility(View.GONE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }




    private class GetsendhelpRecordAdapter extends ArrayAdapter {
        private ArrayList<GetsendhelpData> getsendhelpDataArrayList;


        public GetsendhelpRecordAdapter(Context context, int resource,ArrayList<GetsendhelpData> getsendhelpDataArrayList) {
            super(context, resource, getsendhelpDataArrayList);

            this.getsendhelpDataArrayList =getsendhelpDataArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
               // convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_custom_adapter, parent,false);
                 pos = position;
                final LayoutInflater sInflater = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                convertView = sInflater.inflate(R.layout.dashboard_getsenderhelp_record_adapter, null,false);
            }
          TextView  tv_dash_adapter_sn=(TextView)convertView.findViewById(R.id.tv_dash_adapter_sn);
            TextView  tv_dash_adapter_name=(TextView)convertView.findViewById(R.id.tv_dash_adapter_name);
            TextView  tv_dash_adapter_topid=(TextView)convertView.findViewById(R.id.tv_dash_adapter_topid);
            TextView  tv_dash_adapter_mobileno=(TextView)convertView.findViewById(R.id.tv_dash_adapter_mobileno);
            TextView  tv_dash_adapter_transid=(TextView)convertView.findViewById(R.id.tv_dash_adapter_transid);
            TextView  tv_dash_adapter_amount=(TextView)convertView.findViewById(R.id.tv_dash_adapter_amount);

            TextView  tv_dash_adapter_start_Date=(TextView)convertView.findViewById(R.id.tv_dash_adapter_start_Date);
            final TextView  tv_dash_adapter_Remainingtime=(TextView)convertView.findViewById(R.id.tv_dash_adapter_Remainingtime);
             final Button btn_dash_reciver_adapter_upload =(Button)convertView.findViewById(R.id.btn_dash_reciver_adapter_upload);

            Button btn_dash_adapter_viewDetails =(Button)convertView.findViewById(R.id.btn_dash_adapter_viewDetails);

             final GetsendhelpData getsendhelpData = getsendhelpDataArrayList.get(position);
            tv_dash_adapter_sn.setText(getsendhelpData.getSno()+"");
            tv_dash_adapter_name.setText(getsendhelpData.getMemberName());
            tv_dash_adapter_topid.setText(getsendhelpData.getLoginId());
            tv_dash_adapter_mobileno.setText(getsendhelpData.getMemberMobile());
            tv_dash_adapter_transid.setText(getsendhelpData.getAssignmentNo());
            tv_dash_adapter_amount.setText(getsendhelpData.getAmount());
            tv_dash_adapter_start_Date .setText(getsendhelpData.getCreatedDate());

            int totalSecs = Integer.parseInt(getsendhelpData.getRemainingTime());



            CountDownTimer timer =     new CountDownTimer(totalSecs*1000, 1000){
                public void onTick(long millisUntilFinished){
                    int seconds = (int) (millisUntilFinished / 1000);

                    int  hours = seconds / 3600;
                    int  minutes = (seconds % 3600) / 60;
                    int secondsssss = seconds % 60;

                    String  timeString = String.format("%02d:%02d:%02d", hours, minutes, secondsssss);
                    tv_dash_adapter_Remainingtime.setText(timeString +" hr.");

                }
                public void onFinish(){

                    tv_dash_adapter_Remainingtime.setText("0" +"  hr.");

                }
            };timer.start();


          //  tv_dash_adapter_Remainingtime.setText(getsendhelpData.getRemainingTime());

            btn_dash_reciver_adapter_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

 //String url ="http://api.just200.in/Just200Panel.svc/ UploadPaySlip/"+ getsendhelpData.getPK_HelpDRCRID()+"/"+getsendhelpData.getReceipt()+"/"+getsendhelpData.getPK_HelpDRCRID();


                    if(b ){
                        showFileChooser();
                      //  btn_dash_reciver_adapter_upload.setVisibility(View.GONE);
                        //btn_dash_reciver_adapter_upload_imageshow.setVisibility(View.VISIBLE);
                        btn_dash_reciver_adapter_upload.setText("Display");


                        b=false;
                    }else {

                        showImagePopup();
                       // btn_dash_reciver_adapter_upload.setText("Upload");
                        //btn_dash_reciver_adapter_upload_imageshow.setVisibility(View.GONE);
                       // btn_dash_reciver_adapter_upload.setVisibility(View.VISIBLE);


                       // b= true;
                    }

                   // showFileChooser();
                }
            });

            btn_dash_adapter_viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      viewDetail();

                }
            });


                    return convertView;
        }
    }


    private class GetReceiverhelpRecordAdapter extends ArrayAdapter {
        private ArrayList<GetReceivehelpData> getreceivehelpDataArrayList;


        public GetReceiverhelpRecordAdapter(Context context, int resource,ArrayList<GetReceivehelpData> getreceivehelpDataArrayList) {
             super(context, resource, getreceivehelpDataArrayList);

            this.getreceivehelpDataArrayList =getreceivehelpDataArrayList;
        }

        private class ViewHolder {
            TextView tv_dash_reciver_adapter_sn;
            TextView tv_dash_reciver_adapter_name;
            TextView tv_dash_reciver_adapter_topid ;
            TextView tv_dash_reciver_adapter_mobileno ;
            TextView tv_dash_reciver_adapter_transid ;
            TextView tv_dash_reciver_adapter_amount ;

            TextView tv_dash_reciver_adapter_start_Date;
            TextView tv_dash_reciver_adapter_Remainingtime ;
            TextView tv_dash_reciver_adapter_plantype ;
            TextView tv_dash_reciver_adapter_acholdername ;
            TextView tv_dash_reciver_adapter_ac_no ;
            TextView tv_dash_reciver_adapter_ac_branch ;

            Button btn_dash_receivedhelp_record_adapter_accept ;
            Button btn_dash_receivedhelp_record_adapter_decline ;
            Button btn_dash_receivedhelp_record_adapter_viewDetails ;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                // convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_custom_adapter, parent,false);

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.dashboard_receiver_help_record_adapter, null,false);
                viewHolder = new ViewHolder();

                viewHolder.  tv_dash_reciver_adapter_sn=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_sn);
                viewHolder.  tv_dash_reciver_adapter_name=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_name);
                viewHolder.  tv_dash_reciver_adapter_topid=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_topid);
                viewHolder.  tv_dash_reciver_adapter_mobileno=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_mobileno);
                viewHolder.  tv_dash_reciver_adapter_transid=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_transid);
                viewHolder.  tv_dash_reciver_adapter_amount=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_amount);

                viewHolder.  tv_dash_reciver_adapter_start_Date=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_start_Date);
                viewHolder.  tv_dash_reciver_adapter_Remainingtime=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_Remainingtime);
                viewHolder.  tv_dash_reciver_adapter_plantype =(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_plantype);
                viewHolder.  tv_dash_reciver_adapter_acholdername=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_acholdername);
                viewHolder.  tv_dash_reciver_adapter_ac_no=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_ac_no);
                viewHolder.  tv_dash_reciver_adapter_ac_branch=(TextView)convertView.findViewById(R.id.tv_dash_reciver_adapter_ac_branch);

                viewHolder. btn_dash_receivedhelp_record_adapter_accept=(Button)convertView.findViewById(R.id.btn_dash_sendhelp_record_adapter_accept);
                viewHolder. btn_dash_receivedhelp_record_adapter_decline=(Button)convertView.findViewById(R.id.btn_dashsendhelp_record_adapter_decline);
                viewHolder. btn_dash_receivedhelp_record_adapter_viewDetails=(Button)convertView.findViewById(R.id.btn_dashsendhelp_record_adapter_viewDetails);

                convertView.setTag(viewHolder);
            }else {

                viewHolder = (ViewHolder) convertView.getTag();
            }


           // GetsendhelpData getsendhelpData = getSendhelprecordList.get()
             GetReceivehelpData   getReceivehelpData = getreceivehelpDataArrayList.get(position);
            viewHolder. tv_dash_reciver_adapter_sn.setText(getReceivehelpData.getSno()+"");
            viewHolder. tv_dash_reciver_adapter_name.setText(getReceivehelpData.getMemberName());
            viewHolder.tv_dash_reciver_adapter_topid.setText(getReceivehelpData.getLoginId());
            viewHolder.tv_dash_reciver_adapter_mobileno.setText(getReceivehelpData.getMemberMobile());
            viewHolder. tv_dash_reciver_adapter_transid.setText(getReceivehelpData.getAssignmentNo());
            viewHolder. tv_dash_reciver_adapter_amount.setText(getReceivehelpData.getDrAmount());
            viewHolder. tv_dash_reciver_adapter_start_Date .setText(getReceivehelpData.getCreatedDate());

            int totalSecs = Integer.parseInt(getReceivehelpData.getRemainingTime());


            final ViewHolder finalViewHolder = viewHolder;
            CountDownTimer timer =     new CountDownTimer(totalSecs*1000, 1000){
                public void onTick(long millisUntilFinished){
                    int seconds = (int) (millisUntilFinished / 1000);

                    int  hours = seconds / 3600;
                    int  minutes = (seconds % 3600) / 60;
                    int secondsssss = seconds % 60;

                    String  timeString = String.format("%02d:%02d:%02d", hours, minutes, secondsssss);
                    finalViewHolder.tv_dash_reciver_adapter_Remainingtime.setText(timeString + " hr.");

                }
                public void onFinish(){

                    finalViewHolder.tv_dash_reciver_adapter_Remainingtime.setText("0" +" hr.");

                }
            };timer.start();








          //  viewHolder.tv_dash_reciver_adapter_Remainingtime.setText(timeString);

            viewHolder. tv_dash_reciver_adapter_acholdername.setText(getReceivehelpData.getMemberName());
            viewHolder. tv_dash_reciver_adapter_ac_no.setText(getReceivehelpData.getMemberAccNo());
            viewHolder. tv_dash_reciver_adapter_ac_branch.setText(getReceivehelpData.getMemberBranch());
            viewHolder. tv_dash_reciver_adapter_plantype.setText(getReceivehelpData.getUserType());



            String levelplan= getReceivehelpData.getLevelPlan();
            levelplan = levelplan.replaceAll(" ","");
            final String finalLevelplan = levelplan;

            String userType =getReceivehelpData.getUserType();
            userType =  userType.replaceAll(" ","" );

            final String finaluserType = userType;
            viewHolder. btn_dash_receivedhelp_record_adapter_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    if(finalLevelplan.equalsIgnoreCase("StarterPlan")) {
                        GetReceivehelpData getReceivehelpData1 = getreceivehelpDataArrayList.get(position);
                        String url = "http://api.just200.in/Just200Panel.svc/ApproveReceivedHelpStarterPlan/" + getReceivehelpData1.getPK_HelpDRCRID() + "/" + memid + "/" + finaluserType;
                        iAccept(url);

                    }else
                    if(finalLevelplan.equalsIgnoreCase("MiddlePlan")){
                        GetReceivehelpData getReceivehelpData2 = getreceivehelpDataArrayList.get(position);
                        String url =   "http://api.just200.in/Just200Panel.svc/ApproveReceivedHelpMiddlePlan/"+ getReceivehelpData2.getPK_HelpDRCRID() + "/" + memid + "/" + finaluserType;
                        iAccept(url);
                    }

                }
            });

            viewHolder. btn_dash_receivedhelp_record_adapter_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetReceivehelpData getReceivehelpData2 = getreceivehelpDataArrayList.get(position);
                    String url =  "http://api.just200.in/Just200Panel.svc/webCancelHelp/"+getReceivehelpData2.getPK_HelpDRCRID()+"/"+getReceivehelpData2.getUserType();
                   iReject(url);
                }
            });


            return convertView;
        }
    }


    private void iAccept(String REGISTER_URL) {
      //  final GetsendhelpData getsendhelpData = getSendhelprecordList.get(pos);
        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        String    APIResponces="";
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++)
                            {


                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                    APIResponces = jsonObject1.getString("APIResponce");

                            }

                            if(APIResponces.equals("Success")){
                                startActivity(new Intent(getActivity(),DrawerwithSwipeTabActivity.class));
                                getActivity().finish();
                                Toast.makeText(getActivity(),"Accepted Successfully",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(),"Invalid",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
        {


          /* @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
             //   params.put("PK_HelpDRCRID", getsendhelpData.getPK_HelpDRCRID());
                params.put("BankReceiptUrl", Encodedimage_string);
             //   params.put("HelpRemark", getsendhelpData.getHelpRemark());
                return params;
            }*/

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }
    private void iReject(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        String    APIResponces="";
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++)
                            {


                                JSONObject jsonObject1 = jArray.getJSONObject(i);
                                APIResponces = jsonObject1.getString("APIResponce");

                            }

                            if(APIResponces.equals("Success")){
                                Toast.makeText(getActivity(),"Accepted Successfully",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(),"Invalid",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
        {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cm_name", "sounds");
                params.put("sub_category_name", "test");
                return params;
            }*/

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest2.setShouldCache(false);
        requestQueue.add(stringRequest2);
        // AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void showFileChooser() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
   startActivityForResult(pickPhoto, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // this.onActivityResult(requestCode, resultCode, data);
            int a = requestCode;
        if (requestCode == 1) {

            Uri filePath = data.getData();
            Log.v("image path....", filePath + "");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Encodedimage_string = encodeTobase64(bitmap);
                final GetsendhelpData getsendhelpData = getSendhelprecordList.get(pos);
                String url ="http://api.just200.in/Just200Panel.svc/UploadPaySlip/"+ getsendhelpData.getPK_HelpDRCRID()+"/"+Encodedimage_string+"/"+getsendhelpData.getHelpRemark();
               // String url ="http://api.just200.in/Just200Panel.svc/UploadPaySlip";
                iAccept(url);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }





    public static String encodeTobase64(Bitmap image){
        Bitmap image1 = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b =baos.toByteArray();
        String imageEncode = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("image log", imageEncode);
        return imageEncode;
    }
    public static Bitmap decodeBase64(String input){
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }

    private void showImagePopup(){
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoge);




        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);


        if(Encodedimage_string != null){
            Bitmap bp = decodeBase64(Encodedimage_string);
            image.setImageBitmap(bp);
        }

        // dialog.show();

        ImageView declineButton = (ImageView) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });


        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(width, height / 3);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        //  dialog.getWindow().getAttributes().verticalMargin = 0.50F;
        // dialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //  window.setAttributes(wlp);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();


    }


    private void viewDetail (){

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoge);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Set dialog title
        // dialog.setTitle("Custom Dialog");


        ImageView iv_nextText=(ImageView)dialog.findViewById(R.id.imageDialog);

        // text.setText(quotes[count]);



        // handler.postDelayed(method(text),1000);

        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        //image.setImageResource(R.drawable.close_2);
        final GetsendhelpData getsendhelpData = getSendhelprecordList.get(pos);
        if(getsendhelpData.getReceipt() != null){

         //  new  DownloadAsyncTask(image).execute(getsendhelpData.getReceipt());
            Picasso.with(getActivity()).load( getsendhelpData.getReceipt()).placeholder(R.drawable.loading).error(R.drawable.profile).resize(200, 200).into(image);
        }

        // dialog.show();

        ImageView declineButton = (ImageView) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                //  handler.removeCallbacks(method(text));

            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(width, height / 3);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        //  dialog.getWindow().getAttributes().verticalMargin = 0.50F;
        // dialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //  window.setAttributes(wlp);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();


    }


}
