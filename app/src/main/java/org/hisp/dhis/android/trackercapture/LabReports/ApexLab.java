package org.hisp.dhis.android.trackercapture.LabReports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.LabReport;

import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hiaes.dhis.android.trackercapture.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sourabh Bhardwaj on 21-05-2017.
 */

public class ApexLab extends AppCompatActivity {

    private String TAG = LabReports.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private String jsonStr;
    private List<OrganisationUnit> assignedOrganisationUnits;
//  private static String url = "/sqlViews/bEOYTt4PTQg/data.json";

//    private  String url = "http://apps.hispindia.org/aes_test/api/sqlViews/GsiGUuHemvy/data.json?var=orgunit:";
    private  String url = "http://ds-india.org/aes/api/sqlViews/GsiGUuHemvy/data.json?var=orgunit:";

    private  String orguid;
    final Map<String, String> map = new HashMap<>();


    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labreports);

//        Bundle b = getIntent().getExtras();
//        String orgid = b.getString("orgid");
//        String orgname = b.getString("orgname");
        assignedOrganisationUnits= MetaDataController.getAssignedOrganisationUnits();
        url=url+assignedOrganisationUnits.get(0).getId()+"&";

        contactList = new ArrayList<>();
//        url=url+orgid+"&";
        lv = (ListView) findViewById(R.id.list);



        new GetContacts().execute();
//        Log.d("Spinner_values:",orgid+orgname);
        Log.d("url:",url);

    }

    //Async Call7

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ApexLab.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Httphandler1 sh = new Httphandler1();
            jsonStr = sh.makeServiceCall(url);
//            String jsonStr1 = sh.makeServiceCall("http://bulksms.mysmsmantra.com:8080/WebSMS/SMSAPI.jsp?username=hispindia&password=hisp1234&sendername=HSSPIN&mobileno=9643274071&message=test");

            Log.e(TAG, "Response from url: " + jsonStr);
//            Log.e(TAG, "Response from sms url: " + jsonStr1);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("rows");
                    LabReport labreport = new LabReport();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length()-1; i++) {


                        //attributes
                        JSONArray c = contacts.getJSONArray(i);
                        String tei= c.get(1).toString().substring(0,10);
                        String pen= c.get(2).toString();
                        String AES_Epid_ID= c.get(3).toString();
                        String NIMHANS_AES_ID= c.get(4).toString();
                        String name= c.get(5).toString();
                        String age= c.get(6).toString();
                        String gender= c.get(7).toString();

                        //sample received
                        String apex_result_1= c.get(8).toString();
                        String apex_result_2= c.get(9).toString();
                        String apex_result_3= c.get(10).toString();

                        if(apex_result_1==null||apex_result_1=="")
                        {
                            apex_result_1="No";
                        }
                        if(apex_result_2==null||apex_result_2=="")
                        {
                            apex_result_2="No";
                        }
                        if(apex_result_3==null||apex_result_3=="")
                        {
                            apex_result_3="No";
                        }

                        //results
                        //JE IgM
                        String apex_result_4= c.get(11).toString();

                        //Bacterial Taqman Real Time PCR for Scrub-Ty"
                        String apex_result_5= c.get(12).toString();

                        //Lab results - CSF - HSV Taqman Real Time PCR
                        String apex_result_6= c.get(13).toString();

                        //Lab results - CSF - Bacterial Taqman Real Time PCR for Streptoc
                        String apex_result_7= c.get(14).toString();

                        //Lab results - CSF - Bacterial Taqman Real Time PCR for Hemophil
                        String apex_result_8= c.get(15).toString();

                        //Lab results - CSF - Bacterial Taqman Real Time PCR for Neisseri
                        String apex_result_9= c.get(16).toString();

                        //Lab results - CSF - Enterovirus Taqman Real Time PCR
                        String apex_result_10= c.get(17).toString();

                        //Lab results - CSF - Leptospira DNA PCR
                        String apex_result_11= c.get(18).toString();

                        //Lab results - CSF - Trioplex PCR
                        String apex_result_12= c.get(19).toString();

                        //Lab results - Serum - JE IgM
                        String apex_result_13= c.get(20).toString();

                        //Lab results - Serum - Scrub typhus IgM ELISA
                        String apex_result_14= c.get(21).toString();

                        //Lab results - Serum - Dengue IgM ELISA
                        String apex_result_15= c.get(22).toString();

                        //Lab results - Serum - Dengue NS1 Antigen ELISA
                        String apex_result_16= c.get(23).toString();

                        //Lab results - Serum - Chikungunya IgM ELISA
                        String apex_result_17= c.get(24).toString();

                        //Lab results - Serum - West Nile Virus IgM ELISA
                        String apex_result_18= c.get(25).toString();

                        //Lab results - Serum- Leptospira IgM ELISA
                        String apex_result_19= c.get(26).toString();

                        //Lab results - Serum- Trioplex PCR
                        String apex_result_20= c.get(27).toString();

                        //Lab results - Whole blood - Scrub Typhus PCR
                        String apex_result_21= c.get(28).toString();

                        String checkedMark = "\u2713";
                        String crossMark = "\u274C";

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("AES_Epid_ID", AES_Epid_ID);
                        contact.put("NIMHANS_AES_ID", NIMHANS_AES_ID);
                        contact.put("tei", tei);
                        contact.put("pen", pen);
                        contact.put("name", name);
                        contact.put("age", age);
                        contact.put("gender", gender);
//
//                        if (apex_result_1.equals("") || apex_result_1.equals(null))
//                        {
//                            Log.d("apex_result_1",apex_result_1);
//                            contact.put("apex_result_1", "null");
//                        }

                        contact.put("apex_result_1", apex_result_1.toString());

                        contact.put("apex_result_2", apex_result_2.toString());

                        contact.put("apex_result_3", apex_result_3.toString());

                        contact.put("apex_result_4", apex_result_4.toString());

                        contact.put("apex_result_5", apex_result_5.toString());

                        contact.put("apex_result_6", apex_result_6.toString());

                        contact.put("apex_result_7", apex_result_7.toString());

                        contact.put("apex_result_8", apex_result_8.toString());


                        contact.put("apex_result_9", apex_result_9.toString());


                        contact.put("apex_result_10", apex_result_10.toString());

                        contact.put("apex_result_11", apex_result_11.toString());

                        contact.put("apex_result_12", apex_result_12.toString());

                        contact.put("apex_result_13", apex_result_13.toString());

                        contact.put("apex_result_14", apex_result_14.toString());

                        contact.put("apex_result_15", apex_result_15.toString());
                        contact.put("apex_result_16", apex_result_16.toString());
                        contact.put("apex_result_17", apex_result_17.toString());
                        contact.put("apex_result_18", apex_result_18.toString());
                        contact.put("apex_result_19", apex_result_19.toString());
                        contact.put("apex_result_20", apex_result_20.toString());
                        contact.put("apex_result_21", apex_result_21.toString());



                        // adding contact to contact list
                        contactList.add(contact);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No Result found for current user",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */




            ListAdapter adapter = new SimpleAdapter(
                    ApexLab.this, contactList,
                    R.layout.list_item_apex, new String[]{"AES_Epid_ID", "NIMHANS_AES_ID","tei","pen",
                    "name","age","gender","apex_result_1","apex_result_2","apex_result_3","apex_result_4","apex_result_5","apex_result_6","apex_result_7","apex_result_8","apex_result_9","apex_result_10","apex_result_11","apex_result_12","apex_result_13","apex_result_14","apex_result_15","apex_result_16","apex_result_17","apex_result_18","apex_result_19","apex_result_20","apex_result_21"}, new int[]{R.id.AES_Epid_ID,
                    R.id.NIMHANS_AES_ID, R.id.enroll_date_, R.id.orgname_, R.id.name, R.id.age, R.id.gender, R.id.sample_r_csf_, R.id.sample_r_serum_, R.id.sample_r_wholeb_, R.id.apex_result_4_id, R.id.apex_result_5_id, R.id.apex_result_6_id, R.id.apex_result_7_id, R.id.apex_result_8_id, R.id.apex_result_9_id,R.id.apex_result_10_id,R.id.apex_result_11_id ,R.id.apex_result_12_id, R.id.apex_result_13_id, R.id.apex_result_14_id, R.id.apex_result_15_id, R.id.apex_result_16_id, R.id.apex_result_17_id,R.id.apex_result_18_id,
                        R.id.apex_result_19_id,R.id.apex_result_20_id,R.id.apex_result_21_id});
            lv.setAdapter(adapter);



        }

    }
}
