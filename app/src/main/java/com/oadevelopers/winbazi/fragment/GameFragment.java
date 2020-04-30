package com.oadevelopers.winbazi.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oadevelopers.winbazi.activity.NotificationActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.oadevelopers.winbazi.R;
import com.oadevelopers.winbazi.activity.MainActivity;
import com.oadevelopers.winbazi.adapter.GameAdapter;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.common.Constant;
import com.oadevelopers.winbazi.model.GamePojo;
import com.oadevelopers.winbazi.utils.ExtraOperations;
import com.oadevelopers.winbazi.utils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    public SharedPreferences preferences;
    public String prefName = "Sky_Winner";
    public int count = 0;
    private View view;
    private boolean isNavigationHide = false;
    private RecyclerView recyclerView;
    private LinearLayout gameLinearLayout;
    private RecyclerView.Adapter gameAdapter;

    private ArrayList<GamePojo> gamePojoList;
    private RequestQueue gameRequestQueue;
    private JsonArrayRequest gameJsonArrayRequest;

    private ShimmerFrameLayout shimmer_view_container;
    private NestedScrollView nestedScrollView;
    private TextView announceText;
    private LinearLayout noMatchesLL;
    private LinearLayout upcomingLL;
    private CardView notificationCard;

    private Timer timer;
    private int page = 0;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game, container, false);

        initView();

        timer = new Timer();
        gamePojoList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (new ExtraOperations().haveNetworkConnection(getActivity())) {
            loadAnnouncements();
            loadGames();
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        NestedScrollView nested_content = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateNavigation(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateNavigation(true);
                }
            }
        });

        notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCounter();

                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * MainActivity.navigation.getHeight()) : 0;
        MainActivity.navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void loadAnnouncements() {
        Uri.Builder builder = Uri.parse(Constant.ANNOUNCEMENT_URL).buildUpon();
        builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final JSONArray jsonArray = response.getJSONArray("Result");
                    announceText.setText(jsonArray.getJSONObject(0).getString("title"));
                    if (jsonArray.length() == 1) {
                        String announcement = jsonArray.getJSONObject(0).getString("title");
                        announceText.setText(announcement);
                    } else if (jsonArray.length() == 2) {
                        try {
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    if (jsonArray.length() == page) {
                                        page = 0;
                                    } else {
                                        page++;
                                    }
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() //run on ui thread
                                        {
                                            public void run() {
                                                if (page == 0) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(0).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 1) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(1).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                }
                                            }
                                        });
                                    }
                                }
                            }, 2000, 5000);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } else if (jsonArray.length() == 3) {
                        try {
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    if (jsonArray.length() == page) {
                                        page = 0;
                                    } else {
                                        page++;
                                    }
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() //run on ui thread
                                        {
                                            public void run() {
                                                if (page == 0) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(0).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 1) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(1).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 2) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(2).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                }

                                            }
                                        });
                                    }
                                }
                            }, 2000, 5000);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } else if (jsonArray.length() == 4) {
                        try {
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    if (jsonArray.length() == page) {
                                        page = 0;
                                    } else {
                                        page++;
                                    }
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() //run on ui thread
                                        {
                                            public void run() {
                                                if (page == 0) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(0).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 1) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(1).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 2) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(2).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 3) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(3).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                }
                                            }
                                        });
                                    }
                                }
                            }, 2000, 5000);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } else if (jsonArray.length() == 5) {
                        try {
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    if (jsonArray.length() == page) {
                                        page = 0;
                                    } else {
                                        page++;
                                    }
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() //run on ui thread
                                        {
                                            public void run() {
                                                if (page == 0) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(0).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 1) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(1).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 2) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(2).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 3) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(3).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                } else if (page == 4) {
                                                    String announcement = null;
                                                    try {
                                                        announcement = jsonArray.getJSONObject(4).getString("title");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    announceText.setText(announcement);
                                                }
                                            }
                                        });
                                    }
                                }
                            }, 2000, 5000);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    error.printStackTrace();
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(false);
        MySingleton.getInstance(getActivity()).addToRequestque(jsonObjectRequest);
    }

    private void initView() {
        this.shimmer_view_container = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        this.nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScroll);
        this.announceText = (TextView) view.findViewById(R.id.announceText);
        this.noMatchesLL = (LinearLayout) view.findViewById(R.id.noMatchesLL);
        this.upcomingLL = (LinearLayout) view.findViewById(R.id.upcomingLL);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.notificationCard = (CardView) view.findViewById(R.id.notificationCard);
    }


    private void loadGames() {
        recyclerView.setVisibility(View.GONE);
        noMatchesLL.setVisibility(View.GONE);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmer();
        Uri.Builder builder = Uri.parse(Constant.LIST_GAME_URL).buildUpon();
        builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
        gameJsonArrayRequest = new JsonArrayRequest(builder.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        shimmer_view_container.stopShimmer();
                        shimmer_view_container.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_MATCH(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        shimmer_view_container.stopShimmer();
                        shimmer_view_container.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        noMatchesLL.setVisibility(View.VISIBLE);
                    }
                });
        gameRequestQueue = Volley.newRequestQueue(getActivity());
        gameJsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        gameJsonArrayRequest.setShouldCache(false);
        gameRequestQueue.getCache().clear();
        gameRequestQueue.add(gameJsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_MATCH(JSONArray response) {
        gamePojoList.clear();
        for (int i = 0; i < response.length(); i++) {
            GamePojo gamePojo = new GamePojo();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                gamePojo.setId(json.getString("id"));
                gamePojo.setTitle(json.getString("title"));
                gamePojo.setBanner(json.getString("banner"));
                gamePojo.setUrl(json.getString("url"));
                gamePojo.setType(json.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            gamePojoList.add(gamePojo);
        }
        if (!gamePojoList.isEmpty()) {
            gameAdapter = new GameAdapter(gamePojoList, getActivity());
            gameAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(gameAdapter);
            shimmer_view_container.stopShimmer();
            shimmer_view_container.setVisibility(View.GONE);
            noMatchesLL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            shimmer_view_container.stopShimmer();
            shimmer_view_container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noMatchesLL.setVisibility(View.VISIBLE);
        }
    }


    public void saveCounter() {
        count = 0;

        preferences = getActivity().getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter", count);
        editor.apply();
    }


    public void onResume() {
        super.onResume();
        if (new ExtraOperations().haveNetworkConnection(getActivity())) {
            shimmer_view_container.startShimmer();
        }
    }

    public void onPause() {
        shimmer_view_container.stopShimmer();
        super.onPause();
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
