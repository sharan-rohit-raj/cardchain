package com.example.cardchain;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CardListAdapter extends BaseAdapter {


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

        ImageView imageView = view.findViewById(R.id.card_list_img);
        TextView cardNumber = view.findViewById(R.id.card_num_list);
        TextView cardName = view.findViewById(R.id.card_name_list);
        ListCardModel currModel = listCardModels.get(i);
        imageView.setImageResource(currModel.getImageID());
        imageBlur = new ImageBlur(imageView.getContext());
        imageBlur.makeBlur(imageView);
        cardName.setText(currModel.getCardname());
        cardNumber.setText(currModel.getCardnumber());


        return view;
    }
}
