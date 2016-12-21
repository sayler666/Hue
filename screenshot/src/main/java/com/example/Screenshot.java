package com.example;

import com.squareup.okhttp.OkHttpClient;
import retrofit.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
public class Screenshot {

  private static HueApi hueApi;

  public static void main(String[] args) {
    OkHttpClient client = new OkHttpClient();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(HueApi.HUE_BRIDGE_IP)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
    hueApi = retrofit.create(HueApi.class);

    int delay = 0;
    int interval = 150;
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        createScreen();
      }
    }, delay, interval);
  }

  public static void createScreen() {
    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    long sta = System.currentTimeMillis();
    try {
      BufferedImage image = new Robot().createScreenCapture(screenRect);
      //BufferedImage imageScaled = Scalr.resize(image, Scalr.Method.BALANCED, 2, 2);

//      try {
//        ImageIO.write(imageScaled, "bmp", new File("scr.bmp"));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }

      int[] colors = ColorThief.getColor(image);

      if (colors != null) {
        int red = colors[0];
        int green = colors[1];
        int blue = colors[2];
        float[] hsb = new float[3];

        Color.RGBtoHSB(red,green,blue,hsb);
        //hsb[1]*=1.5;
        //hsb[0]*=1.5;
        Color color =Color.getHSBColor(hsb[0],hsb[1],hsb[2]);

        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();

        double bri = brightness(red, green, blue);

        red = (int) ((red > 0.04045) ? Math.pow((red + 0.055) / (1.0 + 0.055), 2.4) : (red / 12.92));
        green = (int) ((green > 0.04045) ? Math.pow((green + 0.055) / (1.0 + 0.055), 2.4) : (green / 12.92));
        blue = (int) ((blue > 0.04045) ? Math.pow((blue + 0.055) / (1.0 + 0.055), 2.4) : (blue / 12.92));

        double X = red * 0.664511 + green * 0.154324 + blue * 0.162028;
        double Y = red * 0.283881 + green * 0.668433 + blue * 0.047685;
        double Z = red * 0.000088 + green * 0.072310 + blue * 0.986039;

        double x = X / (X + Y + Z);
        double y = Y / (X + Y + Z);

        long end = System.currentTimeMillis();

        changeLight(x, y, (int) (bri * 2.5));

        System.out.println("time:  " + (end - sta));
      }

    } catch (AWTException e) {
      e.printStackTrace();
    }
  }

  private static int brightness(int r, int g, int b) {
    return (int) Math.sqrt(
        r * r * .241 +
            g * g * .691 +
            b * b * .068);
  }

  public static void changeLight(double x, double y, int bri) {
    State state = new State(x, y, bri);
    System.out.println(state.toString());
    hueApi.changeLight(1, state).enqueue(new Callback<ResponseState>() {
      @Override
      public void onResponse(Response<ResponseState> response, Retrofit retrofit) {
        System.out.println("success");
      }

      @Override
      public void onFailure(Throwable t) {
        System.out.println("failure");
      }
    });

    hueApi.changeLight(2, state).enqueue(new Callback<ResponseState>() {
      @Override
      public void onResponse(Response<ResponseState> response, Retrofit retrofit) {
        System.out.println("success");
      }

      @Override
      public void onFailure(Throwable t) {
        System.out.println("failure");
      }
    });
  }
}
