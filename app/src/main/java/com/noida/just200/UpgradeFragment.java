package com.noida.just200;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;

/**
 * Created by Ratan on 7/29/2015.
 */
public class UpgradeFragment extends Fragment {
    private  ProgressDialog pd;
    private SessionManager session;
    private TextView tv_sending_amount,tv_receving_amount,tv_earning_amount,tv_earning_level;
    private TextView tv_upgrade_staterplanlevel_type, tv_upgrade_middleplanlevel_type,tv_upgrade_endplanlevel_type;
    private Button btn_upgrade_starterplan, btn_upgrade_middleplan,btn_upgrade_endplan;
    String memid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.upgrade_layout, null);

        session = new SessionManager(getActivity());
        HashMap<String,String> map = session.getUserDetails();
           memid = map.get(SessionManager.KEY_MEMID);
          findIDS(v);

        showProgressDialog();
        registerUserPost(UrlUtilities.upgrade_URL + memid);
        String sp_URL="http://api.just200.in/Just200Panel.svc/GetPlanwiseIncome/"+memid+"/SP";
        upgardeLevelPlanSP(sp_URL);
        String mp_URL="http://api.just200.in/Just200Panel.svc/GetPlanwiseIncome/"+memid+"/MP";
        upgardeLevelPlanMP(mp_URL);
        String ep_URL="http://api.just200.in/Just200Panel.svc/GetPlanwiseIncome/"+memid+"/EP";
        upgardeLevelPlanEP(ep_URL);

        return v;


    }

    private void findIDS(View v){
        tv_sending_amount =(TextView)v.findViewById(R.id.tv_sending_amount);
        tv_receving_amount =(TextView)v.findViewById(R.id.tv_receving_amount);
        tv_earning_amount =(TextView)v.findViewById(R.id.tv_earning_amount);
        tv_earning_level =(TextView)v.findViewById(R.id.tv_erning_level);
        btn_upgrade_starterplan=(Button)v.findViewById(R.id.btn_upgrade_starterplan);
        btn_upgrade_middleplan=(Button)v.findViewById(R.id.btn_upgrade_middleplan);
        btn_upgrade_endplan=(Button)v.findViewById(R.id.btn_upgrade_endplan);

        tv_upgrade_staterplanlevel_type =(TextView)v.findViewById(R.id.tv_upgrade_staterplanlevel_type);
        tv_upgrade_middleplanlevel_type =(TextView)v.findViewById(R.id.tv_upgrade_middleplanlevel_type);
        tv_upgrade_endplanlevel_type =(TextView)v.findViewById(R.id.tv_upgrade_end_planlevel_type);
    }



    private   void registerUserPost(String REGISTER_URL) {
        // REGISTER_URL = UrlUtilities.login_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();


                        String res = response;

                        displayUpgardeRecords(res);
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
        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MemId", "205");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private  void displayUpgardeRecords(String response){
        try {
            JSONArray jArray = new JSONArray(response);

            for (int i = 0; i < jArray.length(); i++)
            {
                JSONObject jsonObject = jArray.getJSONObject(i);
                String EarningAmount = jsonObject.getString("EarningAmount");
                String ReceivingAmount = jsonObject.getString("ReceivingAmount");
                String SendingAmount = jsonObject.getString("SendingAmount");
                String StarterLevel = jsonObject.getString("StarterLevel");

                tv_sending_amount.setText(SendingAmount);
                tv_earning_amount.setText(EarningAmount);
                tv_receving_amount.setText(ReceivingAmount);
                tv_earning_level.setText(StarterLevel);

            }

           pd.dismiss();


        }
        catch (JSONException e) {
            e.printStackTrace();
        }





    }







    private void showProgressDialog(){
        pd = new ProgressDialog(getActivity(), R.style.StyledDialog);
        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading.........");
        pd.setIndeterminate(false);
        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click

    }

    private   void upgardeLevelPlanSP(String REGISTER_URL) {
        // REGISTER_URL = UrlUtilities.login_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        String textVisible ="" ;String planUpdate=""; String levelType ="";String upgradeAmount ="";
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject jsonObject = jArray.getJSONObject(i);
                                String balanceAmount = jsonObject.getString("BalanceAmount");
                                String cLevel = jsonObject.getString("CLevel");
                                 levelType = jsonObject.getString("LevelType");
                                 planUpdate = jsonObject.getString("PlanUpdate");
                                 textVisible = jsonObject.getString("TextVisible");
                                 upgradeAmount = jsonObject.getString("UpgradeAmount");



                            }

                            tv_upgrade_staterplanlevel_type.setText(levelType);
                            if(textVisible.equals("U")){
                                btn_upgrade_starterplan.setVisibility(View.VISIBLE);
                                final String finalUpgradeAmount = upgradeAmount;
                                btn_upgrade_starterplan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    String url =    "http://api.just200.in/Just200Panel.svc/UpgradeLevel/"+memid+"/"+ finalUpgradeAmount;
                                        upgardeButtonUrl(url);
                                    }
                                });
                            }else{
                                btn_upgrade_starterplan.setVisibility(View.GONE);
                            }



                            pd.dismiss();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }





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
        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MemId", "205");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private   void upgardeLevelPlanMP(String REGISTER_URL) {
        // REGISTER_URL = UrlUtilities.login_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        String textVisible ="" ;String planUpdate="";String levelType ="";String upgradeAmount ="";
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject jsonObject = jArray.getJSONObject(i);
                                String balanceAmount = jsonObject.getString("BalanceAmount");
                                String cLevel = jsonObject.getString("CLevel");
                                 planUpdate = jsonObject.getString("PlanUpdate");
                                 levelType = jsonObject.getString("LevelType");
                                 textVisible = jsonObject.getString("TextVisible");
                                 upgradeAmount = jsonObject.getString("UpgradeAmount");



                            }

                              tv_upgrade_middleplanlevel_type.setText(levelType);
                            if(textVisible.equals("U")){
                                btn_upgrade_middleplan.setVisibility(View.VISIBLE);
                                final String finalUpgradeAmount = upgradeAmount;
                                btn_upgrade_middleplan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = "http://api.just200.in/Just200Panel.svc/UpgradeLevel/" + memid + "/" + finalUpgradeAmount;
                                        upgardeButtonUrl(url);
                                    }
                                });
                            }else{
                                btn_upgrade_middleplan.setVisibility(View.GONE);
                            }


                            pd.dismiss();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }





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
        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MemId", "205");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private   void upgardeLevelPlanEP(String REGISTER_URL) {
        // REGISTER_URL = UrlUtilities.login_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        String textVisible ="" ;String planUpdate="";String levelType ="";String upgradeAmount="";
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject jsonObject = jArray.getJSONObject(i);
                                String balanceAmount = jsonObject.getString("BalanceAmount");
                                String cLevel = jsonObject.getString("CLevel");
                                 planUpdate = jsonObject.getString("PlanUpdate");
                                levelType = jsonObject.getString("LevelType");
                                 textVisible = jsonObject.getString("TextVisible");
                                 upgradeAmount = jsonObject.getString("UpgradeAmount");



                            }
                            tv_upgrade_endplanlevel_type.setText(levelType);

                            if(textVisible.equals("U")){
                                btn_upgrade_endplan.setVisibility(View.VISIBLE);
                                final String finalUpgradeAmount = upgradeAmount;
                                btn_upgrade_endplan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String url = "http://api.just200.in/Just200Panel.svc/UpgradeLevel/" + memid + "/" + finalUpgradeAmount;
                                        upgardeButtonUrl(url);
                                    }
                                });
                            }else{
                                btn_upgrade_endplan.setVisibility(View.GONE);
                            }

                            pd.dismiss();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }





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
        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MemId", "205");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private   void upgardeButtonUrl(String REGISTER_URL) {
        // REGISTER_URL = UrlUtilities.login_URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    int count = 0;
                    @Override
                    public void onResponse(String response) {
                        String APIResponces ="";
                        String textVisible ="" ;String planUpdate="";String levelType ="";
                        //   Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jsonObject = jArray.getJSONObject(i);
                                 APIResponces = jsonObject.getString("APIResponce");


                            }
                            if(APIResponces.equals("Success")){
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(UpgradeFragment.this).attach(UpgradeFragment.this).commit();
                                Toast.makeText(getActivity(),"Upgrade Successfully",Toast.LENGTH_LONG).show();
                            }

                            pd.dismiss();


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }





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
        /*    @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MemId", "205");
                return params;
            }
*/
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
        // AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
