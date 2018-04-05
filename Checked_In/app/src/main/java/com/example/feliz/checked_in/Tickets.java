package com.example.feliz.checked_in;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by Feliz on 2017/08/14.
 */

public class Tickets extends AppCompatActivity
{
    ImageView image;
    String text2Qr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket);
        image = (ImageView) findViewById(R.id.image);
     //   text2Qr = text.getText().toString().trim();
        text2Qr ="Event Id:1 Event Name:Thunder storm ";
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try

        {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,100,100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

    }
}
