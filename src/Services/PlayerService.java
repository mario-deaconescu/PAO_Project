package Services;

public class PlayerService {

    private double totalBalance = 1000.0;
    private String name;
    private static final PlayerService instance = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public double takeBalance(double amount) {
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > totalBalance){
            throw new IllegalArgumentException("Amount must be less than total balance");
        }
        totalBalance -= amount;
        return amount;
    }

    public void giveBalance(double amount) {
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        totalBalance += amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double requestBalance() {
        System.out.println(name + ", your balance is: " + totalBalance);
        System.out.println("How much would you like to pay?");
        String input = System.console().readLine();
        try{
            double amount = Double.parseDouble(input);
            if(amount < 0){
                throw new IllegalArgumentException("Amount must be positive");
            }
            if(amount > totalBalance){
                throw new IllegalArgumentException("Amount must be less than total balance");
            }
            return takeBalance(amount);
        } catch (NumberFormatException e){
            System.out.println("Invalid input");
            return requestBalance();
        }
    }
}
