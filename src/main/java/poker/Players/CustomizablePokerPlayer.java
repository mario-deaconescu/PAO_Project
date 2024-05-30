package poker.Players;

public abstract class CustomizablePokerPlayer extends PokerPlayer{

    public abstract String getParams();

    public abstract void setParams(String params);

    public CustomizablePokerPlayer(String name, String params){
        super(name);
        setParams(params);
    }

    public CustomizablePokerPlayer(String name, double balance){
        super(name, balance);
    }

    public CustomizablePokerPlayer(String name){
        super(name);
    }

    public abstract void initFromConsole();
}
