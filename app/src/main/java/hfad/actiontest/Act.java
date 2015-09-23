package hfad.actiontest;

/**
 * Created by Deeprom on 22/9/2558.
 */
public class Act {
    private String name;

    public static final Act[] get_acts = {
            new Act("Script 001"),
            new Act("Script 002"),
            new Act("Script 003"),
            new Act("Script 004"),
            new Act("Script 005"),
            new Act("Script 006"),
            new Act("Script 007"),
            new Act("Script 008"),
            new Act("Script 009")
    };

    private Act(String name){
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
