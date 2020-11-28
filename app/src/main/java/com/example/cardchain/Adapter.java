package com.example.cardchain;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.util.List;

import MainFragments.FragmentSlide;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class Adapter extends PagerAdapter {
    private  MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    ImageBlur imageBlur;
    FragmentSlide parent;
    public Adapter(List<Model> models, Context context, FragmentSlide parent){
        this.models = models;
        this.context = context;
        this.parent=parent;
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
        final ImageView barCodeImage = view.findViewById(R.id.barcodeImg);
        final Bitmap bitmap;
        final Button shareCardBtn = view.findViewById(R.id.share_card_slide);
        barCodeImage.setVisibility(View.INVISIBLE);
        final TextView cardNameTxt = view.findViewById(R.id.CardName);
        final TextView cardOwnerName = view.findViewById(R.id.card_hold_name);
        final TextView cardNumTxt = view.findViewById((R.id.slide_card_num));
        final TextView cardHoldTitle = view.findViewById(R.id.card_hold_title);
        final Button deleteCard = view.findViewById(R.id.delete_card_slide);
        deleteCard.setVisibility(View.INVISIBLE);
        shareCardBtn.setVisibility(View.INVISIBLE);
        final Model curModel=models.get(position);
        imageButton.setImageResource(curModel.getImage());
        imageBlur = new ImageBlur(imageButton.getContext());
        imageBlur.makeBlur(imageButton);
        cardNameTxt.setText(curModel.getCardname());
        cardNumTxt.setText(curModel.getCardnumber());
        cardOwnerName.setText(curModel.getCardHoldName());
        BitMatrix bitMatrix = null;
        try {

            bitMatrix = multiFormatWriter.encode(curModel.getBarcode(), BarcodeFormat.valueOf(curModel.getBarcodeType()), 1000, 400);


        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Make delete button Visible
                deleteCard.setVisibility(View.VISIBLE);
                shareCardBtn.setVisibility(View.VISIBLE);
                //Make all other view invisible
                barCodeImage.setVisibility(View.INVISIBLE);
                cardHoldTitle.setVisibility(View.INVISIBLE);
                cardNumTxt.setVisibility(View.INVISIBLE);
                cardNameTxt.setVisibility(View.INVISIBLE);
                cardOwnerName.setVisibility(View.INVISIBLE);
                cardHoldTitle.setVisibility(View.INVISIBLE);
                Log.i("Adapter","Long Click");

                deleteCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Adapter","Delete Button clicked");
                        parent.deleteCard(curModel.getCardnumber(),curModel.getCardname(),position);

                    }
                });
                shareCardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parent.shareCard(bitmap);
                    }
                });
                return true;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteCard.setVisibility(View.INVISIBLE);
                shareCardBtn.setVisibility(View.INVISIBLE);
                if (!curModel.isShowingCode()) {
                    curModel.toggle();



                        barCodeImage.setVisibility(View.VISIBLE);
                        barCodeImage.setImageBitmap(bitmap);
//                        imageButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                        cardNumTxt.setVisibility(View.INVISIBLE);
                        cardNameTxt.setVisibility(View.INVISIBLE);
                        cardOwnerName.setVisibility(View.INVISIBLE);
                        cardHoldTitle.setVisibility(View.INVISIBLE);

                }else{

                    curModel.toggle();
//                    imageButton.setImageResource(curModel.getImage());
//                    imageBlur = new ImageBlur(imageButton.getContext());
//                    imageBlur.makeBlur(imageButton);
//                    imageButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
                    barCodeImage.setVisibility(View.INVISIBLE);
                    cardNumTxt.setVisibility(View.VISIBLE);
                    cardNameTxt.setVisibility(View.VISIBLE);
                    cardOwnerName.setVisibility(View.VISIBLE);
                    cardHoldTitle.setVisibility(View.VISIBLE);
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
    @Override public int getItemPosition(Object itemIdentifier) {
        int index = models.indexOf(itemIdentifier);
        return index == -1 ? POSITION_NONE : index;
    }
}
