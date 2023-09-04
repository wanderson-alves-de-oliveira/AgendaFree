package br.com.agenda.simonebraga.view.view;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Calendar;
import java.util.Date;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.view.controle.CriarNotificacao;
import br.com.agenda.simonebraga.view.controle.RefreshAviso;


public class MinhaAtividade extends AppCompatActivity {
    private FrameLayout adContainerView;

    public TextView aviso;
    public TextView horasAvisoT;
//teste ou home /////////////////////////////////////////////////////////////////////////
    // private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";

    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-1070048556704742/1291285426";

    ///////////////////////////////////////////////////////////////////////////////////////////
    private AdLoader.Builder builder;
    private FrameLayout frameLayoutf;

    private String informacao;
    private NativeAd nativeAd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        frameLayoutf = (FrameLayout) findViewById(R.id.fl_adplaceholderf);

        //Call the function to initialize AdMob SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener () {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //get the reference to your FrameLayout
        adContainerView = findViewById(R.id.adView_container);

        //Create an AdView and put it into your FrameLayout


        builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });


        refreshAd ();



        setContentView(R.layout.tela_ligada);
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//coloca em fullscreen
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        Date mais = new Date ();
        mais.setTime (mais.getTime () + 5000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mais);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);

        }


        findViewById(R.id.btPararAlarme).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                sino();
                System.exit(0);

            }
        });

        findViewById(R.id.btcincomin).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                sino();
                maisCinco( CriarNotificacao.informacao);
                System.exit(0);

            }
        });
        Intent it = getIntent();
        informacao = CriarNotificacao.informacao;
        aviso =(TextView) findViewById( R.id.tvAvisoT );
        horasAvisoT =(TextView) findViewById( R.id.horasAvisoT );

        aviso.setText(informacao);

        int minutos = calendar.get(Calendar.MINUTE);
        if (minutos<10) {
            horasAvisoT.setText (calendar.get (Calendar.HOUR_OF_DAY) + ":0" + calendar.get (Calendar.MINUTE));

        }else {
            horasAvisoT.setText (calendar.get (Calendar.HOUR_OF_DAY) + ":" + calendar.get (Calendar.MINUTE));

        }
    }

    private void sino(){
        if(CriarNotificacao.ringtone!=null) {

            CriarNotificacao.ringtone.stop ();
            NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext ().NOTIFICATION_SERVICE);
            notificationManager.cancel (CriarNotificacao.id);
            notificationManager.cancel (8);
            CriarNotificacao.desligamentoAltomatico=false;

        }

    }



    private void maisCinco(String titulo){
         Date mais = new Date ();
        mais.setTime (mais.getTime () + 300000);
         Calendar calendar = Calendar.getInstance();
        calendar.setTime(mais);

        calendar.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent(getBaseContext(), CriarNotificacao.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("titulo", titulo);
        intent.putExtra("aviso", titulo);
        intent.putExtra("id", 7);
        intent.putExtra("repetir", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast (getBaseContext(), 7, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Regista o alerta no sistema.
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getBaseContext().getSystemService (ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC, calendar.getTimeInMillis (), pendingIntent);
        AlarmManagerCompat alarmManagerCompat = null;
        alarmManagerCompat.setAlarmClock (alarmManager, calendar.getTimeInMillis (), pendingIntent, pendingIntent);

        mais = new Date ();
        mais.setTime (mais.getTime () + 120000);
        new RefreshAviso ().desligarAlarme (mais, getBaseContext(), 8, new Date ().getTime (),true);
    }






    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd() {


        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (MinhaAtividade.this.nativeAd != null) {
                            MinhaAtividade.this.nativeAd.destroy();
                        }
                        MinhaAtividade.this.nativeAd = nativeAd;
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateNativeAdView(nativeAd, adView);


                        frameLayoutf = (FrameLayout) findViewById(R.id.fl_adplaceholderf);

                        frameLayoutf.removeAllViews ();

                         frameLayoutf.addView(adView);



                    }

                });


        AdLoader adLoader =
                builder  .withAdListener(
                                new AdListener () {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {


                                    }

                                    @Override
                                    public void onAdClicked () {
                                        super.onAdClicked ();
                                        sino ();
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }








}