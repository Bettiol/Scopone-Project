package application.model.engine.types.cards;

public enum Suit {
    BASTONI("bastone"), COPPE("coppa"), SPADE("spada"), DENARI("danaro");

    private final String value;

    private Suit(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

}
