package plugin2.views;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class ExampleTabGroup extends AbstractLaunchConfigurationTabGroup {

	// Create an array of tabs to be displayed in the debug dialog
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {new ExampleTab()};
		setTabs(tabs);
	}
}
