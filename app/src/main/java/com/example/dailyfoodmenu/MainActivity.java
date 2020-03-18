package com.example.dailyfoodmenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyfoodmenu.service.IServices;
import com.example.dailyfoodmenu.service.SampleServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Çıkmak istediğinizden emin misiniz?");
        builder.setCancelable(true);
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    Context context = this;
    DatePicker dpFood;
    TextView txtDate;
    RecyclerView lstFoodList;
    Button btnAllList;
    TextView txtFooterCalorie;
    TextView txtFooterDetail;

    private static SimpleDateFormat dfPattern = new SimpleDateFormat("dd.MM.yyyy");
    private static String sampleFoodListJson = "[{\"foodDate\":\"09.03.2020\",\"calorie\":1.1,\"foodDescription\":\"Mercimek Çorba\"},{\"foodDate\":\"09.03.2020\",\"calorie\":1.2,\"foodDescription\":\"Etli Kuru Fasulye\"},{\"foodDate\":\"09.03.2020\",\"calorie\":1.3,\"foodDescription\":\"Pirinç Pilavı\"},{\"foodDate\":\"09.03.2020\",\"calorie\":1.4,\"foodDescription\":\"Turşu\"},{\"foodDate\":\"09.03.2020\",\"calorie\":1.5,\"foodDescription\":\"Laz Böreği\"},{\"foodDate\":\"10.03.2020\",\"calorie\":1.1,\"foodDescription\":\"Şehriye Çorba\"},{\"foodDate\":\"10.03.2020\",\"calorie\":1.2,\"foodDescription\":\"Mantarlı Tavuk Biftek\"},{\"foodDate\":\"10.03.2020\",\"calorie\":1.3,\"foodDescription\":\"Makarna Salata\"},{\"foodDate\":\"10.03.2020\",\"calorie\":1.4,\"foodDescription\":\"Ayran\"},{\"foodDate\":\"10.03.2020\",\"calorie\":1.5,\"foodDescription\":\"Meyve\"},{\"foodDate\":\"11.03.2020\",\"calorie\":1.1,\"foodDescription\":\"Tavuk Suyu Çorba\"},{\"foodDate\":\"11.03.2020\",\"calorie\":1.2,\"foodDescription\":\"Yoğurtlu Köfte\"},{\"foodDate\":\"11.03.2020\",\"calorie\":1.3,\"foodDescription\":\"Barbunya Pilaki\"},{\"foodDate\":\"11.03.2020\",\"calorie\":1.4,\"foodDescription\":\"Seçmeli İçecek\"},{\"foodDate\":\"11.03.2020\",\"calorie\":1.5,\"foodDescription\":\"Trileçe/Sütlaç\"},{\"foodDate\":\"12.03.2020\",\"calorie\":1.1,\"foodDescription\":\"Yeşil Mercimek Çorba\"},{\"foodDate\":\"12.03.2020\",\"calorie\":1.2,\"foodDescription\":\"Orman Kebabı\"},{\"foodDate\":\"12.03.2020\",\"calorie\":1.3,\"foodDescription\":\"Peynirli Su Böreği\"},{\"foodDate\":\"12.03.2020\",\"calorie\":1.4,\"foodDescription\":\"Pembe Sultan\"},{\"foodDate\":\"12.03.2020\",\"calorie\":1.5,\"foodDescription\":\"Komposto\"},{\"foodDate\":\"13.03.2020\",\"calorie\":1.1,\"foodDescription\":\"Ezogelin Çorba\"},{\"foodDate\":\"13.03.2020\",\"calorie\":1.2,\"foodDescription\":\"Et Döner-Garnitür\"},{\"foodDate\":\"13.03.2020\",\"calorie\":1.3,\"foodDescription\":\"Pirinç Pilavı\"},{\"foodDate\":\"13.03.2020\",\"calorie\":1.4,\"foodDescription\":\"Ayran\"},{\"foodDate\":\"13.03.2020\",\"calorie\":1.5,\"foodDescription\":\"Sakızlı Muhallebi/Revani\"}]";
    private static List<FoodModel> foodList = new ArrayList<FoodModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dpFood = findViewById(R.id.dpFood);
        txtDate = findViewById(R.id.txtDate);
        btnAllList = findViewById(R.id.btnAllList);
        txtFooterCalorie = findViewById(R.id.txtFooterCalorie);
//        txtFooterDetail = findViewById(R.id.txtFooterFoodToplam);
        lstFoodList = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        lstFoodList.setLayoutManager(linearLayoutManager);
        lstFoodList.setHasFixedSize(true);

        /*Su anki tarih bilgisi component ve filter a ekleniyor*/
        Calendar thisDay = Calendar.getInstance();
        int year = thisDay.get(Calendar.YEAR);
        int month = thisDay.get(Calendar.MONTH);
        int day = thisDay.get(Calendar.DAY_OF_MONTH);
        Date currentDate = BindDateParams(year, month, day);
        getSampleFoodListAsAService(true, currentDate, currentDate, true);

        dpFood.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date currentDate = BindDateParams(year, monthOfYear, dayOfMonth);
                bindListView(true, currentDate, currentDate);            }
        });

        btnAllList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar thisDay = Calendar.getInstance();
                int year = thisDay.get(Calendar.YEAR);
                int month = thisDay.get(Calendar.MONTH);
                int day = thisDay.get(Calendar.DAY_OF_MONTH);
                Date currentDate = BindDateParams(year, month, day);
                getSampleFoodListAsAService(true, currentDate, currentDate, true);
            }
        });

//        lstFoodList.setFocusableInTouchMode(true);
//        lstFoodList.requestFocus();
//        lstFoodList.setClickable(true);
//        lstFoodList.setFocusable(true);
//        lstFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//////                FoodModel selectedItem = (FoodModel)parent.getItemAtPosition(position);
////                View selectedItem2 = (View)parent.getSelectedView();
////                TextView txtFoodDate = (TextView) view.findViewById(R.id.txtCalorie);
////                TextView txtFoodDetail = (TextView) view.findViewById(R.id.txtFoodDetail);
////                try {
////                    Date tarih = dfPattern.parse(txtFoodDate.getText().toString());
////                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
////                    dialog.setTitle(FoodAppUtil.convertDateToDetailStr(tarih))
////                            .setMessage(txtFoodDetail.getText())
////                            .setCancelable(false)
////                            .setPositiveButton("Tamam",null)
////                            .show();
////                }
////                catch (Exception e){
////                    Toast.makeText(context,"Tarih çevrimi yapılırken hata oluştu2.",Toast.LENGTH_LONG);
////                }
//            }
//        });
    }

    private Date BindDateParams(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        String newDateStr = FoodAppUtil.convertDateToDetailStr(calendar.getTime());
        txtDate.setText(newDateStr);
        return  calendar.getTime();
    }

    private void bindListView(boolean pIsFilter, Date pStartDate, Date pEndDate) {
        if(foodList.size() < 1)
            foodList = getSampleFoodListJson();

        List<FoodModel> filteredList = new ArrayList<>();
        if(pIsFilter)
            filteredList = findFoodListOld(pStartDate,pEndDate,foodList);
//        CustomAdapter adapter = new CustomAdapter(context, filteredList);
//        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(filteredList, context);
//        List<FoodModel> finalFilteredList = filteredList;
        List<FoodModel> finalFilteredList = filteredList;
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(filteredList, context, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                FoodModel food = finalFilteredList.get(position);
                String message = "Seçilen yemek adi : " + food.getFoodDescription()
                        + "\nYemeğin kalorisi : " + " " + food.getCalorie()
                        + "\nUzman notu : Lezzetli bir yemektir." ;
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                showModalDialog("Bilgi", message);
            }
        });
        lstFoodList.setAdapter(adapter);

        float toplam = ((float) filteredList.stream().mapToDouble(m -> m.getCalorie()).sum());
        //        String formattedSum = String.valueOf(String.format("%.2f", toplam));
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("#.##",otherSymbols);
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        String formattedSum = df.format(toplam);
        txtFooterCalorie.setText(formattedSum);
    }

    private static List<FoodModel> findFoodList(Date pStartDate, Date pEndDate, List<FoodModel> pDataList)
    {
        Calendar beforeDay = Calendar.getInstance();
        beforeDay.setTime(pStartDate);
        beforeDay.add(Calendar.DATE, -1);

        Calendar afterDay = Calendar.getInstance();
        afterDay.setTime(pEndDate);
        afterDay.add(Calendar.DATE, 1);

        List<FoodModel> resultSortedFoodList = new ArrayList<FoodModel>();
        resultSortedFoodList = pDataList.stream()
                .filter(dates ->
                        dates.getFoodDate().after(beforeDay.getTime()) && dates.getFoodDate().before(afterDay.getTime()))
                .collect(Collectors.toList());
        resultSortedFoodList.sort((mFirst, mLast) -> mFirst.getFoodDate().compareTo(mLast.getFoodDate()));
        if(resultSortedFoodList != null && resultSortedFoodList.size() > 0) {
//            float toplam = ((float) resultSortedFoodList.stream().mapToDouble(m -> m.getCalorie()).sum());
//            resultSortedFoodList.add(new FoodModel(null, toplam, "<- TOPLAM"));
            resultSortedFoodList.forEach(p -> System.out.println(dfPattern.format(p.getFoodDate()) + " - " + p.getFoodDescription() + " - " + p.getCalorie()));
        }
        else
            System.out.println(dfPattern.format(pStartDate) + " - " +dfPattern.format(pStartDate) +" tarihleri arasında kayıtlı herhangi bir veri bulunamadı!");
         return resultSortedFoodList;
    }

    private List<FoodModel> findFoodListOld(Date pStartDate, Date pEndDate, List<FoodModel> pDataList) {
        List<FoodModel> resultSortedFoodList = pDataList.stream()
                .filter(dates -> (FoodAppUtil.getYear(dates.getFoodDate()) >= FoodAppUtil.getYear(pStartDate)
                                && FoodAppUtil.getMonth(dates.getFoodDate()) >= FoodAppUtil.getMonth(pStartDate)
                                && FoodAppUtil.getDay(dates.getFoodDate())>= FoodAppUtil.getDay(pStartDate)
                        )
                                && (FoodAppUtil.getYear(dates.getFoodDate()) <= FoodAppUtil.getYear(pEndDate)
                                && FoodAppUtil.getMonth(dates.getFoodDate()) <= FoodAppUtil.getMonth(pEndDate)
                                && FoodAppUtil.getDay(dates.getFoodDate())<= FoodAppUtil.getDay(pEndDate))
                )
                .collect(Collectors.toList());
        resultSortedFoodList.sort((mFirst, mLast) -> mFirst.getFoodDate().compareTo(mLast.getFoodDate()));
        if(resultSortedFoodList != null && resultSortedFoodList.size() > 0)
            resultSortedFoodList.forEach(p->System.out.println(dfPattern.format(p.getFoodDate()) + " - "+ p.getFoodDescription()));
        else
            System.out.println(dfPattern.format(pStartDate) + " - " +dfPattern.format(pStartDate) +" tarihleri arasında kayıtlı herhangi bir veri bulunamadı!");
        System.out.println("---------------------------------------filter end------------------------------------------------------------------------------");
        return resultSortedFoodList;
    }

    private static List<FoodModel> findAndPrintFoodList(String pStartDate, String pEndDate, List<FoodModel> pDataList) {
        return findAndPrintFoodList( dfPattern.format(pStartDate), dfPattern.format(pStartDate), pDataList);
    }

    private static List<FoodModel> findAndPrintFoodList(Date pStartDate, Date pEndDate, List<FoodModel> pDataList) {
        System.out.println("---------------------------------------filter start------------------------------------------------------------------------------");
        List<FoodModel> resultSortedFoodList = pDataList.stream()
                .filter(dates -> (dates.getFoodDate().after(pStartDate) || dates.getFoodDate().equals(pStartDate) || dfPattern.format(dates.getFoodDate()).equals(dfPattern.format(pStartDate)))
                        && (dates.getFoodDate().before(pEndDate) || dates.getFoodDate().equals(pEndDate) || dfPattern.format(dates.getFoodDate()).equals(dfPattern.format(pEndDate))))
                .collect(Collectors.toList());
        resultSortedFoodList.sort((mFirst, mLast) -> mFirst.getFoodDate().compareTo(mLast.getFoodDate()));
        if(resultSortedFoodList != null && resultSortedFoodList.size() > 0)
            resultSortedFoodList.forEach(p->System.out.println(dfPattern.format(p.getFoodDate()) + " - "+ p.getFoodDescription()));
        else
            System.out.println(dfPattern.format(pStartDate) + " - " +dfPattern.format(pStartDate) +" tarihleri arasında kayıtlı herhangi bir veri bulunamadı!");
        System.out.println("---------------------------------------filter end------------------------------------------------------------------------------");
        return resultSortedFoodList;
    }

    private static List<FoodModel> getSampleFoodList(){
        List<FoodModel> foodList = new ArrayList<FoodModel>() {
            {
                try {
                    add(new FoodModel(dfPattern.parse("24.02.2020"), 1.1f, "Mercimek Çorba"));
                    add(new FoodModel(dfPattern.parse("24.02.2020"), 1.2f, "Dana Rosto"));
                    add(new FoodModel(dfPattern.parse("24.02.2020"), 1.3f, "Nohutlu Pirinç Pilavı"));
                    add(new FoodModel(dfPattern.parse("24.02.2020"), 1.4f, "Yoğurtlu Kereviz Salatası"));
                    add(new FoodModel(dfPattern.parse("24.02.2020"), 1.5f, "Meyve"));

                    add(new FoodModel(dfPattern.parse("25.02.2020"), 1.4f, "Yarma Çorba"));
                    add(new FoodModel(dfPattern.parse("25.02.2020"), 1.5f, "Tavuk Sote"));
                    add(new FoodModel(dfPattern.parse("25.02.2020"), 1.6f, "Bulgur Pilavı"));
                    add(new FoodModel(dfPattern.parse("25.02.2020"), 1.7f, "Ayran"));
                    add(new FoodModel(dfPattern.parse("25.02.2020"), 1.8f, "Ayva Tatlısı"));

                    add(new FoodModel(dfPattern.parse("26.02.2020"), 1.9f, "Tarhana Çorba"));
                    add(new FoodModel(dfPattern.parse("26.02.2020"), 1.10f, "Yoğurtlu Köfte"));
                    add(new FoodModel(dfPattern.parse("26.02.2020"), 1.11f, "Şakşuka"));
                    add(new FoodModel(dfPattern.parse("26.02.2020"), 1.12f, "Meyve"));
                    add(new FoodModel(dfPattern.parse("26.02.2020"), 1.13f, "Seçmeli İçecek"));

                    add(new FoodModel(dfPattern.parse("27.02.2020"), 1.14f, "Ezogelin Çorba"));
                    add(new FoodModel(dfPattern.parse("27.02.2020"), 1.15f, "Orman Kebap"));
                    add(new FoodModel(dfPattern.parse("27.02.2020"), 1.16f, "Kol Böreği"));
                    add(new FoodModel(dfPattern.parse("27.02.2020"), 1.17f, "Komposto"));
                    add(new FoodModel(dfPattern.parse("27.02.2020"), 1.18f, "Yoğurt"));

                    add(new FoodModel(dfPattern.parse("28.02.2020"), 1.19f, "Brokoli Çorba"));
                    add(new FoodModel(dfPattern.parse("28.02.2020"), 1.20f, "Et Döner-Garnitür"));
                    add(new FoodModel(dfPattern.parse("28.02.2020"), 1.21f, "Pirinç Pilavı"));
                    add(new FoodModel(dfPattern.parse("28.02.2020"), 1.22f, "Ayran"));
                    add(new FoodModel(dfPattern.parse("28.02.2020"), 1.23f, "Şöbiyet"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        return foodList;
    }

    private static List<FoodModel> getSampleFoodListJson() {
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
        List<FoodModel> resultList = new ArrayList<FoodModel>();

        Type listType = new TypeToken<ArrayList<FoodModel>>(){}.getType();
        resultList= gson.fromJson(sampleFoodListJson, listType);

//        /*Diger deserialize yontemi;*/
//        FoodModel[] list = gson.fromJson(sampleFoodListJson, FoodModel[].class);
//        resultList =  Arrays.asList(list);

        return resultList;
    }

    public synchronized void getSampleFoodListAsAService(boolean pIsFilter, Date pStartDate, Date pEndDate, boolean pRefreshServices) {
        Call<List<FoodModel>> call = SampleServices.getServiceInstance().getSampleFoodList();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setIndeterminate(true);
//        progressDoalog.setCancelable(true);
        progressDoalog.setMessage("Yükleniyor...");
//        progressDoalog.setTitle("Veriler alınıyor");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        call.enqueue(new Callback<List<FoodModel>>() {
            @Override
            public void onResponse(Call<List<FoodModel>> call, Response<List<FoodModel>> response) {
                Log.d("onResponseTag", response.raw().request().url().toString());
                if(!response.isSuccessful())
                    showModalDialog("Sonuc hatalı", "Sonuc alınırken hata oluştu.");
                else
                    foodList = response.body();

                if(foodList.size() < 1)
                    foodList = getSampleFoodListJson();

                bindListView(pIsFilter, pStartDate, pEndDate);
                progressDoalog.dismiss();
            }

            @Override
            public void onFailure(Call<List<FoodModel>> call, Throwable t) {
                showModalDialog("Food listesi getirilirken service fail oldu", t.getMessage());
                t.printStackTrace();
                foodList = getSampleFoodListJson();
                progressDoalog.dismiss();
            }
        });

    }

    private void showModalDialog(String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Tamam",null)
                .show();
    }

    public void clickexit(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Çıkmak istediğinizden emin misiniz?");
        builder.setCancelable(true);
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
//                System.exit(0);
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
