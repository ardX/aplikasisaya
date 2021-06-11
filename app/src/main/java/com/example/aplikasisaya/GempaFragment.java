package com.example.aplikasisaya;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GempaFragment extends Fragment {

    private ProgressDialog progDailog;
    private Bitmap map;
    private String tanggal;
    private String jam;
    private String lintang;
    private String bujur;
    private String magnitude;
    private String kedalaman;
    private String wilayah;
    private String potensi;
    private GetEarthquake gTask;
    private View root;

    public GempaFragment() {
        // Required empty public constructor
    }

    public static GempaFragment newInstance(String param1, String param2) {
        GempaFragment fragment = new GempaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gempa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        root = view;
        gTask = new GetEarthquake();
        gTask.execute();

        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gTask = new GetEarthquake();
                gTask.execute();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void setAll() {
        ((ImageView) root.findViewById(R.id.image)).setImageBitmap(map);
        ((TextView) root.findViewById(R.id.waktu)).setText(tanggal + ", " + jam);
        ((TextView) root.findViewById(R.id.magnitude)).setText(magnitude);
        ((TextView) root.findViewById(R.id.kedalaman)).setText(kedalaman);
        ((TextView) root.findViewById(R.id.lokasi)).setText(lintang + "\n" + bujur);
        ((TextView) root.findViewById(R.id.wilayah)).setText(wilayah);
        ((TextView) root.findViewById(R.id.tsunami)).setText(potensi);
    }

    public class GetEarthquake extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(getActivity());
            progDailog.setMessage("Memuat...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.setCanceledOnTouchOutside(false);
            progDailog.show();
        }

        @Override
        protected Integer doInBackground(Void... poid) {
            int status = 0;
            try {
                URL url = new URL("https://data.bmkg.go.id/DataMKG/TEWS/autogempa.xml");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                InputStream is = url.openConnection().getInputStream();
                xpp.setInput(is, "UTF_8");
                boolean insideItem = false;
                String urlgambar = "https://data.bmkg.go.id/DataMKG/TEWS/";

                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("gempa")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("Tanggal")) {
                            if (insideItem)
                                tanggal = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Jam")) {
                            if (insideItem)
                                jam = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Lintang")) {
                            if (insideItem)
                                lintang = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Bujur")) {
                            if (insideItem)
                                bujur = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Magnitude")) {
                            if (insideItem)
                                magnitude = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Kedalaman")) {
                            if (insideItem)
                                kedalaman = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Wilayah")) {
                            if (insideItem)
                                wilayah = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Potensi")) {
                            if (insideItem)
                                potensi = xpp.nextText();
                        } else if (xpp.getName().equalsIgnoreCase("Shakemap")) {
                            if (insideItem)
                                urlgambar += xpp.nextText();
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("gempa")) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
                InputStream in = new java.net.URL(urlgambar).openStream();
                map = BitmapFactory.decodeStream(in);
                status = 1;
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(Integer success) {
            super.onPostExecute(success);
            progDailog.dismiss();
            setAll();
        }
    }
}