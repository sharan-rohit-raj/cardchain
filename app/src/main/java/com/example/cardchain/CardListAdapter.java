package com.example.cardchain;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class CardListAdapter extends BaseAdapter {
    private MultiFormatWriter multiFormatWriter= new MultiFormatWriter();

    private Context context;
    private ArrayList<ListCardModel> listCardModels;
    ImageBlur imageBlur;
    public CardListAdapter(Context context, ArrayList<ListCardModel> listCardModels) {
        this.context = context;
        this.listCardModels = listCardModels;
    }


    @Override
    public int getCount() {
        return listCardModels.size();
    }

    @Override
    public Object getItem(int i) {
        return listCardModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(context, R.layout.card_list, null);
        }
        final ImageView imageView = view.findViewById(R.id.card_list_img);
        final TextView cardNumber = view.findViewById(R.id.card_num_list);
        final TextView cardName = view.findViewById(R.id.card_name_list);
        final ListCardModel currModel = listCardModels.get(i);
        final TextView cardNameDesc = view.findViewById(R.id.card_name_desc);
        final TextView cardNumDesc = view.findViewById(R.id.card_num_desc);
        imageView.setImageResource(currModel.getImageID());
        imageBlur = new ImageBlur(imageView.getContext());
        imageBlur.makeBlur(imageView);
        cardName.setText(currModel.getCardname());
        cardNumber.setText(currModel.getCardnumber());
        BitMatrix bitMatrix=null;
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            bitMatrix = multiFormatWriter.encode(currModel.getBarcode(), BarcodeFormat.valueOf(currModel.getBarcodeType()), 150, 100);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        final Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currModel.isShowingCode()) {
                    currModel.toggleCard();
                    imageView.setImageBitmap(bitmap);
                    cardName.setVisibility(View.INVISIBLE);
                    cardNumber.setVisibility(View.INVISIBLE);
                    cardNameDesc.setVisibility(View.INVISIBLE);
                    cardNumDesc.setVisibility(View.INVISIBLE);
                }else{
                    currModel.toggleCard();
                    imageView.setImageResource(currModel.getImageID());
                    cardName.setVisibility(View.VISIBLE);
                    cardNumber.setVisibility(View.VISIBLE);
                    cardNameDesc.setVisibility(View.VISIBLE);
                    cardNumDesc.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }
}
