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
import com.oadevelopers.winbazi.adapter.ResultAdapter;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.common.Constant;
import com.oadevelopers.winbazi.model.ResultPojo;
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
public class CompleteFragment extends Fragment {

    private View view;

    private NestedScrollView nestedScrollView;
    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout noResults;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private LinearLayout upcomingLinearLayout;
    private RecyclerView.Adapter adapter;

    private ArrayList<ResultPojo> resultPojoList;
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;

    private SessionManager session;
    private String id;
    private String username;
    private String password;

    private Bundle bundle;
    private String strId, strTitle;

    public CompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complete, container, false);
        session = new SessionManager(getActivity());

        initView();
        initListener();
        initSession();
        loadBundle();

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        resultPojoList = new ArrayList<>();
    }

    private void initView() {
        this.mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.upcomingLinearLayout = (LinearLayout) view.findViewById(R.id.upcomingLL);
        this.noResults = (LinearLayout) view.findViewById(R.id.noResultsLL);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
    }

    private void initSession() {
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
    }


    private void loadMatch() {
        recyclerView.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
        Uri.Builder builder = Uri.parse(Constant.COMPLETED_MATCH_URL).buildUpon();
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
                        noResults.setVisibility(View.VISIBLE);
                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity());
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonArrayRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_MATCH(JSONArray response) {
        resultPojoList.clear();
        for (int i = 0; i < response.length(); i++) {
            ResultPojo resultPojo = new ResultPojo();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);
                resultPojo.setId(json.getString("id"));
                resultPojo.setTitle(json.getString("title"));
                resultPojo.setTime(json.getString("time"));
                resultPojo.setPrize_pool(json.getInt("prize_pool"));
                //resultPojo.setImage(json.getString("image"));
                resultPojo.setPer_kill(json.getInt("per_kill"));
                resultPojo.setEntry_fee(json.getInt("entry_fee"));
                resultPojo.setEntry_type(json.getString("entry_type"));
                resultPojo.setVersion(json.getString("version"));
                resultPojo.setMap(json.getString("map"));
                resultPojo.setIs_private(json.getString("is_private"));
                resultPojo.setMatch_type(json.getString("match_type"));
                resultPojo.setSponsored_by(json.getString("sponsored_by"));
                resultPojo.setSpectate_url(json.getString("spectate_url"));
                resultPojo.setMatch_notes(json.getString("match_notes"));
                resultPojo.setMatch_desc(json.getString("match_desc"));
                resultPojo.setMatch_status(json.getString("match_status"));
                resultPojo.setTotal_joined(json.getInt("total_joined"));
                resultPojo.setJoined_status(json.getString("joined_status"));
                resultPojo.setPlatform(json.getString("platform"));
                resultPojo.setPool_type(json.getString("pool_type"));
                resultPojo.setAdmin_share(json.getInt("admin_share"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultPojoList.add(resultPojo);
        }
        if (!resultPojoList.isEmpty()) {
            adapter = new ResultAdapter(resultPojoList, getActivity());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            noResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);

        } else {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
        }
    }

    public void onResume() {
        initSession();
        loadMatch();
        super.onResume();
        if (new ExtraOperations().haveNetworkConnection(getActivity())) {
            mShimmerViewContainer.startShimmer();
        }
    }

    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
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
