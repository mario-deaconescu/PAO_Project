package blackjack.players;

import blackjack.BlackjackAction;
import cards.Card;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class RandomBlackjackPlayer extends CustomizableBlackjackPlayer{

    private double hitChance;

    public RandomBlackjackPlayer(String name, String params) {
        super(name, params);
    }

    public RandomBlackjackPlayer(String name) {
        super(name);
    }

    @Override
    public void initialBet() {
        if(balance == 0){
            return;
        }
        Random rand = new Random();
        bet = rand.nextInt((int) balance);
    }

    @Override
    public BlackjackAction play(Card dealerCard, HashMap<String, Collection<Card>> otherPlayersHands) {
        Random rand = new Random();
        if(hand.getValue() >= 21){
            return BlackjackAction.STAND;
        }
        if(rand.nextDouble() < hitChance){
            return BlackjackAction.HIT;
        } else {
            return BlackjackAction.STAND;
        }
    }

    @Override
    public String getParams() {
        return String.valueOf(hitChance);
    }

    @Override
    public void setParams(String params) {
        hitChance = Double.parseDouble(params);
    }

    @Override
    public void initFromConsole() {
        System.out.println("Enter hit chance:");
        hitChance = Double.parseDouble(System.console().readLine());
    }
}
