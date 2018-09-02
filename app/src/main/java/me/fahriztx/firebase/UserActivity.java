package me.fahriztx.firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity {


    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.editMessage)
    EditText editText;
    @BindView(R.id.btnSend)
    Button btnSend;

    private List<String> stringList = new ArrayList<>();
    private DatabaseReference message;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if (user != null) {

            String uid = user.getUid();

            database = FirebaseDatabase.getInstance();

            DatabaseReference usersRef = database.getReference("users");
            DatabaseReference uidRef = usersRef.child(uid);
            message = uidRef.child("message");

            final ArrayAdapter adapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1, stringList);

            listView.setAdapter(adapter);

            message.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("LOG_DS", dataSnapshot.toString());
                    stringList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        stringList.add(ds.child("task").getValue().toString());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnSend)
    public void onBtnSendClick(View v) {
        String text = editText.getText().toString();
        message.push().child("task").setValue(text);
        editText.setText("");
    }

    @OnClick(R.id.btnSignOut)
    public void onBtnSignOutClick(View v) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
