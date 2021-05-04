package munchkin.integrator.domain.classes;

public enum Classes {
    TIEF("Voleur"),
    ;

    private final String title;

    Classes(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
