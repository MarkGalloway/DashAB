package ab.dash.ast;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/

/** A symbol to represent built in types such boolean, integer, real, character primitive types */
public class BuiltInSpecifierSymbol extends Symbol implements Specifier {
    int specifierIndex;
    public BuiltInSpecifierSymbol(String name, int specifierIndex) {
        super(name);
        this.specifierIndex = specifierIndex;
    }
    public int getSpecifierIndex() { return specifierIndex; }
    public String toString() { return getName(); }    
}