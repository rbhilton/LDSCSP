package edu.weber.rhilton.cs3270.ldscsp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class TalkViewFragment extends Fragment {

    View rootView;
    Button btnGetHTML;
    TextView txvSpanish, txvEnglish;
    EditText edtTalkURL;
    ScrollView scrSpanish, scrEnglish;
    RadioButton rbtSpan, rbtPort;
    String language = "spa";
    String url = "";
    double  scrollMaxEng, scrollMaxSpa;
    double scrollRatio = 0.0;

    public TalkViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_talk_view, container, false);

        btnGetHTML = (Button) rootView.findViewById(R.id.btnGetHTML);
        scrSpanish = (ScrollView) rootView.findViewById(R.id.scrSpanish);
        scrEnglish = (ScrollView) rootView.findViewById(R.id.scrEnglish);
        txvEnglish = (TextView) rootView.findViewById(R.id.txvEnglish);
        txvSpanish = (TextView) rootView.findViewById(R.id.txvSpanish);
        edtTalkURL = (EditText) rootView.findViewById(R.id.edtTalkURL);
        rbtSpan = (RadioButton) rootView.findViewById(R.id.rbtSpan);
        rbtPort = (RadioButton) rootView.findViewById(R.id.rbtPort);

        edtTalkURL.setText("https://www.lds.org/general-conference/2017/04/our-good-shepherd?lang=eng");

        rbtSpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                MainActivity ma = (MainActivity) getActivity();
                if (rbtSpan.isChecked()) ma.setLang("spa");
                if (rbtPort.isChecked()) ma.setLang("por");
            }
        });

        btnGetHTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                loadTalkText();
            }
        });

        scrSpanish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.d("test","MainActivity onTouchEvent() " + motionEvent.toString());
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    double curScrollPos = (double) scrSpanish.getScrollY();
                    int newScrollPos = (int) Math.round((curScrollPos * scrollRatio));
                    scrEnglish.setScrollY(newScrollPos);
                }
                return false;
            }
        });

        scrEnglish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.d("test","MainActivity onTouchEvent() " + motionEvent.toString());
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    double curScrollPos = (double) scrEnglish.getScrollY();
                    int newScrollPos = (int) Math.round((curScrollPos * (1.0 / scrollRatio)));
                    scrSpanish.setScrollY(newScrollPos);
                }
                return false;
            }
        });

        edtTalkURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnGetHTML.setEnabled(false);
                String urlFront = edtTalkURL.getText().toString();
                if (urlFront.length() > 13) {
                    if (urlFront.substring(0,13).equals("/general-conf")) {
                        btnGetHTML.setEnabled(true);
                    }
                }
            }
        });


        return rootView;
    }

    private void loadTalkText() {
        if (rbtSpan.isChecked()) {language = "spa";} else {language = "por";}
        url = edtTalkURL.getText().toString();
        url = "https://www.lds.org/" + url;
        Log.d("test","url before=" + language);
        if (url.contains("?lang")) url = url.substring(0,url.indexOf("?lang="));
        Log.d("test","url after=" + language);
        new getConfTalk().execute(url + "?lang=" +  language);

        //new getWebPage().execute("");
        //getEnglish("https://www.lds.org/general-conference/2017/04/gathering-the-family-of-god?lang=eng");
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity ma = (MainActivity) getActivity();
        if (ma.getLang().equals("spa")) {
            rbtSpan.setChecked(true);
        } else {
            rbtPort.setChecked(true);
        }
        Log.d("test","TV Fragment onResume set edtTalkURL to " + ma.getTalkURL());
        edtTalkURL.setText(ma.getTalkURL());
        loadTalkText();
    }

    private  class getConfTalk extends AsyncTask<String,Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String rawHTML = "";
            //Log.d("test","MainActivity getConfTalk(" + strings[0] + ")");
            try {
                Document doc = Jsoup.connect(strings[0]).get();
                //Log.d("test","getConfTalk doInBackground try: " + doc.html().length());
                Element talk = doc.select("div.body-block").first();
                HtmlToPlainText worker = new HtmlToPlainText();
                rawHTML = worker.getPlainText(talk);
            } catch (IOException iox) {
                Log.e("test","getConfTalk doInBackground() IOException: " + iox.getMessage());

            }

            return rawHTML;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("test","onPostExecute: language=" + language);
            if (language.equals("eng")) {
                s = removeLinks(s);
                txvEnglish.setText(s);
                scrollMaxEng = (double) s.length();
                //Log.d("test","English Length=" + scrollMaxEng);
                scrollRatio = scrollMaxEng / scrollMaxSpa;
                //Log.d("test","ratio=" + scrollRatio);
            }

            if (!language.equals("eng")) {
                s = removeLinks(s);
                txvSpanish.setText(s);
                scrollMaxSpa = (double) s.length();
                //Log.d("test","Spanish Length=" + scrollMaxSpa);
                language = "eng";
                new getConfTalk().execute(url + "?lang=" +  language);
            }

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
                        //Log.d("test","Status: " + status + "   Raw Json String Length = " + rawJson.length());
                        //Log.d("test","Return: " + rawJson);
                }

            } catch (MalformedURLException e) {
                Log.d("test",e.getMessage());
            }
            catch (IOException iox) {
                Log.d("test","IOException: " + iox.getMessage());
            }
            return rawJson;
        }

        @Override
        protected void onPostExecute(String result) {
            // Executed when the AsyncTask completes
            super.onPostExecute(result);

            txvEnglish.setText(result);

        }

    }

    public void getEnglish(String url)
    {
        Log.d("test","MainActivity getEnglish(" + url + ")");
        try {
            Document doc = Jsoup.connect(url).get();
            txvEnglish.setText(doc.toString());
        } catch (IOException iox) {
            Log.e("test","getEnglish() IOException: " + iox.getMessage());
        }
    }

    private String removeLinks(String t) {
            //Log.d("test","###START TEXT###" + t + "###END TEXT###");
            Log.d("test","Simple search: " + t.indexOf("<https:"));
            Log.d("test","found link: " + t.matches("[^<]*<[^>]*>.*"));
            t = t.replaceAll("<[^>]*>","");
            return t;
    }

}
