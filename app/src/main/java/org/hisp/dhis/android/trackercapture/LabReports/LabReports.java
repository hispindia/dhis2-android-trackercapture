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

public class LabReports extends AppCompatActivity {

    private String TAG = LabReports.class.getSimpleName();
    private List<OrganisationUnit> assignedOrganisationUnits;
    private ProgressDialog pDialog;
    private ListView lv;
    private String  jsonStr;


    // URL to get contacts JSON
//    private static String url = "http://apps.hispindia.org/aes_test/api/sqlViews/Tbcmm6xK9Mg/data.json?var=orgunit:";
    private static String url = "http://ds-india.org/aes/api/sqlViews/Tbcmm6xK9Mg/data.json?var=orgunit:";
    final Map<String, String> map = new HashMap<>();
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labreports);
        assignedOrganisationUnits= MetaDataController.getAssignedOrganisationUnits();
        url=url+assignedOrganisationUnits.get(0).getId();
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }

    //Async Call7

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LabReports.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Httphandler1 sh = new Httphandler1();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("rows");
                    LabReport labreport = new LabReport();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONArray c = contacts.getJSONArray(i);
                        String enroldate= c.get(1).toString().substring(0,10);
                        String orgname= c.get(2).toString();
                        String AES_Epid_ID= c.get(3).toString();
                        String NIMHANS_AES_ID= c.get(4).toString();
                        String name= c.get(5).toString();
                        String age= c.get(6).toString();
                        String gender= c.get(7).toString();
                        String samplecollected_csf= c.get(8).toString();
                        String samplesenttoapexlab_csf= c.get(9).toString();
                        String samplecollected_serum= c.get(10).toString();
                        String samplesenttoapexlab_serum= c.get(11).toString();
                        String samplecollected_wholeblood= c.get(12).toString();
                        String samplesent_wholeblood= c.get(13).toString();
                        String labresult_csf_wbccount= c.get(14).toString();
                        String labresult_csf_jeigmcount= c.get(15).toString();
                        String csf_gulucoselevel= c.get(16).toString();
                        String csf_proteinlevel= c.get(17).toString();
                        String serum_jeigmcount= c.get(18).toString();
                        String serum_igmden= c.get(19).toString();
                        String serum_scrumtyphusigm= c.get(20).toString();

                        String checkedMark = "\u2705";
                        String crossMark = "\u274C";

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("AES_Epid_ID", AES_Epid_ID);
                        contact.put("NIMHANS_AES_ID", NIMHANS_AES_ID);
                        contact.put("enroldate", enroldate);
                        contact.put("orgname", orgname);
                        contact.put("name", name);
                        contact.put("age", age);
                        contact.put("gender", gender);
                        Log.d("csf_gulucoselevel",csf_gulucoselevel);

                        if (samplecollected_csf.equals("") || samplecollected_csf.equals(null))
                        {

                            contact.put("samplecollected_csf", "No");
                        }
                        else
                        {

                            contact.put("samplecollected_csf", "Yes");
                        }

                        if (samplesenttoapexlab_csf.equals("") || samplesenttoapexlab_csf.equals(null))
                        {
                            Log.d("samplesenttoapexlab_csf",samplesenttoapexlab_csf);
                            contact.put("samplesenttoapexlab_csf", "No");
                        }
                        else
                        {
                            Log.d("samplesenttoapexlab_csf",samplesenttoapexlab_csf);
                            contact.put("samplesenttoapexlab_csf", "Yes");
                        }

                        if (samplecollected_serum.equals("") || samplecollected_serum.equals(null))
                        {
                            Log.d("samplecollected_serum",samplecollected_serum);
                            contact.put("samplecollected_serum", "No");
                        }
                        else
                        {
                            Log.d("samplecollected_serum",samplecollected_serum);
                            contact.put("samplecollected_serum", "Yes");
                        }

                        if (samplesenttoapexlab_serum.equals("") || samplesenttoapexlab_serum.equals(null))
                        {
                            Log.d("samplesenapexlab_serm",samplesenttoapexlab_serum);
                            contact.put("samplesenttoapexlab_serum", "No");
                        }
                        else
                        {
                            Log.d("samplesenttoaab_serum",samplesenttoapexlab_serum);
                            contact.put("samplesenttoapexlab_serum", "Yes");
                        }
                        if (samplecollected_wholeblood.equals("") || samplecollected_wholeblood.equals(null))
                        {
                            Log.d("samplesenapexlab_serm",samplesenttoapexlab_serum);
                            contact.put("samplecollected_wholeblood", "No");
                        }
                        else
                        {
                            Log.d("samplesenttoaab_serum",samplesenttoapexlab_serum);
                            contact.put("samplecollected_wholeblood", "Yes");
                        }
                        if (samplesent_wholeblood.equals("") || samplesent_wholeblood.equals(null))
                        {
                            Log.d("samplesenapexlab_serm",samplesenttoapexlab_serum);
                            contact.put("samplesent_wholeblood", "No");
                        }
                        else
                        {
                            Log.d("samplesenttoaab_serum",samplesenttoapexlab_serum);
                            contact.put("samplesent_wholeblood", "Yes");
                        }

//                            contact.put("samplecollected_csf", samplecollected_csf.toString());



//                            contact.put("samplesenttoapexlab_csf", samplesenttoapexlab_csf.toString());

//                            contact.put("samplecollected_serum", samplecollected_serum.toString());
//
//
//                            contact.put("samplesenttoapexlab_serum", samplesenttoapexlab_serum.toString());

//                            contact.put("samplecollected_wholeblood", samplecollected_wholeblood.toString());

//                            contact.put("samplesent_wholeblood", samplesent_wholeblood.toString());

                            contact.put("labresult_csf_wbccount", labresult_csf_wbccount.toString());

                            contact.put("labresult_csf_jeigmcount", labresult_csf_jeigmcount.toString());

                            contact.put("csf_gulucoselevel", csf_gulucoselevel.toString());

                            contact.put("csf_proteinlevel", csf_proteinlevel.toString());

                            contact.put("serum_jeigmcount", serum_jeigmcount.toString());

                            contact.put("serum_igmden", serum_igmden.toString());

                            contact.put("serum_scrumtyphusigm", serum_scrumtyphusigm.toString());


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
                                "Check Your Internet Connection please!",
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
                    LabReports.this, contactList,
                    R.layout.list_item, new String[]{"AES_Epid_ID", "NIMHANS_AES_ID","enroldate","orgname",
                    "name","age","gender","samplecollected_csf","samplesenttoapexlab_csf","samplecollected_serum","samplesenttoapexlab_serum","samplecollected_wholeblood","samplesent_wholeblood","labresult_csf_wbccount","labresult_csf_jeigmcount","csf_gulucoselevel","csf_proteinlevel","serum_jeigmcount","serum_igmden","serum_scrumtyphusigm"}, new int[]{R.id.AES_Epid_ID,
                    R.id.NIMHANS_AES_ID,R.id.enrolldate_,R.id.orgname_, R.id.name,R.id.age,R.id.gender,R.id.samplecollected_csf,R.id.samplesenttoapexlab_csf,R.id.samplecollected_serum,R.id.samplesenttoapexlab_serum,R.id.samplecollected_wholeblood,R.id.sample_sent_whole_,R.id.labresult_csf_wbccount,R.id.labresult_csf_jeigmcount,R.id.csf_gulucoselevel,R.id.csf_proteinlevel,R.id.serum_jeigmcount,R.id.serum_igmden,R.id.serum_scrumtyphusigm});
            lv.setAdapter(adapter);



        }

    }
}
