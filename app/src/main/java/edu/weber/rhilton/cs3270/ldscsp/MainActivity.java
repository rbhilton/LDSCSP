package edu.weber.rhilton.cs3270.ldscsp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
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
    String _confYear = "2017";
    String _confMonth = "04";
    String _confTalk = "";
    String _lang = "spa";

    MediaPlayer _mediaPlayer;


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
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("LDSCSP",MODE_PRIVATE);
        SharedPreferences.Editor sped = sp.edit();
        sped.putString("confYear",_confYear);
        sped.putString("confMonth",_confMonth);
        sped.putString("confTalk", _confTalk);
        sped.putString("confTalkURL", _talkURL);
        sped.putString("lang",_lang);
        sped.commit();
        Log.d("test","MainActivity onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("LDSCSP",MODE_PRIVATE);
        _confYear = sp.getString("confYear","2017");
        _confMonth = sp.getString("confMonth","10");
        _confTalk = sp.getString("confTalk","");
        _talkURL = sp.getString("confTalkURL","");
        _lang = sp.getString("lang","eng");
        Log.d("test","MainActivity onResume() confTalk=" + _confTalk);

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
            case R.id.mniSelectConference:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, new ConferenceSelectFragment(),"CS")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.mniPlayTalk:
                Log.e("test","menuItem=" + item);
                Log.e("test","menu title = " + item.getTitle());
                String title = item.getTitle().toString();
                if (title.equals("Play Talk")) {
                    String url = "http://media2.ldscdn.org/assets/general-conference/october-2017-general-conference/2017-10-4030-david-a-bednar-64k-spa.mp3";
                    playTalk(url);
                    item.setTitle("Stop Talk");
                } else {
                    Log.e("test","Cancelling player");
                    _mediaPlayer.stop();
                    item.setTitle("Play Talk");
                }
                break;
        }
        return true;
    }

    private void playTalk(String url)
    {
        new BackgroundSound().execute(url);
    }

    public class BackgroundSound extends AsyncTask<String, Void, MediaPlayer> {
        MediaPlayer mediaPlayer;
        @Override
        protected MediaPlayer doInBackground(String... params) {
            String url = params[0];
            mediaPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(url));
            //mediaPlayer.setLooping(true); // Set looping
            mediaPlayer.setVolume(100,100);
            mediaPlayer.start();

            return mediaPlayer;
        }

        @Override
        protected void onPostExecute(MediaPlayer mediaPlayer) {
            super.onPostExecute(mediaPlayer);
            _mediaPlayer = mediaPlayer;
            Log.e("test","onPostExecute()");
        }
    }

    public void setTalkURL(String talkURL) {
        Log.d("test","MainActivity setTalk() to " + talkURL);
        _talkURL = talkURL;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new TalkViewFragment(),"TV")
                .addToBackStack(null)
                .commit();
    }

    public String getTalkURL()
    {
        Log.d("test","MainActivity getTalkURL() = " + _talkURL);
        return _talkURL;
    }

    public void updateConference(String confYear, String confMonth)
    {
        _confYear = confYear;
        _confMonth = confMonth;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, new TalkViewFragment(),"TF")
                .addToBackStack(null)
                .commit();
    }

    public String getConfYear()
    {
        return _confYear;
    }

    public String getConfMonth()
    {
        return _confMonth;
    }

    public String getLang() {return _lang;}

    public void setLang(String lang) {_lang = lang;}
}

