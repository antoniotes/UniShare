package it.antoniotes.unishare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.db.DBHelper;

public class SetPasswordActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name;
    EditText username, email, password;
    Button signOutBtn, registerBtn;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        db = new DBHelper(this);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password_field);
        email = findViewById(R.id.email);
        registerBtn = findViewById(R.id.registerbtn);
        getGoogleInfo();
        stepOver();
        //getFacebookInfo();
        setSignOutBtn();
        setRegisterBtn();
    }

    public void getGoogleInfo(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!= null){
//            user.setName(acct.getDisplayName());
//            user.setEmail(acct.getEmail());
            name.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            //alreadyRegistered();
        }
    }

    /*public void getFacebookInfo(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            name.setText(object.getString("name"));
                            //email.setText(object.getString("mail"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();

    }*/

    public void setSignOutBtn(){
        signOutBtn = findViewById(R.id.signout);
        signOutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                closeGoogle();
                signOut();
            }
        });
    }

    public void signOut(){
        startActivity(new Intent(new Intent(SetPasswordActivity.this, LoginActivity.class)));
    }

    public void closeGoogle(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                //startActivity(new Intent(new Intent(SetPasswordActivity.this, LoginActivity.class)));
            }
        });
    }

    public void setRegisterBtn(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String mail = email.getText().toString();

                if(user.equals("")||pass.equals("")||mail.equals("")){
                    Toast.makeText(getApplicationContext(), "Compila i campi", Toast.LENGTH_SHORT).show();

                } else {
                    Boolean checkUser = db.checkUsername(user);
                    if(checkUser == false) {
                        Boolean checkMail = db.checkMail(mail);
                        if (checkMail == false) {
                            Boolean insert = db.insertData(user, pass, mail);
                            if (insert == true) {
                                Toast.makeText(getApplicationContext(), "Registrazione Completata", Toast.LENGTH_SHORT).show();
                                closeGoogle();
                                UserSession session = UserSession.getInstance();
                                session.setLoggedInUsername(user);
                                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registrazione fallita", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                        Toast.makeText(getApplicationContext(), "Mail già presente nel Database, accedi", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), "Username già presente nel Database, scegli un altro Username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void mailExsist(String findedMail){
        Boolean ifExist = db.checkMail(findedMail);
        if(ifExist==true){
            stepOver();
        }
/*        String verifyMail = email.getText().toString();
        Boolean alreadyExist = db.checkMail(verifyMail);
        if(alreadyExist==true){
            stepOver();
        }*/
    }

    public void stepOver(){
        String findedMail = email.getText().toString();
        if(db.checkMail(findedMail)==true){
            closeGoogle();
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
        } else {
            Log.d("Antonio","Placeholder");
        }

    }
}