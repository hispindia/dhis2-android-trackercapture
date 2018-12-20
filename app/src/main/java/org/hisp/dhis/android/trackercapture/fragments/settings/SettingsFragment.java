/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.ui.fragments.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.HttpUrl;
import com.squareup.otto.Subscribe;
import java.io.File;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.controllers.DhisController;
import org.hisp.dhis.android.sdk.controllers.DhisService;
import org.hisp.dhis.android.sdk.controllers.PeriodicSynchronizerController;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.controllers.SyncStrategy;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.events.LoadingMessageEvent;
import org.hisp.dhis.android.sdk.events.UiEvent;
import org.hisp.dhis.android.sdk.network.Session;
import org.hisp.dhis.android.sdk.persistence.Dhis2Application;
import org.hisp.dhis.android.sdk.persistence.models.UserAccount;
import org.hisp.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.hisp.dhis.android.sdk.ui.activities.LoginActivity;
import org.hisp.dhis.android.sdk.ui.views.FontTextView;
import org.hisp.dhis.android.sdk.utils.UiUtils;
import org.w3c.dom.Text;

/**
 * Basic settings Fragment giving users options to change update frequency to the server,
 * and logging out.
 *
 * @author Simen Skogly Russnes on 02.03.15.
 */
public class SettingsFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TAG = SettingsFragment.class.getSimpleName();
    private Spinner updateFrequencySpinner;
    private Button logoutButton;
    private Button synchronizeButton;
    private Button synchronizeRemovedEventsButton;
    private ProgressBar mProgressBar;
    private TextView syncTextView;
    private FontTextView developeroptions;
    private FontTextView autosyncfreq;
    private LoadingMessageEvent progressMessage;
    private static final String TZ_LANG= "sw";
    private static final String VI_LANG= "vi";
    private static final String IN_LANG= "in";
    private static final String TZ_SYNCSERVER= "Sawazisha/hifadhi kwenye seva";
    private static final String VI_SYNCSERVER= "Đồng bộ với máy chủ";

    private static final String TZ_SYNCREMOTE= "Kwa mbali hifadhi/sawazisha data iliyofutwa";
    private static final String VI_SYNCREMOTE= "Đồng bộ dữ liệu đã xóa";
    private static final String TZ_DELETE= "Futa data zote za ndani na utoke";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we need to disable options menu in this fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        if(getActionBar() != null) {
            getActionBar().setTitle(getString(R.string.settings));
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        final UserAccount uslocal= MetaDataController.getUserLocalLang();
        String user_locallang=uslocal.getUserSettings().toString();
        String localdblang=user_locallang;
        autosyncfreq=(FontTextView) view.findViewById(R.id.update_frequency_label);
        developeroptions=(FontTextView) view.findViewById(R.id.developer_title);
        if(localdblang.equals(TZ_LANG))
        {
            getActionBar().setTitle("Panga/kuweka");
            updateFrequencySpinner = (Spinner) view.findViewById(R.id.settings_update_frequency_spinner_tz);
            updateFrequencySpinner.setVisibility(View.VISIBLE);
            updateFrequencySpinner.setSelection(PeriodicSynchronizerController.getUpdateFrequency(getActivity()));
            updateFrequencySpinner.setOnItemSelectedListener(this);
        }
        else if(localdblang.equals(VI_LANG))
        {
            getActionBar().setTitle("Cài đặt hệ thống");
            updateFrequencySpinner = (Spinner) view.findViewById(R.id.settings_update_frequency_spinner_tz);
            updateFrequencySpinner.setVisibility(View.VISIBLE);
            updateFrequencySpinner.setSelection(PeriodicSynchronizerController.getUpdateFrequency(getActivity()));
            updateFrequencySpinner.setOnItemSelectedListener(this);
        }
        else if(localdblang.equals("in"))
        {
            getActionBar().setTitle("pengaturan");
            updateFrequencySpinner = (Spinner) view.findViewById(R.id.settings_update_frequency_spinner_tz);
            updateFrequencySpinner.setVisibility(View.VISIBLE);
            updateFrequencySpinner.setSelection(PeriodicSynchronizerController.getUpdateFrequency(getActivity()));
            updateFrequencySpinner.setOnItemSelectedListener(this);
        }
        else
        {
            updateFrequencySpinner = (Spinner) view.findViewById(R.id.settings_update_frequency_spinner);
            updateFrequencySpinner.setVisibility(View.VISIBLE);
            updateFrequencySpinner.setSelection(PeriodicSynchronizerController.getUpdateFrequency(getActivity()));
            updateFrequencySpinner.setOnItemSelectedListener(this);
        }


        synchronizeButton = (Button) view.findViewById(R.id.settings_sync_button);

        synchronizeRemovedEventsButton = (Button) view.findViewById(
                R.id.settings_sync_remotely_deleted_events_button);
        logoutButton = (Button) view.findViewById(R.id.settings_logout_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.settings_progessbar);
        syncTextView = (TextView) view.findViewById(R.id.settings_sync_textview);
        mProgressBar.setVisibility(View.GONE);
        logoutButton.setOnClickListener(this);
        synchronizeButton.setOnClickListener(this);
        synchronizeRemovedEventsButton.setOnClickListener(this);

        if(localdblang.equals(TZ_LANG))
        {
            synchronizeButton.setText(TZ_SYNCSERVER);
            autosyncfreq.setText("zamisha taarifa kwenye seva mara kwa mara ");
            synchronizeRemovedEventsButton.setText(TZ_SYNCREMOTE);
            logoutButton.setText(TZ_DELETE);
            developeroptions.setText("Chaguo la watengenezaji");
        }
        else if(localdblang.equals(VI_LANG))
        {
            logoutButton.setText("Xóa toàn bộ dữ liệu và ra khỏi hệ thống");
            developeroptions.setText("Các chức năng cho người phát triển hệ thống");
        }
        else if(localdblang.equals("my"))
        {
            logoutButton.setText("အခ်က္လက္အားလံုးကိုဖ်က္ၿပီး အစီအစဥ္မွ ထြက္ျခင္း");
            developeroptions.setText("ကြန္ပ်ဴတာပ႐ိုဂရမ္ေရးဆြဲသူ၏ေရြးခ်ယ္မႈ");
        }
        else if(localdblang.equals(IN_LANG))
        {
            logoutButton.setText("Hapus semua data lokal dan keluar");
            developeroptions.setText("Pilihan pengembang");
        }

        //if(DhisController.isLoading() && getProgressMessage() != null)
        {
            //syncTextView.setText(getProgressMessage());
            //Log.d(TAG, getProgressMessage());
        }
        //else if(!DhisController.isLoading())
        {
            //setSummaryFromLastSync in syncTextView
            //syncTextView.setText(DhisController.getLastSynchronizationSummary());
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settings_logout_button) {
            String logout_message_trans="";
            String logout_title_trans="";
            String logout_trans="";
            String cancel_trans="";
            final UserAccount uslocal= MetaDataController.getUserLocalLang();
            String user_locallang=uslocal.getUserSettings().toString();
            String localdblang=user_locallang;
            if(localdblang.equals(TZ_LANG))
            {
                logout_message_trans="Una uhakika unataka kuingia nje? Data yako yote iliyohifadhiwa kwenye kifaa itapotea ikiwa unachagua kuingia.";
                logout_title_trans="Kuingia nje";
                logout_trans="Futa data zote za ndani na uondoke";
                cancel_trans="kufuta";
            }
            else if(localdblang.equals(VI_LANG))
            {
                logout_message_trans="Bạn có chắc chắn bạn muốn thoát? Tất cả dữ liệu của bạn được lưu trữ trên thiết bị sẽ bị mất nếu bạn chọn đăng xuất.";
                logout_title_trans="Thoát ra";
                logout_trans="Xóa tất cả dữ liệu cục bộ và đăng xuất";
                cancel_trans="hủy bỏ";
            }
            else
            {
                logout_message_trans=getString(R.string.logout_message);
                logout_title_trans=getString(R.string.logout_title);
                logout_trans=getString(R.string.logout);
                cancel_trans=getString(R.string.cancel);
            }



            UiUtils.showConfirmDialog(getActivity(), logout_title_trans,
                    logout_message_trans,
                    logout_trans, cancel_trans,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (DhisController.hasUnSynchronizedDatavalues) {
                                //show error dialog
                                UiUtils.showErrorDialog(getActivity(),
                                        getString(R.string.error_message),
                                        getString(R.string.unsynchronized_data_values),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                            } else {
                                Session session = DhisController.getInstance().getSession();
                                if (session != null) {
                                    HttpUrl httpUrl = session.getServerUrl();
                                    if (httpUrl != null) {
                                        String serverUrlString = httpUrl.toString();
                                        AppPreferences appPreferences = new AppPreferences(
                                                getActivity().getApplicationContext());
                                        appPreferences.putServerUrl(serverUrlString);
                                    }
                                }

                                DhisService.logOutUser(getActivity());
                                int apiVersion = Build.VERSION.SDK_INT;
                                if(apiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                                    deleteCache(getContext());
                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    deleteAppData(getContext());
                                    deleteCache(getContext());
                                }
                                else {
//                                    deleteCache(getContext());
//                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    deleteAppData(getContext());
                                    deleteCache(getContext());
                                }

                            }
                        }
                    });
        }


        else if (view.getId() == R.id.settings_sync_button) {
            if (isAdded()) {
                final Context context = getActivity().getBaseContext();
                Toast.makeText(context, getString(R.string.syncing), Toast.LENGTH_SHORT).show();
                setProgressMessage(new LoadingMessageEvent(getString(R.string.syncing),
                        LoadingMessageEvent.EventType.METADATA));
                new Thread() {
                    @Override
                    public void run() {
                        DhisService.synchronize(context, SyncStrategy.DOWNLOAD_ALL);
                    }
                }.start();
                startSync();
            }
        } else if (view.getId() == R.id.settings_sync_remotely_deleted_events_button) {
            if (isAdded()) {
                final Context context = getActivity().getBaseContext();
                Toast.makeText(context, getString(R.string.sync_deleted_events),
                        Toast.LENGTH_SHORT).show();
                setProgressMessage(new LoadingMessageEvent(getString(R.string.sync_deleted_events),
                        LoadingMessageEvent.EventType.REMOVE_DATA));

                new Thread() {
                    @Override
                    public void run() {
                        DhisService.synchronizeRemotelyDeletedData(context);
                    }
                }.start();
                startSync();
            }
        }
    }


    private void startSync() {
        changeUiVisibility(false);
        setText(getProgressMessage());
    }

    private void endSync() {
        changeUiVisibility(true);
        syncTextView.setText("");

        final UserAccount uslocal= MetaDataController.getUserLocalLang();
        String user_locallang=uslocal.getUserSettings().toString();
        String localdblang=user_locallang;
        if(localdblang.equals(TZ_LANG))
        {
            synchronizeButton.setText(TZ_SYNCSERVER);
            synchronizeRemovedEventsButton.setText(TZ_SYNCREMOTE);
        }
        else if(localdblang.equals(VI_LANG))
        {
            synchronizeButton.setText(VI_SYNCSERVER);
            synchronizeRemovedEventsButton.setText(VI_SYNCREMOTE);
        }
        else if(localdblang.equals("my"))
        {
            synchronizeButton.setText("အခ်က္လက္ကိုအေဝးမွပယ္ဖ်က္ႏိုင္ျခင္း");
            synchronizeRemovedEventsButton.setText("ဖျက်ပစ်ဒေတာထပ်တူကျအောင်");
        }
        else if(localdblang.equals(IN_LANG))
        {
            synchronizeButton.setText("Sinkronkan dengan server");
            synchronizeRemovedEventsButton.setText("Sinkronkan data yang dihapus dari jarak jauh");
        }
        else
        {
            synchronizeButton.setText(R.string.synchronize_with_server);
            synchronizeRemovedEventsButton.setText(R.string.synchronize_deleted_data);
        }

    }

    private void changeUiVisibility(boolean enabled) {
        if (!enabled) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
        synchronizeButton.setEnabled(enabled);
        synchronizeRemovedEventsButton.setEnabled(enabled);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PeriodicSynchronizerController.setUpdateFrequency(getActivity(), position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // stub implementation
    }

    private void setText(LoadingMessageEvent event)
    {

        if (event != null) {
            if (event.eventType.equals(LoadingMessageEvent.EventType.DATA) ||
                    event.eventType.equals(LoadingMessageEvent.EventType.METADATA) ||
                    event.eventType.equals(LoadingMessageEvent.EventType.STARTUP)) {
                changeUiVisibility(false);
                final UserAccount uslocal= MetaDataController.getUserLocalLang();
                String user_locallang=uslocal.getUserSettings().toString();
                String localdblang=user_locallang;
                if(localdblang.equals(TZ_LANG))
                {
                    synchronizeButton.setText("Inalinganisha ..");
                }
                else if(localdblang.equals(VI_LANG))
                {
                    synchronizeButton.setText("Đang đồng bộ hóa ..");
                }
                else
                {
                    synchronizeButton.setText(getActivity().getApplicationContext().getString(
                            R.string.synchronizing));
                }


            } else if (event.eventType.equals(LoadingMessageEvent.EventType.REMOVE_DATA)) {
                final UserAccount uslocal= MetaDataController.getUserLocalLang();
                String user_locallang=uslocal.getUserSettings().toString();
                String localdblang=user_locallang;
                if(localdblang.equals(TZ_LANG))
                {
                    synchronizeRemovedEventsButton.setText("Inalinganisha ..");
                }
                else if(localdblang.equals(VI_LANG))
                {
                    synchronizeRemovedEventsButton.setText("Đang đồng bộ hóa ..");
                }
                else
                {
                    synchronizeRemovedEventsButton.setText(
                            getActivity().getApplicationContext().getString(
                                    R.string.synchronizing));
                }



                changeUiVisibility(false);
            } else if (event.eventType.equals(LoadingMessageEvent.EventType.FINISH)) {
                endSync();
            }

            if (event.message != null) {
                syncTextView.setText(event.message);
            } else
                Log.d(TAG, "Loading message is null");
        }
    }
    @Subscribe
    public void onLoadingMessageEvent(final LoadingMessageEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                setProgressMessage(event);
                setText(event);
            }
        });
    }

    private void enableUi(boolean enable)
    {
        if(!enable)
        {
            startSync();
        }
        else
        {
            endSync();
        }
    }

    @Subscribe
    public void onSynchronizationFinishedEvent(final UiEvent event)
    {
        if (event.getEventType().equals(UiEvent.UiEventType.SYNCING_START)) {
            enableUi(false);
        } else if (event.getEventType().equals(UiEvent.UiEventType.SYNCING_END))
        {
            enableUi(true);
        }
    }

    public LoadingMessageEvent getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(LoadingMessageEvent progressMessage) {
        this.progressMessage = progressMessage;
    }

    @Override
    public void onPause() {
        super.onPause();
        Dhis2Application.getEventBus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Dhis2Application.getEventBus().register(this);

        //if(!DhisController.isLoading())
        {
            enableUi(true);
        }
        //else
        //    enableUi(false);
        if (!MetaDataController.isDataLoaded(getActivity().getApplicationContext())) {
            LoadingMessageEvent event = new LoadingMessageEvent("",
                    LoadingMessageEvent.EventType.STARTUP);
            setProgressMessage(event);
            setText(event);
        }
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    //Method to clear appdata
    private void deleteAppData(Context context) {
        try {
            // clearing app data
            String packageName = context.getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
        } catch (Exception e) {
            e.printStackTrace();
        } }
}
