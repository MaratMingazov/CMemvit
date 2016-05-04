package plugin2.views;
import org.eclipse.cdt.launch.internal.LocalCDILaunchDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

public class ConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		LocalCDILaunchDelegate dd = new LocalCDILaunchDelegate();
		dd.launch(configuration, mode, launch, monitor);//.
	}

}
