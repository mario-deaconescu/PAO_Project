package utility;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionPrompt {

    public static void show(Collection<String> options, List<Runnable> actions) {
        for(int i = 0; i < options.size(); i++){
            System.out.println(STR."\{i + 1}. \{options.toArray()[i]}");
        }

        var input = System.console().readLine();
        try{
            int choice = Integer.parseInt(input);
            actions.get(choice - 1).run();
        } catch (NumberFormatException | IndexOutOfBoundsException e){
            System.out.println("Invalid input");
            show(options, actions);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
