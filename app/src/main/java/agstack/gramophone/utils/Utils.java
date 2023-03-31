package agstack.gramophone.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Utils {
  public static String getJsonFromAssets(Context context, String fileName) {
    String jsonString;
    try {
      InputStream is = context.getAssets().open(fileName);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      jsonString = new String(buffer, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return jsonString;
  }


    public static String getDate() {
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

      long milliSeconds= System.currentTimeMillis();
      System.out.println(milliSeconds);

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(milliSeconds);
      String date = formatter.format(calendar.getTime());
      System.out.println("Raj"+formatter.format(calendar.getTime()));
      return date;
    }
}