/**

 The WalletApp class represents a JavaFX application for managing bank accounts and wallets.
 It provides methods for switching scenes, displaying error dialogs, and handling file operations.
 */

package it.kolleg.sample;

import Exceptions.RetrieveDataException;
import Exceptions.SaveDataException;
import domain.BankAccount;
import domain.DataStore;
import domain.WalletList;
import infrastruktur.CurrentCurrencyPrices;
import infrastruktur.FileDataStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ui.GlobalContext;

import java.io.IOException;
import java.util.ResourceBundle;

public class WalletApp extends Application {

    private static Stage mainStage;
    public static final String GLOBAL_WALLET_LIST = "walletlist";
    public static final String GLOBAL_BANK_ACCOUNT = "bankaccount";
    public static final String GLOBAL_CURRENT_CURRENCY_PRICES = "currencyprices";
    public static String GLOBAL_SELECTED_WALLET = "selectedWallet";

    /**
     * Switches the scene to the specified FXML file using the given resource bundle.
     *
     * @param fxmlFile       The FXML file to load.
     * @param resourceBundle The resource bundle for localization.
     */
    public static void switchScene(String fxmlFile, String resourceBundle) {
        try {
            Parent root = FXMLLoader.load(WalletApp.class.getResource(fxmlFile), ResourceBundle.getBundle(resourceBundle));
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception ioException) {
            WalletApp.showErrorDialog("Could not load new scene!");
            ioException.printStackTrace();
        }
    }

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The error message to display.
     */
    public static void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("An exception occurred: " + message);
        alert.showAndWait();
    }

    /**
     * Loads the bank account from a file.
     *
     * @return The loaded bank account.
     * @throws RetrieveDataException If an error occurs while retrieving the data.
     */
    private BankAccount loadBankAccountFromFile() throws RetrieveDataException {
        DataStore dataStore = new FileDataStore();
        BankAccount bankAccount = dataStore.retrieveBankAccount();
        System.out.println("Bankaccount loaded");
        return bankAccount;
    }

    /**
     * Loads the wallet list from a file.
     *
     * @return The loaded wallet list.
     * @throws RetrieveDataException If an error occurs while retrieving the data.
     */
    private WalletList loadWalletListFromFile() throws RetrieveDataException {
        DataStore dataStore = new FileDataStore();
        WalletList walletList = dataStore.retrieveWalletList();
        System.out.println("Walletlist loaded");
        return walletList;
    }

    /**
     * Stores the bank account to a file.
     *
     * @param bankAccount The bank account to store.
     * @throws SaveDataException If an error occurs while saving the data.
     */
    private void storeBankAccountToFile(BankAccount bankAccount) throws SaveDataException {
        DataStore dataStore = new FileDataStore();
        dataStore.saveBankAccount(bankAccount);
    }

    /**
     * Stores the wallet list to a file.
     *
     * @param walletList The wallet list to store.
     * @throws SaveDataException If an error occurs while saving the data.
     */
    private void storeWalletListToFile(WalletList walletList) throws SaveDataException {
        DataStore dataStore = new FileDataStore();
        dataStore.saveWalletList(walletList);
    }

    /**
     * Starts the JavaFX application by loading the main stage, bank account, and wallet list.
     *
     * @param stage The main stage for the application.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        BankAccount bankAccount = (BankAccount) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_BANK_ACCOUNT);
        WalletList walletList = (WalletList) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_WALLET_LIST);

        try {
            bankAccount = loadBankAccountFromFile();
        } catch (RetrieveDataException retrieveDataException) {
            WalletApp.showErrorDialog("Error on loading BankAccount Data. Using new empty BankAccount!");
            retrieveDataException.printStackTrace();
        }

        try {
            walletList = loadWalletListFromFile();
        } catch (RetrieveDataException retrieveDataException) {
            WalletApp.showErrorDialog("Error on loading WalletList Data. Using new empty WalletList!");
            retrieveDataException.printStackTrace();
        }

        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_WALLET_LIST, walletList);
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_BANK_ACCOUNT, bankAccount);
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_CURRENT_CURRENCY_PRICES, new CurrentCurrencyPrices());

        mainStage.setOnCloseRequest(event -> event.consume());
        WalletApp.switchScene("main.fxml", "it.kolleg.sample.main");
    }

    /**
     * Stops the JavaFX application by storing the bank account and wallet list to files.
     */
    @Override
    public void stop() {
        WalletList walletList = (WalletList) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_WALLET_LIST);
        BankAccount bankAccount = (BankAccount) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_BANK_ACCOUNT);
        try {
            storeBankAccountToFile(bankAccount);
            System.out.println("BankAccount details stored to file!");
            storeWalletListToFile(walletList);
            System.out.println("WalletList details stored to file!");
        } catch (SaveDataException saveDataException) {
            WalletApp.showErrorDialog("Could not store bankaccount and/or walletlist details!");
            saveDataException.printStackTrace();
        }
    }

    /**
     * The main entry point for the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}