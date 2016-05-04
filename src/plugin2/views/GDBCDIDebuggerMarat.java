package plugin2.views;

import java.io.File;

import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.ICDISessionConfiguration;
import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;
import org.eclipse.cdt.debug.mi.core.GDBCDIDebugger2;
import org.eclipse.cdt.debug.mi.core.IMILaunchConfigurationConstants;
import org.eclipse.cdt.debug.mi.core.MIException;
import org.eclipse.cdt.debug.mi.core.MIPlugin;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.cdi.model.Target;
import org.eclipse.cdt.debug.mi.core.command.CommandFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;

public class GDBCDIDebuggerMarat extends GDBCDIDebugger2 {
	
	private static ICDISession session = null;
	
	public ICDISession createSession( ILaunch launch, File executable, IProgressMonitor monitor ) throws CoreException {
		session = super.createSession(launch, executable, monitor);
		return session;

	}
	
	public static ICDISession getSession(){
		return session;
	}
	

	protected Session createGDBSession( ILaunch launch, File executable, IProgressMonitor monitor ) throws CoreException {
		Session session = null;
		IPath gdbPath = getGDBPath( launch );
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		
		CommandFactory factory = getCommandFactory( config );
		String[] extraArgs = getExtraArguments( config );
		

		
		boolean usePty = usePty( config );
		try {
	
			String gp = gdbPath.toOSString();
			System.out.println("YES!!!!!!!!YES");
			session = MIPlugin.getDefault().createSession( getSessionType( config ), "/usr/local/bin/gdb", factory, executable, extraArgs, usePty, monitor );
			ICDISessionConfiguration sessionConfig = getSessionConfiguration( session );
			if ( sessionConfig != null ) {
				session.setConfiguration( sessionConfig );
			}
		}
		catch( OperationCanceledException e ) {
		}
		catch( Exception e ) {
			// Catch all wrap them up and rethrow
			if ( e instanceof CoreException ) {
				throw (CoreException)e;
			}
			throw newCoreException( e );
		}
		return session;
	}
}
