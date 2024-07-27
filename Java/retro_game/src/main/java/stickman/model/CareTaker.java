package stickman.model;

public class CareTaker {

    Memento memento;

    public void addMemento(Memento memento){
        this.memento = memento;
    }

    public Memento getMemento(){
        return this.memento;
    }
}
