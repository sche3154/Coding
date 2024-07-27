package stickman.model;

public interface Originator {

    Memento saveMemento();

    void loadMemento(Memento memento);
}
