package directionalChanges.algorithm;

import directionalChanges.algorithm.Price;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the market activity. It contains all prices.
 */
public class Market {

    private List<Price>     stocks;
    private int             index;
    private int             nextIndex;

    public Market()
    {
        stocks = new ArrayList<Price>();
        index = 0;
        nextIndex = 0;
    }

    public Market(List stocks)
    {
        this.stocks = stocks;
        index = 0;
    }

    public void setStock(List stocks)
    {
        this.stocks = stocks;
    }

    public List<Price>  getStocks()
    {
        return stocks;
    }

    public void addPrice(double price)
    {
        if (stocks != null)
            stocks.add(new Price(price));
    }

    public Price getPrice(int index)
    {
        if (stocks != null && index <= stocks.size())
            return stocks.get(index);
        return null;
    }

    public Price nextPrice()
    {
        Price  price;

        if (stocks != null && nextIndex <= stocks.size())
        {

            price = stocks.get(nextIndex);
            nextIndex++;
            index = nextIndex - 1;
            return price;
        }
        return null;
    }

    public int getNextIndex(){
        if (index < (stocks.size() - 1))
            return index + 1;
        return index;
    }

    public int getPreviousIndex()
    {
        if (index == 0)
            return 0;
        return index - 1;
    }

    public int getIndex()
    {
        return index;
    }

    public boolean isNext()
    {
        if (nextIndex < stocks.size())
            return true;
        return false;
    }

    public void resetMarket()
    {
        index = 0;
        nextIndex = 0;
    }

    @Override
    public String toString() {
        StringBuilder   str;
        int             i;

        str = new StringBuilder();
        i = 0;
        str.append("Market{");
        for (Price price : stocks)
        {
            str.append("Index: ")
                    .append(i)
                    .append(" --> ")
                    .append(price.toString())
                    .append("\n");
            i++;
        }
        str.append("}");
        return str.toString();
    }
}
