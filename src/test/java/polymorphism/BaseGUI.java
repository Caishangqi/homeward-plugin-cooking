package polymorphism;

public class BaseGUI {

    public String name = "基础GUI";
    public void run() {
        System.out.println("Base GUI Running");
    }
    public void open() {
        System.out.println("Base GUI Openning");
    }

}

class PlayerGUI extends BaseGUI {
    public String name = "玩家GUI";

    public void run() {
        System.out.println("PlayerGUI Running");
    }

    public void change() {
        System.out.println("PlayerGUI Changing");
    }

    public static void main(String[] args) {
        BaseGUI gui = new PlayerGUI();
        System.out.println(gui.name); //基础GUI
        gui.run();



    }
}
