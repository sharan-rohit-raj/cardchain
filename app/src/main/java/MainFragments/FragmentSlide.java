package MainFragments;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.example.cardchain.ListCardModel;
import com.example.cardchain.Model;
import com.example.cardchain.R;
import com.example.cardchain.ViewPagerZoomAnim;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
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
    ProgressBar progList;
    ArrayList<Integer> imageIDs;
    Model model;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
         view =  inflater.inflate((R.layout.fragment_slide),container,false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        imageIDs = new ArrayList<>();
        models=new ArrayList<>();
        imageIDs.add(R.drawable.pattern1);
        imageIDs.add(R.drawable.pattern2);
        imageIDs.add(R.drawable.pattern3);
        progList = view.findViewById(R.id.progress_list);
        progList.setVisibility(View.VISIBLE);


        displayName = view.findViewById(R.id.display_name);

        if(user.getDisplayName() != null){
            displayName.setText(user.getDisplayName());
        }

        viewPager = view.findViewById(R.id.view_pager);

        viewPager.setPadding(130, 0,130,0);
        viewPager.setPageMargin(dpToPx(20));
        viewPager.setPageTransformer(true, new ViewPagerZoomAnim());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(view.getContext(), "Page: "+ (position+1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onStart() {
        super.onStart();
        if(user.getDisplayName() != null){
            displayName.setText(user.getDisplayName());
        }
        cardListener = db.collection("users").document(user.getUid()).collection("cards").whereEqualTo("tag","all").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    progList.setVisibility(View.INVISIBLE);
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                models=new ArrayList<>();
                progList.setVisibility(View.VISIBLE);
                Log.d(TAG, "Number of Cards: "+ value.size());
                for(QueryDocumentSnapshot doc: value){
                    Random rand = new Random();
                    int randomImage = imageIDs.get(rand.nextInt(imageIDs.size()));

                    model = new Model(randomImage,doc.get("cardholder").toString(), doc.get("cardname").toString(), doc.get("cardnumber").toString(), doc.get("Data").toString(), doc.get("BarcodeType").toString());
                    boolean duplicate = false;
                    for(Model a_model : models){
                        if(a_model.getCardname() == model.getCardname() && a_model.getCardnumber() == model.getCardnumber()){
                            duplicate = true;
                        }
                    }
                    if(duplicate == false){
                        models.add(model);

                    }
                }
                adapter = new Adapter(models, view.getContext(),FragmentSlide.this);
                viewPager.setAdapter(adapter);
                progList.setVisibility(View.INVISIBLE);

            }
        });
    }
    public boolean deleteCard(String cardNum, final String cardName, final int position){
        Log.i("FragmentSlide","Delete Card");
        Query docu = db.collection("users").document(user.getUid()).collection("cards").whereEqualTo("cardnumber",cardNum).whereEqualTo("cardname",cardName);
                docu.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("FragmentSlide","Deleting Card: "+cardName);
                                models.remove(position);
                                adapter.notifyDataSetChanged();
                                document.getReference().delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return true;
    }
}
