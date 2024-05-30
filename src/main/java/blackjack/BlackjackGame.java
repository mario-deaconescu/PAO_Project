package blackjack;

import blackjack.players.BlackjackPlayer;
import cards.BlackjackHand;
import cards.Card;
import cards.CardDeck;
import poker.Players.PokerPlayer;

import java.util.*;

public class BlackjackGame {

    private final List<BlackjackPlayer> players = new ArrayList<>();

    private final CardDeck deck = new CardDeck();
    private final BlackjackHand dealerHand = new BlackjackHand();
    private Map<String, Double> bets = new HashMap<>();

    public void addPlayer(BlackjackPlayer player) {
        players.add(player);
    }

    public void removePlayer(BlackjackPlayer player) {
        players.remove(player);
    }

    public void play(){
        if(players.isEmpty()){
            System.out.println("Not enough players to start the game");
            return;
        }
        deck.shuffle();
        for(BlackjackPlayer player : players){
            player.initialBet();
            bets.put(player.getName(), player.takeBet());
        }
        for (BlackjackPlayer player : players) {
            player.hit(deck);
            player.hit(deck);
        }
        dealerHand.push(deck.pop());
        dealerHand.push(deck.pop());
        HashMap<String, Collection<Card>> playerHands = new HashMap<>();
        for (BlackjackPlayer player : players) {
            var action = player.play(dealerHand.get(0), playerHands);
            while(action == BlackjackAction.HIT){
                if(player.isBusted()){
                    System.out.println(STR."\{player.getName()} busts");
                    player.stand();
                    break;
                }
                player.hit(deck);
                action = player.play(dealerHand.get(0), playerHands);
            }
            if(action == BlackjackAction.DOUBLE_DOWN){
                player.doubleDown(deck);
                if(player.isBusted()){
                    System.out.println(STR."\{player.getName()} busts");
                }
            }
            player.stand();
            playerHands.put(player.getName(), player.getHand().getCards());
        }
        while(dealerHand.getValue() < 17){
            dealerHand.push(deck.pop());
        }
        System.out.println(STR."Dealer's hand: \{dealerHand}");
        for (BlackjackPlayer player : players) {
            if(player.isBusted()){
                continue;
            }
            if(dealerHand.getValue() > 21 || player.getHand().getValue() > dealerHand.getValue()){
                player.giveBalance(bets.get(player.getName()) * 2);
                System.out.println(STR."\{player.getName()} wins");
            } else if(player.getHand().getValue() == dealerHand.getValue()){
                player.giveBalance(bets.get(player.getName()));
                System.out.println(STR."\{player.getName()} ties");
            } else {
                System.out.println(STR."\{player.getName()} loses");
            }
        }
    }

    public void buyIn(String name, double balance) {
        BlackjackPlayer player = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.giveBalance(balance);
    }

    public double buyOut(String name) {
        BlackjackPlayer player = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        double balance = player.getBalance();
        players.remove(player);
        return balance;
    }
}


