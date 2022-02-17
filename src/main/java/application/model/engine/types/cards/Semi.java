package application.model.engine.types.cards;

public enum Semi {
    BASTONI("bastone"), COPPE("coppa"), SPADE("spada"), DENARI("danaro");

    private final String value;

    private Semi(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

}
