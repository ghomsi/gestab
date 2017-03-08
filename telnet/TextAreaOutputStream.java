/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telnet;

import java.io.IOException;
import java.io.OutputStream;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 *
 * @author ghomsi
 */
public class TextAreaOutputStream extends OutputStream {
    private TextArea textArea;
    
    public TextAreaOutputStream(TextArea textArea){
        this.textArea = textArea;
    }
    
    
    
    public void write(int b) throws IOException {
        // redirects data to the text area
        System.out.println(String.valueOf((char)b));
        Platform.runLater(() -> {
            textArea.setText(textArea.getText()+String.valueOf((char)b));
        });
        
        // scrolls the text area to the end of data
        //textArea.positionCaret(textArea.getText().length());
    }
}
