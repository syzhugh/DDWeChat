package test;

import java.awt.BorderLayout; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

import javax.swing.DefaultListModel; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JList; 
import javax.swing.JScrollPane; 

public class ModifyModelSampleModelAction { 
  static String labels[] = { "A", "B", "C", "D", "E", "F", "G" }; 

  public static void main(String args[]) { 
    JFrame frame = new JFrame("Modifying Model"); 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

    // Fill model 

    final DefaultListModel model = new DefaultListModel(); 
    for (int i = 0, n = labels.length; i < n; i++) { 
      model.addElement(labels[i]); 
    } 
    JList jlist = new JList(model); 
    JScrollPane scrollPane1 = new JScrollPane(jlist); 
    frame.add(scrollPane1, BorderLayout.CENTER); 

    JButton jb = new JButton("add F"); 

    jb.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent actionEvent) { 
        model.add(0, "First"); 
        model.clear(); 
        model.addElement("Last"); 
        model.addElement("Last"); 
        model.addElement("Last"); 
        model.addElement("Last"); 
        
        int size = model.getSize(); 
        model.insertElementAt("Middle", size / 2); 
        model.set(0, "New First"); 
        model.setElementAt("New Last", size - 1); 

        model.remove(0); 
        // model.removeAllElements(); 

    // model.removeElement("Last"); 

  // model.removeElementAt(size / 2); 

// model.removeRange(0, size / 2); 

      } 
    }); 

    frame.add(jb, BorderLayout.SOUTH); 

    frame.setSize(640, 300); 
    frame.setVisible(true); 
  } 
} 
