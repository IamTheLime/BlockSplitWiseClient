package pojo;

/**
 * Created by rui on 24/06/2017.
 */

public class FriendDebts {

    private int amount;
    private String date;

    public FriendDebts(int amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public int getAmount() {
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
        return sb.toString();
    }
}
