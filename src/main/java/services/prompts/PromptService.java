package services.prompts;

public abstract class PromptService {
    private final String welcomeMessage;
    protected boolean exit = false;

    protected PromptService() {
        this.welcomeMessage = null;
    }

    protected PromptService(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    protected void leave() {
        this.exit = true;
    }
    protected abstract void prompt();

    public void enter(){
        if(welcomeMessage != null){
            System.out.println(welcomeMessage);
        }
        while (!exit) {
            prompt();
        }
    }
}
