package Poker.Players;

import Cards.PokerHand;

import java.util.Map;

public class CallerPokerPlayer extends PokerPlayer {

    public CallerPokerPlayer(String name, double balance) {
        super(name, balance);
    }

    public CallerPokerPlayer(String name) {
        super(name);
    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet) {
        if(minBet > balance + bet){
            fold();
        } else {
            setBet(minBet);
        }
    }
}
