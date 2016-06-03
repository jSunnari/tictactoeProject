package client.gui;

/**
 * Created by robin on 2016-06-03.
 */
public class Combo {
    public Tile[] tiles;
    public Combo(Tile... tiles) {
        this.tiles = tiles;
    }
    //If our tiles doesnt match up with our different combinations we return false, because nobody has won
    public boolean isComplete() {
        if (tiles[0].getValue().isEmpty())
            return false;
        //we return true if our combo conditions are met
        return tiles[0].getValue().equals(tiles[1].getValue())
                && tiles[0].getValue().equals(tiles[2].getValue());
    }
}