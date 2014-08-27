package directionalChanges.algorithm;

/**
 * Class to represent a price.
 */
public class Price {

    private double         price;

    public Price()
    {
        this.price = 0.0;
    }

    public Price(double price)
    {
        this.price = price;
    }

    private void setPrice(double price)
    {
        this.price = price;
    }

    public Double getPrice()
    {
        return price;
    }

    @Override
    public String toString() {
        return "{Price: " + Double.toString(price) + "}";
    }
}
