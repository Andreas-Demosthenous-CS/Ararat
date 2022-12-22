package araratproject;

import javax.swing.JComponent;

public class InvalidInputException extends Exception {
    private JComponent faultyComponent;
    public InvalidInputException(){
        super("");
    }
    
    public InvalidInputException(String msg, JComponent comp){
        super(msg);
        this.faultyComponent = comp;
    }

    InvalidInputException(String msg) {
        this(msg, null);
    }
    
    public JComponent getFautyComponent(){
        return faultyComponent;
    }
}
