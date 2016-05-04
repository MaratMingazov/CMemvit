package ru.innopolis.lips.memvit;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class MemvitConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	// Create an array of tabs to be displayed in the debug dialog
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {new MemvitConfigurationTab()};
		setTabs(tabs);
	}
}
