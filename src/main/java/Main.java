import blackjack.BlackjackGame;
import blackjack.players.CustomBlackjackPlayer;
import blackjack.players.HumanBlackjackPlayer;
import poker.Players.*;
import poker.PokerGame;
import services.AuthService;
import services.PlayerService;
import services.drivers.DatabaseService;
import services.drivers.PlayerDriver;
import services.drivers.PokerLoggingDriver;
import services.prompts.GameService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args) {
        DatabaseService.getInstance().connect();
        var authService = AuthService.getInstance();
        authService.prompt();
        GameService.getInstance().enter();
        authService.exit();
    }
}