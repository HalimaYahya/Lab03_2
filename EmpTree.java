//@version

/*
The Design Patterns Java Companion

Copyright (C) 1998, by James W. Cooper

IBM Thomas J. Watson Research Center

*/


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


public class EmpTree extends JFrame implements TreeSelectionListener {
   // Attribute. 
  Employee boss;
  Employee marketVP;
  Employee salesMgr;
  Employee advMgr;
  Employee prodMgr;
  Employee prodVP;
  Employee shipMgr;
  JScrollPane sp;
  JPanel treePanel;
  JTree tree;
  DefaultMutableTreeNode troot;
  JLabel cost;
  
  //@author
  // the super class for the Window
  public EmpTree() {
    super("Employee tree");
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    makeEmployees();
    setGUI();
  }

  //@author
  //this method is to set the Graphical user interface.
  private void setGUI() {
    treePanel = new JPanel();
    getContentPane().add(treePanel);
    treePanel.setLayout(new BorderLayout());

    sp = new JScrollPane();
    treePanel.add("Center", sp);
    treePanel.add("South", cost = new JLabel("          "));

    treePanel.setBorder(new BevelBorder(BevelBorder.RAISED));
    troot = new DefaultMutableTreeNode(boss.getName());
    tree = new JTree(troot);
    tree.setBackground(Color.lightGray);
    loadTree(boss);
    /* Put the Tree in a scroller. */

    sp.getViewport().add(tree);
    setSize(new Dimension(200, 300));
    setVisible(true);

  }

  //@param 
  //this method is to load Tree and 
  //how the tree node is display as tree Panel
  public void loadTree(Employee topDog) {
    final DefaultMutableTreeNode troot;
    troot = new DefaultMutableTreeNode(topDog.getName());
    treePanel.remove(tree);
    tree = new JTree(troot);
    tree.addTreeSelectionListener(this);
    sp.getViewport().add(tree);

    addNodes(troot, topDog);
    tree.expandRow(0);
    repaint();
  }

   //@param 
  //this method is for adding the node of the tree
  private void addNodes(DefaultMutableTreeNode pnode, Employee emp) {
     DefaultMutableTreeNode node;

    Enumeration e = emp.elements();
    while (e.hasMoreElements()) {
      Employee newEmp = (Employee) e.nextElement();
      node = new DefaultMutableTreeNode(newEmp.getName());
      pnode.add(node);
      addNodes(node, newEmp);
    }
  }
  
  //@author
  //this one is the implemention for Employee object.
  private void makeEmployees() {
    boss = new Employee("CEO", 200000);
    boss.add(marketVP = new Employee("Marketing VP", 100000));
    boss.add(prodVP = new Employee("Production VP", 100000));

    marketVP.add(salesMgr = new Employee("Sales Mgr", 50000));
    marketVP.add(advMgr = new Employee("Advt Mgr", 50000));

    for (int PrimaryCondition = 0; PrimaryCondition < 5; PrimaryCondition++)
      salesMgr.add( new Employee ("Sales " + new Integer (PrimaryCondition).toString(),
          30000.0F + (float) (Math.random() - 0.5) * 10000));
    advMgr.add(new Employee("Secy", 20000));

    prodVP.add( prodMgr = new Employee("Prod Mgr", 40000));
    prodVP.add( shipMgr = new Employee("Ship Mgr", 35000));
    for (int PrimaryCondition = 0; PrimaryCondition < 4; PrimaryCondition++)
      prodMgr.add( new Employee("Manuf " + new Integer (PrimaryCondition).toString(),
          25000.0F + (float) (Math.random() - 0.5) * 5000));
    for (int PrimaryCondition = 0; PrimaryCondition < 3; PrimaryCondition++)
      shipMgr.add( new Employee("ShipClrk " + new Integer(PrimaryCondition).toString(),
          20000.0F + (float) ( Math.random() - 0.5) * 5000));

  }

  //@param
  //the method for the tree path
  public void valueChanged(TreeSelectionEvent evt) {
    TreePath path = evt.getPath();
    String selectedTerm = path.getLastPathComponent().toString();

    Employee emp = boss.getChild(selectedTerm);
    if (emp != null)
      cost.setText(new Float(emp.getSalaries()).toString());
  }

  //@author
  //the main file for the EmpTree
  static public void main(String argv[]) {
    new EmpTree();
  }
}
 //@author
class Employee {
  String name;

  float salary;

 final Vector subordinates;

  boolean isLeaf;

   Employee parent = null;
   //@param
  //Construction for the Employee class [_name and _salary]
  public Employee(String _name, float _salary) {
    name = _name;
    salary = _salary;
    subordinates = new Vector();
    isLeaf = false;
  }
   //@param
  //Construction override for the [_parent, _name and _salary]
  public Employee(Employee _parent, String _name, float _salary) {
    name = _name;
    salary = _salary;
    parent = _parent;
    subordinates = new Vector();
    isLeaf = false;
  }
   //@param
  //set for the leaf "Tree branches". 
  public void setLeaf(boolean b) {
    isLeaf = b; //if true, do not allow children
  }
   //@return
  //get method fo for the Salary
  public float getSalary() {
    return salary;
  }
   
   //@return
  //get method fo for the ame
  public String getName() {
    return name;
  }

   //@return
  //Loop for the "Tree branches" add Element. 
  public boolean add(Employee e) {
    if (!isLeaf)
      subordinates.addElement(e);
    return isLeaf; //false if unsuccessful
  }
  
  //@return
  //the remove method for the employee 
  public void remove(Employee e) {
    if (!isLeaf)
      subordinates.removeElement(e);
  }

  //open the subordinates for the elements 
  public Enumeration elements() {
    return subordinates.elements();
  }
  
  //@return
  //the open for the tree Panel Child 
  public Employee getChild(String s) {
    Employee newEmp = null;

    if (getName().equals(s))
      return this;
    else {
      boolean found = false;
      Enumeration e = elements();
      while (e.hasMoreElements() && (!found)) {
        newEmp = (Employee) e.nextElement();
        found = newEmp.getName().equals(s);
        if (!found) {
          newEmp = newEmp.getChild(s);
          found = (newEmp != null);
        }
      }
      if (found)
        return newEmp;
      else
        return null;
    }
  }
  
   //@return
  // cheak the Salaries
  public float getSalaries() {
    float sum = salary;
    for (int PrimaryCondition = 0; PrimaryCondition < subordinates.size(); PrimaryCondition++) {
      sum += ((Employee) subordinates.elementAt(PrimaryCondition)).getSalaries();
    }
    return sum;
  }
}
   