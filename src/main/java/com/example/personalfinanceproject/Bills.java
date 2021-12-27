package com.example.personalfinanceproject;

import javafx.beans.property.SimpleStringProperty;

public class Bills {


    private final SimpleStringProperty billName;
    private final SimpleStringProperty amount;

    Bills(String billName, String amount) {
        this.billName = new SimpleStringProperty(billName);
        this.amount = new SimpleStringProperty(amount);

    }

    public String getBills() {
        return billName.get();
    }

    public SimpleStringProperty billsProperty() {
        return billName;
    }

    public void setBills(String bills) {
        this.billName.set(bills);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(String.valueOf(amount));
    }

    @Override
    public String toString() {
        return "Bills{ " + billName + "}::" + amount;
    }


}
