import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //display weather app GUI
                new WeatherAppGUI().setVisible(true);


                //to test weather data fetched from API(location name)
                //System.out.println(WeatherAPI.getWeatherData("Singapore"));

                //System.out.println(WeatherAPI.getCurrentTime());
            }
        });
    }
}
