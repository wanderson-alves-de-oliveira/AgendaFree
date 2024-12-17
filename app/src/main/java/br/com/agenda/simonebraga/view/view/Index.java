package br.com.agenda.simonebraga.view.view;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.agenda.simonebraga.R;
import br.com.agenda.simonebraga.databinding.ActivityCalendarioWandersonBinding;
import br.com.agenda.simonebraga.view.controle.AvisoAgenda;
import br.com.agenda.simonebraga.view.controle.AvisoAlarme;
import br.com.agenda.simonebraga.view.controle.CadastroAlarme;
import br.com.agenda.simonebraga.view.controle.CadastroMensagem;
import br.com.agenda.simonebraga.view.controle.CriarNotificacao;
import br.com.agenda.simonebraga.view.controle.GerarAviso;
import br.com.agenda.simonebraga.view.controle.InserirFeriado;
import br.com.agenda.simonebraga.view.controle.RefreshAviso;
import br.com.agenda.simonebraga.view.controle.TelaAlarme;
import br.com.agenda.simonebraga.view.controle.TelaMenuIndex;
import br.com.agenda.simonebraga.view.controle.TelaMenuTemas;
import br.com.agenda.simonebraga.view.controle.ValidarMostrarNotificcoes;
import br.com.agenda.simonebraga.view.dao.BDAgenda;
import br.com.agenda.simonebraga.view.modelo.Agenda;



public class Index extends AppCompatActivity {
    //private  TelaMenuDoDia menuDoDia ;
    private RelativeLayout frame;
    private Button[] bt;
    ScrollView index;
    private AlertDialog alerta;
    private DisplayMetrics displayMetrics;
    private int h;
    private int w;
    private String data = "oi";
    private boolean selecionar = false;
    private boolean tarefaHoje = false;
    boolean retVal = false;
    //se for testar retornar para v31
    protected final int PROXIMA_ATUALIZACAO=1250;
    private long repetir =0;
    private boolean sonLigado=true;

    private final Long HORA_AVISO = 60000L;
    private String[] mesesDoAno ;
   private String[] diaDaSemana ;
   private String[] diasDaSemanax=new String[7];
    //creating Object of AdLoader
    private AdLoader adLoader ;
    FrameLayout frameLayout;

    // simple boolean to check the status of ad
    private boolean adLoaded=false;

    //creating Object of Buttons

   private AdLoader.Builder builder;

    //creating Template View object

    private int[] ultimoDia = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
    private TelaMenuIndex menu ;

    private int corTexto=Color.argb(205,120,120,120);
    private int corFundob=Color.argb(130,25,250,50);


    private   Calendar dataFormatada;

    private Ringtone ringtone;
    private FrameLayout adContainerView;

private  int key=0;
//teste ou home /////////////////////////////////////////////////////////////////////////
   // private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";

    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-1070048556704742/1291285426";

///////////////////////////////////////////////////////////////////////////////////////////

    private ActivityCalendarioWandersonBinding binding;
    private NativeAd nativeAd;

    private AdView adView;
     @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityCalendarioWandersonBinding.inflate(getLayoutInflater());
         View view = binding.getRoot();
         setContentView(view);



         traduzir(Locale.getDefault().getDisplayLanguage());


         final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");




        if(new BDAgenda(Index.this).getVersion ()<PROXIMA_ATUALIZACAO){
            new BDAgenda (Index.this).restauraAlarme ();
            new BDAgenda (Index.this).restaurarAgenda (false);
            new BDAgenda (Index.this).restaurarAgenda (true);
            new BDAgenda (Index.this).atualizarVersion (PROXIMA_ATUALIZACAO);
            Date nova = new Date();
            nova.setTime (nova.getTime ());
            new RefreshAviso ().agendar (nova,Index.this);

        }else {

        }



        displayMetrics = getResources().getDisplayMetrics();
        h = this.displayMetrics.heightPixels;
        w = this.displayMetrics.widthPixels;


        dataFormatada = Calendar.getInstance();
        dataFormatada.setTime(new Date());

        new BDAgenda(Index.this).deleteFeriados();
        new InserirFeriado(Index.this, dataFormatada.get(Calendar.YEAR));
        frame = binding.frame;
        index = binding.indice0;
        index.setBackgroundResource(R.drawable.verde);
        ViewGroup.LayoutParams params = frame.getLayoutParams();

        params.width = (w / 7) * 10;
        params.height = (int) (h / 1.6);

        frame.setLayoutParams(params);


        bt = new Button[43];

        int y = h / 12;
        int x = 0;
        for (int i = 1; i < 43; i++) {
            bt[i] = new Button(this);
            bt[i].setLayoutParams(new FrameLayout.LayoutParams(w / 7, h / 12));
            bt[i].setX(x);
            bt[i].setY(y);
            x += w / 7;
            if (x >= (w / 7) * 7) {
                x = 0;
                y += h / 12;
            }

            frame.addView(bt[i]);
        }

        x = 0;
        Button[] bts = new Button[7];
        for (int i = 0; i < 7; i++) {
            bts[i] = new Button(Index.this);
            bts[i].setLayoutParams(new FrameLayout.LayoutParams(w / 7, h / 12));
            bts[i].setX(x);
            x += w / 7;
            bts[i].setText(diaDaSemana[i]);
            frame.addView(bts[i]);
        }
        criarCalendario(dataFormatada);
         binding.bthome.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                tarefaHoje=false;
                dataFormatada.setTime(new Date());
                criarCalendario(dataFormatada);

            }
        });

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date ();
                String t = formato.format(date);
                lerAlarme(t);
                sino ();

            }
        });


         binding.btanterior.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dataFormatada.set(Calendar.DAY_OF_MONTH, 1);
                dataFormatada.set(Calendar.MONTH, dataFormatada.get(Calendar.MONTH) - 1);
                tarefaHoje=false;

                criarCalendario(dataFormatada);
                Calendar c = Calendar.getInstance ();
                c.setTime (new Date ());
                if(dataFormatada.get (Calendar.YEAR)==c.get (Calendar.YEAR)&&dataFormatada.get (Calendar.MONTH)==c.get (Calendar.MONTH)){
                    atualizar (data);
                }
            }
        });


         binding.btposterior.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dataFormatada.set(Calendar.DAY_OF_MONTH, 1);

                dataFormatada.set(Calendar.MONTH, dataFormatada.get(Calendar.MONTH) + 1);
                tarefaHoje=false;

                criarCalendario(dataFormatada);

                Calendar c = Calendar.getInstance ();
                c.setTime (new Date ());
                if(dataFormatada.get (Calendar.YEAR)==c.get (Calendar.YEAR)&&dataFormatada.get (Calendar.MONTH)==c.get (Calendar.MONTH)){
                    atualizar (data);
                }
            }
        });
        temas(Integer.valueOf(new BDAgenda(Index.this).getTema()));









         //Call the function to initialize AdMob SDK
         MobileAds.initialize(this, new OnInitializationCompleteListener () {
             @Override
             public void onInitializationComplete(InitializationStatus initializationStatus) {
             }
         });
         //get the reference to your FrameLayout
         adContainerView = binding.adViewContainer;

         //Create an AdView and put it into your FrameLayout
         adView = new AdView (this);
         adContainerView.addView(adView);
         adView.setAdUnitId(getString(R.string.home));
         loadBanner();

         builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);

         // Initialize the Mobile Ads SDK.
         MobileAds.initialize(this, new OnInitializationCompleteListener() {
             @Override
             public void onInitializationComplete(InitializationStatus initializationStatus) {}
         });


         menuIndex();
         binding.menuIndex.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
             @Override
             public void onClick(View v) {

                 if(key==0){

                     menu.enviarAlerta().show();

                     key++;
                 }else {
                     menuIndex ();
                 }
             }
         });

         if(CriarNotificacao.ringtone!=null) {

             CriarNotificacao.ringtone.stop ();
             NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext ().NOTIFICATION_SERVICE);
             notificationManager.cancel (CriarNotificacao.id);
             alarme ();
         }


         validarMostrarNotificcoes();

     }
    private boolean validarMostrarNotificcoes() {
        ValidarMostrarNotificcoes v = new ValidarMostrarNotificcoes(Index.this);
        return v.validarMostrarNotificcoes();
    }
    /**
     * Populates a {@link NativeAdView} object with data from a given {@link NativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView the view to be populated
     */
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
                        if (Index.this.nativeAd != null) {
                            Index.this.nativeAd.destroy();
                        }
                        Index.this.nativeAd = nativeAd;
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });


        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {


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



    private AdSize getAdSize() {
        //Determine the screen width to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        //you can also pass your selected width here in dp
        int adWidth = (int) (widthPixels / density);

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @SuppressLint("MissingPermission")
    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size to the ad view.
        adView.setAdSize(adSize);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }



    public boolean isSelecionar() {
        return selecionar;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AlertDialog criarCalendario(Calendar dataFormatada) {
        int dia = 1;




        BDAgenda bdAgenda = new BDAgenda(Index.this);
        final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        Calendar di = Calendar.getInstance();
        Calendar df = Calendar.getInstance();

        List<Integer> diasFeriado = new ArrayList<>();
        List<Integer> diasMarcados = new ArrayList();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, dataFormatada.get(Calendar.YEAR));
        c.set(Calendar.MONTH, dataFormatada.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, dataFormatada.get(Calendar.DAY_OF_MONTH));
        int diaDoMes = c.get(Calendar.DAY_OF_MONTH);
        int diax = c.get(Calendar.DAY_OF_WEEK);
        int diaUmNaSemana = 0;
        final int mesx = c.get(Calendar.MONTH);
        final int anox = c.get(Calendar.YEAR);
        int ultimoDiaDoMes = c.getActualMaximum(c.DAY_OF_MONTH);

        TextView mesT = binding.tvMes;
        mesT.setText(mesesDoAno[mesx] + "\n " + anox);
        if (mesx == 0 || mesx == 11) {
            new BDAgenda(Index.this).deleteFeriados();
            new InserirFeriado(Index.this, anox);

        }

        for (int i = 1; i < 43; i++) {
            bt[i].setEnabled(false);
            bt[i].setVisibility(View.INVISIBLE);
            bt[i].setText("");


        }

        try {



            di.setTime(formato.parse("01-" + (mesx + 1) + "-" + anox));
            df.setTime(formato.parse(( ultimoDiaDoMes) + "-" + (mesx + 1) + "-" + anox));



        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Agenda> listaAgenda = bdAgenda.listar(di.getTimeInMillis(), df.getTimeInMillis());
//PEGA OS AVISOS MARCADOS NO CALENDARIO E ARMASENA NO ARRAY diasMarcados
        for (int i = 0; i < listaAgenda.size(); i++) {
            Calendar dataaux;
            dataaux = Calendar.getInstance();
            dataaux.setTime(listaAgenda.get(i).getData());
            int diaaux = dataaux.get(Calendar.DAY_OF_MONTH);
            diasMarcados.add(diaaux);
        }


        List<Agenda> listaAgendaFeriado = bdAgenda.listarFeriados(di.getTimeInMillis(), df.getTimeInMillis());
//PEGA OS AVISOS MARCADOS NO CALENDARIO E ARMASENA NO ARRAY diasMarcados
        for (int i = 0; i < listaAgendaFeriado.size(); i++) {
            Calendar dataaux;
            dataaux = Calendar.getInstance();
            dataaux.setTime(listaAgendaFeriado.get(i).getData());
            int diaaux = dataaux.get(Calendar.DAY_OF_MONTH);

            diasFeriado.add(diaaux);

        }


/////////////////////////////////////////////////////////////////////////////////////////
// VERIFICA EM QUE DIA DA SEMANA CAI O PRIMEIRO DIA DO MÊS
        boolean passou = false;
        for (int i = diaDoMes; i >0; i--) {
            diax--;

            if (diax == 0) {
                diax = 7;
            }
            passou = true;
        }

        if(passou){
            diaUmNaSemana =diax;
        }else {
        }

///////////////////////////////////////////////////////////////////////////////////////////////
//INSERE OS DIAS MARCADOS NO CALENDARIO MUDANDO A COR CONFORME O TIPO
        Resources r = getResources();
        Drawable d = ResourcesCompat.getDrawable(r, R.drawable.estilo, null);




        for (int i = 1; i < 44; i++) {


            if (dia < c.getActualMaximum(Calendar.DAY_OF_MONTH) + 1) {
                bt[i + diaUmNaSemana  ].setText("" + dia);
                bt[i + diaUmNaSemana  ].setEnabled(true);
                bt[i + diaUmNaSemana  ].setVisibility(View.VISIBLE);
                c.set(Calendar.DAY_OF_MONTH, dia);
                diax = c.get(Calendar.DAY_OF_WEEK);
                bt[i + diaUmNaSemana  ].setBackground(d);


                Calendar dataaux;
                dataaux = Calendar.getInstance();
                dataaux.setTime(new Date());
                if (dataFormatada.get(Calendar.YEAR) == dataaux.get(Calendar.YEAR)) {
                    if (tarefaHoje == false && dataFormatada.get(Calendar.MONTH) == dataaux.get(Calendar.MONTH)) {
                        if (dataFormatada.get(Calendar.DAY_OF_MONTH) == dataaux.get(Calendar.DAY_OF_MONTH)) {
                            Drawable dw;
                            //  Resources r = getResources();
                            dw = ResourcesCompat.getDrawable(r, R.drawable.estilo2, null);
                            bt[dataFormatada.get(Calendar.DAY_OF_MONTH) + diaUmNaSemana].setBackground(dw);
                            bt[dataFormatada.get(Calendar.DAY_OF_MONTH) + diaUmNaSemana].setTextColor(Color.WHITE);

                        }
                    } else if (dataFormatada.get(Calendar.MONTH) == dataaux.get(Calendar.MONTH)) {
                        bt[dataFormatada.get(Calendar.DAY_OF_MONTH) + diaUmNaSemana].setTextColor(Color.WHITE);

                    }
                }



                int diaaux = i + diaUmNaSemana  ;
                Resources res = getResources();
                Drawable dw;



                if (diasMarcados.size() > 0) {
                    for (int g = 0; g < diasMarcados.size(); g++) {

                        if (dia == diasMarcados.get(g)) {

                            if (dataFormatada.get(Calendar.MONTH)==dataaux.get(Calendar.MONTH)&&dataFormatada.get(Calendar.YEAR)==dataaux.get(Calendar.YEAR)) {


                                if (dia < dataFormatada.get(Calendar.DAY_OF_MONTH)) {
                                    dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                                    bt[diaaux].setBackground(dw);
                                } else if (dia == dataFormatada.get(Calendar.DAY_OF_MONTH)) {
                                    tarefaHoje = true;
                                    dw = ResourcesCompat.getDrawable(res, R.drawable.hoje, null);
                                    bt[diaaux].setBackground(dw);

                                } else if (dia > dataFormatada.get(Calendar.DAY_OF_MONTH)) {
                                    dw = ResourcesCompat.getDrawable(res, R.drawable.estilo4, null);
                                    bt[diaaux].setBackground(dw);

                                }


                                for (int gg = 0; gg < diasFeriado.size(); gg++) {
                                    if (dia == diasFeriado.get(gg)) {
                                        if (diasFeriado.get(gg) == diasMarcados.get(g)) {
                                            dw = ResourcesCompat.getDrawable(res, R.drawable.estilo5, null);
                                            bt[diaaux].setBackground(dw);

                                            break;
                                        } else {
                                            dw = ResourcesCompat.getDrawable(res, R.drawable.estilo6, null);
                                            bt[diaaux].setBackground(dw);

                                        }

                                    }
                                }


                            }else  if (dataFormatada.getTimeInMillis()<dataaux.getTimeInMillis()) {

                                String data;
                                if(dataFormatada.get(Calendar.MONTH)<10) {
                                    if(dia<10) {

                                        data = "0" +dia + "-" + "0" + dataFormatada.get(Calendar.MONTH) + "-" + dataFormatada.get(Calendar.YEAR);
                                    }else {
                                        data = dia + "-" + "0" + dataFormatada.get(Calendar.MONTH) + "-" + dataFormatada.get(Calendar.YEAR);

                                    }
                                }else {

                                    if(dia<10) {

                                        data = "0" +dia + "-" + dataFormatada.get(Calendar.MONTH) + "-" + dataFormatada.get(Calendar.YEAR);
                                    }else {
                                        data = dia + "-" +dataFormatada.get(Calendar.MONTH) + "-" + dataFormatada.get(Calendar.YEAR);

                                    }
                                }
                                if(bdAgenda.listarPorData(data)==true) {
                                    dw = ResourcesCompat.getDrawable(res, R.drawable.estilo6, null);
                                    bt[diaaux].setBackground(dw);


                                }else{
                                    dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                                    bt[diaaux].setBackground(dw);


                                }
                                for (int gg = 0; gg < diasFeriado.size(); gg++) {


                                    dw = ResourcesCompat.getDrawable(res, R.drawable.estilo5, null);
                                    bt[diaaux].setBackground(dw);

                                }
                            }
                            else   if (dataFormatada.getTimeInMillis()>dataaux.getTimeInMillis()) {

                                dw = ResourcesCompat.getDrawable(res, R.drawable.estilo4, null);
                                bt[diaaux].setBackground(dw);


                            }





                        }



                    }





                }


                if (diax == 1) {

                    bt[i + diaUmNaSemana  ].setTextColor(Color.argb(255, 255, 0, 0));

                } else if (diax == 7) {

                    bt[i + diaUmNaSemana  ].setTextColor(Color.argb(255, 255, 0, 0));

                } else {
                    bt[i + diaUmNaSemana  ].setTextColor(Color.argb(255, 0, 0, 0));

                }

                for (int gg = 0; gg < diasFeriado.size(); gg++) {
                    if (dia == diasFeriado.get(gg)) {
                        dw = ResourcesCompat.getDrawable(res, R.drawable.estilo5, null);
                        bt[diaaux].setBackground(dw);
                        bt[diaaux].setTextColor(Color.WHITE);

                        for (int g = 0; g < diasMarcados.size(); g++) {


                            if (diasFeriado.get(gg) == diasMarcados.get(g)) {

                                dw = ResourcesCompat.getDrawable(res, R.drawable.estilo6, null);
                                bt[diaaux].setBackground(dw);
                                bt[diaaux].setTextColor(Color.WHITE);

                            }
                        }

                    }
                }

                dia++;
                final int j = i;

//OnClickListener dos botões gerados
                bt[i + diaUmNaSemana  ].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            if (validarMostrarNotificcoes()) {
                                Date date = formato.parse (j + "-" + (mesx + 1) + "-" + anox);
                                String t = formato.format (date);
                                if (!isSelecionar ()) {

                                    //  menuAgenda(t,date);
                                    lerAgenda (t);
                                    sino ();

                                } else {
                                    data = t;

                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });
//OnLongClickListener dos botões gerados

                bt[i + diaUmNaSemana  ].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Date date;
                        try {
                            date = formato.parse(j + "-" + (mesx + 1) + "-" + anox);
                            agendar(date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
///////////////////////////////////////////////////////////////////////////////////
            } else {

                break;
            }





        }
        for (int i =1; i < 43; i++) {

            if(bt[i].getText().equals("")) {
                bt[i].setBackgroundColor(corFundob);
                bt[i].setTextColor(corTexto);

                bt[i].setVisibility(View.VISIBLE);


            }

        }

        tarefaHoje=false;
        Calendar cc = Calendar.getInstance();
        cc.set(Calendar.YEAR, dataFormatada.get(Calendar.YEAR));
        cc.set(Calendar.MONTH, dataFormatada.get(Calendar.MONTH) - 1);
        cc.set(Calendar.DAY_OF_MONTH, dataFormatada.get(Calendar.DAY_OF_MONTH));
        int ultimoDiaDoMesAnterior = cc.getActualMaximum(cc.DAY_OF_MONTH);

        for (int i = 7; i >0; i--) {

            if (bt[i].getText().equals("")) {

                bt[i].setText("" + ultimoDiaDoMesAnterior);
                ultimoDiaDoMesAnterior--;


            }
        }
        int primeiroDiaPosterior = 1;

        for (int i = 30; i <43; i++) {

            if (bt[i].getText().equals("")) {

                bt[i].setText("" + primeiroDiaPosterior);
                primeiroDiaPosterior++;


            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Index.this);

        //       builder.setCustomTitle( texto );
        alerta = builder.create();

        return alerta;

    }



    private int cor = R.drawable.estilo9;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void agendar(final Date date) {
        final BDAgenda bdAgenda = new BDAgenda(Index.this);
        final CadastroMensagem cadastroMensagem;

        if (true) {
            cadastroMensagem = new CadastroMensagem(Index.this);

            Calendar calendar = Calendar.getInstance();

            cadastroMensagem.enviarAlerta().show();
            final int hora = calendar.get(Calendar.HOUR_OF_DAY);
            final int minuto = calendar.get(Calendar.MINUTE);





            cadastroMensagem.getBtcor1 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo2;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });

            cadastroMensagem.getBtcor2 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.hoje;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor3 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo5;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor4 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo6;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });


            cadastroMensagem.getBtcor5 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo44;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor6 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo4;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor7 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo3;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor8 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo33;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });


            cadastroMensagem.getBtcor9 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo88;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor10 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo8;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor11 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo889;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor12 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo884;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });

            cadastroMensagem.getBtcor13 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo9;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor14 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo91;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor15 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo92;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });
            cadastroMensagem.getBtcor16 ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    cor= R.drawable.estilo93;
                    cadastroMensagem.getL1 ().setBackgroundResource (cor);
                }
            });

            cadastroMensagem.getRadio_dia_s_n ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=86400000*2;
                }
            });


            cadastroMensagem.getRadio_dia().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=86400000;
                }
            });
            cadastroMensagem.getRadio_semana ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=604800000;
                }
            });
            cadastroMensagem.getRadio_mes ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=2628000000l;
                }
            });
            cadastroMensagem.getRadio_ano ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=31536000000l;
                }
            });
            cadastroMensagem.getRadio_null ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    repetir=0;
                }
            });


            cadastroMensagem.getSwitchSonOnOff ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    if(cadastroMensagem.getSwitchSonOnOff ().isChecked ()){
                        sonLigado=true;
                        cadastroMensagem.sonOnOff();
                    }else {
                        sonLigado=false;
                        cadastroMensagem.sonOnOff();

                    }
                }
            });
            cadastroMensagem.getBtFechar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cadastroMensagem.fechar();

                    Calendar cc = Calendar.getInstance();
                    cc.setTime(date);
                    Calendar ccc = Calendar.getInstance();
                    ccc.setTime(new Date());

                    cc.set(Calendar.YEAR, ccc.get(Calendar.YEAR));
                    cc.set(Calendar.MONTH, cc.get(Calendar.MONTH));
                    cc.set(Calendar.DAY_OF_MONTH, ccc.get(Calendar.DAY_OF_MONTH));
                    tarefaHoje=false;

                    criarCalendario(cc);

                }
            });

            cadastroMensagem.getBt().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cadastroMensagem.popularDados();


                    Agenda p = new Agenda();


                    SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

                    Calendar dataaux;
                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);

                    dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
                    dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
                    dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
                    dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
                    dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));
                    dataaux.set(Calendar.SECOND, 0);

                    int diax = dataaux.get(Calendar.DAY_OF_MONTH);
                    int mesx = dataaux.get(Calendar.MONTH);
                    int anox = dataaux.get(Calendar.YEAR);
                    String[] horarioMarcado = cadastroMensagem.getHora().split(":");
                    int horax =  Integer.valueOf(horarioMarcado[0]);
                    int minutox=0;
                    try {
                        minutox = Integer.valueOf (horarioMarcado[1]);
                    }catch (Exception e){

                    }

                    if(horax>23){
                        horax=0;
                    }
                    String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;
                    try {

                        dataaux.setTime(formatar.parse(dataMarcada));

                        p.setId((dataaux.getTimeInMillis()-604800000)/1000);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    p.setNomeP(dataMarcada);
                    p.setIdCliente(0);

                    p.setData(date);
                    p.setMensagem(cadastroMensagem.getMensagem());
                    p.setHorario(cadastroMensagem.getHora());
                    p.setFeriado("normal");
                    p.setDia (diax);
                    p.setMes (mesx+1);
                    p.setAno (anox);
                    cadastroMensagem.fechar();
                    bdAgenda.inserir(p);
                    bdAgenda.insertCor ((int) p.getId (),cor,repetir);

                    Toast.makeText(Index.this, " Salvo com sucesso!", Toast.LENGTH_LONG).show();

                    //   menuDoDia.fechar();

                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);
                    dataaux.set(Calendar.DAY_OF_MONTH, 2);

                    //   criarCalendario(dataaux);


                    try {
                        dataaux.setTime(formatar.parse(dataMarcada));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int minutosAviso=0;
                    try {
                        minutosAviso = Integer.parseInt(cadastroMensagem.getEdHorasAviso().getText().toString());
                    } catch (Exception e) {
                        minutosAviso=0;
                    }
                    Date aux = dataaux.getTime();
                    long data5 = aux.getTime() - (HORA_AVISO * minutosAviso);
                    Date dataa = aux;
                    dataa.setTime(data5);
                    //  if(dataa.getTime ()>new Date ().getTime ()) {
                    //  String diasDaSemana="";
                    GerarAviso aviso = new GerarAviso (Index.this);
                    aviso.AgendarNotificacao (dataa, dataMarcada, cadastroMensagem.getMensagem (), (int) p.getId (), repetir,sonLigado);

                    //   }

                    Calendar cc = Calendar.getInstance();
                    cc.setTime(dataa);
                    Calendar ccc = Calendar.getInstance();
                    ccc.setTime(new Date());

                    cc.set(Calendar.YEAR, ccc.get(Calendar.YEAR));
                    cc.set(Calendar.DAY_OF_MONTH, ccc.get(Calendar.DAY_OF_MONTH));
                    if(ccc.get (Calendar.MONTH)==cc.get (Calendar.MONTH)){
                        dataaux.set(Calendar.DAY_OF_MONTH,ccc.get (Calendar.DAY_OF_MONTH) );


                    }
                    criarCalendario(dataaux);
                    final SimpleDateFormat formatox= new SimpleDateFormat("dd-MM-yyyy");
                    String datafinal = formatox.format (date);
                    lerAgenda(datafinal);
                }



            });




        } else {
            Toast.makeText(Index.this, " A data escolhida não pode ser anterior ou igual a data atual!", Toast.LENGTH_SHORT).show();

        }

    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void lerAgenda(String data) {
//
//        if(menuDoDia!=null){
//            menuDoDia.fechar();}
        final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;

        try {
            date = formato.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ss = Calendar.getInstance();
        ss.setTime(date);
        Calendar sss = Calendar.getInstance();
        sss.setTime(new Date());
        String datass=ss.get (Calendar.DAY_OF_MONTH)+"-"+(sss.get (Calendar.MONTH)+1)+"-"+sss.get (Calendar.YEAR);
        final AvisoAgenda avisoAgenda = new AvisoAgenda(Index.this, datass,data);
        avisoAgenda.getBtFechar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoAgenda.fechar();

            }
        });

        String finalData = data;
        avisoAgenda.getBtAgendar ().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    agendar (formato.parse(finalData));
                    avisoAgenda.fechar();

                } catch (ParseException e) {
                    e.printStackTrace ();
                }
            }
        });


        Calendar cc = Calendar.getInstance();
        cc.setTime(date);
        Calendar ccc = Calendar.getInstance();
        ccc.setTime(new Date());

        // cc.set(Calendar.YEAR, ccc.get(Calendar.YEAR));
        cc.set(Calendar.MONTH, cc.get(Calendar.MONTH));
        cc.set(Calendar.DAY_OF_MONTH, ccc.get(Calendar.DAY_OF_MONTH));

//        if(ccc.get (Calendar.MONTH)==cc.get (Calendar.MONTH)){
        data=formato.format (cc.getTime ());
//        }
        //   if(avisoAgenda.getListaR ().size ()>0) {

        atualizar(data);
        avisoAgenda.enviarAlerta ().show ();



        //     }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendarh = Calendar.getInstance();
        calendarh.setTime(new Date());


        calendar.set(Calendar.DAY_OF_MONTH, calendarh.get(Calendar.DAY_OF_MONTH));
        tarefaHoje=false;


        dataFormatada.set(Calendar.DAY_OF_MONTH, 1);

        tarefaHoje=false;

        criarCalendario(dataFormatada);


        Calendar c = Calendar.getInstance ();
        c.setTime (new Date ());
        if(dataFormatada.get (Calendar.YEAR)==c.get (Calendar.YEAR)&&dataFormatada.get (Calendar.MONTH)==c.get (Calendar.MONTH)){
            atualizar (data);
        }




    }




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void lerAlarme(String data) {
        if (validarMostrarNotificcoes()) {
            final SimpleDateFormat formato = new SimpleDateFormat ("dd-MM-yyyy");

            Date date = null;

            try {
                date = formato.parse (data);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            final AvisoAlarme avisoAgenda = new AvisoAlarme (Index.this, data);
            avisoAgenda.getBtFechar ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    avisoAgenda.fechar ();

                }
            });

            avisoAgenda.getBtMais ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    despertar (new Date ());
                    avisoAgenda.fechar ();

                }
            });

            avisoAgenda.enviarAlerta ().show ();
            Calendar calendar = Calendar.getInstance ();
            calendar.setTime (date);
            Calendar calendarh = Calendar.getInstance ();
            calendarh.setTime (new Date ());


            calendar.set (Calendar.DAY_OF_MONTH, calendarh.get (Calendar.DAY_OF_MONTH));
            tarefaHoje = false;
            criarCalendario (calendar);
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void menuIndex() {

        menu = new TelaMenuIndex(Index.this);
        if(key>0) {
            menu.enviarAlerta ().show ();
        }


        refreshAd ();

        menu.getTvDespertador ().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date ();
                String t = formato.format(date);
                menu.fechar ();
                key=0;
                menu=null;
                menuIndex ();
                lerAlarme(t);            }
        });
        menu.getTvTemas ().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.fechar ();
                key=0;
                menu=null;
                menuIndex ();

                menuTemas ();

            }
        });


        menu.getTvSons ().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to select Ringtone.
                escolherToque();

            }
        });



        menu.getBtFechar ().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.fechar ();
                menu=null;
                key=0;
                menuIndex ();

            }
        });





    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void menuTemas() {

        final TelaMenuTemas menuTemas = new TelaMenuTemas(Index.this);
        menuTemas.enviarAlerta().show();
        menuTemas.getTema1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temas(1);
                new BDAgenda(Index.this).atualizarTema(1);
            }
        });
        menuTemas.getTema2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(2);
                new BDAgenda(Index.this).atualizarTema(2);

            }
        });

        menuTemas.getTema3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(3);
                new BDAgenda(Index.this).atualizarTema(3);

            }
        });

        menuTemas.getTema4().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(4);
                new BDAgenda(Index.this).atualizarTema(4);

            }
        });

        menuTemas.getTema5().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(5);
                new BDAgenda(Index.this).atualizarTema(5);

            }
        });

        menuTemas.getTema6().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(6);
                new BDAgenda(Index.this).atualizarTema(6);

            }
        });

        menuTemas.getTema7().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(7);
                new BDAgenda(Index.this).atualizarTema(7);

            }
        });

        menuTemas.getTema8().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(8);
                new BDAgenda(Index.this).atualizarTema(8);

            }
        });

        menuTemas.getTema9().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(9);
                new BDAgenda(Index.this).atualizarTema(9);

            }
        });

        menuTemas.getTema10().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temas(10);
                new BDAgenda(Index.this).atualizarTema(10);

            }
        });


        menuTemas.getBtFechar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuTemas.fechar();

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void temas(int tema) {

        if(tema==1){
            index.setBackgroundResource(R.drawable.preto);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,25,50,50);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }else  if(tema==2){

            index.setBackgroundResource(R.drawable.rosa);

            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,250,50,25);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }else  if(tema==3){

            index.setBackgroundResource(R.drawable.verde);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,25,250,250);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }
        else  if(tema==4){

            index.setBackgroundResource(R.drawable.azul);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,5,55,255);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }
        else  if(tema==5){

            index.setBackgroundResource(R.drawable.amarelo);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,205,200,15);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }else  if(tema==6){
            index.setBackgroundResource(R.drawable.cor_branca);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,25,50,50);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }else  if(tema==7){

            index.setBackgroundResource(R.drawable.cor_vermelho);

            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,250,50,25);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }else  if(tema==8){

            index.setBackgroundResource(R.drawable.cor_verde);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,25,250,250);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }
        else  if(tema==9){

            index.setBackgroundResource(R.drawable.cor_azul);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,5,55,255);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }
        else  if(tema==10){

            index.setBackgroundResource(R.drawable.cor_amarelo);
            corTexto=Color.argb(205,255,255,255);
            corFundob=Color.argb(130,205,200,15);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            tarefaHoje=false;
            criarCalendario(calendar);
        }











    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        alarme();
        finishAffinity();

    }

    public void alarme() {

        if(CriarNotificacao.ringtone!=null) {

            final TelaAlarme telaAlarme = new TelaAlarme (Index.this);
            String ms =new  BDAgenda(Index.this).agendamento (CriarNotificacao.id).getMensagem ();
            telaAlarme.getAlarme ().setText (ms);
            telaAlarme.enviarAlerta ().show ();
             frameLayout = telaAlarme.getFrameLayout ();

            refreshAd ();

            telaAlarme.getAlarme ().setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    telaAlarme.fechar ();

                }
            });


        }

    }
    private void sino(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(getApplicationContext ().NOTIFICATION_SERVICE);

        if(CriarNotificacao.ringtone!=null) {

            CriarNotificacao.ringtone.stop ();
            notificationManager.cancel (CriarNotificacao.id);


        }
        Date mais = new Date ();
        mais.setTime (mais.getTime () +30000);
        new RefreshAviso ().desligarAlarme (mais, Index.this, 8, new Date ().getTime (),false);


    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void atualizar(String data) {
//
        final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
//
//        final TelaAtualizar telaA= new TelaAtualizar (Index.this);
//        telaA.enviarAlerta ().show ();
//        telaA.getAtualizar ().setOnClickListener (new View.OnClickListener () {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onClick (View view) {
//                Calendar calendar = Calendar.getInstance();
//                try {
//                    calendar.setTime(formato.parse (data));
//                } catch (ParseException e) {
//                    e.printStackTrace ();
//                }
//                criarCalendario(calendar);
//                telaA.fechar ();
//
//            }
//        });


        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formato.parse (data));
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        criarCalendario(calendar);


    }


    float posXP =0;
    float posXPP =0;

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {



        float posX = this.w * 0.01f;
        float posY = this.h *0.01f;


      ///  sino ();

        ///////////AQUI GERENCIA OS TOQUES NA TELA//////////////////////////////////////////////////////
        if (event.getPointerCount() == 1 && event.getPointerId( 0 ) == 0 ) {

            if (event.getAction () == MotionEvent.ACTION_DOWN) {

                posXP=event.getX ();
            }else if (event.getAction () == MotionEvent.ACTION_MOVE) {

                posXPP=event.getX ();
                if(posXPP>posXP+100) {
                    dataFormatada.set (Calendar.MONTH, dataFormatada.get (Calendar.MONTH) + 1);
                    tarefaHoje = false;

                    criarCalendario (dataFormatada);
                }else if(posXPP<posXP-100) {
                    dataFormatada.set (Calendar.MONTH, dataFormatada.get (Calendar.MONTH) - 1);
                    tarefaHoje = false;

                    criarCalendario (dataFormatada);
                }
            }

        }






        return super.onTouchEvent( event );
    }



    private void btsemana(){

        CadastroAlarme cadastroAlarme = new CadastroAlarme(Index.this);




    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void despertar(final Date date) {
        final BDAgenda bdAgenda = new BDAgenda(Index.this);
        final CadastroAlarme cadastroAlarme;

        int[] diasDaSemana=new int[7];

        diasDaSemana[0]= -1;
        diasDaSemana[1]= -1;
        diasDaSemana[2]= -1;
        diasDaSemana[3]= -1;
        diasDaSemana[4]= -1;
        diasDaSemana[5]= -1;
        diasDaSemana[6]= -1;


        repetir=86400000;

        if (true) {
            cadastroAlarme = new CadastroAlarme(Index.this);

            Calendar calendar = Calendar.getInstance();

            cadastroAlarme.enviarAlerta().show();
            final int hora = calendar.get(Calendar.HOUR_OF_DAY);
            final int minuto = calendar.get(Calendar.MINUTE);


            cadastroAlarme.getBtDO ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[0]== -1){
                        diasDaSemana[0]=1;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroAlarme.getBtDO ().setBackground (dw);
                    }else {
                        diasDaSemana[0]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtDO ().setBackground (dw);
                    }

                }
            });




            cadastroAlarme.getBtSE ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[1]== -1){
                        diasDaSemana[1]=2;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroAlarme.getBtSE ().setBackground (dw);
                    }else {
                        diasDaSemana[1]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtSE ().setBackground (dw);
                    }

                }
            });


            cadastroAlarme.getBtTE ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[2]== -1){
                        diasDaSemana[2]=3;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroAlarme.getBtTE ().setBackground (dw);
                    }else {
                        diasDaSemana[2]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtTE ().setBackground (dw);
                    }

                }
            });


            cadastroAlarme.getBtQA ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[3]== -1){
                        diasDaSemana[3]=4;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);
                        repetir=86400000;

                        cadastroAlarme.getBtQA ().setBackground (dw);
                    }else {
                        diasDaSemana[3]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtQA ().setBackground (dw);
                    }

                }
            });

            cadastroAlarme.getBtQI ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[4]== -1){
                        diasDaSemana[4]=5;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroAlarme.getBtQI ().setBackground (dw);
                    }else {
                        diasDaSemana[4]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtQI ().setBackground (dw);
                    }

                }
            });


            cadastroAlarme.getBtSX ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[5]== -1){
                        diasDaSemana[5]=6;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroAlarme.getBtSX ().setBackground (dw);
                    }else {
                        diasDaSemana[5]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);

                        cadastroAlarme.getBtSX ().setBackground (dw);
                    }

                }
            });



            cadastroAlarme.getBtSA ().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    Resources res = getResources();

                    if(diasDaSemana[6]== -1){
                        diasDaSemana[6]=7;
                        repetir=86400000;

                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo3, null);

                        cadastroAlarme.getBtSA ().setBackground (dw);
                    }else {
                        diasDaSemana[6]= -1;
                        Drawable dw = ResourcesCompat.getDrawable(res, R.drawable.estilo, null);
                        cadastroAlarme.getBtSA ().setBackground (dw);
                    }

                }
            });



            cadastroAlarme.getBtFechar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cadastroAlarme.fechar();

                    Calendar cc = Calendar.getInstance();
                    cc.setTime(date);
                    Calendar ccc = Calendar.getInstance();
                    ccc.setTime(new Date());

                    cc.set(Calendar.YEAR, ccc.get(Calendar.YEAR));
                    cc.set(Calendar.MONTH, cc.get(Calendar.MONTH));
                    cc.set(Calendar.DAY_OF_MONTH, ccc.get(Calendar.DAY_OF_MONTH));
                    tarefaHoje=false;

                    criarCalendario(cc);

                }
            });

            cadastroAlarme.getBt().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cadastroAlarme.popularDados();


                    Agenda p = new Agenda();


                    SimpleDateFormat formatar = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

                    Calendar dataaux;
                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);

                    dataaux.set(Calendar.YEAR, dataaux.get(Calendar.YEAR));
                    dataaux.set(Calendar.MONTH, dataaux.get(Calendar.MONTH));
                    dataaux.set(Calendar.DAY_OF_MONTH, dataaux.get(Calendar.DAY_OF_MONTH));
                    dataaux.set(Calendar.HOUR_OF_DAY, dataaux.get(Calendar.HOUR_OF_DAY));
                    dataaux.set(Calendar.MINUTE, dataaux.get(Calendar.MINUTE));
                    dataaux.set(Calendar.SECOND, 0);

                    int diax = dataaux.get(Calendar.DAY_OF_MONTH);
                    int mesx = dataaux.get(Calendar.MONTH);
                    int anox = dataaux.get(Calendar.YEAR);
                    String[] horarioMarcado = cadastroAlarme.getHora().split(":");
                    int horax=0;
                    try {
                        horax = Integer.valueOf (horarioMarcado[0]);
                    }catch (Exception e){

                    }                    int minutox=0;
                    try {
                        minutox = Integer.valueOf (horarioMarcado[1]);
                    }catch (Exception e){

                    }

                    if(horax>23){
                        horax=0;
                    }
                    String dataMarcada = diax + "-" + (mesx + 1) + "-" + anox + "_" + horax + ":" + minutox;
                    try {

                        dataaux.setTime(formatar.parse(dataMarcada));

                        p.setId((dataaux.getTimeInMillis()-604800000)/1000);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    p.setNomeP(dataMarcada);
                    p.setIdCliente(0);

                    p.setData(date);
                    String alarme = "";

                    for(int k=0;k<diasDaSemana.length;k++){
                        if (diasDaSemana[k] > -1) {
                            alarme += diasDaSemanax[k];

                        }else {
                            alarme += "- ";

                        }
                    }

                    p.setHorario(cadastroAlarme.getHora());

                    p.setFeriado(alarme);


                    Toast.makeText(Index.this, " Salvo com sucesso!", Toast.LENGTH_LONG).show();

                    dataaux = Calendar.getInstance();
                    dataaux.setTime(date);
                    dataaux.set(Calendar.DAY_OF_MONTH, 2);
                    criarCalendario(dataaux);


                    try {
                        dataaux.setTime(formatar.parse(dataMarcada));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int minutosAviso=0;
                    try {
                        minutosAviso = Integer.parseInt(cadastroAlarme.getEdHorasAviso().getText().toString());
                    } catch (Exception e) {
                        minutosAviso=0;
                    }
                    Date aux = dataaux.getTime();
                    long data5 = aux.getTime() - (HORA_AVISO * minutosAviso);
                    Date dataa = aux;
                    dataa.setTime(data5);
                    //  if(dataa.getTime ()>new Date ().getTime ()) {
                    //  String diasDaSemana="";
                    GerarAviso aviso = new GerarAviso (Index.this);
                    aviso.AgendarAlarme (dataa, cadastroAlarme.getMensagem (), cadastroAlarme.getMensagem (), (int) p.getId (), repetir, diasDaSemana);

                    //   }

                    Calendar cc = Calendar.getInstance();
                    cc.setTime(dataa);
                    Calendar ccc = Calendar.getInstance();
                    ccc.setTime(new Date());

                    cc.set(Calendar.YEAR, ccc.get(Calendar.YEAR));
                    cc.set(Calendar.DAY_OF_MONTH, ccc.get(Calendar.DAY_OF_MONTH));

                    criarCalendario(cc);
                    final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                    String date="";
                    date = formato.format (new Date ());

                    p.setMensagem(cadastroAlarme.getMensagem());


                    cadastroAlarme.fechar();
                    bdAgenda.inserir(p);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        lerAlarme (date);
                    }
                }



            });

        } else {
            Toast.makeText(Index.this, " A data escolhida não pode ser anterior ou igual a data atual!", Toast.LENGTH_SHORT).show();

        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (checkSystemWritePermission()) {
                new BDAgenda(Index.this).atualizarSon(uri.toString(), 2);
                Toast.makeText(Index.this, "TOQUE SELECIONADO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Index.this, "PERMISSÃO PARA ALTERAR TOQUE NEGADA", Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void escolherToque(){

        if(!checkSystemWritePermission()){
            openAndroidPermissionsMenu();

        }
        else {
            final Uri currentTone =
                    RingtoneManager.getActualDefaultRingtoneUri (Index.this,
                            RingtoneManager.TYPE_ALARM);
            new BDAgenda (Index.this).atualizarSon (currentTone.toString (), 3);

            Intent intent = new Intent (RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra (RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
            intent.putExtra (RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecione um toque");
            intent.putExtra (RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
            intent.putExtra (RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
            intent.putExtra (RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            startActivityForResult (intent, 999);
        }
    }
    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);

        }else {
            retVal=true;
        }
        return retVal;
    }
    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    private void traduzir(String local){

        switch(local){

            case "português":

                mesesDoAno = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "" +
                        "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

                diaDaSemana = new String[]{"D", "S", "T", "Q", "Q", "S", "S"};

                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "S ";
                diasDaSemanax[2]= "T ";
                diasDaSemanax[3]= "Q ";
                diasDaSemanax[4]= "Q ";
                diasDaSemanax[5]= "S ";
                diasDaSemanax[6]= "S ";

                break;
            case "español":

                mesesDoAno = new String[]{
                        getString (R.string.es_janeiro),
                        getString (R.string.es_fevereiro),
                        getString (R.string.es_marco),
                        getString (R.string.es_abril),
                        getString (R.string.es_maio),
                        getString (R.string.es_junho),
                        getString (R.string.es_julho),
                        getString (R.string.es_agosto),
                        getString (R.string.es_sentembro),
                        getString (R.string.es_outubro),
                        getString (R.string.es_novembro),
                        getString (R.string.es_dezembro)};
                diaDaSemana = new String[]{
                        getString (R.string.es_D),
                        getString (R.string.es_S),
                        getString (R.string.es_T),
                        getString (R.string.es_Q),
                        getString (R.string.es_QI),
                        getString (R.string.es_SE),
                        getString (R.string.es_SA)
                };

                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "L ";
                diasDaSemanax[2]= "M ";
                diasDaSemanax[3]= "M ";
                diasDaSemanax[4]= "J ";
                diasDaSemanax[5]= "V ";
                diasDaSemanax[6]= "S ";

                break;

            case "italiano":

                mesesDoAno = new String[]{
                        getString (R.string.it_janeiro),
                        getString (R.string.it_fevereiro),
                        getString (R.string.it_marco),
                        getString (R.string.it_abril),
                        getString (R.string.it_maio),
                        getString (R.string.it_junho),
                        getString (R.string.it_julho),
                        getString (R.string.it_agosto),
                        getString (R.string.it_sentembro),
                        getString (R.string.it_outubro),
                        getString (R.string.it_novembro),
                        getString (R.string.it_dezembro)};
                diaDaSemana = new String[]{
                        getString (R.string.it_D),
                        getString (R.string.it_S),
                        getString (R.string.it_T),
                        getString (R.string.it_Q),
                        getString (R.string.it_QI),
                        getString (R.string.it_SE),
                        getString (R.string.it_SA)
                };

                diasDaSemanax[0]= "D ";
                diasDaSemanax[1]= "L ";
                diasDaSemanax[2]= "M ";
                diasDaSemanax[3]= "M ";
                diasDaSemanax[4]= "G ";
                diasDaSemanax[5]= "V ";
                diasDaSemanax[6]= "S ";

                break;
            default:



                mesesDoAno = new String[]{"January", "February", "March", "April", "May", "" +
                        "June", "July", "August", "September", "October", "November", "December"};

                diaDaSemana = new String[]{"S", "M", "T", "W", "T", "F", "S"};



                diasDaSemanax[0]= "S ";
                diasDaSemanax[1]= "M ";
                diasDaSemanax[2]= "T ";
                diasDaSemanax[3]= "W ";
                diasDaSemanax[4]= "T ";
                diasDaSemanax[5]= "F ";
                diasDaSemanax[6]= "S ";
                break;

        }

    }



    public static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

}
