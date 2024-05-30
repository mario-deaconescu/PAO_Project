package blackjack.players;

import blackjack.BlackjackAction;
import cards.BlackjackHand;
import cards.Card;
import cards.CardDeck;

import java.util.Collection;
import java.util.HashMap;

public abstract class BlackjackPlayer implements Comparable<BlackjackPlayer>{

    private final String name;
    protected BlackjackHand hand = new BlackjackHand();
    protected double balance;
    protected double bet = 0.0;
    protected boolean playing = true;

    public BlackjackPlayer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public BlackjackPlayer(String name) {
        this(name, 0.0);
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public double getBalance() {
        return balance;
    }

    public void giveBalance(double amount) {
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public double getBet() {
        return bet;
    }

    public void stand(){
        playing = false;
    }

    public void hit(CardDeck deck){
        hand.push(deck.pop());
    }

    public double takeBet(){
        double tempBet = bet;
        bet = 0.0;
        balance -= tempBet;
        return tempBet;
    }

    protected void setBet(double newBet) {
        if(newBet < 0){
            throw new IllegalArgumentException("Bet must be positive");
        }
        if(newBet > balance + bet){
            throw new IllegalArgumentException("Bet must be less than balance");
        }
        this.balance -= newBet - bet;
        this.bet = newBet;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName() + " has " + getBalance() + " and bet " + getBet();
    }

    public void startPlaying(){
        playing = true;
    }

    public boolean isPlaying(){
        return playing;
    }

    public int compareTo(BlackjackPlayer o) {
        // Compare names
        int nameCompare = name.compareTo(o.name);
        if(nameCompare != 0){
            return nameCompare;
        }
        // Compare balances
        return Double.compare(balance, o.balance);
    }

    public boolean isAllIn(){
        return playing && (balance == 0);
    }

    public boolean isBusted(){
        return hand.getValue() > 21;
    }

    public void doubleDown(CardDeck deck){
        if(balance < bet){
            throw new IllegalStateException("Not enough balance to double down");
        }
        bet *= 2;
        hit(deck);
        stand();
    }


    public abstract void initialBet();

    public abstract BlackjackAction play(Card dealerCard, HashMap<String, Collection<Card>> otherPlayersHands);
}
