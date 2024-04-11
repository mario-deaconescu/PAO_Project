package Poker.Players;

import Cards.PokerHand;

import java.util.Map;

public class RandomPokerPlayer extends PokerPlayer{

    private double callChance = 0.5;
    private double raiseChance = 0.5;

    public RandomPokerPlayer(String name, double balance){
        super(name, balance);
    }

    public RandomPokerPlayer(String name){
        super(name);
    }

    public RandomPokerPlayer(String name, double balance, double callChance, double raiseChance){
        super(name, balance);
        this.callChance = callChance;
        this.raiseChance = raiseChance;
    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet){
        if(minBet > balance + bet){
            fold();
        } else {
            double action = Math.random();
            if(action < raiseChance){
                double raise = Math.random() * (balance + bet - minBet);
                setBet(minBet + raise);
            } else if(action < callChance){
                setBet(minBet);
            } else {
                if(bet < minBet){
                    fold();
                } else {
                    setBet(minBet);
                }
            }
        }
    }
}
