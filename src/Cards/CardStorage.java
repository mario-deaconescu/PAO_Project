package Cards;

public interface CardStorage {

    public void push(Card card);

    public Card pop();

    public boolean isEmpty();

    public int size();

    public void clear();

    public Card peek();

    public void moveAll(CardStorage storage);

    public String toString();
}
