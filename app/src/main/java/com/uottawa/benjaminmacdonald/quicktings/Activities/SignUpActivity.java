package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawa.benjaminmacdonald.quicktings.Classes.User;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 9001;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        Button emailSignUp = (Button) findViewById(R.id.emailSignUp);
        emailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);

                signUpWithEmail(email.getText().toString(), password.getText().toString());
            }
        });


        // --------------------------- GOOGLE ---------------------------------
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button realGoogleSign = (Button) findViewById(R.id.userGoogleSignUp);
        realGoogleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        SignInButton googleButton = (SignInButton) findViewById(R.id.googleSignUp);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        //------------------------------- FACEBOOK -------------------------------

        // Initialize Facebook Login button

        Button realFacebookSign = (Button) findViewById(R.id.userFacebookSignUp);
        realFacebookSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton loginButton = (LoginButton) findViewById(R.id.facebookSignUp);
                loginButton.callOnClick();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebookSignUp);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately

            }
        }
        //its facebook
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        return;
    }

    private void signUpWithEmail(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        EditText name = (EditText) findViewById(R.id.firstName);
                        EditText lastName = (EditText) findViewById(R.id.lastName);

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        User user = new User(email, name.getText().toString(), lastName.getText().toString(), "");

                        //connect to database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference();

                        String id = currentUser.getUid();
                        ref.child("users").child(id).setValue(user);

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Email authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        User user = new User(account.getEmail(), account.getGivenName(), account.getFamilyName(), account.getPhotoUrl().toString());

                        //connect to database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference();

                        String id = currentUser.getUid();
                        ref.child("users").child(id).setValue(user);

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Firebase authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        final FirebaseUser currentUser = mAuth.getCurrentUser();

                        GraphRequest request = GraphRequest.newMeRequest(
                                token,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {

                                        System.out.println(object);

                                        try {
                                            User user = new User(object.getString("email"),
                                                    object.getString("first_name"), object.getString("last_name"), object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                            //connect to database
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference ref = database.getReference();

                                            String id = currentUser.getUid();
                                            ref.child("users").child(id).setValue(user);

                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);

                                        } catch (JSONException e) {
                                            //failed
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email,first_name,last_name,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Facebook authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
