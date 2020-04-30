package com.oadevelopers.winbazi.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.oadevelopers.winbazi.R;
import com.oadevelopers.winbazi.adapter.PlayAdapter;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.common.Constant;
import com.oadevelopers.winbazi.model.PlayPojo;
import com.oadevelopers.winbazi.session.SessionManager;
import com.oadevelopers.winbazi.utils.ExtraOperations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    private View view;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout noMatchesLL;

    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private LinearLayout upcomingLinearLayout;
    private LinearLayout participatedLinearLayout;
    private RecyclerView participatedRecyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<PlayPojo> playPojoList;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;

    private SessionManager session;
    private String id;
    private String username;
    private String password;

    private Bundle bundle;
    private String strId, strTitle;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        session = new SessionManager(getActivity());

        initView();
        initSession();
        initListener();
        loadBundle();

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.participatedRecyclerView.setHasFixedSize(true);
        this.participatedRecyclerView.setNestedScrollingEnabled(false);
        this.participatedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new ExtraOperations().haveNetworkConnection(getActivity())) {
                    loadMatch();
                } else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Default load for first time
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (new ExtraOperations().haveNetworkConnection(getActivity())) {
                    loadMatch();
                } else {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void initListener() {
        playPojoList = new ArrayList<>();
    }

    private void loadMatch() {
        recyclerView.setVisibility(View.GONE);
        noMatchesLL.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
        Uri.Builder builder = Uri.parse(Constant.UPCOMING_MATCH_URL).buildUpon();
        builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
        builder.appendQueryParameter("game_id", strId);
        builder.appendQueryParameter("user_id", id);
        jsonArrayRequest = new JsonArrayRequest(builder.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_MATCH(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        noMatchesLL.setVisibility(View.VISIBLE);
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonArrayRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_MATCH(JSONArray response) {
        playPojoList.clear();
        for (int i = 0; i < response.length(); i++) {
            PlayPojo playPojo = new PlayPojo();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                playPojo.setId(json.getString("id"));
                playPojo.setTitle(json.getString("title"));
                playPojo.setTime(json.getString("time"));
                playPojo.setPrize_pool(json.getInt("prize_pool"));
                playPojo.setImage(json.getString("image"));
                playPojo.setPer_kill(json.getInt("per_kill"));
                playPojo.setEntry_fee(json.getInt("entry_fee"));
                playPojo.setEntry_type(json.getString("entry_type"));
                playPojo.setVersion(json.getString("version"));
                playPojo.setMap(json.getString("map"));
                playPojo.setIs_private(json.getString("is_private"));
                playPojo.setMatch_type(json.getString("match_type"));
                playPojo.setRules(json.getString("rules"));
                playPojo.setMatch_status(json.getString("match_status"));
                playPojo.setMatch_id(json.getString("match_id"));
                playPojo.setRoom_id(json.getString("room_id"));
                playPojo.setRoom_pass(json.getString("room_pass"));
                playPojo.setRoom_size(json.getInt("room_size"));
                playPojo.setTotal_joined(json.getInt("total_joined"));
                playPojo.setJoined_status(json.getString("joined_status"));
                playPojo.setUser_joined(json.getString("user_joined"));
                playPojo.setMatch_desc(json.getString("match_desc"));
                playPojo.setIs_cancel(json.getString("is_cancel"));
                playPojo.setCancel_reason(json.getString("cancel_reason"));
                playPojo.setAccess_key(json.getString("access_key"));
                playPojo.setPlatform(json.getString("platform"));
                playPojo.setPool_type(json.getString("pool_type"));
                playPojo.setAdmin_share(json.getInt("admin_share"));
                playPojo.setPool_type(json.getString("pool_type"));
                playPojo.setSlot(json.getInt("slot"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            playPojoList.add(playPojo);
        }
        if (!playPojoList.isEmpty()) {
            adapter = new PlayAdapter(playPojoList, getActivity());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            noMatchesLL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        } else {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noMatchesLL.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        }
    }


    private void initView() {
        this.mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.participatedRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewParticipated);
        this.upcomingLinearLayout = (LinearLayout) view.findViewById(R.id.upcomingLL);
        this.participatedLinearLayout = (LinearLayout) view.findViewById(R.id.participatedLL);
        this.noMatchesLL = (LinearLayout) view.findViewById(R.id.noMatchesLL);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
    }

    private void initSession() {
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
    }

    public void onResume() {
        super.onResume();
        initSession();
        loadMatch();
        if (new ExtraOperations().haveNetworkConnection(getActivity())) {
            mShimmerViewContainer.startShimmer();
        }
    }

    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadBundle() {
        bundle = new Bundle();
        bundle = this.getArguments();
        if (bundle != null) {
            strId = bundle.getString("ID_KEY");
            strTitle = bundle.getString("TITLE_KEY");
        }
    }

}
