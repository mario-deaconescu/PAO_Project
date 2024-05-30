package poker.Players;

import cards.PokerHand;
import utility.ValuePrompt;

import java.util.Map;

public class BlufferPokerPlayer extends CustomizablePokerPlayer{

    private double bluffChance = 0.2;


    public BlufferPokerPlayer(String name, String params){
        super(name, params);
    }
    BlufferPokerPlayer(String name, double balance, double bluffChance){
        super(name, balance);
        this.bluffChance = bluffChance;
    }

    public BlufferPokerPlayer(String name, double balance){
        super(name, balance);
    }

    public BlufferPokerPlayer(String name){
        super(name);
    }

    @Override
    public void initFromConsole() {
        bluffChance = ValuePrompt.prompt("Enter bluff chance", Double::parseDouble);
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

    @Override
    public String getParams() {
        return String.valueOf(bluffChance);
    }

    @Override
    public void setParams(String params) {
        bluffChance = Double.parseDouble(params);
    }
}
