package com.ersen.persistencemodulesample.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ersen.persistencemodulesample.BuildConfig;
import com.ersen.persistencemodulesample.R;
import com.ersen.persistencemodulesample.constants.AppConstants;
import com.ersen.persistencemodulesample.models.Treat;
import com.ersen.persistencemodulesample.utils.DownloadTreatContent;
import com.ersen.persistencemodulesample.utils.FileAndStorageUtils;
import com.ersen.persistencemodulesample.views.TreatAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DownloadTreatContent.OnTreatContentDownloaded {

    private ArrayList<Treat> mTreats;
    private Button mDownloadContentOffline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseTreats();
        setAdapter();
        mDownloadContentOffline = (Button) findViewById(R.id.button_download_content_offline);
        mDownloadContentOffline.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_download_content_offline:
                saveTreats();
                break;
        }
    }

    @Override
    public void onTreatContentDownloadStarted() {
        mDownloadContentOffline.setEnabled(false);
    }

    @Override
    public void onTreatContentDownloadFinished(int status) {
        mDownloadContentOffline.setEnabled(true);
        switch (status) {
            case DownloadTreatContent.SUCCESS:
                Toast.makeText(MainActivity.this, getString(R.string.msg_content_successfully_downloaded), Toast.LENGTH_SHORT).show();
                break;
            case DownloadTreatContent.FAILED:
                Toast.makeText(MainActivity.this, getString(R.string.error_content_failed_to_download), Toast.LENGTH_SHORT).show();
                break;
        }

        if(BuildConfig.DEBUG){
            for(Treat treat : mTreats){
                Log.d(MainActivity.class.getSimpleName(), treat.toString());
            }
        }
    }

    private void initialiseTreats() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); //Check to see if the treats were to saved to shared preferences
        String savedTreats = sharedPreferences.getString(AppConstants.PREF_TREATS_KEY, null);
        if (!TextUtils.isEmpty(savedTreats)) {
            Gson gson = new Gson(); //The array of treat objects were saved as a string in JSON form via GSON so its used again to re-construct that
            Type treatArrayType = new TypeToken<ArrayList<Treat>>() {
            }.getType();
            mTreats = gson.fromJson(savedTreats, treatArrayType);
        } else { //else Create the treats because it was not found in the shared preferences
            String[] treatNames = getResources().getStringArray(R.array.treat_names);
            String[] treatImageUrls = getResources().getStringArray(R.array.treat_image_urls);
            if (treatNames.length != treatImageUrls.length) {
                throw new IllegalStateException("The treat names and treat image urls arrays must be the same length for this sample to work!");
            }
            mTreats = new ArrayList<>();
            for (int i = 0; i < treatNames.length; i++) {
                mTreats.add(new Treat(treatNames[i], treatImageUrls[i]));
            }
        }
    }

    private void setAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_treats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TreatAdapter(this, mTreats));
    }


    private void saveTreats() {
        if (FileAndStorageUtils.isExternalStorageReady()) {
            new DownloadTreatContent(this, mTreats).execute();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.error_external_storage_not_ready), Toast.LENGTH_SHORT).show();
        }
    }


}
