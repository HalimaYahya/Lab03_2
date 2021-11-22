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
  Employee boss, marketVP, prodVP;

  Employee salesMgr, advMgr;

  Employee prodMgr, shipMgr;

  JScrollPane sp;

  JPanel treePanel;

  JTree tree;

  DefaultMutableTreeNode troot;

  JLabel cost;

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

  //--------------------------------------
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

  //------------------------------------
  public void loadTree(Employee topDog) {
    DefaultMutableTreeNode troot;
    troot = new DefaultMutableTreeNode(topDog.getName());
    treePanel.remove(tree);
    tree = new JTree(troot);
    tree.addTreeSelectionListener(this);
    sp.getViewport().add(tree);

    addNodes(troot, topDog);
    tree.expandRow(0);
    repaint();
  }

  //--------------------------------------
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

  //--------------------------------------
  private void makeEmployees() {
    boss = new Employee("CEO", 200000);
    boss.add(marketVP = new Employee("Marketing VP", 100000));
    boss.add(prodVP = new Employee("Production VP", 100000));

    marketVP.add(salesMgr = new Employee("Sales Mgr", 50000));
    marketVP.add(advMgr = new Employee("Advt Mgr", 50000));

    for (int i = 0; i < 5; i++)
      salesMgr.add(new Employee("Sales " + new Integer(i).toString(),
          30000.0F + (float) (Math.random() - 0.5) * 10000));
    advMgr.add(new Employee("Secy", 20000));

    prodVP.add(prodMgr = new Employee("Prod Mgr", 40000));
    prodVP.add(shipMgr = new Employee("Ship Mgr", 35000));
    for (int i = 0; i < 4; i++)
      prodMgr.add(new Employee("Manuf " + new Integer(i).toString(),
          25000.0F + (float) (Math.random() - 0.5) * 5000));
    for (int i = 0; i < 3; i++)
      shipMgr.add(new Employee("ShipClrk " + new Integer(i).toString(),
          20000.0F + (float) (Math.random() - 0.5) * 5000));

  }

  //--------------------------------------
  public void valueChanged(TreeSelectionEvent evt) {
    TreePath path = evt.getPath();
    String selectedTerm = path.getLastPathComponent().toString();

    Employee emp = boss.getChild(selectedTerm);
    if (emp != null)
      cost.setText(new Float(emp.getSalaries()).toString());
  }

  //--------------------------------------
  static public void main(String argv[]) {
    new EmpTree();
  }
}

class Employee {
  String name;

  float salary;

  Vector subordinates;

  boolean isLeaf;

  Employee parent = null;

  //--------------------------------------
  public Employee(String _name, float _salary) {
    name = _name;
    salary = _salary;
    subordinates = new Vector();
    isLeaf = false;
  }

  //--------------------------------------
  public Employee(Employee _parent, String _name, float _salary) {
    name = _name;
    salary = _salary;
    parent = _parent;
    subordinates = new Vector();
    isLeaf = false;
  }

  //--------------------------------------
  public void setLeaf(boolean b) {
    isLeaf = b; //if true, do not allow children
  }

  //--------------------------------------
  public float getSalary() {
    return salary;
  }

  //--------------------------------------
  public String getName() {
    return name;
  }

  //--------------------------------------
  public boolean add(Employee e) {
    if (!isLeaf)
      subordinates.addElement(e);
    return isLeaf; //false if unsuccessful
  }

  //--------------------------------------
  public void remove(Employee e) {
    if (!isLeaf)
      subordinates.removeElement(e);
  }

  //--------------------------------------
  public Enumeration elements() {
    return subordinates.elements();
  }

  //--------------------------------------
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

  //--------------------------------------
  public float getSalaries() {
    float sum = salary;
    for (int i = 0; i < subordinates.size(); i++) {
      sum += ((Employee) subordinates.elementAt(i)).getSalaries();
    }
    return sum;
  }
}
   