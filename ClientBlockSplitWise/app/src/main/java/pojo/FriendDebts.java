package pojo;

import java.io.Serializable;

/**
 * Created by rui on 24/06/2017.
 */

public class FriendDebts implements Serializable {

    private boolean debt;
    private float amount;
    private String date;

    public FriendDebts(boolean debt, float amount, String date) {
        this.debt = debt;
        this.amount = amount;
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Amount: ").append(amount).append("€");
        sb.append(", date: ").append(date);
        sb.append(", debt: ").append(debt);
        return sb.toString();
    }

    public boolean isDebt() {
        return debt;
    }

    public void setDebt(boolean debt) {
        this.debt = debt;
    }
}
