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
import org.hisp.dhis.android.trackercapture.R;
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

public class NimhansLabAmes extends AppCompatActivity {

    private String TAG = LabReports.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private String jsonStr;
    private List<OrganisationUnit> assignedOrganisationUnits;
//  private static String url = "/sqlViews/bEOYTt4PTQg/data.json";

//    private  String url = "http://apps.hispindia.org/aes_test/api/sqlViews/Orh74OlRLmg/data.json?var=orgunit:";
    private  String url = "http://ds-india.org/aes/api/sqlViews/ITIksjAoaf4/data.json?var=orgunit:";

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
//        Log.d("url:",url);

    }

    //Async Call7

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(NimhansLabAmes.this);
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

                        String sample_r_csf= c.get(8).toString();
                        String sample_r_serum= c.get(9).toString();
                        String sample_r_wholeb= c.get(10).toString();

                        String apex_result_1= c.get(11).toString();
                        String apex_result_2= c.get(12).toString();
                        String apex_result_3= c.get(13).toString();
                        String apex_result_4= c.get(14).toString();
                        String apex_result_5= c.get(15).toString();
                        String apex_result_6= c.get(16).toString();

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

                        contact.put("apex_result_0", apex_result_1.toString());
                        contact.put("sample_r_csf", sample_r_csf.toString());
                        contact.put("sample_r_serum", sample_r_serum.toString());
                        contact.put("sample_r_wholeb", sample_r_wholeb.toString());

                        contact.put("apex_result_1", apex_result_2.toString());

                        contact.put("apex_result_2", apex_result_3.toString());

                        contact.put("apex_result_3", apex_result_4.toString());

                        contact.put("apex_result_4", apex_result_5.toString());

                        contact.put("apex_result_5", apex_result_6.toString());


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
                    NimhansLabAmes.this, contactList,
                    R.layout.list_item_nimhans_aes, new String[]{"AES_Epid_ID", "NIMHANS_AES_ID","tei","pen",
                    "name","age","gender","sample_r_csf","sample_r_serum","sample_r_wholeb","apex_result_0","apex_result_1","apex_result_2","apex_result_3","apex_result_4","apex_result_5"}, new int[]{R.id.AES_Epid_ID,
                    R.id.NIMHANS_AES_ID, R.id.enroll_date_, R.id.orgname_, R.id.name, R.id.age, R.id.gender, R.id.sample_r_csf_, R.id.sample_r_serum_, R.id.sample_r_wholeb_,R.id.labresults_00_, R.id.labresults_11, R.id.labresults_22, R.id.labresults_33, R.id.labresults_44, R.id.labresults_55});
            lv.setAdapter(adapter);



        }

    }
}
