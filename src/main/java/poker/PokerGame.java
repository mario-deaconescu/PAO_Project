package poker;

import cards.CardDeck;
import cards.CardHand;
import cards.PokerHand;
import poker.Players.PokerPlayer;
import services.drivers.PokerLoggingDriver;

import java.util.*;
import java.util.stream.Collectors;

public final class PokerGame {

    private final List<PokerPlayer> players = new ArrayList<>();

    private final CardDeck deck = new CardDeck();
    private final PokerHand table = new PokerHand();
    private double bet = 0.0;
    private Map<String, Double> pot = new HashMap<>();
    private final List<PokerRound> rounds = new ArrayList<>();

    public void addPlayer(PokerPlayer player) {
        players.add(player);
    }

    public void removePlayer(PokerPlayer player) {
        players.remove(player);
    }

    public double getBet() {
        return bet;
    }

    private Map<String, Double> getOtherBets(PokerPlayer player) {
        return players.stream()
                .filter(p -> !Objects.equals(p, player) && p.isPlaying())
                .collect(Collectors.toMap(PokerPlayer::getName, PokerPlayer::getBet));
    }

    private void takePlayerBet(PokerPlayer player) {
        pot.put(player.getName(), pot.getOrDefault(player.getName(), 0.0) + player.takeBet());
    }

    private double totalPot() {
        return pot.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private void givePot(PokerPlayer player) {
        for (PokerPlayer p : players) {
            takePlayerBet(p);
        }
        player.giveBalance(totalPot());
        bet = 0.0;
        for (PokerPlayer otherPlayer : players) {
            otherPlayer.fold();
        }
        System.out.println(STR."\{player.getName()} wins \{totalPot()} (\{player.calculateBestHand(table).getCategory()})");
    }

    private boolean checkTable() {
        // Check if there is only one player left
        var winners = players.stream().filter(PokerPlayer::isPlaying).toList();
        if (winners.size() == 1) {
            givePot(winners.getFirst());
            return false;
        }
        return true;
    }

    private boolean bettingRound() {
        bet = 0.0;
        Map<String, Boolean> pending = players.stream().collect(Collectors.toMap(PokerPlayer::getName, PokerPlayer::isPlaying));
        do {
            for (PokerPlayer player : players) {
                if (!player.isPlaying())
                    continue;
                player.play(table, totalPot(), getOtherBets(player), bet);
                if (!player.isPlaying()) {
                    // Player folded
                    takePlayerBet(player);
                }
                if (!checkTable()) {
                    return false;
                }
                pending.put(player.getName(), false);
                if (player.getBet() > bet) {
                    bet = player.getBet();
                    // Mark all other players as pending
                    for (PokerPlayer otherPlayer : players) {
                        if (!Objects.equals(otherPlayer, player) && otherPlayer.isPlaying()) {
                            pending.put(otherPlayer.getName(), true);
                        }
                    }
                }
                pending.put(player.getName(), false);
            }
        } while (pending.containsValue(true));
        for (PokerPlayer player : players) {
            takePlayerBet(player);
        }
        return true;
    }

    private void showHands() {
        System.out.println("Table: " + table);
        for (PokerPlayer player : players) {
            if (player.isPlaying()) {
                System.out.println(player.getName() + ": " + player.getHand());
                System.out.println(player.getName() + " best hand: " + player.calculateBestHand(table));
            }
        }
    }

    private void splitPot(List<PokerPlayer> winners) {
        double potContributionOfWinners = winners.stream()
                .mapToDouble(player -> pot.get(player.getName()))
                .sum();
        for (PokerPlayer winner : winners) {
            double share = pot.get(winner.getName()) / potContributionOfWinners;
            double winnings = share * totalPot();
            winner.giveBalance(winnings);
            System.out.println(STR."\{winner.getName()} wins \{winnings} (\{winner.calculateBestHand(table).getCategory()})");
        }
        bet = 0.0;
        pot = new HashMap<>();
        for (PokerPlayer player : players) {
            player.fold();
        }
    }

    private void showdown() {
        showHands();
        // Sort players by best hand
        Map<PokerPlayer, PokerHand> bestHands = players.stream()
                .filter(PokerPlayer::isPlaying)
                .collect(Collectors.toMap(player -> player, player -> player.calculateBestHand(table)));
        List<PokerPlayer> winners = new ArrayList<>(players.stream()
                .filter(PokerPlayer::isPlaying)
                .toList());
        winners.sort((p1, p2) -> bestHands.get(p2).compareTo(bestHands.get(p1)));
        int bestHandValue = bestHands.get(winners.getFirst()).getHandValue();
        List<PokerPlayer> winnersWithBestHand = winners.stream()
                .filter(player -> bestHands.get(player).getHandValue() == bestHandValue)
                .toList();
        if (winnersWithBestHand.size() == 1) {
            givePot(winnersWithBestHand.getFirst());
        } else {
            splitPot(winnersWithBestHand);
        }
    }

    private void addRound(PokerRound.Round round) {
        rounds.add(new PokerRound(round, table.copyHand().getCards(), players.stream()
                .collect(Collectors.toMap(PokerPlayer::getName, PokerPlayer::getPlayerState))));
    }

    public void play() {
        if (players.size() < 2) {
            throw new IllegalStateException("Not enough players");
        }
        rounds.clear();
        // Prepare the deck
        deck.shuffle();
        List<CardHand> hands = deck.dealHands(2, players.size());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getHand().moveAll(hands.get(i));
            players.get(i).startPlaying();
        }
        // First round of betting
        boolean continueGame = bettingRound();
        addRound(PokerRound.Round.PREFLOP);
        if (!continueGame) {
            return;
        }
        // Deal the flop
        table.moveAll(deck.dealStack(3));
        // Second round of betting
        continueGame = bettingRound();
        addRound(PokerRound.Round.FLOP);
        if (!continueGame) {
            return;
        }
        // Deal the turn
        table.push(deck.pop());
        // Third round of betting
        continueGame = bettingRound();
        addRound(PokerRound.Round.TURN);
        if (!continueGame) {
            return;
        }
        // Deal the river
        table.push(deck.pop());
        // Fourth round of betting
        continueGame = bettingRound();
        addRound(PokerRound.Round.RIVER);
        if (!continueGame) {
            return;
        }
        // Showdown
        showdown();
        PokerLoggingDriver.getInstance().createPokerGame(rounds, players.size());
    }

    public void buyIn(String name, double balance) {
        PokerPlayer player = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.giveBalance(balance);
    }

    public double buyOut(String name) {
        PokerPlayer player = players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        double balance = player.getBalance();
        players.remove(player);
        return balance;
    }
}
