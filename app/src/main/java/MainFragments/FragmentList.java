package MainFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cardchain.CardListAdapter;
import com.example.cardchain.ListCardModel;
import com.example.cardchain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class FragmentList extends Fragment {
    String TAG = "FragmentList";
    FirebaseAuth auth;
    FirebaseUser user;
    ListenerRegistration cardListener;
    FirebaseFirestore db;
    ArrayList<Integer> imageIDs;
    ArrayList<ListCardModel> cardModels;
    ListCardModel cardModel;
    CardListAdapter cardListAdapter;
    ListView cardListView;
    ProgressBar progList;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        view =inflater.inflate((R.layout.fragment_list),container,false);

        //Store all the images
        imageIDs = new ArrayList<>();
        cardModels = new ArrayList<>();
        imageIDs.add(R.drawable.pattern1);
        imageIDs.add(R.drawable.pattern2);
        imageIDs.add(R.drawable.pattern3);


        cardListView = view.findViewById(R.id.cards_list);
        progList = view.findViewById(R.id.progress_list);
        progList.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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
                        cardModels.add(cardModel);
                        cardListAdapter = new CardListAdapter(view.getContext(), cardModels);
                        cardListView.setAdapter(cardListAdapter);
                    }
                }
                progList.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        progList.setVisibility(View.INVISIBLE);
        if(cardListener != null){
            cardListener.remove();
        }
    }
}
