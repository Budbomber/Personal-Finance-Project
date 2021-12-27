package com.example.personalfinanceproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class FinanceMainController {
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
    private Button removeIncome;
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


    private TableView<Bills> billsTableView;
    private TableView<Income> incomeTableView;

    /* Lists are set to take data and update data for Income and Bills \\ */

    private final ObservableList<Income> providerData = FXCollections.observableArrayList();
    private ObservableList<Income> providerResults = FXCollections.observableArrayList(providerData);
    private final ObservableList<Bills> masterData = FXCollections.observableArrayList();
    private ObservableList<Bills> billResults = FXCollections.observableArrayList();


    public FinanceMainController() { /* Adding some example bills and income */
        masterData.add(new Bills("Virgin Media", "50"));
        masterData.add(new Bills("Car insurance", "33"));
        masterData.add(new Bills("vet fees", "10000"));

        providerData.add(new Income("Hertz", "1500"));
        providerData.add(new Income("Dog", "900"));
        providerData.add(new Income("Carrots", "100"));
    }


    @FXML
    public void initialize() {
        setActions();
        setSearch();
        initBillsTable();
        initIncomeTable();
    }

    public void setActions() {
        addBillButton.setOnAction(actionEvent -> addBill());
        addIncomeButton.setOnAction(actionEvent -> addIncome());
        removeBillButton.setOnAction(actionEvent -> removeBill());
        removeIncome.setOnAction(actionEvent -> removeIncome());
        clearBillsButton.setOnAction(actionEvent -> clearBillsTable());
        clearIncomeButton.setOnAction(actionEvent -> clearIncomeTable());
    }

    public void setSearch() {
        searchButton.setText("Search");
        searchButton.setOnAction(actionEvent -> loadBillData());
        searchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");
        searchField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                loadBillData();
                loadIncomeData();
            }
        });
    }

    public void totalIncome() {
        int total = 0;
        int i;
        for (i = 0; i < incomeTableView.getItems().size(); i++) {
            String valueOf = incomeTableView.getItems().get(i).getAmount();

            total = total + Integer.parseInt(valueOf);

            System.out.println(valueOf);
        }
        totalIncomeField.setText(String.valueOf(total));

    }
    public void totalBills() {

        int billTotal = 0;
        int i;
        for (i = 0; i < billsTableView.getItems().size(); i++) {
            String valueOf = billsTableView.getItems().get(i).getAmount();

            billTotal = billTotal + Integer.parseInt(valueOf);

            System.out.println(valueOf);
        }
        totalBillsField.setText(String.valueOf(billTotal));

    }



    public void removeBill() {
        String removeBillName = removeBillNameField.getText().toLowerCase();
        String removeBillAmount = removeBillAmountField.getText();

        if (removeBillName.isEmpty() || removeBillAmount.isEmpty()) {
            System.out.println("Error, please check inputs.");
        } else {
            masterData.removeIf(value -> value.getBills().toLowerCase().equals(removeBillName));
            masterData.removeIf(value -> value.getAmount().toLowerCase().equals(removeBillAmount));
            loadBillData();
        }

    }

    public void removeIncome() {
        String removeIncomeName = removeIncomeNameField.getText().toLowerCase();
        String removeIncomeAmount = removeIncomeAmountField.getText().toLowerCase();

        if (removeIncomeName.isEmpty() || removeIncomeAmount.isEmpty()) {
            System.out.println("Error, please check inputs.");
        } else {
            providerData.removeIf(value -> value.getProvider().toLowerCase().equals(removeIncomeName));
            providerData.removeIf(value -> value.getProvider().toLowerCase().equals(removeIncomeAmount));
            loadIncomeData();
        }

    }


    public void addBill() {
        if (addBillNameField.getText().isEmpty() || addBillAmountField.getText().isEmpty()) {
            System.out.println("Error, Please check inputs");
            //TODO Add a better method

        } else {
            masterData.add(new Bills(addBillNameField.getText(), addBillAmountField.getText()));
            loadBillData();
        }

    }

    public void addIncome() {
        if (addIncomeNameField.getText().isEmpty() || addIncomeAmountField.getText().isEmpty()) {
            System.out.println("Error Adding income value");
            //TODO Add a better method
        } else {
            providerData.add(new Income(addIncomeNameField.getText(), addIncomeAmountField.getText()));
            loadIncomeData();
        }

    }
    public void clearIncomeTable() {
        providerData.clear();
        loadIncomeData();
    }

    public void clearBillsTable() {
        masterData.clear();
        loadBillData();
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
        });

        Thread th = new Thread(billTask);
        th.setDaemon(true);
        th.start();

    }

}


