package com.example.cardchain;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class Adapter extends PagerAdapter {
    private  MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Model> models, Context context){
        this.models = models;
        this.context = context;

    }
    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.item, container, false);
        final ImageButton imageButton = view.findViewById(R.id.card_img);
        imageButton.setImageResource(models.get(position).getImage());

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!models.get(position).isShowingCode()) {
                    models.get(position).toggle();

                    BitMatrix bitMatrix = null;
                    try {
                        //TODO Retrieve barcode images and assign a barcode to each model so that this can be models.get(position).barcode
                        bitMatrix = multiFormatWriter.encode("6046463501982188", BarcodeFormat.valueOf("CODE_128"), 1000, 400);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageButton.setImageBitmap(bitmap);
                        LayoutParams layout=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                        layout.addRule(RelativeLayout.CENTER_IN_PARENT);
                        imageButton.setLayoutParams(layout);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }else{

                    models.get(position).toggle();
                    imageButton.setImageResource(models.get(position).getImage());
                    imageButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
