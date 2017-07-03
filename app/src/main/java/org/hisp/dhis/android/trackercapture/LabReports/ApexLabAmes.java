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
import org.hisp.dhis.android.sdk.persistence.models.LabReport;
import org.hisp.dhis.android.trackercapture.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sourabh Bhardwaj on 21-05-2017.
 */

public class ApexLabAmes extends AppCompatActivity {

    private String TAG = LabReports.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private String jsonStr;
//  private static String url = "/sqlViews/bEOYTt4PTQg/data.json";

//    private  String url = "http://apps.hispindia.org/aes_test/api/sqlViews/Orh74OlRLmg/data.json?var=orgunit:";
    private  String url = "http://ds-india.org/aes/api/sqlViews/TNEACle8fKx/data.json?var=orgunit:";

    private  String orguid;
    final Map<String, String> map = new HashMap<>();


    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labreports);

        Bundle b = getIntent().getExtras();
        String orgid = b.getString("orgid");
        String orgname = b.getString("orgname");

        contactList = new ArrayList<>();
        url=url+orgid;
        lv = (ListView) findViewById(R.id.list);



        new GetContacts().execute();
        Log.d("Spinner_values:",orgid+orgname);
        Log.d("url:",url);

    }

    //Async Call7

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ApexLabAmes.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Httphandler1 sh = new Httphandler1();
            jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("rows");
                    LabReport labreport = new LabReport();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length()-1; i++) {


                        JSONArray c = contacts.getJSONArray(i);


                        String tei= c.get(1).toString().substring(0,10);
                        String pen= c.get(2).toString();
                        String AES_Epid_ID= c.get(3).toString();
                        String NIMHANS_AES_ID= c.get(4).toString();
                        String name= c.get(5).toString();
                        String age= c.get(6).toString();
                        String gender= c.get(7).toString();

                        String apex_result_1= c.get(8).toString();
                        String apex_result_2= c.get(9).toString();
                        String apex_result_3= c.get(10).toString();
                        String apex_result_4= c.get(11).toString();
                        String apex_result_5= c.get(12).toString();
                        String apex_result_6= c.get(13).toString();
                        String apex_result_7= c.get(14).toString();
                        String apex_result_8= c.get(15).toString();
                        String apex_result_9= c.get(16).toString();
                        String apex_result_10= c.get(17).toString();
                        String apex_result_11= c.get(18).toString();
                        String apex_result_12= c.get(19).toString();
                        String apex_result_13= c.get(21).toString();
                        String apex_result_14= c.get(22).toString();
                        String apex_result_15= c.get(23).toString();

                        String checkedMark = "\u2713";
                        String crossMark = "\u274C";

//
//                        labreport.setAesepid(AES_Epid_ID);
//                        labreport.setNIMHANS_AES_ID(NIMHANS_AES_ID);
//                        labreport.setPname(name);
//                        labreport.setAge(age);
//                        labreport.setGender(gender);
//                        labreport.setSamplecollected_csf(samplecollected_csf);
//                        labreport.setSamplesenttoapexlab_csf(samplesenttoapexlab_csf);
//                        labreport.setSamplecollected_serum(samplecollected_serum);
//                        labreport.setSamplesenttoapexlab_serum(samplesenttoapexlab_serum);
//                        labreport.setSamplecollected_wholeblood(samplecollected_wholeblood);
//                        labreport.setLabresult_csf_wbccount(labresult_csf_wbccount);
//                        labreport.setLabresult_csf_jeigmcount(labresult_csf_jeigmcount);
//                        labreport.setCsf_gulucoselevel(csf_gulucoselevel);
//                        labreport.setCsf_proteinlevel(csf_proteinlevel);
//                        labreport.setCsf_sample2_jeigmcount(csf_sample2_jeigmcount);
//                        labreport.setCsf_sample2_wbc_count(csf_sample2_wbc_count);
//                        labreport.setSerum_jeigmcount(serum_jeigmcount);
//                        labreport.setSerum_igmden(serum_igmden);
//                        labreport.setSerum_scrumtyphusigm(serum_scrumtyphusigm);
//                        labreport.setLabresult_csf_jeigmcount(labresult_csf_jeigmcount);
//                        labreport.setLabresult_csf_wbccount(labresult_csf_wbccount);
//                        labreport.save();



//
//                        Log.d("laqqb",labreport.getName());

//                        String id = c[0];
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
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


                        contact.put("apex_result_9", apex_result_9.toString());


                        contact.put("apex_result_10", apex_result_10.toString());

                        contact.put("apex_result_11", apex_result_11.toString());

                        contact.put("apex_result_12", apex_result_12.toString());

                        contact.put("apex_result_13", apex_result_13.toString());

                        contact.put("apex_result_14", apex_result_14.toString());

                        contact.put("apex_result_15", apex_result_15.toString());



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
                    ApexLabAmes.this, contactList,
                    R.layout.list_item_apex, new String[]{"AES_Epid_ID", "NIMHANS_AES_ID","tei","pen",
                    "name","age","gender","apex_result_1","apex_result_2","apex_result_3","apex_result_4","apex_result_5","apex_result_6","apex_result_7","apex_result_8","apex_result_9","apex_result_9","apex_result_10","apex_result_11","apex_result_12","apex_result_13","apex_result_14","apex_result_15"}, new int[]{R.id.AES_Epid_ID,
                    R.id.NIMHANS_AES_ID, R.id.enroll_date_, R.id.orgname_, R.id.name, R.id.age, R.id.gender, R.id.labresults_11, R.id.labresults_22, R.id.labresults_33, R.id.labresults_44, R.id.labresults_55, R.id.labresults_66, R.id.labresults_77, R.id.labresults_88, R.id.labresults_99, R.id.labresults_1010, R.id.labresults_1111, R.id.labresults_1212});
            lv.setAdapter(adapter);



        }

    }
}
