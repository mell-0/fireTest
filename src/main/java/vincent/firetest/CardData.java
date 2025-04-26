package vincent.firetest;

public class CardData
{
    public String idMeal;
    public String nameMeal;
    public String image;
    public String description;

    public CardData(String idMeal, String nameMeal, String image, String description)
    {
        this.idMeal = idMeal;
        this.nameMeal = nameMeal;
        this.image = image;
        this.description = description;
    }

    public String getIdMeal()
    {
        return idMeal;
    }

    public String getNameMeal()
    {
        return nameMeal;
    }

    public String getImage()
    {
        return image;
    }

    public String getDescription()
    {
        return description;
    }

    public String toString()
    {
        return idMeal + " " + nameMeal + " " + image;
    }
}
