package poker;

import cards.Card;
import cards.PokerHand;

import java.util.Collection;

public record PokerPlayerState(String name, Collection<Card> hand, double currentBet, double totalBet, boolean folded, boolean allIn) {
}
