package Poker.Players;

import Cards.PokerHand;

import java.util.Map;

import Services.PlayerService;

public class HumanPokerPlayer extends PokerPlayer {

    public HumanPokerPlayer(double balance) {
        super(PlayerService.getInstance().getName(), balance);
    }

    public HumanPokerPlayer() {
        super(PlayerService.getInstance().getName());
    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet) {
        System.out.println("Your turn, " + getName() + "!");
        System.out.println("Table: " + table);
        System.out.println("Your hand: " + hand);
        System.out.println("Your balance: " + balance);
        System.out.println("Current bet: " + bet);
        System.out.println("Minimum bet: " + minBet);
        System.out.println("Pot: " + pot);
        System.out.println("Other bets: ");
        otherBets.forEach((name, bet) -> System.out.println(name + ": " + bet));
        boolean checkOrCall = minBet == bet;
        boolean canRaise = balance + bet > minBet;
        boolean canCall = balance + bet >= minBet;
        if (isAllIn()) {
            System.out.println("You are all in!");
            return;
        }
        if (!canCall) {
            System.out.println("You can't call!");
            System.out.println("Do you want to fold or go all in?");
        } else if (canRaise) {
            System.out.println("Do you want to fold, " + (checkOrCall ? "check, " : "call, ") + "or raise?");
        } else {
            System.out.println("Do you want to fold or " + (checkOrCall ? "check?" : "call?"));
        }
        // Implement user input
        String action = System.console().readLine();
        switch (action) {
            case "fold":
                fold();
                break;
            case "check":
                if (!checkOrCall) {
                    System.out.println("Invalid action");
                    play(table, pot, otherBets, minBet);
                    return;
                }
                break;
            case "call":
                if (!canCall) {
                    System.out.println("Invalid action");
                    play(table, pot, otherBets, minBet);
                    return;
                }
                setBet(minBet);
                break;
            case "raise":
                if (!canRaise) {
                    System.out.println("Invalid action");
                    play(table, pot, otherBets, minBet);
                    return;
                }
                System.out.println("How much do you want to raise?");
                // Implement user input
                double raise = Double.parseDouble(System.console().readLine());
                setBet(minBet + raise);
                break;
            case "all in":
                if (isAllIn()) {
                    System.out.println("Invalid action");
                    play(table, pot, otherBets, minBet);
                    return;
                }
                setBet(balance + bet);
                break;
            default:
                System.out.println("Invalid action");
                play(table, pot, otherBets, minBet);
        }
    }
}
