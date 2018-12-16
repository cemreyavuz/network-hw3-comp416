package networkhw3.utils;

public class Pair<A, B>{

    private A key;
    private B value;

    public Pair(A key,B value){
        this.key = key;
        this.value = value;
    }

    public A getKey(){
        return key;
    }

    public B getValue(){
        return value;
    }
}