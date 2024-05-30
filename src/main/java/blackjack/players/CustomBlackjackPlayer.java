package blackjack.players;

import blackjack.BlackjackAction;
import cards.Card;
import poker.Players.BlufferPokerPlayer;
import poker.Players.CallerPokerPlayer;
import poker.Players.CustomizablePokerPlayer;
import poker.Players.RandomPokerPlayer;
import services.drivers.CustomBlackjackPlayerDriver;
import services.drivers.CustomPokerPlayerDriver;
import utility.ActionPrompt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomBlackjackPlayer extends BlackjackPlayer{

    private CustomizableBlackjackPlayer actualPlayer;

    public static void update(String name) {
        System.out.println(STR."Updating player \{name}");
        System.out.println("Choose a strategy for the player:");
        AtomicReference<CustomizableBlackjackPlayer> player = new AtomicReference<>();
        AtomicReference<String> strategy = new AtomicReference<>();
        ActionPrompt.show(List.of("RandomPokerPlayer", "LimitBlackjackPlayer"), List.of(
                () -> {
                    player.set(new RandomBlackjackPlayer(name));
                    strategy.set("RandomBlackjackPlayer");
                },
                () -> {
                    player.set(new LimitBlackjackPlayer(name));
                    strategy.set("LimitBlackjackPlayer");
                }
        ));
        player.get().initFromConsole();
        var customPlayerDriver = CustomBlackjackPlayerDriver.getInstance();
        customPlayerDriver.update(name, strategy.get(), player.get().getParams());
        System.out.println(STR."Player \{name} updated");
    }

    private void createCustomPlayer(String name) {
        System.out.println(STR."Creating player \{name}");
        System.out.println("Choose a strategy for the player:");
        System.out.println("1. RandomBlackjackPlayer");
        System.out.println("2. LimitBlackjackPlayer");
        var action = System.console().readLine();
        String strategy = switch (action) {
            case "1" -> {
                actualPlayer = new RandomBlackjackPlayer(name);
                yield "RandomBlackjackPlayer";
            }
            case "2" -> {
                actualPlayer = new LimitBlackjackPlayer(name);
                yield "LimitBlackjackPlayer";
            }
            default -> throw new IllegalArgumentException("Invalid strategy");
        };
        actualPlayer.initFromConsole();
        var customPlayerDriver = CustomBlackjackPlayerDriver.getInstance();
        customPlayerDriver.insert(name, strategy, actualPlayer.getParams());
        System.out.println(STR."Player \{name} created");
    }

    public CustomBlackjackPlayer(String name){
        super(name);
        var customPlayerDriver = CustomBlackjackPlayerDriver.getInstance();
        var player = customPlayerDriver.get(name);
        if (player.isEmpty()) {
            createCustomPlayer(name);
        } else {
            var p = player.get();
            switch (p.strategy()) {
                case "RandomBlackjackPlayer":
                    actualPlayer = new RandomBlackjackPlayer(p.name(), p.params());
                    break;
                case "LimitBlackjackPlayer":
                    actualPlayer = new LimitBlackjackPlayer(p.name(), p.params());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid strategy");
            }
        }
    }

    @Override
    public void initialBet() {
        actualPlayer.initialBet();
    }

    @Override
    public BlackjackAction play(Card dealerCard, HashMap<String, Collection<Card>> otherPlayersHands) {
        return actualPlayer.play(dealerCard, otherPlayersHands);
    }
}
