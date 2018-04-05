package com.example.feliz.checked_in;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Feliz on 2017/10/04.
 */

public class Notification extends AppCompatActivity
{
    String TAG = Notification.class.getSimpleName();
    public ArrayList<HashMap<String, String>> eList;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    JSONArray arr;
    String tile;
    String Clubname;
    String Eventname;
    String Price;
    ImageView image;
    String text2Qr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        listView = (ListView)findViewById(R.id.Msg_list);
        image = (ImageView) findViewById(R.id.image);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(Notification.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        final  ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("arraylist");
        arr = new JSONArray(object);

        for(int i =0;i<arr.length();i++){
            try {
                tile = getIntent().getExtras().getString("title");

                if(arr.getJSONObject(i).getString("eventname").equals(tile))
                {
                    Clubname =arr.getJSONObject(i).getString("eventclub").toString();
                    Eventname = arr.getJSONObject(i).getString("eventname").toString();
                    Price = arr.getJSONObject(i).getString("Ticketprice").toString();

                    text2Qr =Eventname;
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try

                    {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,150,150);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                    }
                    catch (WriterException e){
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //eList = new ArrayList<>(); //inst
        }
        Message();
    }

    public void Message()
    {
        String result ="You have successfully purchased a ticket to "+ Eventname +" Cost " +"R " +Price;
        list.add(result);
        adapter.notifyDataSetChanged();
    }
}
