package cards;

import java.util.*;

public final class CardDeck extends CardStack{

    public CardDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                push(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public CardHand dealHand(int count){
        CardHand hand = new CardHand();
        for (int i = 0; i < count; i++) {
            hand.push(pop());
        }
        return hand;
    }

    public CardStack dealStack(int count){
        CardStack stack = new CardStack();
        for (int i = 0; i < count; i++) {
            stack.push(pop());
        }
        return stack;
    }

    public List<CardHand> dealHands(int handSize, int handCount){
        List<CardHand> hands = new ArrayList<>();
        for (int i = 0; i < handCount; i++) {
            hands.add(dealHand(handSize));
        }
        return hands;
    }
}
