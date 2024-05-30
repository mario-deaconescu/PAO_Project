package blackjack.players;

import blackjack.BlackjackAction;
import cards.Card;
import services.PlayerService;

import java.util.Collection;
import java.util.HashMap;

public class HumanBlackjackPlayer extends BlackjackPlayer{

    public HumanBlackjackPlayer() {
        super(PlayerService.getInstance().getName());
    }

    @Override
    public void initialBet() {
        System.out.println(STR."Your balance: \{getBalance()}");
        System.out.println("Enter your bet: ");
        double bet = Double.parseDouble(System.console().readLine());
        if(bet > getBalance()){
            System.out.println("You don't have enough balance");
            initialBet();
        }
        this.bet = bet;
    }

    @Override
    public BlackjackAction play(Card dealerCard, HashMap<String, Collection<Card>> otherPlayersHands) {
        System.out.println(STR."Your turn, \{getName()}!");
        System.out.println(STR."Dealer's card: \{dealerCard}");
        System.out.println("Other players' hands: ");
        for (String player : otherPlayersHands.keySet()) {
            System.out.println(STR."\{player}: \{otherPlayersHands.get(player)}");
        }
        System.out.println(STR."Your hand: \{hand}");
        System.out.println(STR."Your balance: \{getBalance()}");
        System.out.println(STR."Current bet: \{bet}");
        System.out.println("Do you want to hit, stand, or double down?");
        String action = System.console().readLine();
        return switch (action) {
            case "hit" -> BlackjackAction.HIT;
            case "stand" -> BlackjackAction.STAND;
            case "double down" -> BlackjackAction.DOUBLE_DOWN;
            default -> {
                System.out.println("Invalid action");
                yield play(dealerCard, otherPlayersHands);
            }
        };
    }
}
