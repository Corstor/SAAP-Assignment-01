package layered.business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class P2d implements java.io.Serializable {

    private double x, y;

    @JsonCreator
    public P2d(@JsonProperty("x") double x, @JsonProperty("y") double y){
        this.x=x;
        this.y=y;
    }

    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    public P2d sum(V2d v){
        return new P2d(x+v.getX(),y+v.getY());
    }

    public V2d sub(P2d v){
        return new V2d(x-v.getX(),y-v.getY());
    }
    
    public String toString(){
        return "P2d("+x+","+y+")";
    }
}
