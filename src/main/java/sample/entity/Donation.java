package sample.entity;

import org.json.JSONObject;

import java.util.UUID;

public class Donation {
    private UUID id;
    private String username;
    private String message;
    private double amount;
    private String currency;

    public Donation() {
        this.id = UUID.randomUUID();
    }

    public Donation(JSONObject json) throws org.json.JSONException {
        this.id = UUID.randomUUID();
        JSONObject data = json.getJSONObject("result").getJSONObject("data").getJSONObject("data");
        amount = data.getDouble("amount");
        currency = data.getString("currency");
        try {
            username = data.getString("username");
        } catch (Exception e) {
            username = "Аноним";
        }
        try {
            message = data.getString("message");
        } catch (Exception e) {
            message = "";
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
