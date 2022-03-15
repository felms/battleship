public class Ship {

    private int cells;
    private String name;
    private int hits;


    Ship(int cells, String name) {
        this.cells = cells;
        this.name = name;
        this.hits = 0;
    }

    public int getCells() {
        return cells;
    }

    public String getName() {
        return name;
    }

    public int getHits() {
        return this.hits;
    }

    public void takeAShot(){
        this.hits++;
    }
}
