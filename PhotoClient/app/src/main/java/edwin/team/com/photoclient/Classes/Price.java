package edwin.team.com.photoclient.Classes;

import java.io.Serializable;

/**
 * Created by Hafid on 11-11-2014.
 */
public class Price implements Serializable {

    private int width;
    private int height;
    private double price;
    private int sizepriceId;


    public Price(int width, int height, double price, int sizepriceId){
        this.width = width;
        this.height = height;
        this.price = price;
        this.sizepriceId = sizepriceId;
    }

    public int getWidth (){ return this.width;  }
    public int getHeight (){ return this.height;  }
    public double getPrice (){ return this.price;  }
    public int getSizepriceId (){ return this.sizepriceId;  }

}
