package com.example.personalfinanceproject;

import javafx.beans.property.SimpleStringProperty;

public class Income {

    private final SimpleStringProperty provider;
    private final SimpleStringProperty amount;

    Income(String provider, String amount) {

        this.provider = new SimpleStringProperty(provider);
        this.amount = new SimpleStringProperty(amount);
    }
    public String getProvider() {
        return provider.get();
    }

    public SimpleStringProperty providerProperty() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider.set(provider);
    }

    public String getAmount() {
        return this.amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(String.valueOf(amount));
    }

    @Override
    public String toString() {
        return  provider + ": " + amount;
    }
}
