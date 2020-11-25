package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

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
//        deleteDocs();
//        deleteFields();
//        transaction();
//        batchWrited();
//        datasetCreated();
//        queriesExpt();
//        limit();
//        realTime();
        cacheChanges();

    }

    private void cacheChanges() {
        ff.collection("cities")
                .whereEqualTo("state","CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(String.valueOf(MainActivity.this), "listen:error", error);
                            return;
                        }
                        for(DocumentChange dc:value.getDocumentChanges())
                        {
                            switch (dc.getType())
                            {
                                case ADDED:
                                    Toast.makeText(MainActivity.this, "Added"+dc.getDocument().getData(), Toast.LENGTH_SHORT).show();
                                case REMOVED:
                                    Toast.makeText(MainActivity.this, "Removed"+dc.getDocument().getData(), Toast.LENGTH_SHORT).show();
                                case MODIFIED:
                                    Toast.makeText(MainActivity.this, "modified"+dc.getDocument().getData(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void realTime() {
        DocumentReference dr=ff.collection("cities").document("BJ");
        dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                    Log.d(String.valueOf(MainActivity.this), "error");

                //callback from server
//                if (value!=null&&value.exists())
//                    Log.d(String.valueOf(MainActivity.this), "present"+value.getData());

                //local changes
                String source=value!=null&&value.getMetadata().hasPendingWrites()
                        ?"local":"server";
                if(value!=null&&value.exists())
                    Log.d(String.valueOf(MainActivity.this),"values-------"+value.getData());

            }
        });
    }

    private void limit() {
        CollectionReference collectionReference=ff.collection("cities");

        collectionReference.orderBy("name").limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                            for (QueryDocumentSnapshot a:task.getResult())
                                Log.d(String.valueOf(MainActivity.this),a.getId()+"-----"+a.getData());
                    }
                });
    }

    private void queriesExpt() {
        CollectionReference cf=ff.collection("cities");

        cf.whereEqualTo("capital",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                Toast.makeText(MainActivity.this, queryDocumentSnapshot.getId()+"----"+queryDocumentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void datasetCreated() {
        CollectionReference cities = ff.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "San Francisco");
        data1.put("state", "CA");
        data1.put("country", "USA");
        data1.put("capital", false);
        data1.put("population", 860000);
        data1.put("regions", Arrays.asList("west_coast", "norcal"));
        cities.document("SF").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Los Angeles");
        data2.put("state", "CA");
        data2.put("country", "USA");
        data2.put("capital", false);
        data2.put("population", 3900000);
        data2.put("regions", Arrays.asList("west_coast", "socal"));
        cities.document("LA").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Washington D.C.");
        data3.put("state", null);
        data3.put("country", "USA");
        data3.put("capital", true);
        data3.put("population", 680000);
        data3.put("regions", Arrays.asList("east_coast"));
        cities.document("DC").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Tokyo");
        data4.put("state", null);
        data4.put("country", "Japan");
        data4.put("capital", true);
        data4.put("population", 9000000);
        data4.put("regions", Arrays.asList("kanto", "honshu"));
        cities.document("TOK").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Beijing");
        data5.put("state", null);
        data5.put("country", "China");
        data5.put("capital", true);
        data5.put("population", 21500000);
        data5.put("regions", Arrays.asList("jingjinji", "hebei"));
        cities.document("BJ").set(data5);



    }

    private void batchWrited() {
        WriteBatch wb=ff.batch();
        //adding new famous place;
        DocumentReference dr=ff.collection("users").document("delhi");
        wb.set(dr,new Famous());

        //changing rating parameter in jaipur custom
        DocumentReference d1=ff.collection("users").document("custom");
        wb.update(d1,"rating",10);

        //commit
        wb.commit();
    }

    private void transaction() {
        final DocumentReference dref=ff.collection("users").document("custom");
        
        ff.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot ds=transaction.get(dref);

                double rating=ds.getDouble("rating")+1;
                transaction.update(dref,"rating",rating);
                return null;
            }
        });
    }

    private void deleteFields() {
        DocumentReference dd=ff.collection("users").document("vm");

        Map<String,Object> mp=new HashMap<>();

        mp.put("age",FieldValue.delete());

        dd.update(mp);
    }

    private void deleteDocs() {
        ff.collection("users").document("vm")
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
            }
        });
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