package directionalChanges.algorithm;

/**
 * Created by jerem on 02/08/14.
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
