package net.pmsv.diadiemcaobang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import net.pmsv.diadiemcaobang.Adapter.AdapterDiaDiem;
import net.pmsv.diadiemcaobang.BLL.DiaDiemBLL;
import net.pmsv.diadiemcaobang.DAL.SQLiteDataAccessHelper;
import net.pmsv.diadiemcaobang.DTO.DiaDiemDTO;
import net.pmsv.diadiemcaobang.Utility.RecyclerItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity  implements TextWatcher {

    private RecyclerView recyclerView;
    private AdapterDiaDiem adapterDiaDiem, adapterDiaDiemSearch;
    private List<DiaDiemDTO> diadiemList, diaDiemByID;
    private SQLiteDataAccessHelper dataAccessHelper;
    private JellyToolbar toolbar;
    private Toolbar toolbar1;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private CircleImageView profileImage;
    private TextView txtNameOfUser, txtEmailOfUser;
    private View headerLayout;

    private static final String TEXT_KEY = "text";
    private Boolean JellyToolBarIsExpand = false;
    private GoogleApiClient mGoogleApiClient;

    DiaDiemBLL diaDiemBLL;
    AutoCompleteTextView txtSearchToolbar;
    ArrayList<String> arrTenDiaDiem = new ArrayList<String>();
    private HashMap<String, String> hashmapOfDiaDiem;
    SharedPreferences pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        initializeView();

        diaDiemBLL = new DiaDiemBLL(this);
        diadiemList = new ArrayList<>();
        diadiemList = diaDiemBLL.layDanhSachDiaDiem();
        diaDiemByID = new ArrayList<>();
        adapterDiaDiem = new AdapterDiaDiem(this, diadiemList);

//        add data to HashMap and arrTenDiaDiem
        String tenDiaDiemAfterXoaDau;
        hashmapOfDiaDiem = new HashMap<String, String>();
        for (int i = 0; i < diadiemList.size(); i++) {
            tenDiaDiemAfterXoaDau = removeAccent(diadiemList.get(i).getTen());
            hashmapOfDiaDiem.put(diadiemList.get(i).getTen(), diadiemList.get(i).getId());
            hashmapOfDiaDiem.put(tenDiaDiemAfterXoaDau, diadiemList.get(i).getId());
            arrTenDiaDiem.add(diadiemList.get(i).getTen());
            arrTenDiaDiem.add(tenDiaDiemAfterXoaDau);
        }

        getJellyToolBar();
        setSupportActionBar(toolbar1);
        initCollapsingToolbar();
        getEventOfEditTextSearch();
        getDataAccess();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDiaDiem);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (view.getId() == R.id.thumbnail) {
                        }
                    }
                })
        );

//        set Background home
        try {
            Glide.with(this).load(R.drawable.background_home_thaibinh).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        get datalogin saved
        pre = getSharedPreferences("my_data", MODE_PRIVATE);

//        Picasso.with(HomeActivity.this)
//                .load(pre.getString("profilePucture", "sss"))
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(profileImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        //Try again online if cache failed
//                        Picasso.with(HomeActivity.this)
//                                .load(pre.getString("profilePucture", "sss"))
//                                .error(R.drawable.anhdaidien)
//                                .into(profileImage, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError() {
//                                        Log.v("Picasso","Could not fetch image");
//                                    }
//                                });
//                    }
//                });
        Log.d("asd propicture fb ", "--: " + pre.getString("id", "khong co"));
        setDataNavigation();
//        Toast.makeText(getApplicationContext(), pre.getString("name", "chua co name") + " -- typeLogin: " + pre.getString("typelogin", "chua co type"), Toast.LENGTH_LONG).show();
    }

    public void setDataNavigation(){
        Picasso.with(getApplicationContext())
                .load(pre.getString("profilePicture", "https://viblo.asia/images/mm.png"))
                .placeholder(R.drawable.anhdaidien)
                .noFade()
                .error(R.drawable.anhdaidien)
                .into(profileImage);
        txtNameOfUser.setText(pre.getString("name", "Name"));
        txtEmailOfUser.setText(pre.getString("email", "Email"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //    test remove accent
    public String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }



    public void animateIntent(View view) {
        Intent intent = new Intent(this, DiaDiemDetailActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Get the transition name from the string
            String transitionName = getString(R.string.transition_string);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            view,   // Starting view
                            transitionName    // The String
                    );
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    //
    @Override
    public void onBackPressed() {

        if(nvDrawer.isShown())
        {
            mDrawer.closeDrawers();
        }
        else {
            if (recyclerView.getAdapter().getItemCount() == 1) {
                getDefaultToolbar();
                recyclerView.setAdapter(adapterDiaDiem);
            } else if (JellyToolBarIsExpand == true) {

                getDefaultToolbar();
            } else if (toolbar.isShown() && JellyToolBarIsExpand == false) {
                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                appBarLayout.setExpanded(true);
                recyclerView.scrollToPosition(0);
            } else if (JellyToolBarIsExpand == false) {
                super.onBackPressed();
            }
        }
        }



    private void getDefaultToolbar() {
        toolbar.collapse();
        txtSearchToolbar.getText().clear();
    }

    private void initializeView() {
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        txtSearchToolbar = (AutoCompleteTextView) findViewById(R.id.txtSearchInMain);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar = (JellyToolbar) findViewById(R.id.toolbar);

//      init navigation drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemIconTintList(null);
        headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        profileImage = (CircleImageView) headerLayout.findViewById(R.id.profile_image);
        txtNameOfUser = (TextView) headerLayout.findViewById(R.id.txtNameOfUser);
        txtEmailOfUser = (TextView) headerLayout.findViewById(R.id.txtEmailOfUser);

        getEventDrawerChangeState();
        setupDrawerContent(nvDrawer);   // set drawer: onclickMenuItem

    }

    public void getEventDrawerChangeState()
    {
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                toolbar.getToolbar().setNavigationIcon(R.drawable.ic_back);
                if(!pre.getString("typelogin", "a").equals("a")) {
                    if (txtEmailOfUser.getText().toString().equals("Email")) {
                        Toast.makeText(getApplicationContext(), "vao day", Toast.LENGTH_SHORT).show();
                        setDataNavigation();
                    }
                }
                

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                toolbar.getToolbar().setNavigationIcon(R.drawable.ic_menu);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

//  event when click menu item of navigation drawer
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();
                        switch (id) {
                            case 2131624224: // when click logout
                                doWhenClickLogout();
                                break;
                        }
                        return true;
                    }
                });
    }
    public void gotoaddtheme()
    {
        Intent intent = new Intent(getApplicationContext(),addtheme.class);
        startActivity(intent);
    }
    public void doWhenClickLogout()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String typeLogin = pre.getString("typelogin", "none");
        if (typeLogin.equals("fb")) {
//            Toast.makeText(getApplicationContext(), "dang login fb -- type : " + typeLogin, Toast.LENGTH_SHORT).show();
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            pre.edit().clear().commit();
            startActivity(intent);
            finish();

        } else if (typeLogin.equals("gg")) {
//            Toast.makeText(getApplicationContext(), "dang login gg -- type : " + typeLogin, Toast.LENGTH_SHORT).show();
            logoutgg();
            pre.edit().clear().commit();
            finish();
        } else if (typeLogin.equals("offline")) {
//            Toast.makeText(getApplicationContext(), "dang login offline -- type : " + typeLogin + "\n Chuc nang login offline chua hoan thien", Toast.LENGTH_SHORT).show();
            pre.edit().clear().commit();
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Bạn Chưa Đăng Nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataAccess(){
        dataAccessHelper = new SQLiteDataAccessHelper(this);
        File database = getApplicationContext().getDatabasePath(SQLiteDataAccessHelper.DBNAME);
        if (false == database.exists()) {
            dataAccessHelper.getReadableDatabase();
            //Copy db
            if (copyDatabase(HomeActivity.this)) {
                Toast.makeText(HomeActivity.this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getJellyToolBar(){
        toolbar.getToolbar().setNavigationIcon(R.drawable.ic_menu);
        toolbar.setJellyListener(jellyListener);
        toolbar.getToolbar().setPadding(20, getStatusBarHeight(), 0, 0);
        txtSearchToolbar = (AutoCompleteTextView) LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        txtSearchToolbar.setBackgroundResource(R.color.colorTransparent1);
        toolbar.setContentView(txtSearchToolbar);
        toolbar.getToolbar().setTitle("Ẩm Thực 3 Miền");
        toolbar.setVisibility(View.INVISIBLE);

        toolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public void logoutgg() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d("asd", "User Logged out");
                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("asd", "Google API Client Connection Suspended");
            }
        });
    }

//    private void configLayout() {
////        if sdk > kitkat : nolimit layout
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            Window w = getWindow();
////            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////        }
//
////        full screen
//
//
////        getWindow().getDecorView().setSystemUiVisibility(
////                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        Window window = this.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorTransparent1));
//
//    }

//    get data by id and add adapter when search complete
    private void doSearch(String idDiaDiem)
    {
        diaDiemByID = diaDiemBLL.getDiaDiemByID(idDiaDiem);
        adapterDiaDiemSearch = new AdapterDiaDiem(HomeActivity.this, diaDiemByID);
        recyclerView.setAdapter(adapterDiaDiemSearch);
    }

    private void getEventOfEditTextSearch() {
        txtSearchToolbar.setAdapter(
                new ArrayAdapter<String>
                        (
                                this,
                                android.R.layout.simple_list_item_1,
                                arrTenDiaDiem
                        ));

        txtSearchToolbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        hashmapOfDiaDiem.get(txtSearchToolbar.getText().toString()), Toast.LENGTH_SHORT).show();

                String idDiaDiemSearch = hashmapOfDiaDiem.get(txtSearchToolbar.getText().toString());
                doSearch(idDiaDiemSearch);
            }
        });
        txtSearchToolbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    Toast.makeText(getApplicationContext(), "ban da nhan enter " + txtSearchToolbar.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }


    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(txtSearchToolbar.getText())) {
                toolbar.collapse();
            } else {
                txtSearchToolbar.getText().clear();
            }
        }

        @Override
        public void onToolbarExpanded() {
            super.onToolbarExpanded();
            JellyToolBarIsExpand = true;
            txtSearchToolbar.requestFocus();
        }

        @Override
        public void onToolbarCollapsed() {
            super.onToolbarCollapsed();
            JellyToolBarIsExpand = false;
        }

    };

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_KEY, txtSearchToolbar.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtSearchToolbar.setText(savedInstanceState.getString(TEXT_KEY));
        txtSearchToolbar.setSelection(txtSearchToolbar.getText().length());
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        collapsingToolbar.setCollapsedTitleTextColor(getColor(R.color.colorTitleDark));
                    }
                    else
                    {
                        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTitleDark));
                    }
                    isShow = true;
                    toolbar.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    toolbar.collapse();
                    txtSearchToolbar.getText().clear();
                    isShow = false;
                    toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(SQLiteDataAccessHelper.DBNAME);
            String outFileName = SQLiteDataAccessHelper.DBLOCATION + SQLiteDataAccessHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("HomeActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        txtSearchToolbar.setText(txtSearchToolbar.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     *
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}