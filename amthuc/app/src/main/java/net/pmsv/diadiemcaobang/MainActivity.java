package net.pmsv.diadiemcaobang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import net.pmsv.diadiemcaobang.DAL.SQLiteDataAccessHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SQLiteDataAccessHelper SQLiteDataAccessHelper;

    public Button btnDangNhap, btnDangKy, btnBoQua;
    LoginButton btnFbLogin;
    SignInButton btnloginGG;
    public Toolbar myToolbar;
    private CallbackManager callbackManager;
    Boolean isInEditMode = true;
    String id, name, email, gender, birthday, profilePicture;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        btnDangKy = (Button) findViewById(R.id.btDangKy);
        btnDangNhap = (Button) findViewById(R.id.btDangNhap);
        btnBoQua = (Button) findViewById(R.id.btBoQua);
        myToolbar = (Toolbar) findViewById(R.id.toolbarChiTiet);
        btnFbLogin = (LoginButton) findViewById(R.id.btnLoginFB);
        btnloginGG = (SignInButton) findViewById(R.id.btnloginGG);

        btnFbLogin.setLoginBehavior(LoginBehavior.SUPPRESS_SSO);
        btnFbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        setSupportActionBar(myToolbar);
        getEvent();

    }

    private void getEvent() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Truy cap thanh cong", Toast.LENGTH_SHORT).show();
                Intent itHome = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(itHome);
                finish();
            }
        });


        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Đăng nhập offline hiện chưa hoạt động", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(MainActivity.this, addtheme.class);
                startActivity(home);
                finish();
            }
        });


        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

//                Toast.makeText(getApplicationContext(), "dang nhap thanh cong" + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    id = object.getString("id");
                                    name = object.getString("name");
                                    email = object.getString("email");
//                                    Toast.makeText(getApplicationContext(),id + " -" +  name + " - " + email + " - " + gender + " - " + birthday + " - " + profilePicture, Toast.LENGTH_SHORT).show();
                                    saveDataLogin(id, name, email, "https://graph.facebook.com/" + id + "/picture?type=large", "fb");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }


            @Override
            public void onCancel() {
//                Toast.makeText(getApplicationContext(), "da cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
//                Toast.makeText(getApplicationContext(), "bi loi", Toast.LENGTH_SHORT).show();
            }
        });

        btnloginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGG();
            }
        });
    }

    public void saveDataLogin(String id, String name, String email, String profilePicture, String type)
    {
        SharedPreferences sharedPref = getSharedPreferences("my_data", MODE_PRIVATE);
        SharedPreferences.Editor edtUserProfile = sharedPref.edit();
        edtUserProfile.putString("id", id);
        edtUserProfile.putString("name", name);
        edtUserProfile.putString("email", email);
        edtUserProfile.putString("profilePicture", profilePicture);
        edtUserProfile.putString("typelogin", type);
        edtUserProfile.commit();
    }

//    login GG
    private void signInGG() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutGG(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(), "Vao logout   " + status, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

//        get du lieu user gg
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            saveDataLogin(acct.getId(), acct.getDisplayName(), acct.getEmail(), acct.getPhotoUrl().toString(), "gg");
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

//        else {
//            // Signed out, show unauthenticated UI.
//        }
    }

//  onConnectionFailed of gg
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Login GG Failed", Toast.LENGTH_SHORT).show();
    }

}
