package cards;

public interface CardStorage {

    void push(Card card);

    Card pop();

    boolean isEmpty();

    int size();

    void clear();

    Card peek();

    void moveAll(CardStorage storage);

    String toString();
}
