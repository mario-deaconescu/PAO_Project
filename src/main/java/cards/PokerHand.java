package cards;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokerHand extends CardHand implements Comparable<PokerHand> {
    public enum Category{
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }

    public Category getCategory(){
        if(cards.isEmpty()){
            throw new IllegalStateException("Hand is empty");
        }
        if(cards.size() > 5){
            throw new IllegalStateException("Hand has more than 5 cards");
        }
        // Determine the category of the hand
        // Sort the cards by rank
        List<Card> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparing(Card::rank));

        // Check for a flush
        boolean flush = sorted.stream().allMatch(card -> card.suit() == sorted.getFirst().suit()) && sorted.size() == 5;
        // Check for a straight
        boolean straight = true;
        for(int i = 1; i < sorted.size(); i++){
            if(sorted.get(i).rank().getValue() != sorted.get(i - 1).rank().getValue() + 1){
                straight = false;
                break;
            }
        }
        // Check for a straight flush
        if(flush && straight){
            // Check for a royal flush
            if(sorted.getFirst().rank() == Card.Rank.TEN){
                return Category.ROYAL_FLUSH;
            }
            return Category.STRAIGHT_FLUSH;
        }

        if(flush){
            return Category.FLUSH;
        }

        if(straight){
            return Category.STRAIGHT;
        }

        // Check for a four of a kind
        Map<Card.Rank, Long> rankCounts = sorted.stream().collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        if(rankCounts.containsValue(4L)){
            return Category.FOUR_OF_A_KIND;
        }
        // Check for a full house
        boolean threeOfAKind = rankCounts.containsValue(3L);
        boolean pair = rankCounts.containsValue(2L);
        if(threeOfAKind && pair){
            return Category.FULL_HOUSE;
        } else if(threeOfAKind){
            return Category.THREE_OF_A_KIND;
        } else if(pair){
            // Check for two pairs
            if(rankCounts.values().stream().filter(count -> count == 2L).count() == 2){
                return Category.TWO_PAIR;
            }
            return Category.PAIR;
        }
        return Category.HIGH_CARD;
    }

    @Override
    public int compareTo(PokerHand other) {
        return getHandValue() - other.getHandValue();
    }

    @Override
    public PokerHand copyHand(){
        PokerHand copy = new PokerHand();
        for(Card card : cards){
            copy.push(card);
        }
        return copy;
    }

    public int getHandValue(){
        var category = getCategory();
        var categoryValue = category.ordinal();
        var highestCard = getHighestCard().rank().getValue();
        return categoryValue * 13 + highestCard;
    }
}
