package MainFragments;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cardchain.Adapter;
import com.example.cardchain.CardListAdapter;
import com.example.cardchain.ListCardModel;
import com.example.cardchain.Model;
import com.example.cardchain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentSlide extends Fragment {
    String TAG = "FragmentSlide";
    ViewPager viewPager;
    Adapter adapter;
    ArrayList<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView displayName;
    ListenerRegistration cardListener;
    ArrayList<ListCardModel> cardModels;
    ListCardModel cardModel;
    ProgressBar progList;
    ArrayList<Integer> imageIDs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view =  inflater.inflate((R.layout.fragment_slide),container,false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        imageIDs = new ArrayList<>();
        cardModels = new ArrayList<>();

        progList = view.findViewById(R.id.progress_list);
        progList.setVisibility(View.VISIBLE);
        cardListener = db.collection("users").document(user.getUid()).collection("cards").whereEqualTo("tag","all").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    progList.setVisibility(View.INVISIBLE);
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                progList.setVisibility(View.VISIBLE);
                Log.d(TAG, "Number of Cards: "+ value.size());
                for(QueryDocumentSnapshot doc: value){
                    Random rand = new Random();
                    int randomImage = imageIDs.get(rand.nextInt(imageIDs.size()));

                    cardModel = new ListCardModel(doc.get("cardnumber").toString(), doc.get("cardname").toString(), randomImage);
                    boolean duplicate = false;
                    for(ListCardModel a_model : cardModels){
                        if(a_model.getCardname() == cardModel.getCardname() && a_model.getCardnumber() == cardModel.getCardnumber()){
                            duplicate = true;
                        }
                    }
                    if(duplicate == false){
                        ListView cardListView;
                        CardListAdapter cardListAdapter;
                        cardListView = view.findViewById(R.id.cards_list);
                        cardModels.add(cardModel);
                        cardListAdapter = new CardListAdapter(view.getContext(), cardModels);
                        cardListView.setAdapter(cardListAdapter);
                    }
                }
                progList.setVisibility(View.INVISIBLE);

            }
        });
        models = new ArrayList<>();
        models.add(new Model(R.drawable.pattern1));
        models.add(new Model(R.drawable.pattern2));
        models.add(new Model(R.drawable.pattern3));

        displayName = view.findViewById(R.id.display_name);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user.getDisplayName() != null){
            displayName.setText(user.getDisplayName());
        }
        adapter = new Adapter(models, view.getContext());

        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0,130,0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(view.getContext(), "Page: "+ (position+1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(user.getDisplayName() != null){
            displayName.setText(user.getDisplayName());
        }
    }
}
