package com.noida.just200;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private  ProgressDialog pd;

    @InjectView(R.id.input_email)
    EditText edt_username;
    @InjectView(R.id.input_password)
    EditText edtpassword;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    SessionManager session;
    private LinearLayout lin_forgetpassword;
    private  String name,email,memid,password,apiresponce,starterlevel, usertype;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Boolean saveLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        session = new SessionManager(this);

        session = new SessionManager(this);
        boolean b =  session.isLoggedIn();
        boolean c = b;

        if(b){
            startActivity(new Intent(getApplicationContext(), DrawerwithSwipeTabActivity.class));
            finish();
        }


        lin_forgetpassword =(LinearLayout)findViewById(R.id.tv_forget_password);

        TextView tv_user_user=(TextView)findViewById(R.id.tv_useruser);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        tv_user_user.setTypeface(tf);
        edt_username = (EditText)findViewById(R.id.input_email);
        edtpassword = (EditText)findViewById(R.id.input_password);
        edt_username.clearComposingText();
        edt_username.requestFocus();


       /* InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/

        final CheckBox ch=(CheckBox)findViewById(R.id.ch_rememberme);
        loginPreferences = this.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edt_username.setText(loginPreferences.getString("username", ""));
            edtpassword.setText(loginPreferences.getString("password", ""));
            ch.setChecked(true);
        }



        _loginButton = (Button)findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validate();




                name = edt_username.getText().toString();
                password = edtpassword.getText().toString();

                if (ch.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", name);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                    InputMethodManager imm = (InputMethodManager)getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_username.getWindowToken(), 0);
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }

        });
        lin_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  forgetPasswordDialog();
            }
        });


    }


    public void validate() {
        boolean valid = true;

         email = edt_username.getText().toString();
        password = edtpassword.getText().toString();

        if (email.isEmpty() || email.length() < 4) {
            edt_username.setError("enter a valid User Name");

        } /*else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtpassword.setError("between 4 and 10 alphanumeric characters");

        }*/else {

            String url = UrlUtilities.login_URL+email + "/"+password;
            showProgressDialog();
            registerUserPost(url);

        }

    }



    private void registerUserPost(String REGISTER_URL) {
       // REGISTER_URL = UrlUtilities.login_URL;

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();


                        displayCountryList(response);



                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
        {
           /* @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cm_name", "photos");
                params.put("sub_category_name", "test");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void showProgressDialog(){
        pd = new ProgressDialog(this,R.style.StyledDialog);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading.........");
        pd.setIndeterminate(false);
        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click

    }


    private  void displayCountryList(String response){
        String responsecode ="";
        try {
            JSONArray jArray = new JSONArray(response);

            for (int i = 0; i < jArray.length(); i++)
            {

                JSONObject jObj = jArray.getJSONObject(i);

                apiresponce= jObj.getString("APIResponce");
                name = jObj.getString("Name");
                memid = jObj.getString("MemId");
                //email = jObj.getString("EmailID");
                starterlevel = jObj.getString("StarterLevel");
                 usertype =jObj.getString("UserType");



                //  DbHandler.dbHandler.saveCardProfile(new CardProfile(cardno,Displayname, totalbalance,redemac,balance,MemberID));
            }



            if((apiresponce.equals("Success")&& usertype.equals("User"))){


                Toast  toast2= Toast.makeText(LoginActivity.this, "Welcome " + name, Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
                session.createLoginSession(name, memid,email,password);
               startActivity(new Intent(getApplicationContext(), DrawerwithSwipeTabActivity.class));
               finish();

            }  if(usertype.equals("UserTemp")){
               Intent intent =new Intent(getApplicationContext(), TempUserActivity.class);
                intent.putExtra("MID",memid);
                startActivity(intent);
            }
            if(apiresponce.equals("Error")){

                Toast  toast2= Toast.makeText(LoginActivity.this,  "invalid user name or password", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.TOP, 25, 500);
                View view1 = toast2.getView();
                view1.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast2.show();
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                edt_username.setText("");
                edtpassword.setText("");
                edt_username.requestFocus();

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*public void forgetPasswordDialog() {
//       final String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/MemberPasswordChange/"+id;

        final EditText loinid, mobileno;
        Button okbtn;
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);



        dialog.setCancelable(true);
        dialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Forget Password</font>"));
        // dialog.getWindow().setBackgroundDrawableResource(R.color.blue);
        dialog.setContentView(R.layout.forgetpassworddialog);

        // dialog.setTitle("Forget Password");


        loinid = (EditText) dialog.findViewById(R.id.editTxt_forgetpassloginid_dialog);
        mobileno = (EditText) dialog.findViewById(R.id.editTxt_forgetpassmobileno_dialog);
        okbtn = (Button) dialog.findViewById(R.id.forgetsetPasswordBtn);
        ImageView iv = (ImageView)dialog.findViewById(R.id.close_dialog);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginid = loinid.getText().toString();

                // forgetlogin.setText("");
                String mobilenono = mobileno.getText().toString();


             *//*    SessionManager sessionManager = new SessionManager(getActivity());
                HashMap<String, String > map = sessionManager.getUserDetails();
                loginid = map.get(SessionManager.KEY_LOGINID);*//*
                if (loginid.length() > 3 && mobileno.length()==10) {
                    String url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/CommanRequestPassword/"+loginid+"/"+mobilenono;


                    new ForgetpassdialogAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
                      *//*  ForgetPasswordActivity forgetPasswordActivity = new ForgetPasswordActivity();
                        forgetPasswordActivity.new ForgetAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,loginid,mobilenono);*//*
                    // new ForgetAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loginid, mobileno);
                    dialog.dismiss();
                } *//*else if (loginid.length() == 16 && mobileno.length()==10)
                    {

                    }*//*
                else {
                    Toast toast = Toast.makeText(getActivity(), "enter correct loginid or mobileno.", Toast.LENGTH_LONG);
                    View tostview = toast.getView();
                    tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                    toast.setGravity(Gravity.TOP, 25, 500);
                    toast.show();
                }
            }



        });


        dialog.show();

    }*/

  /*  class ForgetpassdialogAsyncTask extends AsyncTask<String, Void, String> {

        //  String Url = "http://demo8.mlmsoftindia.com/ShinePanel.svc/ForgetPassword/" + params[0] + "/" + params[1];
        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();
        HttpResponse response;
        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog =
                new ProgressDialog(getActivity(),R.style.ThemeMyDialog);



        protected void onPreExecute() {
            dialog.setMessage("Getting data... Please wait...");
            dialog.setCancelable(false);
            dialog.show();



        }

        protected String doInBackground(String... urls) {

            String URL = null;

            try {

                URL = urls[0];
                HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
                ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

                HttpPost httpPost = new HttpPost(URL);

             *//*   //add name value pair for the country code
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("start",String.valueOf(start)));
                nameValuePairs.add(new BasicNameValuePair("limit",String.valueOf(limit)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*//*
                response = httpclient.execute(httpPost);

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    content = out.toString();
                } else {
                    //Closes the connection.
                    Log.w("HTTP1:", statusLine.getReasonPhrase());
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                Log.w("HTTP2:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            } catch (IOException e) {
                Log.w("HTTP3:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            } catch (Exception e) {
                Log.w("HTTP4:", e);
                content = e.getMessage();
                error = true;
                cancel(true);
            }

            return content;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(getActivity(), "Connection Server Failed", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 500);
            View view1 = toast.getView();
            view1.setBackgroundResource(R.drawable.toast_drawablecolor);
            toast.show();
            getActivity().finish();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dialog.dismiss();
            if (response != null) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
                    String code = jsonObject1.getString("MessageCode");
                    String Mobile = jsonObject1.getString("Mobile");
                    String OTP = jsonObject1.getString("OTP");
                    String LoginID = jsonObject1.getString("LoginID");
                    //  [{"LoginID":"axl419","MessageCode":"1","MessageText":"Valid Details","Mobile":"7388870003","OTP":"1844","Password":null}]
                    if (code.equals("1")) {
                        Intent intent = new Intent(getActivity(),OTPPasswordChangeActivity.class);
                        intent.putExtra("loginname", loginid);
                        intent.putExtra("mobile", Mobile);
                        intent.putExtra("otp", OTP);
                        startActivity(intent);
                        Toast toast = Toast.makeText(getActivity(), "Send OTP on your mobile.", Toast.LENGTH_LONG);

                        View tostview = toast.getView();
                        tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "invalid loginid or mobileno.", Toast.LENGTH_LONG);
                        View tostview = toast.getView();
                        tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                        toast.setGravity(Gravity.TOP, 25, 500);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast toast= Toast.makeText(getActivity(), "invalid loginid or mobileno.", Toast.LENGTH_LONG);
                View tostview = toast.getView();
                tostview.setBackgroundResource(R.drawable.toast_drawablecolor);
                toast.setGravity(Gravity.TOP, 25, 500);
                toast.show();
            }
        }
    }*/

}
