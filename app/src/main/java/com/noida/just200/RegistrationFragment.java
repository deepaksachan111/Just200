package com.noida.just200;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ratan on 7/29/2015.
 */
public class RegistrationFragment extends Fragment {

    private EditText sponsercode,sponsername, userid,password,confirmpassword,firstname,mobileno;
    private Button submit,reset;
    private String sponservalue ,stringuser_id;
    private TextView tv_userid;

    private String st_sponsercode,st_userid,st_password,st_cofirm_password,st_firstname,st_mobileno;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.registration_layout,null);
        findIDS(v);



        sponsercode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                sponservalue = sponsercode.getText().toString();
                if (!sponservalue.equals("")) {
                    String url = "http://api.just200.in/Just200Panel.svc/GetMemberName/" + sponservalue;
                    new SponsercodecheckketAsynctask().execute(url, sponservalue);
                }

                // epincode.setError("invalid ");
            /*    if(s.toString().length()>6){
                    et.setError("", null);
                }else{
                    et.setError("", errorIcon);
                }*/
            }
        });

        userid.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                stringuser_id = userid.getText().toString();

                if(stringuser_id.length()<5){
                  tv_userid.setVisibility(View.GONE);
                    userid.setError("Invalid UserId");
                }

                else {
                    String url = "http://api.just200.in/Just200Panel.svc/ValidateUserLogin/"+stringuser_id;
                    new UseridcheckAsynktask().execute(url, stringuser_id);
                }

                // epincode.setError("invalid ");
            /*    if(s.toString().length()>6){
                    et.setError("", null);
                }else{
                    et.setError("", errorIcon);
                }*/
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  st_sponsercode = sponsercode.getText().toString().trim();
                  st_userid = userid.getText().toString().trim();
                st_password = password.getText().toString().trim();
                st_cofirm_password =confirmpassword.getText().toString().trim();
                st_firstname =firstname.getText().toString().trim();
                st_mobileno = mobileno.getText().toString().trim();

                if (st_sponsercode.length() < 4) {
                    sponsercode.setError("enter sponsercode");
                    sponsercode.requestFocus();

                } else if (st_userid.length() < 4) {
                    userid.setError("enter user id");
                    sponsercode.setError(null);
                    userid.requestFocus();

                }
                else if (st_password.length() < 4) {
                    password.setError("password mimimum 4 character");
                    userid.setError(null);
                    password.requestFocus();


                }else if (!st_password.equals(st_cofirm_password)) {
                    confirmpassword.setError("newpass & confirmpass not match");
                    password.setError(null);
                    confirmpassword.requestFocus();

                }  else if (st_firstname.length() < 2) {
                    firstname.setError("enter first name");
                    firstname.requestFocus();

                }
              else if (st_mobileno.length() <10) {
                    mobileno.setError("mo no. mustbe 10 digit ");
                    mobileno.requestFocus();

            }

                else {
                  SessionManager  sessionManager = new SessionManager(getActivity());
                    HashMap<String, String> user = sessionManager.getUserDetails();

                    String memid = user.get(SessionManager.KEY_MEMID);
                   // http://api.just200.in/Just200Panel.svc/MemberRegistartion/{SponsorId}/{UserId}/{Password}/{Name}/{MobileNo}/{CreatedBy}
                    String url = "http://api.just200.in/Just200Panel.svc/MemberRegistartion/"+st_sponsercode+"/"+st_userid+"/"+st_password+"/"+st_firstname+"/"+st_mobileno+"/"+memid;   ;
                    new MemberRegistrationAsynktask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                }
            }
        });


        return v;
    }



    private void findIDS(View v){

        sponsercode=(EditText)v.findViewById(R.id.edt_sponser_code);
        sponsername=(EditText)v.findViewById(R.id.edt_sponser_name);
        userid=(EditText)v.findViewById(R.id.edt_userid_regis);
        tv_userid =(TextView)v.findViewById(R.id.tv_userid);
        password=(EditText)v.findViewById(R.id.edt_password_reg);
        confirmpassword=(EditText)v.findViewById(R.id.edt_confirm_password);
        firstname=(EditText)v.findViewById(R.id.edt_firstname);
        mobileno=(EditText)v.findViewById(R.id.edt_registraion_mobileno);
        submit=(Button)v.findViewById(R.id.btn_reg_submit);
       // reset=(Button)v.findViewById(R.id.btn_reg_reset);
    }

    private class SponsercodecheckketAsynctask extends AsyncTask<String, Void, String> {


        HttpResponse response;
        private String content ;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(true);
            //  dialog.show();
        }

        protected String doInBackground(String... urls) {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost  = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("userid", urls[1]));
          /*  try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            try {
                response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                content = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }

            return content;
        }


        protected void onPostExecute(String content) {
            dialog.dismiss();

            if(content == null){

                    Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }else

                displayResult_sponsercode(content);

        }


    }


    private void displayResult_sponsercode(String res) {
        try {
            JSONArray jArray = new JSONArray(res);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject1 = jArray.getJSONObject(i);
                String APIResponce= jsonObject1.getString("APIResponce");
                String Name= jsonObject1.getString("Name");


              /*  if (Name.equals("null")) {

                    Log.v("Res..............",Name);
                    sponsercode.setError("Invalid");
                    Toast  toast2= Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.TOP, 25, 500);
                    View view1 = toast2.getView();
                    view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast2.show();
                } else
                */

                if (APIResponce.contains("Invalid Agent ID")) {
                    sponsercode.setError("Invalid");
                    sponsername.setVisibility(View.GONE);
                }
                else if(APIResponce.contains("Success")){
                     sponsername.setText(Name);
                    sponsername.setVisibility(View.VISIBLE);

                }


        }
        }

            catch (JSONException e) {
                e.printStackTrace();
            }



    }

    private class UseridcheckAsynktask extends AsyncTask<String, Void, String> {


        HttpResponse response;
        private String content ;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(true);
            //  dialog.show();
        }

        protected String doInBackground(String... urls) {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost  = new HttpPost(urls[0]);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("userid", urls[1]));
          /*  try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            try {
                response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                content = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }

            return content;
        }


        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (content == null) {

                Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }else{

                try {
                    JSONArray jArray = new JSONArray(content);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonObject1 = jArray.getJSONObject(i);
                        String APIResponce= jsonObject1.getString("APIResponce");


                        if (APIResponce.contains("Invalid User Id")) {
                            userid.setError("Invalid");
                            tv_userid.setVisibility(View.GONE);
                        }
                        else if(APIResponce.contains("Valid User Id")){
                            tv_userid.setVisibility(View.VISIBLE);
                          tv_userid.setText("Valid UserID");

                        }


                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        }

    private class MemberRegistrationAsynktask extends AsyncTask<String, Void, String> {


        HttpResponse response;
        private String content ;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(true);
              dialog.show();
        }

        protected String doInBackground(String... urls) {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost  = new HttpPost(urls[0]);

            //List<NameValuePair> nameValuePairs = new ArrayList<>();
          //  nameValuePairs.add(new BasicNameValuePair("userid", urls[1]));
          /*  try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            try {
                response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                content = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }

            return content;
        }


        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (content == null) {

                Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
            }else{

                try {
                    JSONArray jArray = new JSONArray(content);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonObject1 = jArray.getJSONObject(i);
                        String APIResponce= jsonObject1.getString("APIResponce");


                        if (APIResponce.contains("Success")) {

                            viewDetail();
                            Toast.makeText(getActivity(),"Member Registered Successfully",Toast.LENGTH_LONG).show();

                        }



                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void viewDetail (){

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoge_registrersuccess);
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Set dialog title
        // dialog.setTitle("Custom Dialog");

        TextView txt_regi_name = (TextView) dialog.findViewById(R.id.txt_register_name);
        TextView txt_regi_name2 = (TextView) dialog.findViewById(R.id.txt_register_name2);
        TextView txt_regi_loginid = (TextView) dialog.findViewById(R.id.txt_register_loginId);
        TextView txt_regi_sponserid = (TextView) dialog.findViewById(R.id.txt_register_sponserid);
        txt_regi_name.setText(firstname.getText().toString());
        txt_regi_name2.setText(firstname.getText().toString());
        txt_regi_loginid.setText(userid.getText().toString());
        txt_regi_sponserid.setText(sponsercode.getText().toString());

        //ImageView declineButton = (ImageView) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
    /*    declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                //  handler.removeCallbacks(method(text));

            }
        });*/

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(width, height / 2);
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

