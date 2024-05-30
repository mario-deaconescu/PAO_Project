package poker.Players;

import cards.PokerHand;
import services.drivers.CustomPokerPlayerDriver;
import utility.ActionPrompt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CustomPokerPlayer extends PokerPlayer {

    private CustomizablePokerPlayer actualPlayer;

    private void createCustomPlayer(String name) {
        System.out.println(STR."Creating player \{name}");
        System.out.println("Choose a strategy for the player:");
        System.out.println("1. RandomPokerPlayer");
        System.out.println("2. CallerPokerPlayer");
        System.out.println("3. BlufferPokerPlayer");
        var action = System.console().readLine();
        String strategy = switch (action) {
            case "1" -> {
                actualPlayer = new RandomPokerPlayer(name);
                yield "RandomPokerPlayer";
            }
            case "2" -> {
                actualPlayer = new CallerPokerPlayer(name);
                yield "CallerPokerPlayer";
            }
            case "3" -> {
                actualPlayer = new BlufferPokerPlayer(name);
                yield "BlufferPokerPlayer";
            }
            default -> throw new IllegalArgumentException("Invalid strategy");
        };
        actualPlayer.initFromConsole();
        var customPlayerDriver = CustomPokerPlayerDriver.getInstance();
        customPlayerDriver.insert(name, strategy, actualPlayer.getParams());
        System.out.println(STR."Player \{name} created");
    }

    public static void update(String name){
        System.out.println(STR."Updating player \{name}");
        System.out.println("Choose a strategy for the player:");
        AtomicReference<CustomizablePokerPlayer> player = new AtomicReference<>();
        AtomicReference<String> strategy = new AtomicReference<>();
        ActionPrompt.show(List.of("RandomPokerPlayer", "CallerPokerPlayer", "BlufferPokerPlayer"), List.of(
                () -> {
                    player.set(new RandomPokerPlayer(name));
                    strategy.set("RandomPoketPlayer");
                },
                () -> {
                    player.set(new CallerPokerPlayer(name));
                    strategy.set("CallerPokerPlayer");
                },
                () -> {
                    player.set(new BlufferPokerPlayer(name));
                    strategy.set("BlufferPokerPlayer");
                }
        ));
        player.get().initFromConsole();
        var customPlayerDriver = CustomPokerPlayerDriver.getInstance();
        customPlayerDriver.update(name, strategy.get(), player.get().getParams());
        System.out.println(STR."Player \{name} updated");
    }

    public CustomPokerPlayer(String name) {
        super(name);
        var customPlayerDriver = CustomPokerPlayerDriver.getInstance();
        var player = customPlayerDriver.get(name);
        if (player.isEmpty()) {
            createCustomPlayer(name);
        } else {
            var p = player.get();
            switch (p.strategy()) {
                case "RandomPokerPlayer":
                    actualPlayer = new RandomPokerPlayer(p.name(), p.params());
                    break;
                case "CallerPokerPlayer":
                    actualPlayer = new CallerPokerPlayer(p.name(), p.params());
                    break;
                case "BlufferPokerPlayer":
                    actualPlayer = new BlufferPokerPlayer(p.name(), p.params());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid strategy");
            }
        }
    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet) {
        actualPlayer.play(table, pot, otherBets, minBet);
    }
}
