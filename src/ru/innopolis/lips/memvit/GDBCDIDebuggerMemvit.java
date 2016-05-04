package ru.innopolis.lips.memvit;

import java.io.File;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.mi.core.GDBCDIDebugger2;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

public class GDBCDIDebuggerMemvit extends GDBCDIDebugger2 {
	
	private static ICDISession session = null;
	
	public ICDISession createSession( ILaunch launch, File executable, IProgressMonitor monitor ) throws CoreException {
		session = super.createSession(launch, executable, monitor);
		return session;
	}
	
	public static ICDISession getSession(){
		return session;
	}
}
