package services;

import exceptions.InvalidLoginException;
import services.drivers.PlayerDriver;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PlayerService {

    private double totalBalance = 0;
    private static final PlayerService instance = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public double takeBalance(double amount) {
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > totalBalance){
            throw new IllegalArgumentException("Amount must be less than total balance");
        }
        totalBalance -= amount;
        return amount;
    }

    public void giveBalance(double amount) {
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        totalBalance += amount;
    }

    public String getName() {
        return PlayerDriver.getInstance().getName();
    }


    public double requestBalance() {
        System.out.println(STR."\{getName()}, your balance is: \{totalBalance}");
        System.out.println("How much would you like to pay?");
        String input = System.console().readLine();
        try{
            double amount = Double.parseDouble(input);
            if(amount < 0){
                throw new IllegalArgumentException("Amount must be positive");
            }
            if(amount > totalBalance){
                throw new IllegalArgumentException("Amount must be less than total balance");
            }
            return takeBalance(amount);
        } catch (NumberFormatException e){
            System.out.println("Invalid input");
            return requestBalance();
        }
    }

    public void retrieveBalance() {
        totalBalance = PlayerDriver.getInstance().getBalance();
    }

    public void updateBalance() {
        PlayerDriver.getInstance().updateBalance(totalBalance);
    }

    public void login(String username, String password) throws InvalidLoginException {
        PlayerDriver.getInstance().login(username, password);
        retrieveBalance();
        System.out.println(STR."Welcome, \{getName()}! Your balance is: \{totalBalance}");
    }

    public void register(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PlayerDriver.getInstance().register(username, password);
        retrieveBalance();
        System.out.println(STR."Welcome, \{getName()}! Your balance is: \{totalBalance}");
    }

    public void logout() {
        updateBalance();
        PlayerDriver.getInstance().logout();
    }
}
