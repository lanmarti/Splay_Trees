/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

/**
 *
 * @author Laurens Martin
 */
public class FullSemiSplay extends AbstractSplayTree {

    public FullSemiSplay() {
    }

    @Override
    public AbstractTree copy() {
        return new FullSemiSplay(this);
    }

    // HERWERK DIT toppen klonen!
    private FullSemiSplay(FullSemiSplay clone) {
        this.root = clone.getRoot();
        this.size = clone.getSize();
        this.limit = clone.limit;
    }

    @Override
    public TopStack splay(TopStack path) {
        return null;
    }
    
}
