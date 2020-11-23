package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore ff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ff=FirebaseFirestore.getInstance();
//        addingDataToFireBase1();
//        addingDocument();
//        merging();
//        dataTypes();
//        customClass();
//        updating();
//        updateInArray();
//        increment();

//        getMultiDocs();
//        getdata();
//        getCustom();
        
    }

    private void getCustom() {
        DocumentReference dr=ff.collection("users").document("custom");
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Famous city=documentSnapshot.toObject(Famous.class);
                Toast.makeText(MainActivity.this, city.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getdata() {
        DocumentReference dr=ff.collection("users").document("vm");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists())
                        Toast.makeText(MainActivity.this, ds.getData()+"", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getMultiDocs() {
        ff.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                Toast.makeText(MainActivity.this, q.getId() + "   " + q.getData(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }

    private void increment() {
        DocumentReference dr=ff.collection("users").document("custom");

        dr.update("rating",FieldValue.increment(6));
    }

    private void updateInArray() {
        DocumentReference dr=ff.collection("users").document("DataTypes");

        //adding
        dr.update("array", FieldValue.arrayUnion(3));
        //removing
        dr.update("array", FieldValue.arrayRemove(3));

    }

    private void updating() {
        DocumentReference dr=ff.collection("users").document("vm");

        dr.update("first","dinesh");
    }

    private void customClass() {
        Famous city=new Famous("jaipur","Hawa mehal",5);

        ff.collection("users")
                .document("custom")
                .set(city);

    }

    private void dataTypes() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("stringExample", "Hello world!");
        docData.put("booleanExample", true);
        docData.put("numberExample", 3.14159265);
        docData.put("dateExample", new Timestamp(new Date()));
        docData.put("listExample", Arrays.asList(1, 2, 3));
        docData.put("nullExample", null);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("a", 5);
        nestedData.put("b", true);

        docData.put("Objects",nestedData);

        ff.collection("users").document("DataTypes")
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void merging() {

        //data merging
        Map<String,Object> map=new HashMap<>();
        map.put("first","vijay");
        map.put("middle","singh");
        map.put("last","rathore");
        map.put("scsdc","scsdc");
        map.put("age",20);

        ff.collection("users").document("vm")
                .set(map, SetOptions.merge());
    }

    private void addingDocument() {
        //adding doc to existing collection with different titles

        Map<String,Object> map=new HashMap<>();
        map.put("first","vijaydsacdac");
        map.put("middle","singh");
        map.put("last","mishra");
        map.put("age",20);

        ff.collection("users")
                .document("vm")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addingDataToFireBase1() {
        //dataSet
        Map<String,String> map=new HashMap<>();
        map.put("name","Raman");
        map.put("city","jaipur");
        map.put("age","20");

        //adding
        ff.collection("users")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}