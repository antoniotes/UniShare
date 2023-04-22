package it.antoniotes.unishare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import it.antoniotes.unishare.data.User;
import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.db.DBHelper;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CallbackManager callbackManager;
    ImageView googleBtn, facebookBtn, register;
    EditText username, password;
    Button loginBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setGoogleSignInBtn();
        db = new DBHelper(this);
        //setFacebookBtn();
        setRegisterBtn();
        callbackManager = CallbackManager.Factory.create();
        setLoginBtn();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


    }

    /*public void setFacebookBtn(){

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        navigateToSecondActivity();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), "Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                    }
                });
        facebookBtn = findViewById(R.id.facebook_btn);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });
    }*/

    public void setGoogleSignInBtn(){
        googleBtn = findViewById(R.id.google_btn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                getGoogleInfo();
                navigateToSecondActivity();
            } catch (ApiException e) {
                //e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void navigateToSecondActivity(){
        Intent i = new Intent(LoginActivity.this, SetPasswordActivity.class);
        startActivity(i);
    }

    public void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    public void setLoginBtn(){
        loginBtn = findViewById(R.id.loginbtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });
    }
    public void verifyData(){
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(user.equals("")||pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Inserisci Username e Password", Toast.LENGTH_SHORT).show();
        } else {
            Boolean checkUserPass = db.checkUsernamePassword(user, pass);
            if(checkUserPass== true){
                Toast.makeText(getApplicationContext(), "Utente Trovato!!!", Toast.LENGTH_SHORT).show();
                UserSession session = UserSession.getInstance();
                session.setLoggedInUsername(user);
                //Log.d("antonio", "Username" + session.getLoggedInUsername());
                //--------------ho commentato questa chiamata a db perché secondo me crasha
                // session.setLoggedID(db.getID(user, pass));
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
            } else{
                Toast.makeText(getApplicationContext(), "Utente non trovato", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void getGoogleInfo(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!= null){
            String gmail = acct.getEmail();
            UserSession session = UserSession.getInstance();
            session.setLoggedInUsername(db.getUsernameFromEmail(gmail));
        }
    }

    public void createSessionForGoogleClient(){

    }

    public void setRegisterBtn(){
        register = findViewById(R.id.newuser_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSecondActivity();
            }
        });
    }
}


