package com.oadevelopers.winbazi.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.google.android.material.textfield.TextInputEditText;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import com.oadevelopers.winbazi.R;
import com.oadevelopers.winbazi.activity.JoiningMatchActivity;
import com.oadevelopers.winbazi.activity.MainActivity;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.common.Constant;

import static com.oadevelopers.winbazi.activity.JoiningMatchActivity.progressBar;

public class ActionAlertMessage {
    private String accessKey;
    private String encodeGameUserID1;
    private String encodeGameUserID2;
    private String encodeGameUserID3;
    private String encodeGameUserID4;

    private Context context;

    public ActionAlertMessage() {
    }

    public ActionAlertMessage(Context context) {
        this.context = context;
    }

    public void showJoinMatchAlert(final JoiningMatchActivity joiningMatchActivity, final String id, final String username, final String name, final String matchID, final String entryType, final String matchType, final String privateStatus, final int entryFee) {
        if (matchType.equals("Solo")) {
            final Dialog dialog = new Dialog(joiningMatchActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_joinprompt_solo);
            dialog.setCancelable(false);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final TextInputLayout accessCodeView = (TextInputLayout) dialog.findViewById(R.id.accessCodeView);
            final TextInputEditText accessCode = (TextInputEditText) dialog.findViewById(R.id.accessCode);
            final TextInputEditText gameID = (TextInputEditText) dialog.findViewById(R.id.gameID);

            Button button = (Button) dialog.findViewById(R.id.next);
            Button button2 = (Button) dialog.findViewById(R.id.cancel);

            final TextView textError = (TextView) dialog.findViewById(R.id.textError);
            TextView accessCodeInfoText = (TextView) dialog.findViewById(R.id.accessCodeInfoText);

            if (privateStatus.equals("yes")) {
                accessCodeView.setVisibility(View.VISIBLE);
                accessCodeInfoText.setVisibility(View.VISIBLE);
            } else {
                accessCode.setVisibility(View.GONE);
                accessCodeInfoText.setVisibility(View.GONE);
            }

            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    accessKey = accessCode.getText().toString().trim();
                    encodeGameUserID1 = gameID.getText().toString().trim();


                    if (privateStatus.equals("yes") && accessKey.isEmpty() || accessKey.contains(" ")) {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Access Code. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (!encodeGameUserID1.isEmpty()) {
                        joinSoloMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, name, matchID, entryType, matchType, privateStatus, entryFee);
                        dialog.dismiss();
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Game Username. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        } else if (matchType.equals("Duo")) {
            final Dialog dialog = new Dialog(joiningMatchActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_joinprompt_duo);
            dialog.setCancelable(false);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final TextInputLayout accessCodeView = (TextInputLayout) dialog.findViewById(R.id.accessCodeView);
            final TextInputEditText accessCode = (TextInputEditText) dialog.findViewById(R.id.accessCode);
            final TextInputEditText gameID1 = (TextInputEditText) dialog.findViewById(R.id.gameID1);
            final TextInputEditText gameID2 = (TextInputEditText) dialog.findViewById(R.id.gameID2);

            Button button = (Button) dialog.findViewById(R.id.next);
            Button button2 = (Button) dialog.findViewById(R.id.cancel);

            final TextView textError = (TextView) dialog.findViewById(R.id.textError);
            TextView accessCodeInfoText = (TextView) dialog.findViewById(R.id.accessCodeInfoText);

            if (privateStatus.equals("yes")) {
                accessCodeView.setVisibility(View.VISIBLE);
                accessCodeInfoText.setVisibility(View.VISIBLE);
            } else {
                accessCode.setVisibility(View.GONE);
                accessCodeInfoText.setVisibility(View.GONE);
            }

            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    accessKey = accessCode.getText().toString().trim();
                    encodeGameUserID1 = gameID1.getText().toString().trim();
                    encodeGameUserID2 = gameID2.getText().toString().trim();

                    if (privateStatus.equals("yes") && accessKey.isEmpty() || accessKey.contains(" ")) {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Access Code. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (!encodeGameUserID1.isEmpty()) {
                        if (!encodeGameUserID1.equals(encodeGameUserID2)) {
                            if (encodeGameUserID2.isEmpty()) {
                                encodeGameUserID2 = "null";
                            }
                            joinDuoMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, encodeGameUserID2, name, matchID, entryType, matchType, privateStatus, entryFee);
                            dialog.dismiss();
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            textError.setVisibility(View.VISIBLE);
                            textError.setText("Same Game Username Not Allowed. Retry.");
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Game Username. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        } else if (matchType.equals("Squad")) {
            final Dialog dialog = new Dialog(joiningMatchActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_joinprompt_squad);
            dialog.setCancelable(false);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final TextInputLayout accessCodeView = (TextInputLayout) dialog.findViewById(R.id.accessCodeView);
            final TextInputEditText accessCode = (TextInputEditText) dialog.findViewById(R.id.accessCode);
            final TextInputEditText gameID1 = (TextInputEditText) dialog.findViewById(R.id.gameID1);
            final TextInputEditText gameID2 = (TextInputEditText) dialog.findViewById(R.id.gameID2);
            final TextInputEditText gameID3 = (TextInputEditText) dialog.findViewById(R.id.gameID3);
            final TextInputEditText gameID4 = (TextInputEditText) dialog.findViewById(R.id.gameID4);

            Button button = (Button) dialog.findViewById(R.id.next);
            Button button2 = (Button) dialog.findViewById(R.id.cancel);

            final TextView textError = (TextView) dialog.findViewById(R.id.textError);
            TextView accessCodeInfoText = (TextView) dialog.findViewById(R.id.accessCodeInfoText);

            if (privateStatus.equals("yes")) {
                accessCodeView.setVisibility(View.VISIBLE);
                accessCodeInfoText.setVisibility(View.VISIBLE);
            } else {
                accessCode.setVisibility(View.GONE);
                accessCodeInfoText.setVisibility(View.GONE);
            }

            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    accessKey = accessCode.getText().toString().trim();
                    encodeGameUserID1 = gameID1.getText().toString().trim();
                    encodeGameUserID2 = gameID2.getText().toString().trim();
                    encodeGameUserID3 = gameID3.getText().toString().trim();
                    encodeGameUserID4 = gameID4.getText().toString().trim();

                    if (privateStatus.equals("yes") && accessKey.isEmpty() || accessKey.contains(" ")) {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Access Code. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (!encodeGameUserID1.isEmpty()) {
                        if ((!encodeGameUserID1.equals(encodeGameUserID2)) && (!encodeGameUserID1.equals(encodeGameUserID3)) && (!encodeGameUserID1.equals(encodeGameUserID4))) {
                            if (encodeGameUserID2.isEmpty()) {
                                encodeGameUserID2 = "null";
                            }
                            if (encodeGameUserID3.isEmpty()) {
                                encodeGameUserID3 = "null";
                            }
                            if (encodeGameUserID4.isEmpty()) {
                                encodeGameUserID4 = "null";
                            }
                            if (!encodeGameUserID2.equals("null")) {
                                if (!encodeGameUserID3.equals("null")) {
                                    if ((!encodeGameUserID2.equals(encodeGameUserID3)) && (!encodeGameUserID2.equals(encodeGameUserID4)) && (!encodeGameUserID3.equals(encodeGameUserID4))) {
                                        joinSquadMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, encodeGameUserID2, encodeGameUserID3, encodeGameUserID4, name, matchID, entryType, matchType, privateStatus, entryFee);
                                        dialog.dismiss();
                                        progressBar.setVisibility(View.VISIBLE);
                                    } else {
                                        textError.setVisibility(View.VISIBLE);
                                        textError.setText("Same Game Username Not Allowed. Retry.");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    if ((!encodeGameUserID2.equals(encodeGameUserID4))) {
                                        joinSquadMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, encodeGameUserID2, encodeGameUserID3, encodeGameUserID4, name, matchID, entryType, matchType, privateStatus, entryFee);
                                        dialog.dismiss();
                                        progressBar.setVisibility(View.VISIBLE);
                                    } else {
                                        textError.setVisibility(View.VISIBLE);
                                        textError.setText("Same Game Username Not Allowed. Retry.");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            } else if (!encodeGameUserID3.equals("null")) {
                                if ((!encodeGameUserID3.equals(encodeGameUserID4))) {
                                    joinSquadMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, encodeGameUserID2, encodeGameUserID3, encodeGameUserID4, name, matchID, entryType, matchType, privateStatus, entryFee);
                                    dialog.dismiss();
                                    progressBar.setVisibility(View.VISIBLE);
                                } else {
                                    textError.setVisibility(View.VISIBLE);
                                    textError.setText("Same Game Username Not Allowed. Retry.");
                                    progressBar.setVisibility(View.GONE);
                                }
                            } else {
                                joinSquadMatch(joiningMatchActivity, id, username, accessKey, encodeGameUserID1, encodeGameUserID2, encodeGameUserID3, encodeGameUserID4, name, matchID, entryType, matchType, privateStatus, entryFee);
                                dialog.dismiss();
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        } else {
                            textError.setVisibility(View.VISIBLE);
                            textError.setText("Same Game Username Not Allowed. Retry.");
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        textError.setVisibility(View.VISIBLE);
                        textError.setText("Invalid Game Username. Retry.");
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }

    private void joinSoloMatch(final JoiningMatchActivity joiningMatchActivity, final String id, final String username, final String accessKey, final String encodeGameUserID1, final String name, final String matchID, final String entryType, final String matchType, final String privateStatus, final int entryFee) {
        if (new ExtraOperations().haveNetworkConnection(joiningMatchActivity)) {
            Uri.Builder builder = Uri.parse(Constant.JOIN_MATCH_URL).buildUpon();
            builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
            builder.appendQueryParameter("match_id", matchID);
            builder.appendQueryParameter("user_id", id);
            builder.appendQueryParameter("username", username);
            builder.appendQueryParameter("name", name);
            builder.appendQueryParameter("is_private", privateStatus);
            builder.appendQueryParameter("accessKey", accessKey);
            builder.appendQueryParameter("pubg_id1", encodeGameUserID1);
            builder.appendQueryParameter("entry_type", entryType);
            builder.appendQueryParameter("match_type", matchType);
            builder.appendQueryParameter("entry_fee", String.valueOf(entryFee));
            StringRequest request = new StringRequest(Request.Method.GET, builder.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        String success = jsonObject1.getString("success");
                        String msg = jsonObject1.getString("msg");

                        if (success.equals("0")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("1")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("2")) {
                            progressBar.setVisibility(View.GONE);
                            successDialog(joiningMatchActivity);
                        } else if (success.equals("3")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
//                        parameters.put("fname", firstname);
//                        parameters.put("lname", lastname);
//                        parameters.put("username", uname);
//                        parameters.put("password", md5pass);
//                        parameters.put("email", eMail);
//                        parameters.put("mobile", mobileNumber);
                    return parameters;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            MySingleton.getInstance(joiningMatchActivity).addToRequestque(request);
        } else {
            Toast.makeText(joiningMatchActivity, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void joinDuoMatch(final JoiningMatchActivity joiningMatchActivity, final String id, final String username, final String accessKey, final String encodeGameUserID1, final String encodeGameUserID2, final String name, final String matchID, final String entryType, final String matchType, final String privateStatus, final int entryFee) {
        if (new ExtraOperations().haveNetworkConnection(joiningMatchActivity)) {
            Uri.Builder builder = Uri.parse(Constant.JOIN_MATCH_URL).buildUpon();
            builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
            builder.appendQueryParameter("match_id", matchID);
            builder.appendQueryParameter("user_id", id);
            builder.appendQueryParameter("username", username);
            builder.appendQueryParameter("name", name);
            builder.appendQueryParameter("is_private", privateStatus);
            builder.appendQueryParameter("accessKey", accessKey);
            builder.appendQueryParameter("pubg_id1", encodeGameUserID1);
            builder.appendQueryParameter("pubg_id2", encodeGameUserID2);
            builder.appendQueryParameter("entry_type", entryType);
            builder.appendQueryParameter("match_type", matchType);
            builder.appendQueryParameter("entry_fee", String.valueOf(entryFee));
            StringRequest request = new StringRequest(Request.Method.GET, builder.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        String success = jsonObject1.getString("success");
                        String msg = jsonObject1.getString("msg");

                        if (success.equals("0")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("1")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("2")) {
                            progressBar.setVisibility(View.GONE);
                            successDialog(joiningMatchActivity);
                        } else if (success.equals("3")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
//                        parameters.put("fname", firstname);
//                        parameters.put("lname", lastname);
//                        parameters.put("username", uname);
//                        parameters.put("password", md5pass);
//                        parameters.put("email", eMail);
//                        parameters.put("mobile", mobileNumber);
                    return parameters;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            MySingleton.getInstance(joiningMatchActivity).addToRequestque(request);
        } else {
            Toast.makeText(joiningMatchActivity, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void joinSquadMatch(final JoiningMatchActivity joiningMatchActivity, final String id, final String username, final String accessKey, final String encodeGameUserID1, final String encodeGameUserID2, final String encodeGameUserID3, final String encodeGameUserID4, final String name, final String matchID, final String entryType, final String matchType, final String privateStatus, final int entryFee) {
        if (new ExtraOperations().haveNetworkConnection(joiningMatchActivity)) {
            Uri.Builder builder = Uri.parse(Constant.JOIN_MATCH_URL).buildUpon();
            builder.appendQueryParameter("access_key", Config.PURCHASE_CODE);
            builder.appendQueryParameter("match_id", matchID);
            builder.appendQueryParameter("user_id", id);
            builder.appendQueryParameter("username", username);
            builder.appendQueryParameter("name", name);
            builder.appendQueryParameter("is_private", privateStatus);
            builder.appendQueryParameter("accessKey", accessKey);
            builder.appendQueryParameter("pubg_id1", encodeGameUserID1);
            builder.appendQueryParameter("pubg_id2", encodeGameUserID2);
            builder.appendQueryParameter("pubg_id3", encodeGameUserID3);
            builder.appendQueryParameter("pubg_id4", encodeGameUserID4);
            builder.appendQueryParameter("entry_type", entryType);
            builder.appendQueryParameter("match_type", matchType);
            builder.appendQueryParameter("entry_fee", String.valueOf(entryFee));
            StringRequest request = new StringRequest(Request.Method.GET, builder.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        String success = jsonObject1.getString("success");
                        String msg = jsonObject1.getString("msg");

                        if (success.equals("0")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("1")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        } else if (success.equals("2")) {
                            progressBar.setVisibility(View.GONE);
                            successDialog(joiningMatchActivity);
                        } else if (success.equals("3")) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(joiningMatchActivity, msg + "", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
//                        parameters.put("fname", firstname);
//                        parameters.put("lname", lastname);
//                        parameters.put("username", uname);
//                        parameters.put("password", md5pass);
//                        parameters.put("email", eMail);
//                        parameters.put("mobile", mobileNumber);
                    return parameters;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            MySingleton.getInstance(joiningMatchActivity).addToRequestque(request);
        } else {
            Toast.makeText(joiningMatchActivity, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void successDialog(final JoiningMatchActivity joiningMatchActivity) {
        final Dialog dialog = new Dialog(joiningMatchActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirmation);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView titleTv = (TextView) dialog.findViewById(R.id.titleTv);
        TextView subTitleTv = (TextView) dialog.findViewById(R.id.subTitleTv);
        TextView noteTv = (TextView) dialog.findViewById(R.id.noteTv);

        Button cancelBt = (Button) dialog.findViewById(R.id.cancelBt);
        Button okBt = (Button) dialog.findViewById(R.id.okBt);

        noteTv.setText("Congratulations!!! You have successfully joined this match. Entry fee has been deducted from your account if any. Room Id and Password are visible in match description before 15 minutes.");

        cancelBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(joiningMatchActivity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    joiningMatchActivity.startActivity(intent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        okBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                try {
                    Intent intent = new Intent(joiningMatchActivity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    joiningMatchActivity.startActivity(intent);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
