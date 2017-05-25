package edu.weber.rhilton.cs3270.ldscsp;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button btnGetHTML;
    TextView txvHTMLResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnGetHTML = (Button) findViewById(R.id.btnGetHTML);
        txvHTMLResults = (TextView) findViewById(R.id.txvHTMLResults);

        btnGetHTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new getWebPage().execute("");
                //getEnglish("https://www.lds.org/general-conference/2017/04/gathering-the-family-of-god?lang=eng");
                new getConfTalk().execute("https://www.lds.org/general-conference/2017/04/gathering-the-family-of-god?lang=eng");

            }
        });
    }


    public class getConfTalk extends AsyncTask<String,Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String rawHTML = "";
            Log.d("test","MainActivity getEnglish(" + strings[0] + ")");
            try {
                Document doc = Jsoup.connect(strings[0]).get();
                Element talk = doc.select("div.body-block").first();
                rawHTML = talk.html();
            } catch (IOException iox) {

            }

            return rawHTML;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            txvHTMLResults.setText(s);
        }
    }

    public class getWebPage extends AsyncTask<String, Integer, String> {

        String rawJson = "";

        @Override
        protected String doInBackground(String... params) {

            Log.d("test","In AsyncTask getWebPage");
            try {
                URL url = new URL("https://www.lds.org/general-conference/2017/04/gathering-the-family-of-god?lang=eng");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int status = conn.getResponseCode();
                switch (status) {
                    case 200:  //200 or 201 indicate success
                    case 201:
                        BufferedReader br =
                                new BufferedReader (new InputStreamReader(conn.getInputStream()));
                        rawJson = br.readLine();
                        Log.d("test","Status: " + status + "   Raw Json String Length = " + rawJson.length());
                        Log.d("test","Return: " + rawJson);
                }

            } catch (MalformedURLException e) {
                Log.d("test",e.getMessage());
            }
            catch (IOException e) {
                Log.d("test",e.getMessage());
            }
            return rawJson;
        }

        @Override
        protected void onPostExecute(String result) {
            // Executed when the AsyncTask completes
            super.onPostExecute(result);

            txvHTMLResults.setText(result);

        }

    }

    public void getEnglish(String url)
    {
        Log.d("test","MainActivity getEnglish(" + url + ")");
        try {
            Document doc = Jsoup.connect(url).get();
            txvHTMLResults.setText(doc.toString());
        } catch (IOException iox) {

        }
    }
}

