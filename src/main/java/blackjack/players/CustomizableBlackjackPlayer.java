package blackjack.players;

public abstract class CustomizableBlackjackPlayer extends BlackjackPlayer{

    public abstract String getParams();

    public abstract void setParams(String params);

    public CustomizableBlackjackPlayer(String name, String params){
        super(name);
        setParams(params);
    }

    public CustomizableBlackjackPlayer(String name, double balance){
        super(name, balance);
    }

    public CustomizableBlackjackPlayer(String name){
        super(name);
    }

    public abstract void initFromConsole();
}
