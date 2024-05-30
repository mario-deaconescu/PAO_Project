package cards;

public record Card(Suit suit, Rank rank) implements Comparable<Card> {

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

    public static Card fromString(String card) {
        var parts = card.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid card string");
        }
        var rank = Rank.fromSymbol(parts[0].charAt(0));
        var suit = Suit.valueOf(parts[2]);
        if (rank == null) {
            throw new IllegalArgumentException("Invalid card string");
        }
        return new Card(suit, rank);
    }

    public String toString() {
        return STR."\{rank.getSymbol()} of \{suit}";
    }

    @Override
    public int compareTo(Card o) {
        return rank.getValue() - o.rank.getValue();
    }
}
