package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.*;


public class MainActivity extends AppCompatActivity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
          //  DownloadUpdate;
            new DownloadUpdate().execute();
        }
    public class weather {
        public String main;
        public String description;
    }


    public class main {
        public float temp;
    }

    public class List {

        public int dt;
        public main main;
        public weather[] weather;
        public String dt_txt;

    }
    public class Json_weather {

        public int cod;
        public int message;
        public List[] list;
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public void btnClick(View view) {
            new DownloadUpdate().execute();
            Toast.makeText(this,"Weather information has been updated",Toast.LENGTH_LONG).show();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... strings) {
        ////here

            /*
            String stringUrl = "http://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            */

            String stringUrl = " https://mpianatra.com/Courses/forecast.json";
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
                Log.d("fffffff",inputStream.toString());
                Log.d("aaaaaaa",url.toString() );
                Log.d("aaaaaaa","hello" );
                Log.d("aaaaaaa",reader.toString());
                Log.d("cccccccc","Fucking Coding");

                String line;

              //读入数据出错，直接赋值OK

                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                int i=0;
                int n = reader.read();
                while (n != -1) {
                    // Mainly needed for debugging
                 //   Log.d("TAG", line);
                    buffer.append((char)n);
                    n = reader.read();
                }


                Log.d("cccccccc",buffer.toString());
                Gson weather_gs = new Gson();
                Json_weather wea_info =weather_gs.fromJson(buffer.toString(),Json_weather.class);
              //  line=String.valueOf((int)((wea_info.list[0].main.temp)-273));

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                //The temperature
                //return buffer.toString();
              //  line=String.valueOf(tmp);

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
        protected void onPostExecute(String weather) {
            //Update the temperature displayed
            String[] weekDays = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

            Gson weather_gs = new Gson();
            Json_weather wea_info =weather_gs.fromJson(weather,Json_weather.class);
            String line;
            line=String.valueOf((int)((wea_info.list[0].main.temp)-273));
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(line);
            ((TextView) findViewById(R.id.tv_date)).setText(wea_info.list[0].dt_txt);
            String now=dateToWeek(wea_info.list[0].dt_txt.substring(10));
            ((TextView) findViewById(R.id.topDate)).setText(now);

            int i=0;
            while(weekDays[i]!=now&&i<7)
            {
                i++;
            }

            ((TextView) findViewById(R.id.date_next)).setText(weekDays[(i+1)%7]);
            ((TextView) findViewById(R.id.date_nnext)).setText(weekDays[(i+2)%7]);
            ((TextView) findViewById(R.id.date_nnnext)).setText(weekDays[(i+3)%7]);
            ((TextView) findViewById(R.id.date_nnnnext)).setText(weekDays[(i+4)%7]);

            switch (wea_info.list[0].weather[0].main) {
                case "Clear":
                ((ImageView) findViewById(R.id.img_weather_condition)).setImageResource(R.drawable.sunny_small);
                     break;
                case"Clouds":
                    ((ImageView) findViewById(R.id.img_weather_condition)).setImageResource(R.drawable.partly_sunny_small);
                    break;
                case"Rain":
                    ((ImageView) findViewById(R.id.img_weather_condition)).setImageResource(R.drawable.rainy_small);
                    break;
                case"Wind":
                    ((ImageView) findViewById(R.id.img_weather_condition)).setImageResource(R.drawable.windy_small);
                    break;
            }

            switch (wea_info.list[8].weather[0].main) {
                case "Clear":
                    ((ImageView) findViewById(R.id.icon_next)).setImageResource(R.drawable.sunny_small);
                    break;
                case"Clouds":
                    ((ImageView) findViewById(R.id.icon_next)).setImageResource(R.drawable.partly_sunny_small);
                    break;
                case"Rain":
                    ((ImageView) findViewById(R.id.icon_next)).setImageResource(R.drawable.rainy_small);
                    break;
                case"Wind":
                    ((ImageView) findViewById(R.id.icon_next)).setImageResource(R.drawable.windy_small);
                    break;
            }

            switch (wea_info.list[16].weather[0].main) {
                case "Clear":
                    ((ImageView) findViewById(R.id.icon_nnext)).setImageResource(R.drawable.sunny_small);
                     break;
                case"Clouds":
                    ((ImageView) findViewById(R.id.icon_nnext)).setImageResource(R.drawable.partly_sunny_small);
                     break;
                case"Rain":
                    ((ImageView) findViewById(R.id.icon_nnext)).setImageResource(R.drawable.rainy_small);
                     break;
                case"Wind":
                    ((ImageView) findViewById(R.id.icon_nnext)).setImageResource(R.drawable.windy_small);
                      break;
            }

            switch (wea_info.list[24].weather[0].main) {
                case "Clear":
                    ((ImageView) findViewById(R.id.icon_nnnext)).setImageResource(R.drawable.sunny_small);
                    break;
                case"Clouds":
                    ((ImageView) findViewById(R.id.icon_nnnext)).setImageResource(R.drawable.partly_sunny_small);
                    break;
                case"Rain":
                    ((ImageView) findViewById(R.id.icon_nnnext)).setImageResource(R.drawable.rainy_small);
                    break;
                case"Wind":
                    ((ImageView) findViewById(R.id.icon_nnnext)).setImageResource(R.drawable.windy_small);
                    break;
            }
            switch (wea_info.list[32].weather[0].main) {
                case "Clear":
                    ((ImageView) findViewById(R.id.icon_nnnnext)).setImageResource(R.drawable.sunny_small);
                    break;
                case"Clouds":
                    ((ImageView) findViewById(R.id.icon_nnnnext)).setImageResource(R.drawable.partly_sunny_small);
                    break;
                case"Rain":
                    ((ImageView) findViewById(R.id.icon_nnnnext)).setImageResource(R.drawable.rainy_small);
                    break;
                case"Wind":
                    ((ImageView) findViewById(R.id.icon_nnnnext)).setImageResource(R.drawable.windy_small);
                    break;
            }

        }
    }
}
