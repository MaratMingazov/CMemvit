package plugin2.views;

import org.eclipse.cdt.debug.core.cdi.model.ICDIInstruction;
import org.eclipse.cdt.debug.core.cdi.model.ICDIStackFrame;
import org.eclipse.cdt.debug.core.cdi.model.ICDIThread;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;



public class ProgramInfo extends ViewPart {

	private CDIEventListener cdiEventListener = null;
	private Session cdiDebugSession = null;
	private Tree tree;
	
	class RunnableForThread2 implements Runnable{
		public void run() {
			while (true) {
				try { Thread.sleep(1000); } catch (Exception e) { }
				Runnable task = () -> {vizualizateProgramInfoCpp();};
				Display.getDefault().asyncExec(task);
			}			
		}
	}
	
	
	public ProgramInfo() {
	}

	@Override
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);		
		tree.setVisible(true);
		
		TreeColumn columnName = new TreeColumn(tree, SWT.LEFT);
		columnName.setText("");
		columnName.setWidth(300);
		
		columnName = new TreeColumn(tree, SWT.LEFT);
		columnName.setText("");
		columnName.setWidth(300);
		
		
		this.cdiEventListener		= new CDIEventListener();
		tryGetCdiSession();
		
		Runnable runnable = new RunnableForThread2();
		Thread Thread2 = new Thread(runnable);
		Thread2.start();	
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

	@Override
	public void setFocus() {
	}

	
	public void vizualizateProgramInfoCpp(){
		tryGetCdiSession();
		if (cdiEventListener ==null){return;}
		if (!cdiEventListener.isItUpdatedThread()){return;}
		
		ICDIThread CurrentThread =  cdiEventListener.getCurrentThread();	
		ICDIStackFrame[] frames = CDIEventListener.getStackFrames(CurrentThread);		
		
		for (TreeItem item : tree.getItems()){item.dispose();}
		
		
		
		
		for (ICDIStackFrame frame : frames){
			TreeItem item = new TreeItem(tree, SWT.LEFT);
			item.setText(0, frame.getLocator().getFile() + " / " + frame.getLocator().getFunction());	
			ICDIInstruction[] instructions = CDIEventListener.getInstructions(frame);
			for (ICDIInstruction instruction : instructions){
				
				String instr = instruction.getInstruction();
				//System.out.println("Args =  " + instruction.getArgs());
				//System.out.println("getFuntionName =  " + instruction.getFuntionName());
				//System.out.println("getOffset =  " + instruction.getOffset());
				//System.out.println("getOpcode =  " + instruction.getOpcode());
				//System.out.println("getAdress =  " + instruction.getAdress().toString(16));
				//System.out.println("____________");
				
				TreeItem subitem = new TreeItem(item, SWT.LEFT);
				subitem.setText(0, instr);
				subitem.setText(1, instruction.getAdress().toString(16));
				System.out.println(instr + "  " + "0x"+instruction.getAdress().toString(16));
			}
		}

		
	}
	
}
