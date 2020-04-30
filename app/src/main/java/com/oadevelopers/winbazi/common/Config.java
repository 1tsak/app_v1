package com.oadevelopers.winbazi.common;

import android.util.Base64;

public class Config {

  /*  static {
        System.loadLibrary("keys");
    }

   */

    //  public static native String getNativeKey1();
    // public static native String getNativeKey2();
    // public static native String getNativeKey3();
    //public static native String getNativeKey4();
    //public static native String getNativeKey5();

    // Paytm Production API Details
    public static final String M_ID = "SOJqmI71123213450081";   //Paytm Merchand Id we got it in button_paytm credentials
    public static final String CHANNEL_ID = "WAP";              //Paytm Channel Id, got it in button_paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail";     //Paytm industry type got it in button_paytm credential
    public static final String WEBSITE = "DEFAULT";
    public static final String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
    // UPI Payment Id
    public static final String UPI_ID = "8436449981@paytm";
    // How To Join Room YouTube Link
    public static final String YOUTUBE_CHANNEL_ID = "https://www.youtube.com/";
    public static final String HOW_TO_JOIN_URL = "https://www.youtube.com/";
    // Discord Channel Link
    public static final String DISCORD_CHANNEL_ID = "https://discord.gg/";
    // Website URL
    public static final String WEB_URL = "http://multigames.skywinner.in";
    // Refer & Reward Offer
    // set true to enable or set false to disable
    public static final boolean REFER_EARN = true;
    public static final boolean WATCH_EARN = true;
    // AdMob Keys
    // set admob app id and ad unit id
    public static final String AD_APP_ID = "ca-app-pub-5655443106493901~8753708673";
    public static final String AD_REWARDED_ID = "ca-app-pub-5655443106493901/8553316553";
    // Reward Ads Setup
    // set next reward time, watch video, pay rewars
    public static final String WATCH_COUNT = "5";       // Set minimum watch video
    public static final String PAY_REWARD = "1";        // Set amount after rewarded
    // Customer Support Details
    // set true to enable or set false to disable
    public static final boolean ENABLE_EMAIL_SUPPORT = false;
    public static final boolean ENABLE_PHONE_SUPPORT = false;
    public static final boolean ENABLE_WHATSAPP_SUPPORT = true;
    public static final boolean ENABLE_MESSENGER_SUPPORT = false;
    public static final boolean ENABLE_DISCORD_SUPPORT = false;
    public static final boolean ENABLE_TIKTOK_SUPPORT = false;
    public static final boolean ENABLE_VOUCHER_SUPPORT = false;
    // Follow Us Link
    // set true to enable or set false to disable
    public static final boolean ENABLE_TWITTER_FOLLOW = false;
    public static final boolean ENABLE_YOUTUBE_FOLLOW = false;
    public static final boolean ENABLE_FACEBOOK_FOLLOW = false;
    public static final boolean ENABLE_INSTAGRAM_FOLLOW = false;
    // put your admin panel url here
    public static String ADMIN_PANEL_URL = "http://winbazi.in/v2/api/";
    public static String FILE_PATH_URL = "http://winbazi.in/v2/admin/";
    public static String PAYTM_URL = "http://winbazi.in/v2/paytm/";
    public static String PAYPAL_URL = "http://winbazi.in/v2/braintree/";
    public static String PURCHASE_CODE = "NzQyNGVjZjcxxxxxxxxxxxxxxxjUyZTMyZmQyYzdj";

}
