package edu.weber.rhilton.cs3270.ldscsp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String _talkURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new TalkViewFragment(),"TF")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.mniSelectTalk:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, new TalkSelectFragment(),"TS")
                        .addToBackStack(null)
                        .commit();
                break;
        }
        return true;
    }

    public void setTalk(String talkURL) {
        _talkURL = talkURL;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new TalkViewFragment(),"TV")
                .addToBackStack(null)
                .commit();
    }

    public String getTalkURL()
    {
        return _talkURL;
    }
}

