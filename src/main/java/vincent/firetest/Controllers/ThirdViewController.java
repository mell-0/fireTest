package vincent.firetest.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ThirdViewController
{
    @FXML
    public Button ReadApiButton;

    @FXML
    public ListView<String> resultListView;

    @FXML
    void ReadApiButtonClicked()
    {
        getRecipes();
    }


    @FXML
    public void getRecipes()
    {
        try
        {
            String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?f=b";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray mealsArray = jsonResponse.getJSONArray("meals");

            System.out.println(mealsArray.length());
            for (int i=0; i<mealsArray.length(); i++)
            {
                JSONObject meal = mealsArray.getJSONObject(i);
                resultListView.getItems().add(meal.getString("strMeal"));
                System.out.println(meal.getString("strMeal"));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
