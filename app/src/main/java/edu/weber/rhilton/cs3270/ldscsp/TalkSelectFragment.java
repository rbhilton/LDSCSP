package edu.weber.rhilton.cs3270.ldscsp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class TalkSelectFragment extends Fragment {

    View rootView;
    ListView lsvTalkLinks;

    public TalkSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_talk_select, container, false);

        lsvTalkLinks = (ListView) rootView.findViewById(R.id.lsvTalkLinks);

        lsvTalkLinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity ma = (MainActivity) getActivity();
                ma.setTalk(adapterView.getItemAtPosition(i).toString());
            }
        });

        new getConfTalks().execute("https://www.lds.org/general-conference?lang=eng");

        return rootView;
    }

    private  class getConfTalks extends AsyncTask<String,Integer, Document> {

        @Override
        protected Document doInBackground(String... strings) {
            String rawHTML = "";
            Log.d("test","TalkSelectFragment getConfTalks(" + strings[0] + ")");
            Document doc = new Document("");
            try {
                doc = Jsoup.connect(strings[0]).get();
                Log.d("test","returned doc: " + doc.data().length());
                rawHTML = doc.outerHtml();
            } catch (IOException iox) {
                Log.d("test","getConfTalks doInBackground() IOException: " + iox.getMessage());

            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);
            Log.d("test","onPostExecute() html length: " + doc.outerHtml().length());
            Elements els = doc.select(" a.lumen-tile__link");
            ArrayList<String> links = new ArrayList<String>();
            int index = -1;
            for (Element e:els
                 ) {
                if (e != null) {

                    links.add(e.attr("href"));
                    //Log.d("test","lumen-tile__link: " + links.get(links.size()-1));
                }
            }
            Log.d("test","Number of links = " + links.size());
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,links);
            lsvTalkLinks.setAdapter(adapter);
        }
    }

}
