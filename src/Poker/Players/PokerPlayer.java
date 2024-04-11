package Poker.Players;

import Cards.PokerHand;

import java.util.Map;

public abstract class PokerPlayer implements Comparable<PokerPlayer>{

    private final String name;
    protected PokerHand hand = new PokerHand();
    protected double balance;
    protected double bet = 0.0;
    protected boolean playing = true;

    public PokerPlayer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public PokerPlayer(String name) {
        this(name, 0.0);
    }

    public PokerHand getHand() {
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

    public void fold(){
        playing = false;
    }

    public double takeBet(){
        double tempBet = bet;
        bet = 0.0;
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

    public PokerHand calculateBestHand(PokerHand table){
        PokerHand initialHand = hand.copyHand();
        PokerHand tableHand = table.copyHand();
        initialHand.moveAll(tableHand);
        if(initialHand.size() <= 5){
            return initialHand;
        }
        PokerHand bestHand = new PokerHand();
        if(initialHand.size() == 6){
            for (int i = 0; i < initialHand.size(); i++) {
                PokerHand tempHand = initialHand.copyHand();
                tempHand.remove(i);
                if(bestHand.isEmpty() || tempHand.compareTo(bestHand) > 0){
                    bestHand = tempHand;
                }
            }
            return bestHand;
        }
        if(initialHand.size() == 7){
            for (int i = 0; i < initialHand.size(); i++) {
                for (int j = i + 1; j < initialHand.size(); j++) {
                    PokerHand tempHand = initialHand.copyHand();
                    tempHand.remove(j);
                    tempHand.remove(i);
                    if(bestHand.isEmpty() || tempHand.compareTo(bestHand) > 0){
                        bestHand = tempHand;
                    }
                }
            }
            return bestHand;
        }
        throw new IllegalStateException("Hand has more than 7 cards");
    }

    public void startPlaying(){
        playing = true;
    }

    public boolean isPlaying(){
        return playing;
    }

    public int compareTo(PokerPlayer o) {
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

    public abstract void play(PokerHand table, double pot, Map<String, Double> otherBets, double minBet);
}
