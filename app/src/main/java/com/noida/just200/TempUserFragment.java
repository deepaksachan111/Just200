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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.noida.just200.modaldata.GetsendhelpData;
import com.noida.just200.modaldata.ListviewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.noida.just200.R.id.dasboard_middle_level_plan;
import static com.noida.just200.R.id.dasboard_mobile;
import static com.noida.just200.R.id.dasboard_registrationDate;
import static com.noida.just200.R.id.dasboard_starterplan;
import static com.noida.just200.R.id.dashboard_userid;
import static com.noida.just200.R.id.image_dsahboard;
import static com.noida.just200.R.id.linear_addheader_getsend_record;

/**
 * Created by intex on 12/12/2016.
 */

public    class TempUserFragment extends Fragment {
    boolean b  = true;
    int pos;
    String Encodedimage_string;
    ArrayList<GetsendhelpData> getSendhelprecordList = new ArrayList<>();
    ProgressDialog pd;
    private String memid;
    private TextView tvdashboard_username, tv_dashboard_userid, tv_dasboard_mobile, tv_dasboard_starterplan, tv_dasboard_middle_level_plan, tv_dasboard_registrationDate;
    private ListView listView_dashbord_senderRecords;
    private ImageView iv_imageview;
    private LinearLayout linear_addheader_getsend_records;
    private View mHeaderView_sendrecords;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dashboard_layout, null);
        iv_imageview = (ImageView) view.findViewById(image_dsahboard);
        listView_dashbord_senderRecords = (ListView) view.findViewById(R.id.listview_dashboard_senderrecords);
        tvdashboard_username = (TextView) view.findViewById(R.id.dashboard_username);
        tv_dashboard_userid = (TextView) view.findViewById(dashboard_userid);
        tv_dasboard_mobile = (TextView) view.findViewById(dasboard_mobile);
        tv_dasboard_starterplan = (TextView) view.findViewById(dasboard_starterplan);
        tv_dasboard_middle_level_plan = (TextView) view.findViewById(dasboard_middle_level_plan);
        tv_dasboard_registrationDate = (TextView) view.findViewById(dasboard_registrationDate);


        linear_addheader_getsend_records = (LinearLayout) view.findViewById(linear_addheader_getsend_record);
        mHeaderView_sendrecords = getActivity().getLayoutInflater().inflate(R.layout.header_listview_sender_helprecord, null);


        memid = getActivity().getIntent().getStringExtra("MID");

        String url = "http://api.just200.in/Just200Panel.svc/TempMemberDetails/" + memid;
        showProgressDialog();
        registerUserPost(url);
        GetSendHelpRecords("http://api.just200.in/Just200Panel.svc/GetSendHelpRecordTemp/" + memid);


        return view;
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
                                tv_dashboard_userid.setText(userId);
                                tv_dasboard_mobile.setText(mobileno);
                                tv_dasboard_starterplan.setText(startterplan);
                                tv_dasboard_middle_level_plan.setText(middlePlan);

                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date myDate = newDateFormat.parse(registrationdate);
                                newDateFormat.applyPattern("yyyy/dd/MM");
                                String myDateString = newDateFormat.format(myDate);
                                tv_dasboard_registrationDate.setText(myDateString);


                                //  Picasso.with(getActivity()).load( ProfilePic).placeholder(R.drawable.user_user).error(R.drawable.profile).resize(100, 100).into(image_dsahboard);
                             /*   Bitmap bitmap = null;
                                byte[] bb =   databaseHelper.retreiveImageFromDB();

                                if(bb != null){
                                    bitmap =  DbBitmapUtility.getImage(bb) ;
                                }
*/


                                // new DashbordFragment.DownloadAsyncTask(image_dsahboard).execute(ProfilePic);


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


    private void GetSendHelpRecords(String REGISTER_URL) {

        pd.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;

                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        getSendhelprecordList.clear();
                        String APIResponce_getsenderhelprecord="";
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
                                linear_addheader_getsend_records.addView(mHeaderView_sendrecords);
                                listView_dashbord_senderRecords.setAdapter(arrayAdapter);
                                ListviewHelper.getListViewSize(listView_dashbord_senderRecords);

                            } else {
                                linear_addheader_getsend_records.setVisibility(View.GONE);

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

    private class GetsendhelpRecordAdapter extends ArrayAdapter {
        private ArrayList<GetsendhelpData> getsendhelpDataArrayList;


        public GetsendhelpRecordAdapter(Context context, int resource, ArrayList<GetsendhelpData> getsendhelpDataArrayList) {
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
                //  iAccept(url);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }





    public  String encodeTobase64(Bitmap image){
        Bitmap image1 = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b =baos.toByteArray();
        String imageEncode = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("image log", imageEncode);
        return imageEncode;
    }
    public  Bitmap decodeBase64(String input){
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