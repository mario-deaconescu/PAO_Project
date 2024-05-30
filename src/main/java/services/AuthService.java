package services;

import exceptions.InvalidLoginException;
import utility.ActionPrompt;
import utility.ValuePrompt;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class AuthService {

    private static final AuthService instance = new AuthService();

    private AuthService() {
    }

    public static AuthService getInstance() {
        return instance;
    }

    public void login() {
        var username = ValuePrompt.prompt("Enter your username:", s -> s);
        var password = ValuePrompt.promptPassword("Enter your password:");
        try {
            PlayerService.getInstance().login(username, password);
        } catch (InvalidLoginException e) {
            System.out.println("Invalid login");
            login();
        }
    }

    public void register() {
        var username = ValuePrompt.prompt("Enter your username:", s -> s);
        var password = ValuePrompt.promptPassword("Enter your password:");
        try {
            PlayerService.getInstance().register(username, password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Error registering");
            register();
        }
    }

    public void prompt(){
        System.out.println("Welcome to the casino!");
        ActionPrompt.show(List.of("Login", "Register"), List.of(
                this::login,
                this::register
        ));
    }

    public void exit(){
        System.out.println("Goodbye!");
        PlayerService.getInstance().logout();
    }
}
