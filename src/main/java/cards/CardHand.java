package cards;

import java.util.*;

public class CardHand implements CardStorage {

    protected final List<Card> cards = new ArrayList<>();

    public CardHand() {
    }

    public CardHand(CardStorage stack) {
        moveAll(stack);
    }

    public void push(Card card) {
        cards.add(card);
    }

    public Card pop() {
        return cards.removeLast();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    public Card peek() {
        return cards.getLast();
    }

    public void moveAll(CardStorage stack) {
        while (!stack.isEmpty()) {
            cards.add(stack.pop());
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public static Collection<Card> fromString(String cardsString) {
        var trimmed = cardsString.substring(1, cardsString.length() - 1);
        if (trimmed.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(
                        trimmed.split(", ")
                )
                .map(Card::fromString)
                .toList();
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public void remove(int index) {
        cards.remove(index);
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public String toString() {
        return cards.toString();
    }

    public Card getHighestCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Hand is empty");
        }
        return cards.stream().max(Comparator.comparing(Card::rank)).get();
    }

    public CardHand copyHand() {
        CardHand copy = new CardHand();
        for (Card card : cards) {
            copy.push(card);
        }
        return copy;
    }
}
