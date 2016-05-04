package ru.innopolis.lips.memvit;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.browser.*;
import org.eclipse.ui.part.*;
import org.eclipse.swt.SWT;
import java.util.ArrayList;

import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.model.ICDIValue;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariable;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.cdi.model.Variable;


public class Stack extends ViewPart {

	private CDIEventListener cdiEventListener = null;
	private ICDISession cdiDebugSession = null;
	private Browser browser;

	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Runnable task = () -> { VizualizateStackCpp();};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
	
	public void createPartControl(Composite parent) {
		this.cdiEventListener    = new CDIEventListener();
	    tryGetCdiSession();
	    
	    Runnable runnable = new RunnableForThread2();
	    Thread Thread2 = new Thread(runnable);
	    Thread2.start();
		
		browser = new Browser(parent, SWT.NONE);
		browser.setText("<html><body>Here will appear stack- and heap-related debug information</body></html>");
	}	

	@Override
	public void setFocus() {
	}	
	
	private void tryGetCdiSession(){	
		//Session session = CDIDebugger.getSession();
		ICDISession session = GDBCDIDebuggerMemvit.getSession();
		
		if (session == null){return;}
		if (session.equals(this.cdiDebugSession)){return;}
		else{
			this.cdiDebugSession = session;
			if (this.cdiDebugSession != null){this.cdiDebugSession.getEventManager().addEventListener(this.cdiEventListener);}	
		}
	}
	
	private void VizualizateStackCpp(){		
		tryGetCdiSession();
		if (cdiEventListener == null){return;}
		if (!cdiEventListener.isItUpdatedThread()){return;}
			
		String tabContent = VisualizationUtils.composeStackTab(
				cdiEventListener.getActivationRecords(),
				cdiEventListener.getEaxType(),
				cdiEventListener.getEaxValue(),
				cdiEventListener.getHeapVars()
				);
		
		browser.setText(tabContent);
	}	
	
	private boolean isExist(ArrayList<ICDIVariable> varlist, ICDIVariable variable){
		Variable v = (Variable)variable;
		String hexAddress1 = CDIEventListener.getHexAddress(v);
		for (ICDIVariable var : varlist){
			Variable variab = (Variable)var;
			String hexAddress2 = CDIEventListener.getHexAddress(variab);
			if (hexAddress1.equals(hexAddress2)){return true;}	
		}
		return false;
	}
	
	private void fillVarList(ArrayList<ICDIVariable> varlist, ICDIVariable[] variables){		
		for (ICDIVariable variable : variables){
			if (!isExist(varlist, variable)){varlist.add(variable);}
			ICDIValue value 			= CDIEventListener.getLocalVariableValue(variable);
			ICDIVariable[] subvariables = CDIEventListener.getLocalVariablesFromValue(value);
			fillVarList(varlist, subvariables);
		}
	}
	
	private void vizualizateCdiVariables(TreeItem item, ICDIVariable[] variables){
		if (variables == null){return;}
		for (ICDIVariable lvariable : variables){
			Variable variable = (Variable)lvariable;	
			
			ICDIValue value 			= CDIEventListener.getLocalVariableValue(lvariable);
			String valuestring			= CDIEventListener.getValueString(value);
			String typename				= CDIEventListener.getLocalVariableTypeName(lvariable);
			String QualifiedName		= CDIEventListener.getQualifiedName(lvariable);
			TreeItem subItem = new TreeItem(item, SWT.LEFT);
			String hexAddress = CDIEventListener.getHexAddress(variable);
			
			subItem.setText(0,typename + " " + QualifiedName + " : " + valuestring + " address: " + hexAddress);
			vizualizateCdiVariables(subItem, CDIEventListener.getLocalVariablesFromValue(value));		
		}
	}

}