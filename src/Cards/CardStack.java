package Cards;

import java.util.Collection;
import java.util.Stack;

public class CardStack implements CardStorage {

    protected final Stack<Card> cards = new Stack<Card>();

    public CardStack(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    public CardStack() {
    }

    public void push(Card card) {
        cards.push(card);
    }

    public Card pop() {
        return cards.pop();
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
        return cards.peek();
    }

    public void moveAll(CardStorage stack) {
        while (!stack.isEmpty()) {
            cards.push(stack.pop());
        }
    }

    public String toString() {
        return cards.toString();
    }
}
