package services.prompts;

import utility.ActionPrompt;

import java.util.List;

public class GameService extends PromptService{

    private static final GameService instance = new GameService();

    private GameService() {
    }

    public static GameService getInstance() {
        return instance;
    }

    private void poker(){
        PokerService.getInstance().enter();
    }

    private void blackjack(){
        BlackjackService.getInstance().enter();
    }

    @Override
    protected void prompt(){
        ActionPrompt.show(List.of("Poker", "Blackjack", "Exit"), List.of(
                this::poker,
                this::blackjack,
                this::leave
        ));
    }
}
