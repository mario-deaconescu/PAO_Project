package services.prompts;

import blackjack.BlackjackGame;
import blackjack.players.*;
import poker.Players.CustomPokerPlayer;
import services.PlayerService;
import services.drivers.CustomBlackjackPlayerDriver;
import services.drivers.CustomPokerPlayerDriver;
import services.drivers.PlayerDriver;
import services.drivers.dto.CustomPokerPlayerDTO;
import utility.ActionPrompt;
import utility.ValuePrompt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class BlackjackService extends PromptService{

    private static final BlackjackService instance = new BlackjackService();

    private BlackjackService() {
        super("Welcome to the blackjack table!");
    }

    public static BlackjackService getInstance() {
        return instance;
    }

    private void play() {
        var game = new BlackjackGame();
        int numPlayers = ValuePrompt.prompt("Enter number of players", Integer::parseInt);
        var takenNames = new ArrayList<>(CustomBlackjackPlayerDriver.getInstance().getAll().stream().map(CustomPokerPlayerDTO::name).toList());
        takenNames.addAll(PlayerDriver.getInstance().getAllUsernames().stream().toList());
        for(int i = 0; i < numPlayers; i++){
            System.out.println(STR."Adding player \{i + 1}");
            System.out.println(STR."Taken names: \{takenNames.toString()}");
            String name = ValuePrompt.prompt("Enter player name", Function.identity());
            double balance = ValuePrompt.prompt("Enter player balance", Double::parseDouble);
            AtomicReference<BlackjackPlayer> player = new AtomicReference<>();
            ActionPrompt.show(List.of("Random", "Limit", "Custom"), List.of(
                    () -> {
                        var temp = new RandomBlackjackPlayer(name);
                        temp.initFromConsole();
                        player.set(temp);
                    },
                    () -> {
                        var temp = new LimitBlackjackPlayer(name);
                        temp.initFromConsole();
                        player.set(temp);
                    },
                    () -> {
                        var temp = new CustomBlackjackPlayer(name);
                        player.set(temp);
                    }
            ));
            game.addPlayer(player.get());
            game.buyIn(name, balance);
        }
        game.addPlayer(new HumanBlackjackPlayer());
        var playerService = PlayerService.getInstance();
        game.buyIn(playerService.getName(), playerService.requestBalance());
        game.play();
        playerService.giveBalance(game.buyOut(playerService.getName()));
    }

    private void editPlayers() {
        System.out.println("Editing players");
        var customBlackjackPlayerDriver = CustomBlackjackPlayerDriver.getInstance();
        var availablePlayers = customBlackjackPlayerDriver.getAll();
        var options = new ArrayList<>(availablePlayers.stream().map(CustomPokerPlayerDTO::name).toList());
        options.add("<New Player>");
        var actions = new ArrayList<>(availablePlayers.stream().map(
                playerDTO -> (Runnable) () -> {
                    System.out.println(STR."Editing player \{playerDTO.name()}");
                    ActionPrompt.show(List.of("Edit", "Delete"), List.of(
                            () -> {
                                CustomBlackjackPlayer.update(playerDTO.name());
                            },
                            () -> {
                                customBlackjackPlayerDriver.delete(playerDTO.name());
                            }
                    ));
                }
        ).toList());
        actions.add(() -> {
            boolean valid = false;
            String name = "";
            while (!valid) {
                name = ValuePrompt.prompt("Enter player name", Function.identity());
                String finalName = name;
                valid = availablePlayers.stream().noneMatch(playerDTO -> playerDTO.name().equals(finalName));
            }
            new CustomBlackjackPlayer(name);
        });
        ActionPrompt.show(options, actions);
    }

    @Override
    protected void prompt(){
        ActionPrompt.show(List.of("Play", "Edit Players", "Exit"), List.of(
                this::play,
                this::editPlayers,
                this::leave
        ));
    }
}
