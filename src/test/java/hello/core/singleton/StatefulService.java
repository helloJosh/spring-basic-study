package hello.core.singleton;

public class StatefulService {

    //private int price;
    /*public void order(String name, int price){
        System.out.println("name = " + name + "price ="+price);
        this.price = price;
    }*/

    //해결방법
    public int order1(String name, int price){
        System.out.println("name = " + name + "price ="+price);
        return price;
    }
    //public int getPrice(){
    //    return price;
    //}
}
