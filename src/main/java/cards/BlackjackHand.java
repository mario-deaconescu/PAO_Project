package cards;

public class BlackjackHand extends CardHand implements Comparable<BlackjackHand> {

    private int countAces(){
        int count = 0;
        for(Card card : cards){
            if(card.rank() == Card.Rank.ACE){
                count++;
            }
        }
        return count;
    }

    private int mapCardValue(Card card) {
        return switch (card.rank()) {
            case ACE -> 11;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN, JACK, QUEEN, KING -> 10;
            default -> throw new IllegalArgumentException("Invalid card rank");
        };
    }

    public int getValue(){
        int value = 0;
        for(Card card : cards){
            value += mapCardValue(card);
        }
        while(value > 21 && countAces() > 0){
            value -= 10;
        }
        return value;
    }

    @Override
    public int compareTo(BlackjackHand o) {
        return Integer.compare(getValue(), o.getValue());
    }
}
