package com.example.personalfinanceproject;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class FinanceMainController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button calculateBudget;
    @FXML
    private TextField totalIncomeField;
    @FXML
    private VBox incomeTableViewContainer;
    @FXML
    private VBox billsTableViewContainer;
    @FXML
    private Button addIncomeButton;
    @FXML
    private TextField addIncomeNameField;
    @FXML
    private TextField addIncomeAmountField;
    @FXML
    private Button removeIncomeButton;
    @FXML
    private TextField removeIncomeNameField;
    @FXML
    private TextField removeIncomeAmountField;
    @FXML
    private Button addBillButton;
    @FXML
    private TextField addBillNameField;
    @FXML
    private TextField addBillAmountField;
    @FXML
    private Button removeBillButton;
    @FXML
    private TextField removeBillNameField;
    @FXML
    private TextField removeBillAmountField;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearIncomeButton;
    @FXML
    private Button clearBillsButton;
    @FXML
    private TextField totalBillsField;
    @FXML
    private TextField fundsLeftAfterDeduction;
    @FXML
    private Label dateTime;
    @FXML
    private Label daysToNextPay;
    @FXML
    private TextField daysToNextPayField;
    @FXML
    private TextArea dailySpend;
    @FXML
    private TextArea weeklySpend;
    @FXML

    /* Tables view, seen on GUI */
    private TableView<Bills> billsTableView;
    private TableView<Income> incomeTableView;

    /* Lists are set to take data and update data for Income and Bills \\ */

    private final ObservableList<Income> providerData = FXCollections.observableArrayList();
    private ObservableList<Income> providerResults = FXCollections.observableArrayList(providerData);
    private final ObservableList<Bills> masterData = FXCollections.observableArrayList();
    private ObservableList<Bills> billResults = FXCollections.observableArrayList();
    private int totalIncomeFieldHasValue;
    private int totalBillsFieldHasValue;
    private static final String IS_NUMERIC = "[+-]?\\d*(\\.\\d+)?";
    private static final String ERROR_SET = "Error!";
    private static final String ERROR_INPUT_CHECK = "Error! Check your input! \n" + "Format must use  NAME - AMOUNT";


    public FinanceMainController() { /* Adding some example bills and income */
        masterData.add(new Bills("test", "500"));
        providerData.add(new Income("TestIncome", "500"));
    }


    @FXML
    public void initialize() {
        initClock();
        setActions();
        setSearch();
        initBillsTable();
        initIncomeTable();
        billDeduction();
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss" + "        dd-MM-yyyy ");
            dateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void calculateBudget() {
        LocalDate todayDate = LocalDate.now();

        if (fundsLeftAfterDeduction.getText().equals("0") || datePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ERROR_SET);
            alert.setHeaderText(null);
            alert.setContentText(ERROR_INPUT_CHECK);
            alert.showAndWait();

        } else {

            LocalDate untilNextPay = datePicker.getValue();
            int diff = (int) ChronoUnit.DAYS.between(todayDate, untilNextPay);
            daysToNextPayField.setText(String.valueOf(diff));

            double result = Double.parseDouble(fundsLeftAfterDeduction.getText()) / diff;

            double resultWeek = Double.parseDouble(fundsLeftAfterDeduction.getText()) / diff * 7;

            dailySpend.setText("You are able to spend: " + "£" + String.format("%.2f", result) + " daily." + "\n" + "There are: " + daysToNextPayField.getText() + " days until next Pay.");

            if (resultWeek > Integer.parseInt(fundsLeftAfterDeduction.getText())) {
                weeklySpend.setText("Error calculating weekly.");

            } else {

                System.out.println(resultWeek);
                weeklySpend.setText("You are able to spend: " + String.format("%.2f", resultWeek) + " Per week");
            }

        }

    }


    public void setActions() {
        addBillButton.setOnAction(actionEvent -> addBill());
        addIncomeButton.setOnAction(actionEvent -> addIncome());
        removeBillButton.setOnAction(actionEvent -> removeBill());
        removeIncomeButton.setOnAction(actionEvent -> removeIncome());
        clearBillsButton.setOnAction(actionEvent -> clearBillsTable());
        clearIncomeButton.setOnAction(actionEvent -> clearIncomeTable());
        calculateBudget.setOnAction(actionEvent -> calculateBudget());
    }

    public void setSearch() {
        searchButton.setText("Search");
        searchButton.setOnAction(actionEvent -> loadBillData());
        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                loadBillData();
                loadIncomeData();
            }
        });
    }

    public void totalIncome() {
        int incomeTotal = 0;
        int i;
        i = 0;
        while (i < incomeTableView.getItems().size()) {
            String valueOfIncomeTable = incomeTableView.getItems().get(i).getAmount();
            incomeTotal = incomeTotal + Integer.parseInt(valueOfIncomeTable);
            System.out.println(valueOfIncomeTable);
            i++;
        }
        totalIncomeFieldHasValue = incomeTotal;
        totalIncomeField.setText(String.valueOf(incomeTotal));

    }

    public void totalBills() {

        int billTotal = 0;

        for (int j = 0; j < billsTableView.getItems().size(); j++) {
            String valueOfBillTable = billsTableView.getItems().get(j).getAmount();
            billTotal = billTotal + Integer.parseInt(valueOfBillTable);
            System.out.println(valueOfBillTable);
        }
        totalBillsFieldHasValue = billTotal;
        totalBillsField.setText(String.valueOf(billTotal));

    }

    public void billDeduction() {

        int totalDeduction = totalIncomeFieldHasValue - totalBillsFieldHasValue;
        fundsLeftAfterDeduction.setText(String.valueOf(totalDeduction));
    }


    public void removeBill() {
        boolean isANumber = removeBillAmountField.getText().matches(IS_NUMERIC);
        String removeBillName = removeBillNameField.getText().toLowerCase();
        String removeBillAmount = removeBillAmountField.getText().toLowerCase();

        if (removeBillNameField.getText().isEmpty() || removeBillAmount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ERROR_SET);
            alert.setHeaderText(null);
            alert.setContentText(ERROR_INPUT_CHECK);
            alert.showAndWait();

        } else if (!removeBillName.isEmpty() && isANumber) {
            masterData.removeIf(value -> value.getBills().toLowerCase().equals(removeBillName) && value.getAmount().toLowerCase().equals(removeBillAmount));
            loadBillData();
        }
        removeBillNameField.clear();
        removeBillAmountField.clear();
    }


    public void removeIncome() {
        boolean isANumber = removeIncomeAmountField.getText().matches(IS_NUMERIC);
        String removeIncomeName = removeIncomeNameField.getText().toLowerCase();
        String removeIncomeAmount = removeIncomeAmountField.getText().toLowerCase();

        if (removeIncomeNameField.getText().isEmpty() || removeIncomeAmount.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ERROR_SET);
            alert.setHeaderText(null);
            alert.setContentText(ERROR_INPUT_CHECK);
            alert.showAndWait();

        } else if (!removeIncomeName.isEmpty() && isANumber) {
            providerData.removeIf(value -> value.getProvider().toLowerCase().equals(removeIncomeName) && value.getAmount().toLowerCase().equals(removeIncomeAmount));
            loadIncomeData();
        }
        removeIncomeNameField.clear();
        removeIncomeAmountField.clear();
    }


    public void addBill() {
        boolean amountIsNumeric = addBillAmountField.getText().matches(IS_NUMERIC);

        if (addBillNameField.getText().isEmpty() && addBillAmountField.getText().isEmpty()) {
            System.out.println("Error, please check inputs.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ERROR_SET);
            alert.setHeaderText(null);
            alert.setContentText(ERROR_INPUT_CHECK);
            alert.showAndWait();

        } else if (!addBillNameField.getText().isEmpty() && amountIsNumeric) {
            masterData.add(new Bills(addBillNameField.getText(), addBillAmountField.getText()));
            loadBillData();
        }

        addBillNameField.clear();
        addBillAmountField.clear();
    }


    public void addIncome() {
        boolean amountIsNumeric = addIncomeAmountField.getText().matches(IS_NUMERIC);

        if (addIncomeNameField.getText().isEmpty() && addIncomeAmountField.getText().isEmpty()) {
            System.out.println("Error, please check inputs.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ERROR_SET);
            alert.setHeaderText(null);
            alert.setContentText(ERROR_INPUT_CHECK);
            alert.showAndWait();

        } else if (!addIncomeNameField.getText().isEmpty() && amountIsNumeric) {
            providerData.add(new Income(addIncomeNameField.getText(), addIncomeAmountField.getText()));
            loadIncomeData();
        }
        addIncomeNameField.clear();
        addIncomeAmountField.clear();
    }


    public void clearIncomeTable() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm you want to clear the Income Table?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == (ButtonType.OK)) {
            providerData.clear();
            System.out.println("Cleared");
            loadIncomeData();
        } else {
            loadIncomeData();
        }

    }

    public void clearBillsTable() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm you want to clear the Bills Table?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == (ButtonType.OK)) {
            masterData.clear();
            System.out.println("Cleared");
            loadBillData();
        } else {
            loadBillData();
        }

    }


    private void initBillsTable() {

        final String amount = "Amount";
        billsTableView = new TableView<>(FXCollections.observableList(masterData));
        billsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Bills, String> outGoingBills = new TableColumn<>("Bills");
        outGoingBills.setCellValueFactory(new PropertyValueFactory<>("Bills"));

        TableColumn<Bills, Integer> billAmounts = new TableColumn<>(amount);
        billAmounts.setCellValueFactory(new PropertyValueFactory<>(amount));

        billsTableView.getColumns().add(outGoingBills);
        billsTableView.getColumns().add(billAmounts);
        billsTableViewContainer.getChildren().add(billsTableView);
        billsTableView.setEditable(true);
        totalBills();
        billDeduction();
    }

    private void initIncomeTable() {

        final String amount = "Amount";
        incomeTableView = new TableView<>(FXCollections.observableList(providerData));
        incomeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Income, String> incomeProvider = new TableColumn<>("Provider");
        incomeProvider.setCellValueFactory(new PropertyValueFactory<>("Provider"));

        TableColumn<Income, Integer> providerAmounts = new TableColumn<>(amount);
        providerAmounts.setCellValueFactory(new PropertyValueFactory<>(amount));

        incomeTableView.getColumns().add(incomeProvider);
        incomeTableView.getColumns().add(providerAmounts);
        incomeTableViewContainer.getChildren().add(incomeTableView);
        billsTableView.setEditable(true);
        totalIncome();
        billDeduction();
    }


    private void loadIncomeData() {
        String searchText = searchField.getText();

        Task<ObservableList<Income>> incomeTask = new Task<>() {
            @Override
            protected ObservableList<Income> call() {
                updateMessage("Loading Data");
                List<Income> list = new ArrayList<>();
                for (Income value : providerData) {
                    if (value.getProvider().toLowerCase().contains(searchText)) {
                        list.add(value);
                    }
                }
                return FXCollections.observableArrayList(list);
            }
        };
        incomeTask.setOnSucceeded(event -> {
            providerResults = incomeTask.getValue();
            incomeTableView.setItems(FXCollections.observableList(providerResults));
            totalIncome();
            billDeduction();
        });

        Thread th1 = new Thread(incomeTask);
        th1.setDaemon(true);
        th1.start();
    }

    private void loadBillData() {
        String searchText = searchField.getText();

        Task<ObservableList<Bills>> billTask = new Task<>() {
            @Override
            protected ObservableList<Bills> call() {
                updateMessage("Loading data");
                List<Bills> list = new ArrayList<>();
                for (Bills value : masterData) {
                    if (value.getBills().toLowerCase().contains(searchText)) {
                        list.add(value);
                    }
                }
                return FXCollections.observableArrayList(list);
            }
        };

        billTask.setOnSucceeded(event -> {
            billResults = billTask.getValue();
            billsTableView.setItems(FXCollections.observableList(billResults));
            totalBills();
            billDeduction();
        });

        Thread th = new Thread(billTask);
        th.setDaemon(true);
        th.start();

    }

}


