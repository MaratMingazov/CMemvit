package plugin2.views;

import java.io.File;

import org.eclipse.cdt.core.IBinaryParser.IBinaryObject;
import org.eclipse.cdt.debug.core.ICDIDebugger2;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.ICDISessionConfiguration;
import org.eclipse.cdt.debug.mi.core.IMILaunchConfigurationConstants;
import org.eclipse.cdt.debug.mi.core.MIPlugin;
import org.eclipse.cdt.debug.mi.core.MISession;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.command.CommandFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;

public class CDIDebugger implements ICDIDebugger2 {
	private static Session session = null;
	
	public ICDISession createSession(ILaunch launch, File executable, IProgressMonitor monitor) throws CoreException {
		session = null;
		IPath gdbPath = getGDBPath( launch );
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		CommandFactory factory = getCommandFactory(config);
		
		String[] extraArgs = getExtraArguments( config );
		boolean usePty = usePty( config );
		try {
			System.out.println("!!!!!!!!!!!! "+ gdbPath.toOSString());
			String gpath = "/usr/local/bin/gdb";
			
			session = MIPlugin.getDefault().createSession( getSessionType( config ), gpath, factory, executable, extraArgs, usePty, monitor );
			ICDISessionConfiguration sessionConfig = getSessionConfiguration( session );
			if ( sessionConfig != null ) {session.setConfiguration( sessionConfig );}
		}
		catch( OperationCanceledException e ) {}
		catch( Exception e ) {
			if ( e instanceof CoreException ) {throw (CoreException)e;}
			throw newCoreException( e );
		}			
		//session.getEventManager().addEventListener(CDIEventListener.GetInstance());
		return session;			
	}
	
	public static Session getSession(){
		return session;
	}
	
	public ICDISession createDebuggerSession(ILaunch launch, IBinaryObject exe, IProgressMonitor monitor) throws CoreException {
		//return new CDISession();
		return null;
	}
	
	protected IPath getGDBPath(ILaunch launch) throws CoreException {
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		System.out.println("!!!!!!1!!!!!! = " + IMILaunchConfigurationConstants.ATTR_DEBUG_NAME + " , " + IMILaunchConfigurationConstants.DEBUGGER_DEBUG_NAME_DEFAULT);
		String command = config.getAttribute(IMILaunchConfigurationConstants.ATTR_DEBUG_NAME, IMILaunchConfigurationConstants.DEBUGGER_DEBUG_NAME_DEFAULT);
		System.out.println("CMD = " + command);
		try {
			command = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(command, false);
		} catch (Exception e) { MIPlugin.log(e); /* take value of command as it*/}
		return new Path(command);
	}
	   
	protected String[] getExtraArguments( ILaunchConfiguration config ) throws CoreException {return new String[0];}
	
	protected boolean usePty( ILaunchConfiguration config ) throws CoreException {return config.getAttribute( ICDTLaunchConfigurationConstants.ATTR_USE_TERMINAL, true );}
	
	protected ICDISessionConfiguration getSessionConfiguration( ICDISession session ) {
		return null;
	}
		
	protected int getSessionType( ILaunchConfiguration config ) throws CoreException {
		String debugMode = config.getAttribute( ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE, ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN );
		if ( ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN.equals( debugMode ) ) return MISession.PROGRAM;
		if ( ICDTLaunchConfigurationConstants.DEBUGGER_MODE_ATTACH.equals( debugMode ) ) return MISession.ATTACH;
		if ( ICDTLaunchConfigurationConstants.DEBUGGER_MODE_CORE.equals( debugMode ) ) return MISession.CORE;
		throw newCoreException( MIPlugin.getResourceString( "src.AbstractGDBCDIDebugger.0" ) + debugMode, null ); //$NON-NLS-1$
	}
	
	protected CoreException newCoreException( Throwable exception ) {
		String message = MIPlugin.getResourceString( "src.AbstractGDBCDIDebugger.1" ); //$NON-NLS-1$
		return newCoreException( message, exception );
	}
	
	protected CoreException newCoreException( String message, Throwable exception ) {
		int code = ICDTLaunchConfigurationConstants.ERR_INTERNAL_ERROR;
		String ID = MIPlugin.getUniqueIdentifier();
		MultiStatus status = new MultiStatus( ID, code, message, exception );
		status.add( new Status( IStatus.ERROR, ID, code, exception == null ? new String() : exception.getLocalizedMessage(), exception ) );
		return new CoreException( status );
	}
	
	protected CommandFactory getCommandFactory( ILaunchConfiguration config ) throws CoreException {
		String factoryID = MIPlugin.getCommandFactory( config );
		CommandFactory factory = MIPlugin.getDefault().getCommandFactoryManager().getCommandFactory( factoryID );
		String miVersion = getMIVersion( config );
		if ( factory != null ) {factory.setMIVersion( miVersion );}
		return ( factory != null ) ? factory : new CommandFactory( miVersion );
	}
	
	protected String getMIVersion( ILaunchConfiguration config ) {
		return MIPlugin.getMIVersion( config );
	}
}
