package vincent.firetest.Controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import vincent.firetest.CardData;
import vincent.firetest.SessionManager;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static vincent.firetest.HelloApplication.*;

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
    public TextField countryTextBox;

    @FXML
    public TextField ingredientsTextBox;

    @FXML
    public SplitMenuButton caloriesSplitMenuButton; //= new SplitMenuButton();;

    @FXML
    public MenuItem AscendingMenuItem; //= new MenuItem("Ascending");

    @FXML
    public MenuItem DescendingMenuItem; //= new MenuItem("Descending"); // do i need descending? bc it's descending by default

    @FXML
    public MenuItem OffMenuItem; //= new MenuItem("Off");

    public boolean caloriesOn = false;

    @FXML
    public ImageView recipeImageView;

    @FXML
    void initialize()
    {
        System.out.println("Initializing SecondViewController");
        outputTextArea.setWrapText(true); // MAKES TEXT WORD WRAP

        AscendingMenuItem.setOnAction((e)-> {
            System.out.println(e.toString() + " selected");
            caloriesSplitMenuButton.setText("calories ↑");
            caloriesOn = true;
        });

        DescendingMenuItem.setOnAction((e)-> {
            System.out.println(e.toString() + " selected");
            caloriesSplitMenuButton.setText("calories ↓");
            caloriesOn = true;
        });

        OffMenuItem.setOnAction((e)-> {
            System.out.println(e.toString() + " selected");
            caloriesSplitMenuButton.setText("calories");
            caloriesOn = false;
        });
    }

    @FXML
    void backbuttonClicked(ActionEvent actionEvent) throws IOException
    {
        System.out.println("back button clicked");
        setRoot("hello-view");
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

    @FXML
    void caloriesSplitMenuButtonClicked(ActionEvent actionEvent) // DONT NEED THIS
    {
        System.out.println("calories split menu button clicked");
        //caloriesSplitMenuButton = new SplitMenuButton();

    }


    public void getData()
    {
        // asynchronously retrieve all users
        //ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Users").get();

        // this retrieves the documents in alphabetical order based on the username
        ApiFuture<QuerySnapshot> query = fstore.collection("Users").orderBy("UserName").get();

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
//        ApiFuture<QuerySnapshot> query = HelloApplication.fstore.collection("Food")
//                .whereEqualTo("country", "China")
//                .whereArrayContains("ingredients", "pork")
//                .whereEqualTo("calories", 450).get();

        // same thing but breaking it down:
        CollectionReference food = fstore.collection("Food");
        Query query = food; // initializing query

        if (countryTextBox.getText().isEmpty())
            System.out.println("getFoodData, country: No country selected");
        else
        {
            String[] countryList = countryTextBox.getText().split(",");
            System.out.println("getFoodData, country: " + Arrays.toString(countryList));
            query = query.whereIn("country", Arrays.asList(countryList));
        }
//        String[] countryList = {"China", "France"};
//        query = query.whereIn("country", Arrays.asList(countryList)); // whereIn can be used as an or statement

        if (ingredientsTextBox.getText().isEmpty())
            System.out.println("getFoodData, ingredients: No ingredients selected");
        else
        {
            String[] ingredientList = ingredientsTextBox.getText().split(",");
            System.out.println("getFoodData, ingredients: " + Arrays.toString(ingredientList));
            query = query.whereArrayContainsAny("ingredients", Arrays.asList(ingredientList));
        }
        //query = query.whereArrayContainsAny("ingredients", Arrays.asList("chicken", "rice")); // capitalization matters

        System.out.println("getFoodData, calories: " + caloriesOn + "\n");
        if (caloriesOn)
        {
            if (caloriesSplitMenuButton.getText().contains("↑"))
                query = query.orderBy("calories", Query.Direction.ASCENDING);
            else
                query = query.orderBy("calories", Query.Direction.DESCENDING);
        }
        // = query.orderBy("calories", Query.Direction.ASCENDING);

        ApiFuture<QuerySnapshot> querySnapshotList = query.get();


        QuerySnapshot querySnapshot;

        try
        {
            //querySnapshot = query.get();
            querySnapshot = querySnapshotList.get();

            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            outputTextArea.clear();
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


        docData.put("name", "GreeceFood1");
        docData.put("calories", 450);
        docData.put("country", "Greece");
        docData.put("ingredients", Arrays.asList("rice", "tofu", "pork", "garlic", "ginger", "chicken")); // how do I traverse this later?
        docData.put("instructions",
                "n/a\n");

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


        // Add a new document (asynchronously) in collection "cities" with id "LA"
        //ApiFuture<WriteResult> future = HelloApplication.fstore.collection("Food").document("GreeceFood1").set(docData);

        //ApiFuture<WriteResult> future1 = HelloApplication.fstore.collection("Food").document("Mapo Tofu").set(docData1);

        //ApiFuture<WriteResult> future2 = HelloApplication.fstore.collection("Food").document("Greek Salad").set(docData2);

        //ApiFuture<WriteResult> future3 = HelloApplication.fstore.collection("Food").document("Tacos al Pastor").set(docData3);


        // future.get() blocks on response
        //System.out.println("Update time : " + future1.get().getUpdateTime());


        // **START OF ADDING DATA TO FAVORITE LIST** add/delete
//        CardData card1 = new CardData("52771", "Spicy Arrabiata Penne", "https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg", "n/a");
//        CardData card2 = new CardData("52768", "Apple Frangipan Tart", "https://www.themealdb.com/images/media/meals/wxywrq1468235067.jpg", "n/a");
//
//        DocumentReference docRef = fstore.collection("Users").document(SessionManager.getUserId());
//
//        // Atomically add a new region to the "FAVORITE" array field.
//        ApiFuture<WriteResult> arrayUnion =
//                docRef.update("Favorites", FieldValue.arrayUnion(card1, card2));
//        System.out.println("Update time : " + arrayUnion.get());

        // Atomically remove a region from the "FAVORITE" array field.
        // need to create the same object to remove it: we click remove on the card, get the data from the card & use the method below to remove
//        ApiFuture<WriteResult> arrayRm =
//                docRef.update("Favorites", FieldValue.arrayRemove(card1));
//        System.out.println("Update time : " + arrayRm.get());
        // **END**

        // TESTING: loading image with just string
//        Image image = new Image("https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg");
//        Image image = new Image("https://media1.tenor.com/m/k_UsDt9xfWIAAAAC/i-will-eat-you-cat.gif");
//
//        recipeImageView.setImage(image);

//        CollectionReference query = fstore.collection("Food");
//        query.whereEqualTo("idMeal", "52771");

//        ApiFuture<QuerySnapshot> querySnapshotList = fstore.collection("Food")
//                .whereEqualTo("name", "Spicy Arrabiata Penne")
//                .get();
//
//        QuerySnapshot querySnapshot;
//
//        try
//        {
//            querySnapshot = querySnapshotList.get();
//
//            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
//
//            outputTextArea.clear();
//            for (QueryDocumentSnapshot document : documents)
//            {
//                outputTextArea.setText(outputTextArea.getText()
//                        + "Name: " + document.getString("name") + "\n"
//                        + "idMeal: " + document.get("idMeal") + "\n"
//                        + "Country: " + document.getString("country") + "\n"
//                        + "Ingredients: " + document.get("ingredients").toString() + "\n"
//                        + "Instructions:\n" + document.getString("instructions") + "\n");
//
//                Image image = new Image(Objects.requireNonNull(document.getString("image")));
//                recipeImageView.setImage(image);
//            }
//            //System.out.println(documents.size());
//        }
//        catch (InterruptedException | ExecutionException e)
//        {
//            e.printStackTrace();
//        }

        // **GETS THE FAVORITES FIELD IN THE USER**
        DocumentReference docRef = fstore.collection("Users").document(SessionManager.getUserId());

        // Fetch the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists())
        {
            // needs to be Map<String, Object> because that's what firebase returns
            List<Map<String, Object>> favorites = (List<Map<String, Object>>) document.get("Favorites");

            if (favorites != null)
            {
                for (Map<String, Object> fav : favorites)
                {
                    String idMeal = (String) fav.get("idMeal");
                    String nameMeal = (String) fav.get("nameMeal");
                    String image = (String) fav.get("image");
                    String description = (String) fav.get("description");

                    CardData card = new CardData(idMeal, nameMeal, image, description);
                    System.out.println(card);
                }
            }
            else
            {
                System.out.println("Favorites field is null");
            }
        }
        else
        {
            System.out.println("Document does not exist!");
        }

    }
}

// NOTES ON FIREBASE:
// CollectionReference food = fstore.collection("Food");
// VS
// ApiFuture<QuerySnapshot> query = fstore.collection("Food").get();

/*
    CollectionReference
    - This creates a reference to the "Food" collection.
    - It doesn’t actually fetch any data yet
    - You can use it to build queries or perform other operations (like adding documents, setting data, etc.)
*/

/*
    ApiFuture<QuerySnapshot>
    - This line sends a request to Firestore to fetch all documents in the "Food" collection right now.
    - ApiFuture<QuerySnapshot> is an asynchronous result. You’ll need to call .get() on it to actually block and retrieve the result.
*/



/*
        ApiFuture<QuerySnapshot> future = fstore.collection("Users").get();

        // asynchronously update doc, create the document if missing
        Map<String, Object> update = new HashMap<>();

        ArrayList<CardData> ingredientList = new ArrayList<>();

        //new CardData("52771", "Spicy Arrabiata Penne", "https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg", "n/a"));
        CardData card = new CardData("52768", "Apple Frangipan Tart", "https://www.themealdb.com/images/media/meals/wxywrq1468235067.jpg", "n/a");

        Collections.addAll(ingredientList, card);

        update.put("Favorites", ingredientList);

        ApiFuture<WriteResult> writeResult =
                fstore.collection("Users").document(SessionManager.getUserId()).set(update, SetOptions.merge());

        System.out.println("Update time : " + writeResult.get().getUpdateTime());
 */

