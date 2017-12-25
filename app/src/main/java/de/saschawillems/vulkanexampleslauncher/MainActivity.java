package de.saschawillems.vulkanexampleslauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public boolean appInstalled(String packageName) {
        PackageManager pm = this.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return (intent != null);
    }

    private void launchApp(String appName) {
        String packageName = "de.saschawillems." + appName;
        if (appInstalled(packageName)) {
            startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
        }
        else {
            //TODO: Display some message
            Toast.makeText(this, "This Vulkan example is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<VulkanExample> exampleList = loadExampleList(this);

        ListView listView = (ListView) findViewById(R.id.example_list);
        ExampleAdapter adapter = new ExampleAdapter(this, exampleList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VulkanExample selectedExample = exampleList.get(position);
                launchApp(selectedExample.apk);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<VulkanExample> loadExampleList(Context context) {
        ArrayList<VulkanExample> exampleList = new ArrayList<>();

        // Load list of examples from json asset
        String jsonFile = null;
        try {
            InputStream is = context.getAssets().open("examples.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonFile = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            // TODO: Display error message
            return null;
        }

        try {
            JSONObject json = new JSONObject(jsonFile);
            JSONArray jsonEntries = json.getJSONArray("examples");

            for(int i = 0; i < jsonEntries.length(); i++){
                VulkanExample example = new VulkanExample();

                example.title = jsonEntries.getJSONObject(i).getString("title");
                example.apk = jsonEntries.getJSONObject(i).getString("apk");
                example.description = jsonEntries.getJSONObject(i).getString("description");
                example.installed = appInstalled("de.saschawillems." + example.apk);

                exampleList.add(example);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exampleList;
    }
}
