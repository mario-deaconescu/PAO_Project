package blackjack.players;

import blackjack.BlackjackAction;
import cards.Card;
import utility.ValuePrompt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class LimitBlackjackPlayer extends CustomizableBlackjackPlayer{

    private int limit;

    public LimitBlackjackPlayer(String name, String params) {
        super(name, params);
    }

    public LimitBlackjackPlayer(String name) {
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
        if(hand.getValue() < limit){
            return BlackjackAction.HIT;
        } else {
            return BlackjackAction.STAND;
        }
    }

    @Override
    public String getParams() {
        return String.valueOf(limit);
    }

    @Override
    public void setParams(String params) {
        limit = Integer.parseInt(params);
    }

    @Override
    public void initFromConsole() {
        limit = ValuePrompt.prompt("Enter limit", Integer::parseInt);
    }
}
