package edwin.team.com.photoclient.Classes;

import java.io.Serializable;

/**
 * Created by Hafid on 23-11-2014.
 */
public class Album implements Serializable {

    String name;
    String token;
    int amount;


    public Album(String name, String token, int amount){
        this.name = name;
        this.token = token;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
