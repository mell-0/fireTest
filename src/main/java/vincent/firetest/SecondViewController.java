package vincent.firetest;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java.util.Map;

public class SecondViewController
{
    @FXML
    public Button backButton;

    @FXML
    public Button readButton;

    @FXML
    public Button read_foodButton;

    @FXML
    public TextArea outputTextArea;

    @FXML
    public Button addfoodButton;

    @FXML
    public Button updatefoodButton;

    @FXML
    public Button deletefoodButton;

    // can i make it where I can choose which collection gets shown?


    @FXML
    void backbuttonClicked(ActionEvent actionEvent) throws IOException
    {
        System.out.println("back button clicked");
        HelloApplication.setRoot("hello-view");
    }

    @FXML
    void readButtonClicked(ActionEvent actionEvent)
    {
        System.out.println("read button clicked");
        getData();
    }

    @FXML
    void readfoodButtonClicked(ActionEvent actionEvent)
    {
        System.out.println("read food button clicked");
        getFoodData();
    }

    @FXML
    void addfoodButtonClicked(ActionEvent actionEvent) throws ExecutionException, InterruptedException {
        System.out.println("test button clicked");
        addfoodButton();
    }

    public void getData()
    {
        // asynchronously retrieve all users
        //ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Users").get();

        // this retrieves the documents in alphabetical order based on the username
        ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Users").orderBy("UserName").get();

        QuerySnapshot querySnapshot;

        try
        {
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents)
            {
                outputTextArea.setText(outputTextArea.getText() + document.getString("UserName")
                        + " " + document.getString("Password") + "\n");

            }
            //System.out.println(documents.size());
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }

    }

    public void getFoodData()
    {
        //ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Food").get();

        // gets the food with pork & orders it by calories from biggest to smallest, indexing is impractical
//        ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Food")
//                .whereArrayContains("ingredients", "pork")
//                .orderBy("calories", Query.Direction.DESCENDING).get();

        // some compound searches work but what about or searches?
        ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Food")
                .whereEqualTo("country", "China")
                .whereArrayContains("ingredients", "pork")
                .whereEqualTo("calories", 450).get();



        QuerySnapshot querySnapshot;

        try
        {
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents)
            {
                outputTextArea.setText(outputTextArea.getText()
                        + "Name: " + document.getString("name") + "\n"
                        + "Country: " + document.getString("country") + "\n"
                        + "Calories: " + document.get("calories") + "\n"
                        + "Ingredients: " + document.get("ingredients").toString() + "\n"
                        + "Instructions:\n" + document.getString("instructions") + "\n");

            }
            //System.out.println(documents.size());
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
    }


    public void addfoodButton() throws ExecutionException, InterruptedException {
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> docData1 = new HashMap<>();
        Map<String, Object> docData2 = new HashMap<>();
        Map<String, Object> docData3 = new HashMap<>();


//        docData.put("name", "Fried Rice");
//        docData.put("calories", 500);
//        docData.put("country", "China");
//        docData.put("ingredients", Arrays.asList("rice", "eggs", "chicken", "peas", "carrots", "corn")); // how do I traverse this later?
//        docData.put("instructions",
//                "1) Use cold rice: You’ve gotta plan ahead and use thoroughly-chilled cooked rice. So leftover rice is ideal.\n" +
//                "2) Cook eggs: Pour oil into the pan & set it to high, beat an egg in a bowl until it's yellow & pour the egg mixture into the pan. " +
//                        "Set the egg aside when it's done\n" +
//                "3) Cook chicken: Dice up the chicken into bite size pieces, pour oil into the pan & set it to medium & cook until the center of the " +
//                        "chicken is white. Set the chicken aside when it's done\n" +
//                "4) Cook vegetables: Pour oil into the pan & set it to high & throw in the frozen vegetable mix of peas, carrots, & corn." +
//                "5) Putting it together: When the vegetables are cooked, throw in the rice, egg, & chicken. Then add sesame oil, soy sauce & oyster sauce & mix\n");
//
//        docData1.put("name", "Mapo Tofu");
//        docData1.put("calories", 450);
//        docData1.put("country", "China");
//        docData1.put("ingredients", Arrays.asList("tofu", "pork", "garlic", "ginger")); // how do I traverse this later?
//        docData1.put("instructions",
//                "1) Prep ingredients: Dice tofu into cubes and finely chop garlic, ginger, and green onions.\n" +
//                "2) Cook pork: Heat oil in a pan over medium heat, add ground pork, and cook until browned. Remove and set aside.\n" +
//                "3) Make sauce: In the same pan, add garlic, ginger, and doubanjiang. Stir-fry for about a minute.\n" +
//                "4) Combine: Add chicken broth, soy sauce, and tofu. Simmer for about 5 minutes.\n" +
//                "5) Finish: Return the pork to the pan, add a cornstarch slurry to thicken, sprinkle with Sichuan peppercorns, and garnish with green onions.\n");

//        docData2.put("name", "Greek Salad");
//        docData2.put("calories", 300);
//        docData2.put("country", "Greece");
//        docData2.put("ingredients", Arrays.asList("cucumber", "tomato", "onion", "pepper", "olives", "feta cheese", "olive oil", "vinegar")); // how do I traverse this later?
//        docData2.put("instructions",
//                "1) Chop veggies: Dice cucumber, tomatoes, and bell pepper. Slice red onion.\n" +
//                "2) Assemble salad: Combine veggies and olives in a bowl.\n" +
//                "3) Add feta: Place large chunks of feta on top.\n" +
//                "4) Dress: Drizzle with olive oil, red wine vinegar, and sprinkle with oregano. Toss gently and serve.\n");

//        docData3.put("name", "Tacos al Pastor");
//        docData3.put("calories", 600);
//        docData3.put("country", "Mexico");
//        docData3.put("ingredients", Arrays.asList("pork", "pineapple", "chilies", "garlic", "tortillas", "onion", "cilantro", "vinegar")); // how do I traverse this later?
//        docData3.put("instructions",
//                "1) Marinate pork: Blend chilies, achiote paste, garlic, oregano, cumin, and vinegar into a smooth marinade. Coat pork and marinate for at least 4 hours.\n" +
//                "2) Cook pork: Roast pork at 375°F for about 1 hour, or grill until cooked through. Let rest and slice thinly.\n" +
//                "3) Prepare toppings: Dice pineapple, onion, and cilantro.\n" +
//                "4) Assemble: Warm tortillas, add pork, and top with pineapple, onion, and cilantro.\n");

        // Add a new document (asynchronously) in collection "cities" with id "LA"
        //ApiFuture<WriteResult> future = HelloApplication.fstore.collection("Food").document("Fried Rice").set(docData);

        //ApiFuture<WriteResult> future1 = HelloApplication.fstore.collection("Food").document("Mapo Tofu").set(docData1);

        //ApiFuture<WriteResult> future2 = HelloApplication.fstore.collection("Food").document("Greek Salad").set(docData2);

        //ApiFuture<WriteResult> future3 = HelloApplication.fstore.collection("Food").document("Tacos al Pastor").set(docData3);


        // future.get() blocks on response
        //System.out.println("Update time : " + future1.get().getUpdateTime());
    }
}
