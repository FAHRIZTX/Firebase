package me.fahriztx.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.btnLogin) Button btnLogin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(MainActivity.this, new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                Log.d("TOKEN", task.getResult().getToken());
            }
        });
    }

    @OnClick(R.id.btnLogin)
    public void onBtnLoginClick(View v){
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher match = VALID_EMAIL_REGEX.matcher(Email);


        if(TextUtils.isEmpty(Email)){
            email.setError("Email tidak boleh kosong!");
            email.requestFocus();
        }else if(!match.find()) {
            email.setError("Format email salah!");
            email.requestFocus();
        }else if(TextUtils.isEmpty(Password)){
            password.setError("Password tidak boleh kosong!");
            password.requestFocus();
        }else{
            auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();

                        if(user != null){
                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
