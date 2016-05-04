package plugin2.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import java.util.ArrayList;
import org.eclipse.cdt.debug.core.cdi.model.ICDILocalVariableDescriptor;
import org.eclipse.cdt.debug.core.cdi.model.ICDIStackFrame;
import org.eclipse.cdt.debug.core.cdi.model.ICDIThread;
import org.eclipse.cdt.debug.core.cdi.model.ICDIValue;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariable;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.cdi.model.Variable;
import org.eclipse.swt.SWT;


public class Heap extends ViewPart{
	private CDIEventListener cdiEventListener = null;
	private Session cdiDebugSession = null;
	private Tree treeOne;
	private Tree treeTwo;

	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Runnable task = () -> { VizualizateHeapCpp();};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
		
	@Override
	public void createPartControl(Composite parent) {
		
		createTreeOne(parent);
		createTreeTwo(parent);

		
		this.cdiEventListener		= new CDIEventListener();
		tryGetCdiSession();
		
		Runnable runnable = new RunnableForThread2();
		Thread Thread2 = new Thread(runnable);
		Thread2.start();	
	}

	@Override
	public void setFocus() {		
	}
	
	
	private void tryGetCdiSession(){	
		Session session = CDIDebugger.getSession();
		if (session == null){return;}
		if (session.equals(this.cdiDebugSession)){return;}
		else{
			this.cdiDebugSession = session;
			if (this.cdiDebugSession != null){this.cdiDebugSession.getEventManager().addEventListener(this.cdiEventListener);}	
		}
	}
	
	private void createTreeOne(Composite parent){
		treeOne = new Tree(parent, SWT.MIN);
		treeOne.setHeaderVisible(true);
		treeOne.setLinesVisible(true);		
		treeOne.setVisible(true);

		
		TreeColumn columnName = new TreeColumn(treeOne, SWT.LEFT);
		columnName.setText("classes");
		columnName.setWidth(300);
	}
	
	private void createTreeTwo(Composite parent){
		treeTwo = new Tree(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		treeTwo.setHeaderVisible(true);
		treeTwo.setLinesVisible(true);		
		treeTwo.setVisible(true);
		
		TreeColumn columnName = new TreeColumn(treeTwo, SWT.LEFT);
		columnName.setText("instances");
		columnName.setWidth(300);		
	}
	
	private void VizualizateHeapCpp(){
		tryGetCdiSession();
		if (cdiEventListener ==null){return;}
		if (!cdiEventListener.isItUpdatedThread()){return;}
		
		ICDIThread CurrentThread =  cdiEventListener.getCurrentThread();	
		ICDIStackFrame[] Frames = CDIEventListener.getStackFrames(CurrentThread);		
		
		for (TreeItem item : treeOne.getItems()){item.dispose();}
		for (TreeItem item : treeTwo.getItems()){item.dispose();}
		
		for (int i = 0; i< Frames.length; i++){

			
			ICDIStackFrame frame = Frames[i];
			//String FrameName 	= frame.getLocator().getFunction();
			//String Location		= frame.getLocator().getFile();

			//TreeItem item = new TreeItem(treeOne, SWT.LEFT);
			//item.setText(0, Location + " " + FrameName);			
			
			TreeItem subItem;
			
			//ICDIValue registerInstructionPointer = CDIEventListener.findRegisterValueByQualifiedName(frame, "$rip");
			//String registerInstructionPointerString = CDIEventListener.getValueString(registerInstructionPointer);		
			//subItem = new TreeItem(item, SWT.LEFT);
			//subItem.setText(0, "InstructionPointer : " + registerInstructionPointerString);			
			
			ICDIValue registerBasePointer = CDIEventListener.findRegisterValueByQualifiedName(frame, "$rbp");
			String registerBasePointerString = CDIEventListener.getValueString(registerBasePointer);	
			if (registerBasePointerString.length() == 0) {
				registerBasePointer = CDIEventListener.findRegisterValueByQualifiedName(frame, "$ebp");
				registerBasePointerString = CDIEventListener.getValueString(registerBasePointer);	
			}
			//subItem = new TreeItem(item, SWT.LEFT);
			//subItem.setText(0, "BasePointer : " + registerBasePointerString);	
	
			ICDIValue registerStackPointer = CDIEventListener.findRegisterValueByQualifiedName(frame, "$rsp");
			String registerStackPointerString = CDIEventListener.getValueString(registerStackPointer);
			if (registerStackPointerString.length() == 0) {
				registerStackPointer = CDIEventListener.findRegisterValueByQualifiedName(frame, "$esp");
				registerStackPointerString = CDIEventListener.getValueString(registerStackPointer);
			}
		
			ArrayList<ICDIVariable> varlist = new ArrayList<ICDIVariable>();
			ICDILocalVariableDescriptor[] descriptors = CDIEventListener.GetStackFrameLocalVariableDescriptors(frame);
			ICDIVariable[] variables = new ICDIVariable[descriptors.length];
			for (int k = 0; k<descriptors.length; k++){variables[k] = CDIEventListener.getLocalVariable(descriptors[k]);}
			fillVarList(varlist, variables);
			
			for (ICDIVariable cdiVariable : varlist){
				Variable variable = (Variable)cdiVariable;
					
				ICDIValue value 			= CDIEventListener.getLocalVariableValue(variable);
				String valuestring			= CDIEventListener.getValueString(value);
				String QualifiedName		= CDIEventListener.getQualifiedName(variable);
				String hexAddress = CDIEventListener.getHexAddress(variable);
				if (hexAddress.isEmpty()){continue;}
				
				if (hexAddress.compareTo(registerStackPointerString) >=0  && hexAddress.compareTo(registerBasePointerString) <=0 ){
					//subItem = new TreeItem(item, SWT.LEFT);
					//subItem.setText(0, hexAddress + " : " + valuestring + " (" + QualifiedName + ")");	
				}else{
					subItem = new TreeItem(treeOne, SWT.LEFT);
					subItem.setText(0, hexAddress + " : " + valuestring + " (" + QualifiedName + ")");	
				}
					
			}
	
			//ICDIValue eax = CDIEventListener.findRegisterValueByQualifiedName(frame, "$eax");
			//String eaxString = CDIEventListener.getValueString(eax);		
			//subItem = new TreeItem(item, SWT.LEFT);
			//subItem.setText(0, "Return value : " + eaxString);				
			
			//subItem = new TreeItem(item, SWT.LEFT);
			//subItem.setText(0, "StackPointer : " + registerStackPointerString);				

		}
		

	}	
	
	private void fillVarList(ArrayList<ICDIVariable> varlist, ICDIVariable[] variables){		
		for (ICDIVariable variable : variables){
			varlist.add(variable);
			ICDIValue value 			= CDIEventListener.getLocalVariableValue(variable);
			ICDIVariable[] subvariables = CDIEventListener.getLocalVariablesFromValue(value);
			fillVarList(varlist, subvariables);
		}
	}
	

}
