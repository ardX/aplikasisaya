package com.example.aplikasisaya;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListGempaFragment extends Fragment {
    ArrayList<MyListData> myListData = new ArrayList<>();
    MyListAdapter adapter;
    RecyclerView recyclerView;

    public ListGempaFragment() {
        // Required empty public constructor
    }

    public static ListGempaFragment newInstance(String param1, String param2) {
        ListGempaFragment fragment = new ListGempaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_gempa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);

        new GetGempas().execute();
    }

    private class GetGempas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            myListData = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://data.bmkg.go.id/DataMKG/TEWS/gempadirasakan.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Donlot json", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject Infogempa = jsonObj.getJSONObject("Infogempa");
                    JSONArray gempas = Infogempa.getJSONArray("gempa");

                    // looping through All Contacts
                    for (int i = 0; i < gempas.length(); i++) {
                        JSONObject g = gempas.getJSONObject(i);

                        String tanggal = g.getString("Tanggal");
                        String jam = g.getString("Jam");
                        String lintang = g.getString("Lintang");
                        String bujur = g.getString("Bujur");
                        String magnitude = g.getString("Magnitude");
                        String kedalaman = g.getString("Kedalaman");
                        String wilayah = g.getString("Wilayah");
                        String dirasakan = g.getString("Dirasakan");

                        MyListData gempa = new MyListData(tanggal, jam, lintang, bujur, magnitude, kedalaman, wilayah, dirasakan);

                        myListData.add(gempa);

                        Log.e("Donlot json", "Json total : " + myListData.size());
                    }
                } catch (final JSONException e) {
                    Log.e("Donlot json", "Json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }

            } else {
                Log.e("Donlot json", "Couldn't get json from server.");
                Toast.makeText(getContext(),
                        "Couldn't get json from server. Check LogCat for possible errors!",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new MyListAdapter(myListData);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(adapter);
        }
    }
}