import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*; //JComponents
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
    private JSONObject weatherData;
    public WeatherAppGUI() {
        //setup gui & title
        super("Weather Application");

        //to close the app when it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //size of the GUI
        setSize(450, 650);

        //load GUI at center of screen
        setLocationRelativeTo(null);

        //to position components within the gui manually
        setLayout(null);

        //prevent resizing of gui
        setResizable(false);

        addGUIComponents();

    }

    private void addGUIComponents() {

        //set default background image
        JLabel background = new JLabel(loadImage("src/assets/allWeather 4.jpg"));
        background.setBounds(0,0,450,650);
        add(background);


        //create search field
        JTextField searchTextField = new JTextField();

        //set location of search field
        searchTextField.setBounds(15, 15, 351, 45);

        //font style and size
        searchTextField.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        background.add(searchTextField);

        //default location name
        JLabel defaultLocName = new JLabel("Location");
        defaultLocName.setBounds(0, 80, 450, 54);
        defaultLocName.setFont(new Font("Times New Roman", Font.BOLD, 48));
        defaultLocName.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(defaultLocName);

        //weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/sunny3D 1.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        background.add(weatherConditionImage);

        //temperature text
        JLabel tempText = new JLabel("°C");
        tempText.setBounds(0, 350, 450, 54);
        tempText.setFont(new Font("Dialog", Font.BOLD, 35));
        tempText.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(tempText);

        //weather condition description
        JLabel weatherDesc = new JLabel("Condition");
        weatherDesc.setBounds(0, 405, 450, 36);
        weatherDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherDesc.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(weatherDesc);

        //humidity image
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity3D.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        background.add(humidityImage);

        //humidity text
        JLabel humidText = new JLabel("<html><b>Humidity</b> NA %</html>");
        humidText.setBounds(90, 500, 85, 55);
        humidText.setFont(new Font("Dialog", Font.PLAIN, 16));
        humidText.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(humidText);

        //wind speed image
        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed3D.png"));
        windSpeedImage.setBounds(220, 500, 74, 66);
        background.add(windSpeedImage);

        //wind speed text
        JLabel windSpeedText = new JLabel("<html><b>Wind speed</b>  0km/h</html>");
        windSpeedText.setBounds(280, 500, 100, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windSpeedImage.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(windSpeedText);

        //date & time text
        JLabel dateText = new JLabel("<html></b>26/05/03 00:00</html>");
        dateText.setBounds(0,423, 450, 55);
        dateText.setFont(new Font("Dialog", Font.PLAIN, 16));
        dateText.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(dateText);

        //create search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        //change cursor when hovering this button
        searchButton.setCursor(Cursor.getPredefinedCursor((Cursor.HAND_CURSOR)));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get location from user
                String userInput = searchTextField.getText();

                //validate input - remove whitespaces to ensure non-empty text
                if(userInput.replaceAll("\\s", "").length() == 0) {
                    return;
                }

                //retrieve weather data
                weatherData = WeatherAPI.getWeatherData(userInput);

                //update GUI

                //update weather image
                assert weatherData != null;
                String weatherCondition = (String) weatherData.get("weather_condition");


                //depending on condition, we will update the weather image that corresponds with the condition
                switch(weatherCondition) {
                    case "Clear":
                        background.setIcon(loadImage("src/assets/sunnyBG.jpeg"));
                        weatherConditionImage.setIcon(loadImage("src/assets/sunny3D 1.png"));
                        break;
                    case "Cloudy":
                        background.setIcon(loadImage("src/assets/cloudyBG.jpeg"));
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy 3D.png"));
                        break;
                    case "Rain":
                        background.setIcon(loadImage("src/assets/rainBG 3.jpg"));
                        weatherConditionImage.setIcon(loadImage("src/assets/rain3D.png"));
                        break;
                    case "Snow":
                        background.setIcon(loadImage("src/assets/snowBG.jpeg"));
                        weatherConditionImage.setIcon(loadImage("src/assets/snow3D.png"));
                        break;
                }

                //update default location name & making sure all words starts with a capital letter
                StringBuilder result = new StringBuilder();
                String[] words = userInput.split("\\s+");

                for(String word : words) {
                    if(!word.isEmpty()) {
                        String firstLetter = word.substring(0,1).toUpperCase();
                        String restOfWords = word.substring(1).toLowerCase();
                        result.append(firstLetter).append(restOfWords).append(" ");
                    }
                }
                String locName = String.valueOf(result);
                defaultLocName.setText(locName);

                //update date text
                String date = (String) WeatherAPI.getCurrentTime();
                dateText.setText(date);


                //update temperature text
                double temperature = (double) weatherData.get("temperature");
                tempText.setText(temperature + "°C");

                //update weather condition text
                weatherDesc.setText(weatherCondition);

                //update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                //update wind speed text
                double windspeed = (double) weatherData.get("windspeed");
                windSpeedText.setText("<html><b>Wind speed</b> " + windspeed + "km/h</html>");

            }
        });


        background.add(searchButton);
    }
    private ImageIcon loadImage(String resourcePath) {
        try {
            //read image file from path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            //return image icon so our components can render it
            return new ImageIcon(image);
        }catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }


}
