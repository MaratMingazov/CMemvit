package plugin2.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.event.ICDIEvent;
import org.eclipse.cdt.debug.core.cdi.event.ICDIEventListener;
import org.eclipse.cdt.debug.core.cdi.model.ICDIArgument;
import org.eclipse.cdt.debug.core.cdi.model.ICDIArgumentDescriptor;
import org.eclipse.cdt.debug.core.cdi.model.ICDIInstruction;
import org.eclipse.cdt.debug.core.cdi.model.ICDILocalVariable;
import org.eclipse.cdt.debug.core.cdi.model.ICDILocalVariableDescriptor;
import org.eclipse.cdt.debug.core.cdi.model.ICDIObject;
import org.eclipse.cdt.debug.core.cdi.model.ICDIRegister;
import org.eclipse.cdt.debug.core.cdi.model.ICDIRegisterDescriptor;
import org.eclipse.cdt.debug.core.cdi.model.ICDIRegisterGroup;
import org.eclipse.cdt.debug.core.cdi.model.ICDIStackFrame;
import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;
import org.eclipse.cdt.debug.core.cdi.model.ICDIThread;
import org.eclipse.cdt.debug.core.cdi.model.ICDIValue;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariable;
import org.eclipse.cdt.debug.core.cdi.model.ICDIVariableDescriptor;
import org.eclipse.cdt.debug.mi.core.IMITTY;
import org.eclipse.cdt.debug.mi.core.MIInferior;
import org.eclipse.cdt.debug.mi.core.MISession;
import org.eclipse.cdt.debug.mi.core.RxThread;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.cdi.model.Target;
import org.eclipse.cdt.debug.mi.core.cdi.model.Variable;
import org.eclipse.cdt.debug.mi.core.command.CommandFactory;
import org.eclipse.cdt.debug.mi.core.command.MIDataListChangedRegisters;
import org.eclipse.cdt.debug.mi.core.command.MIGDBShowAddressSize;

public class CDIEventListener implements ICDIEventListener{

	private ICDIThread currentThread = null; 
	private boolean itIsUpdatedThread = false;
	
	private String EAXvalue;
	private String EAXvaluetype;
	
	List<VarDescription> heapVars = new ArrayList<>();
	
	public VarDescription[] getHeapVars() {
		VarDescription[] arr = new VarDescription[heapVars.size()];
		heapVars.toArray(arr);
		return arr;
	}
	
	public void handleDebugEvents(ICDIEvent[] event) {
		//System.out.println("");
		for (ICDIEvent ev : event){
			ICDIObject source = ev.getSource();	
			if (source == null){
				setCurrentThread(null);
				setItIsUpdatedThread(false);
				return;
			}
			ICDITarget target = source.getTarget();
			try {
				//target.suspend();
				
				String[] sourcePaths = target.getSourcePaths();
				for (String s : sourcePaths){
					System.out.println("  Sourcepath = " + s);
				}
				
			} catch (CDIException e1) {
				// TODO Auto-generated catch block
				System.out.println("OOOOOONNNNNNETTT");
				e1.printStackTrace();
			}
			
			
			//target.getInstructions(arg0, arg1)
			System.out.println("TARGET = " + target.toString());
			
			if (target instanceof Target){
				Target ttarget = (Target) target;
				System.out.println("MITarget = " + ttarget);
				MISession misession = ttarget.getMISession();
				//misession
				System.out.println("misession = " + misession.toString());
				//RxThread RxThread = misession.getRxThread();
				//System.out.println("RxThreadState = " + RxThread.getState());
				//System.out.println("RxThreadGetName = " + RxThread.getName());
				//System.out.println("RxThreadStackTraceLength = " + RxThread.getStackTrace().length);
				//StackTraceElement[] RxThreadStackTraces = RxThread.getStackTrace();
				//for (StackTraceElement element : RxThreadStackTraces){
				//	System.out.println("   ClassName = " + element.getClassName());
				//	System.out.println("   MethodName = " + element.getMethodName());
				//	System.out.println("   LineNumber = " + element.getLineNumber());
				//	System.out.println("   FileName = " + element.getFileName());
				//	System.out.println("   isNativeMethod = " + element.isNativeMethod());
				//	System.out.println("_________");
				//}
				CommandFactory commandFactory = misession.getCommandFactory();
				MIDataListChangedRegisters changedRegisters = commandFactory.createMIDataListChangedRegisters();
				String[] parameters = changedRegisters.getParameters();
				for (String p : parameters){System.out.println("   " + p);}
				System.out.println("ChangedRegisters = " + changedRegisters.getParameters().length);
				
				MIGDBShowAddressSize MIGDBShowAddressSize = misession.getCommandFactory().createMIGDBShowAddressSize();
				System.out.println("MIGDBShowAddressSizeOperation = " + MIGDBShowAddressSize.getOperation());
			}

			MIInferior process = (MIInferior) target.getProcess();
			
			System.out.println("PID = " + process.getInferiorPID());

			
			//CommandFactory factory = null;
			//MISession session1;
			//MIGDBShowExitCode code = factory.createMIGDBShowExitCode();
			//System.out.println("Session = " + session.toString());
			//System.out.println("PROCESS = " + process.toString());
			
			//InputStream in = process.getInputStream();
			 //  try {
			//	for (int i = 0; i < in.available(); i++) {
			//	   System.out.println("" + in.read());
			//	   }
			//} catch (IOException e) {
			//	// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			   
			   
			//process.
			if (target.isTerminated()){
				setCurrentThread(null);
				setItIsUpdatedThread(false);
				return;	
			}
			try {
				ICDIThread thread = target.getCurrentThread();
				setCurrentThread(thread);
				setItIsUpdatedThread(true);
				
			} catch (CDIException e) {}
		}	
	}
	
	private void setCurrentThread(ICDIThread thread) {
		currentThread = thread;
	}
	
	private void setItIsUpdatedThread(boolean value){
		itIsUpdatedThread =value;
	}
	
	public ICDIThread getCurrentThread() {
		setItIsUpdatedThread(false);
		return currentThread;		
	}	
	
	public boolean isItUpdatedThread(){
		return itIsUpdatedThread;
	}
	
	public ActivationRecord[] getActivationRecords() {
		
		heapVars = new ArrayList<>();
		
		ICDIStackFrame[] frames = CDIEventListener.getStackFrames(getCurrentThread());
		ActivationRecord[] records = new ActivationRecord[frames.length];

		for (int i = 0; i < frames.length; i++) {
			
			String functionname = frames[i].getLocator().getFunction();
			String filename = frames[i].getLocator().getFile();
			ICDIValue registerBasePointer = CDIEventListener.findRegisterValueByQualifiedName(frames[i], "$rbp");
			String startaddress = CDIEventListener.getValueString(registerBasePointer);
			if (startaddress.length() == 0) {
				registerBasePointer = CDIEventListener.findRegisterValueByQualifiedName(frames[i], "$ebp");
				startaddress = CDIEventListener.getValueString(registerBasePointer);
			}
			
			ICDIValue registerStackPointer = CDIEventListener.findRegisterValueByQualifiedName(frames[i], "$rsp");
			String endaddress = CDIEventListener.getValueString(registerStackPointer);
			if (endaddress.length() == 0) {
				registerStackPointer = CDIEventListener.findRegisterValueByQualifiedName(frames[i], "$esp");
				endaddress = CDIEventListener.getValueString(registerStackPointer);
			}
			
			ICDILocalVariableDescriptor[] variabledescriptors = CDIEventListener.GetStackFrameLocalVariableDescriptors(frames[i]);
			
			ArrayList<VarDescription> tempVars = new ArrayList<>();
			for (int k = 0; k < variabledescriptors.length; k++) {
				ICDILocalVariable icdilovalvariable =  CDIEventListener.getLocalVariable(variabledescriptors[k]);
				String hexaddress = CDIEventListener.getHexAddress((Variable)icdilovalvariable);
				String typename = CDIEventListener.getLocalVariableTypeName(icdilovalvariable);
				ICDIValue cdivalue = CDIEventListener.getLocalVariableValue(icdilovalvariable);
				String valuestring = CDIEventListener.getValueString(cdivalue);
				String qualifiedname = CDIEventListener.getQualifiedName(icdilovalvariable);
				VarDescription addedVar = new VarDescription(hexaddress, typename, valuestring, qualifiedname);
				if (hexaddress.compareTo(endaddress) >=0  && hexaddress.compareTo(startaddress) <=0 ) {
					tempVars.add(addedVar);
				} else {
					heapVars.add(addedVar);
				}
				fillVarDescriptors(addedVar, cdivalue, startaddress, endaddress);
			}
			VarDescription[] vars = new VarDescription[tempVars.size()];
			tempVars.toArray(vars);
			
			ICDIArgumentDescriptor[] argumentdescriptors = CDIEventListener.getStackFrameArgumentDescriptors(frames[i]);
			VarDescription[] args = new VarDescription[argumentdescriptors.length];//extra space for return value
			for (int k = 0; k < argumentdescriptors.length; k++) {
				ICDILocalVariable icdilovalvariable =  CDIEventListener.getArgument(argumentdescriptors[k]);
				String hexaddress = CDIEventListener.getHexAddress((Variable)icdilovalvariable);
				String typename = CDIEventListener.getLocalVariableTypeName(icdilovalvariable);
				ICDIValue icdvalue = CDIEventListener.getLocalVariableValue(icdilovalvariable);
				String valuestring = CDIEventListener.getValueString(icdvalue);
				String qualifiedname = CDIEventListener.getQualifiedName(icdilovalvariable);
				
				args[k] = new VarDescription(hexaddress, typename, valuestring, qualifiedname);
			}			
			
			ICDIValue registerReturnValue = CDIEventListener.findRegisterValueByQualifiedName(frames[i], "$eax");
			EAXvalue = CDIEventListener.getValueString(registerReturnValue);	
			EAXvaluetype = CDIEventListener.getValueTypeName(registerReturnValue);	
			
String curLineNumber = String.valueOf(frames[i].getLocator().getLineNumber());
			
			records[i] = new ActivationRecord(curLineNumber, functionname,filename,startaddress,endaddress, "Unknown (not implemented)",vars, args);
		}
		setItIsUpdatedThread(false);
		return records;
	}
	
	private void fillVarDescriptors(VarDescription var, ICDIValue cdivalue, String startaddress, String endaddress){
		ICDIVariable[] subvariables =  CDIEventListener.getLocalVariablesFromValue(cdivalue);
		if (subvariables == null){return;}
		
		ArrayList<VarDescription> tempSubvars = new ArrayList<>();
		for (int k = 0; k < subvariables.length; k++) {
			ICDIVariable icdilovalvariable =  subvariables[k];
			String hexaddress = CDIEventListener.getHexAddress((Variable)icdilovalvariable);
			String typename = CDIEventListener.getLocalVariableTypeName(icdilovalvariable);
			ICDIValue subcdivalue = CDIEventListener.getLocalVariableValue(icdilovalvariable);
			String valuestring = CDIEventListener.getValueString(subcdivalue);
			String qualifiedname = CDIEventListener.getQualifiedName(icdilovalvariable);
			VarDescription addedVar = new VarDescription(hexaddress, typename, valuestring, qualifiedname);
			if (hexaddress.compareTo(endaddress) >=0  && hexaddress.compareTo(startaddress) <=0 ) {
				tempSubvars.add(addedVar);
				var.addNested(addedVar);
			} else {
				heapVars.add(addedVar);
			}
			fillVarDescriptors(addedVar, subcdivalue, startaddress, endaddress);
		}
		VarDescription[] subvars = new VarDescription[tempSubvars.size()];//extra space for return value
		tempSubvars.toArray(subvars);
	}
	
	
	public String getEaxValue(){
		return EAXvalue;
	}
	
	public String getEaxType(){
		return EAXvaluetype;
	}
	
	public String getProgramCounter() {
		//TODO implement
		return null;
	}
	
	public static ICDIStackFrame[] getStackFrames(ICDIThread thread){
		if (thread == null){return null;}
		ICDIStackFrame[] Frames = new ICDIStackFrame[0];
		try {
			Frames = thread.getStackFrames();
		} catch (CDIException e) {e.printStackTrace();}
		return Frames;
	}
	
	public static ICDIStackFrame getTopStackFrame(ICDIThread thread){
		if (thread == null){return null;}
		ICDIStackFrame Frame = null;
		try {
			Frame = thread.getStackFrames()[0];
		} catch (CDIException e) {}		
		return Frame;
	}	
	
	public static ICDILocalVariableDescriptor[] GetStackFrameLocalVariableDescriptors(ICDIStackFrame frame){
		ICDILocalVariableDescriptor[] descriptor = new ICDILocalVariableDescriptor[0];
		try {
			descriptor = frame.getLocalVariableDescriptors();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return descriptor;
	}
	
	public static ICDIArgumentDescriptor[] getStackFrameArgumentDescriptors(ICDIStackFrame frame){
		ICDIArgumentDescriptor[] descriptor = new ICDIArgumentDescriptor[0];
		try {
			descriptor = frame.getArgumentDescriptors();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return descriptor;
	}
	
	public static ICDIValue getLocalVariableValue(ICDIVariable variable){
		ICDIValue value = null;
		try {
			value = variable.getValue();
		} catch (CDIException e) {
			e.printStackTrace();
		}	
		return value;
	}
	
	public static String getLocalVariableTypeName(ICDIVariable variable){
		String typeName = null;
		try {
			typeName = variable.getTypeName();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return typeName;
	}
	
	public static String getQualifiedName(ICDIVariableDescriptor variable){
		String QualifiedName = null;
		try {
			QualifiedName = variable.getQualifiedName();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return QualifiedName;
	}
	
	public static String getValueString(ICDIValue value){
		String valuestring = "";
		if (value == null){return valuestring;}
		try {
			valuestring = value.getValueString();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return valuestring;
	}
	
	public static String getValueTypeName(ICDIValue value){
		String valueTypeName = "";
		if (value == null){return valueTypeName;}
		try {
			valueTypeName = value.getTypeName();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return valueTypeName;
	}
	
	public static ICDIVariable[] getLocalVariablesFromValue(ICDIValue value){
		ICDIVariable[] variables = null;
		try {
			variables = value.getVariables();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return variables;
	}
	
	public static ICDILocalVariable getLocalVariable(ICDILocalVariableDescriptor descriptor){
		ICDILocalVariable variable = null;
		try {
			 variable = descriptor.getStackFrame().createLocalVariable(descriptor);
		} catch (CDIException e) {
			e.printStackTrace();
		}		
		return variable;
	}
	
	public static ICDIArgument getArgument(ICDIArgumentDescriptor descriptor){
		ICDIArgument argument = null;
		try {
			argument = descriptor.getStackFrame().createArgument(descriptor);
		} catch (CDIException e) {
			e.printStackTrace();
		}		
		return argument;
	}
	
	public static String getHexAddress (Variable variable){
		String hexAddress = "";
		try {
			hexAddress = variable.getHexAddress();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return hexAddress;
	}
	
	public static ICDIRegisterGroup[]  getICDIRegisterGroups (ICDIStackFrame frame){
		ICDIRegisterGroup[] registerGroup = new ICDIRegisterGroup[0];
		try {
			registerGroup = frame.getThread().getTarget().getRegisterGroups();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return registerGroup;
	}
	
	public static ICDIRegisterDescriptor[] getICDIRegisterDescriptors(ICDIRegisterGroup registerGroup){
		ICDIRegisterDescriptor[] regDescriptors = new ICDIRegisterDescriptor[0];
		try {
			regDescriptors = registerGroup.getRegisterDescriptors();
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return regDescriptors;
	}
	
	public static ICDIRegister createICDIRegister(ICDIStackFrame frame, ICDIRegisterDescriptor regDescriptor){
		ICDIRegister register = null;
		try {
			register = frame.getTarget().createRegister(regDescriptor);
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return register;
	}
	
	public static ICDIValue getRegisterValue(ICDIStackFrame frame, ICDIRegister register){
		ICDIValue value = null;
		try {
			value = register.getValue(frame);
		} catch (CDIException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static ICDIValue findRegisterValueByQualifiedName(ICDIStackFrame frame, String qualifedName){
		ICDIValue value = null;
		ICDIRegisterGroup[] registerGroups = CDIEventListener.getICDIRegisterGroups(frame);
		for (ICDIRegisterGroup registerGroup : registerGroups){
			ICDIRegisterDescriptor[] regDescriptors = CDIEventListener.getICDIRegisterDescriptors(registerGroup);
			for (ICDIRegisterDescriptor regDescriptor : regDescriptors){
				ICDIRegister cdiRegister = CDIEventListener.createICDIRegister(frame, regDescriptor);
				String qName = CDIEventListener.getQualifiedName(cdiRegister);
				if (qName.equals(qualifedName)){value = CDIEventListener.getRegisterValue(frame, cdiRegister);}
			}
		}		
		return value;
	}
	
	public static ICDIInstruction[] getInstructions(ICDIStackFrame frame){
		ICDIInstruction[] instructions = new ICDIInstruction[0];
		try {
			instructions = frame.getTarget().getInstructions(
					frame.getLocator().getFile(), frame.getLocator().getLineNumber());
		}
		catch (CDIException e) {e.printStackTrace();}
		return instructions;
	}
}
