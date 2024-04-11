package Cards;

public final class Card implements Comparable<Card> {

    public enum Suit {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES
    }

    public enum Rank {
        ACE('A', 14),
        KING('K', 13),
        QUEEN('Q', 12),
        JACK('J', 11),
        TEN('T', 10),
        NINE('9', 9),
        EIGHT('8', 8),
        SEVEN('7', 7),
        SIX('6', 6),
        FIVE('5', 5),
        FOUR('4', 4),
        THREE('3', 3),
        TWO('2', 2);

        private final char symbol;
        private final int value;

        Rank(char symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }

        public char getSymbol() {
            return symbol;
        }

        public int getValue() {
            return value;
        }

        public static Rank fromSymbol(char symbol) {
            for (Rank rank : Rank.values()) {
                if (rank.symbol == symbol) {
                    return rank;
                }
            }
            return null;
        }

        public static Rank fromValue(int value) {
            for (Rank rank : Rank.values()) {
                if (rank.value == value) {
                    return rank;
                }
            }
            return null;
        }

    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public String toString() {
        return rank.getSymbol() + " of " + suit;
    }

    @Override
    public int compareTo(Card o) {
        return rank.getValue() - o.rank.getValue();
    }
}
