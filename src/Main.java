import Poker.Players.BlufferPokerPlayer;
import Poker.Players.RandomPokerPlayer;
import Poker.PokerGame;
import Poker.Players.CallerPokerPlayer;
import Poker.Players.HumanPokerPlayer;
import Services.PlayerService;

public class Main {
    public static void main(String[] args) {
        PlayerService.getInstance().setName("Mario");
        var game = new PokerGame();
        game.addPlayer(new RandomPokerPlayer("Charlie", 100.0, 0.5, 0.2));
        game.addPlayer(new BlufferPokerPlayer("Alice", 100.0));
        game.addPlayer(new HumanPokerPlayer(0.0));
        game.addPlayer(new CallerPokerPlayer("Bob", 100.0));
        game.buyIn("Mario", PlayerService.getInstance().requestBalance());
        game.play();

    }
}