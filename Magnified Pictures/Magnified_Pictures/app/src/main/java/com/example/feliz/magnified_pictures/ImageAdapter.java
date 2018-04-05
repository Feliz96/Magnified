package com.example.feliz.magnified_pictures;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Feliz on 2017/11/26.
 */

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    public  int[] images=
    {
            R.drawable.pic1, R.drawable.pic2,
            R.drawable.pic3, R.drawable.pic4,
            R.drawable.pic5, R.drawable.pic6,
            R.drawable.pic7, R.drawable.pic8,
            R.drawable.pic9, R.drawable.pic10,
            R.drawable.pic11, R.drawable.pic12,
            R.drawable.pic13, R.drawable.pic14,
            R.drawable.pic15, R.drawable.pic16,
            R.drawable.pic17, R.drawable.pic18,
            R.drawable.pic19, R.drawable.pic20,
            R.drawable.pic21

    };

    public ImageAdapter(Context c)
    {
        context = c;
    }


    @Override
    public int getCount()
    {
        return images.length;
    }

    @Override
    public Object getItem(int i)
    {
        return images[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[i]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(240,240));
        return imageView;
    }
}
