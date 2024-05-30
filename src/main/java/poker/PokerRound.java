package poker;

import cards.Card;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record PokerRound(Round round, Collection<Card> table, Map<String, PokerPlayerState> playerStates){
    public PokerRound(Round roundType) {
        this(roundType, null, new HashMap<>());
    }

    public PokerRound(Round roundType, Collection<Card> tableCards) {
        this(roundType, tableCards, new HashMap<>());
    }

    public enum Round {
        PREFLOP,
        FLOP,
        TURN,
        RIVER,
        SHOWDOWN
    }
}
