package Poker.Players;

import Cards.PokerHand;

import java.util.Map;

public class BlufferPokerPlayer extends PokerPlayer{

    private double bluffChance = 0.2;

    BlufferPokerPlayer(String name, double balance, double bluffChance){
        super(name, balance);
        this.bluffChance = bluffChance;
    }

    public BlufferPokerPlayer(String name, double balance){
        super(name, balance);
    }

    BlufferPokerPlayer(String name){
        super(name);
    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet){
        if(minBet > balance + bet){
            fold();
        } else if(Math.random() < bluffChance){
            double raise = Math.random() * (balance + bet - minBet);
            setBet(minBet + raise);
        } else {
            setBet(minBet);
        }
    }
}
