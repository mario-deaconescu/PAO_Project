package poker.Players;

import cards.PokerHand;

import java.util.Map;

public class CallerPokerPlayer extends CustomizablePokerPlayer{

    public CallerPokerPlayer(String name, String params) {
        super(name, params);
    }

    public CallerPokerPlayer(String name, double balance) {
        super(name, balance);
    }

    public CallerPokerPlayer(String name) {
        super(name);
    }

    @Override
    public void initFromConsole() {

    }

    @Override
    public void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet) {
        if(minBet > balance + bet){
            fold();
        } else {
            setBet(minBet);
        }
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public void setParams(String params) {

    }
}
