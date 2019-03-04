package mg.studio.weatherappdesign;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.EventLogTags;
import android.util.Log;
import android.widget.Toast;


import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadUpdate().execute();
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();

    }

    public String chooseWeather(String weatherinfo){
        String weather = "";
        if(weatherinfo.indexOf("雨")!=-1){
            weather = "rainy_small";
        }
        else if(weatherinfo.indexOf("云")!=-1){
            weather = "partly_sunny_small";
        }
        else if(weatherinfo.indexOf("阴")!=-1){
            weather = "windy_small";
        }else{
            weather = "sunny_small";
        }

        return weather;
    }

    private class WeatherInfo{
        public String date;
        public String weather;
        public String temp;
        public String weather1;
        public String weather2;
        public String weather3;
        public String weather4;
        public String d0;
        public String d0_l;
        public String d0_h;
        public String d1;
        public String d1_l;
        public String d1_h;
        public String d2;
        public String d2_l;
        public String d2_h;
        public String d3;
        public String d3_l;
        public String d3_h;
        public String d4;
        public String d4_l;
        public String d4_h;
        WeatherInfo(){

        }

    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://api.shujuzhihui.cn/api/weather/dailyweather?appKey=efcdee809802446f9e3c5f291195052f&city=Chongqing";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String jsonInfo) {
            //Update the temperature displayed
            JSONObject dataJson = JSONObject.fromObject(jsonInfo);
            WeatherInfo weatherInfo = new WeatherInfo();
            String result = dataJson.getString("RESULT");
            dataJson = JSONObject.fromObject(result);
            String str_now = dataJson.getString("weather_now");
            JSONObject now = JSONObject.fromObject(str_now);
            weatherInfo.weather = chooseWeather(now.getString("weather"));

            weatherInfo.temp = now.getString("temp");

            JSONArray next = dataJson.getJSONArray("weather_next");
            JSONObject nextdata = next.getJSONObject(0);
            weatherInfo.date = nextdata.getString("fi");
            weatherInfo.d0_l = nextdata.getString("fd");
            weatherInfo.d0_h = nextdata.getString("fc");
            nextdata = next.getJSONObject(1);
            weatherInfo.weather1 = chooseWeather(nextdata.getString("fa"));
            weatherInfo.d0 = nextdata.getString("fj");
            weatherInfo.d1 = nextdata.getString("fi");
            weatherInfo.d1_l = nextdata.getString("fd");
            weatherInfo.d1_h = nextdata.getString("fc");
            nextdata = next.getJSONObject(2);
            weatherInfo.weather2 = chooseWeather(nextdata.getString("fa"));
            weatherInfo.d2 = nextdata.getString("fi");
            weatherInfo.d2_l = nextdata.getString("fd");
            weatherInfo.d2_h = nextdata.getString("fc");
            nextdata = next.getJSONObject(3);
            weatherInfo.weather3 =chooseWeather(nextdata.getString("fa"));
            weatherInfo.d3 = nextdata.getString("fi");
            weatherInfo.d3_l = nextdata.getString("fd");
            weatherInfo.d3_h = nextdata.getString("fc");
            nextdata = next.getJSONObject(4);
            weatherInfo.weather4 = chooseWeather(nextdata.getString("fa"));
            weatherInfo.d4 = nextdata.getString("fi");
            weatherInfo.d4_l = nextdata.getString("fd");
            weatherInfo.d4_h = nextdata.getString("fc");

            ApplicationInfo appInfo = getApplicationInfo();
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(weatherInfo.temp);
            ((TextView) findViewById(R.id.tv_location)).setText("重庆");
            ((TextView) findViewById(R.id.t0)).setText(weatherInfo.d0_l+" ~ "+weatherInfo.d0_h+" °C");
            ((TextView) findViewById(R.id.t1)).setText(weatherInfo.d1_l+" ~ "+weatherInfo.d1_h+" °C");
            ((TextView) findViewById(R.id.t2)).setText(weatherInfo.d2_l+" ~ "+weatherInfo.d2_h+" °C");
            ((TextView) findViewById(R.id.t3)).setText(weatherInfo.d3_l+" ~ "+weatherInfo.d3_h+" °C");
            ((TextView) findViewById(R.id.t4)).setText(weatherInfo.d4_l+" ~ "+weatherInfo.d4_h+" °C");
            ((TextView) findViewById(R.id.tv_date)).setText("2019/"+weatherInfo.date);
            ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),getResources().getIdentifier(weatherInfo.weather,"drawable",appInfo.packageName)));
            ((ImageView) findViewById(R.id.img_weather_condition1)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),getResources().getIdentifier(weatherInfo.weather1,"drawable",appInfo.packageName)));
            ((ImageView) findViewById(R.id.img_weather_condition2)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),getResources().getIdentifier(weatherInfo.weather2,"drawable",appInfo.packageName)));
            ((ImageView) findViewById(R.id.img_weather_condition3)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),getResources().getIdentifier(weatherInfo.weather3,"drawable",appInfo.packageName)));
            ((ImageView) findViewById(R.id.img_weather_condition4)).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),getResources().getIdentifier(weatherInfo.weather4,"drawable",appInfo.packageName)));


            if(weatherInfo.d0.equals( "周日")){
                weatherInfo.d0 = "SATURDAY";
                weatherInfo.d1 = "SUN";
                weatherInfo.d2 = "MON";
                weatherInfo.d3 = "TUE";
                weatherInfo.d4 = "WED";

            }
            else if(weatherInfo.d0.equals("周一")){
                weatherInfo.d0 = "SUNDAY";
                weatherInfo.d1 = "MON";
                weatherInfo.d2 = "TUE";
                weatherInfo.d3 = "WED";
                weatherInfo.d4 = "THU";

            }
            else if(weatherInfo.d0.equals("周二")){
                weatherInfo.d0 = "MONDAY";
                weatherInfo.d1 = "TUE";
                weatherInfo.d2 = "WED";
                weatherInfo.d3 = "THU";
                weatherInfo.d4 = "FRI";
            }
            else if(weatherInfo.d0.equals("周三")){
                weatherInfo.d0 = "TUESDAY";
                weatherInfo.d1 = "WED";
                weatherInfo.d2 = "THU";
                weatherInfo.d3 = "FRI";
                weatherInfo.d4 = "SAT";
            }
            else if(weatherInfo.d0.equals("周四")){
                weatherInfo.d0 = "WEDNESDAY";
                weatherInfo.d1 = "THU";
                weatherInfo.d2 = "FRI";
                weatherInfo.d3 = "SAT";
                weatherInfo.d4 = "SUN";
            }
            else if(weatherInfo.d0.equals("周五")){
                weatherInfo.d0 = "THURSDAY";
                weatherInfo.d1 = "FRI";
                weatherInfo.d2 = "SAT";
                weatherInfo.d3 = "SUN";
                weatherInfo.d4 = "MON";
            }
            else if(weatherInfo.d0.equals("周六")){
                weatherInfo.d0 = "FRIDAY";
                weatherInfo.d1 = "SAT";
                weatherInfo.d2 = "SUN";
                weatherInfo.d3 = "MON";
                weatherInfo.d4 = "TUE";
            }

            ((TextView) findViewById(R.id.tv_date1)).setText(weatherInfo.d1);
            ((TextView) findViewById(R.id.tv_date0)).setText(weatherInfo.d0);
            ((TextView) findViewById(R.id.tv_date2)).setText(weatherInfo.d2);
            ((TextView) findViewById(R.id.tv_date3)).setText(weatherInfo.d3);
            ((TextView) findViewById(R.id.tv_date4)).setText(weatherInfo.d4);
            Toast.makeText(getApplicationContext(),"Weather forecast has been updated",Toast.LENGTH_LONG).show();
        }
    }
}
