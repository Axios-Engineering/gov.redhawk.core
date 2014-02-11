/**
 * This file is protected by Copyright.
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.internal.ui.port.nxmplot.view;

import gov.redhawk.internal.ui.port.nxmplot.PlotSettingsDialog;
import gov.redhawk.model.sca.ScaUsesPort;
import gov.redhawk.ui.port.nxmplot.AbstractNxmPlotWidget;
import gov.redhawk.ui.port.nxmplot.FftSettings;
import gov.redhawk.ui.port.nxmplot.IPlotView;
import gov.redhawk.ui.port.nxmplot.PlotActivator;
import gov.redhawk.ui.port.nxmplot.PlotPageBook2;
import gov.redhawk.ui.port.nxmplot.PlotSettings;
import gov.redhawk.ui.port.nxmplot.PlotSource;
import gov.redhawk.ui.port.nxmplot.PlotType;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.services.IDisposable;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * The spectral view provides view that displays spectral data in a plot.
 *
 * @since 4.2
 * @noreference This class is not intended to be referenced by clients
 */
public class PlotView2 extends ViewPart implements IPlotView {
	/** The ID of the view. */
	public static final String ID = "gov.redhawk.ui.port.nxmplot.PlotView2";

	private static final String ADJUST_PLOT_SETTINGS_ACTION_ID = "AdjustPlotSettingsMenuItemAction";
	
	private static int secondardId;
	
	/** The private action for toggling raster enabled state. */
	private IAction plotTypeAction;

	/** The private action for creating a new plot connection */
	private IAction newPlotViewAction;

	/** The private action for adjusting plot settings. */
	private IAction adjustPlotSettingsAction;

	private IMenuManager menu;

	private class PlotTypeMenuAction extends Action {

		public PlotTypeMenuAction() {
			super("Change Plot Type", IAction.AS_PUSH_BUTTON | IAction.AS_CHECK_BOX | SWT.None);
			setChecked(plotPageBook.getCurrentType() == PlotType.RASTER); // updates tool tip to display what action button will do
		}

		@Override
		public void run() {
			PlotType currentType = plotPageBook.getCurrentType();
			if (currentType == PlotType.RASTER) {
				plotPageBook.showPlot(PlotType.LINE);
				this.setChecked(false);
			} else {
				plotPageBook.showPlot(PlotType.RASTER);
				this.setChecked(true);
			}
		}

		@Override
		public void setChecked(final boolean checked) {
			super.setChecked(checked);
			if (!checked) {
				this.setToolTipText("Show Raster");
			} else {
				this.setToolTipText("Show Line");
			}
		}
	} // end class PlotTypeMenuAction

	private PlotPageBook2 plotPageBook;

	private DisposeListener disposeListener = new DisposeListener() {

		@Override
		public void widgetDisposed(DisposeEvent e) {
			if (!diposed && !PlatformUI.getWorkbench().isClosing()) {
				getSite().getPage().hideView(PlotView2.this);
			}
		}
	};

	private boolean diposed;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPartControl(final Composite parent) {
		this.plotPageBook = new PlotPageBook2(parent, SWT.None);
		this.plotPageBook.addDisposeListener(disposeListener);

		createActions();
		createToolBars();
		createMenu();
	}
	
	@Override
	public void dispose() {
		this.diposed = true;
		if (this.plotPageBook != null && !plotPageBook.isDisposed()) {
			this.plotPageBook.removeDisposeListener(disposeListener);
			this.plotPageBook = null;
		}
		super.dispose();
	}

	/**
	 * @param port ScaPort object to plot the output from.
	 * @param fftSettings settings to use if an FFT is to be displayed (null for none)
	 * @param qualifiers
	 * @return IDisposable (since 4.3, was IPlotSession in 4.2)
	 * @see PlotPageBook2#addSource2(PlotSource)
	 * @deprecated since 4.4, use PlotPageBook2#addSource2(PlotSource)
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public IDisposable addPlotSource(ScaUsesPort port, final FftSettings fftSettings, String qualifiers) {
		return this.plotPageBook.addSource(port, fftSettings, qualifiers);
	}

	public IDisposable addPlotSource(@NonNull PlotSource plotSource) {
		return this.plotPageBook.addSource(plotSource);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		if (this.plotPageBook != null && !this.plotPageBook.isDisposed()) {
			this.plotPageBook.setFocus();
		}
	}

	private void createMenu() {
		final IActionBars bars = getViewSite().getActionBars();
		menu = bars.getMenuManager();
		if (this.newPlotViewAction != null) {
			menu.add(this.newPlotViewAction);
		}
		if (this.adjustPlotSettingsAction != null) {
			menu.add(this.adjustPlotSettingsAction);
		}
		
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		
		this.plotPageBook.contributeMenuItems(menu);
	}

	/**
	 * Create the view toolbars.
	 */
	private void createToolBars() {
		final IActionBars bars = getViewSite().getActionBars();

		final IToolBarManager toolBarManager = bars.getToolBarManager();
		toolBarManager.add(this.plotTypeAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	public static String createSecondaryId() {
		return String.valueOf(secondardId++);
	}

	/** Creates the actions. **/
	private void createActions() {
		this.plotTypeAction = new PlotTypeMenuAction();

		final ImageDescriptor rasterImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PlotActivator.PLUGIN_ID, "icons/raster.png");
		this.plotTypeAction.setImageDescriptor(rasterImageDescriptor);

		this.newPlotViewAction = createNewPlotViewAction();
		this.adjustPlotSettingsAction = createAdjustPlotSettingsAction();
	}

	private IAction createNewPlotViewAction() {
		IAction action = new Action() {
			@Override
			public void run() {
				try {
					final IViewPart newView = getSite().getPage().showView(getSite().getId(), createSecondaryId(), IWorkbenchPage.VIEW_ACTIVATE);
					if (newView instanceof PlotView2) {
						final PlotView2 newPlotView = (PlotView2) newView;
						newPlotView.setPartName(getPartName());
						newPlotView.setTitleToolTip(getTitleToolTip());
						newPlotView.getPlotPageBook().showPlot(plotPageBook.getCurrentType());
						for (PlotSource source : plotPageBook.getSources()) {
							newPlotView.addPlotSource(source);
						}
						PlotSettings settings = plotPageBook.getActivePlotWidget().getPlotSettings();
						settings.setPlotType(null);
						newPlotView.getPlotPageBook().getActivePlotWidget().applySettings(settings);
					}
				} catch (final PartInitException e) {
					StatusManager.getManager().handle(new Status(IStatus.ERROR, PlotActivator.PLUGIN_ID, "Failed to open new Plot View", e),
						StatusManager.SHOW | StatusManager.LOG);
				}
			} // end method
		};
		action.setEnabled(true);
		action.setText("New Plot View");
		action.setToolTipText("Open a new Plot View with all the same plots.");
		
		return action;
	}
	
	private IAction createAdjustPlotSettingsAction() {
		IAction action = new Action() {
			@Override
			public void run() {
				AbstractNxmPlotWidget activeWidget = plotPageBook.getActivePlotWidget();
				PlotSettings plotSettings = activeWidget.getPlotSettings();
				PlotSettingsDialog dialog = new PlotSettingsDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), plotSettings);
				final int result = dialog.open();
				if (result == Window.OK) {
					PlotSettings newSettings = dialog.getSettings();
					PlotType newType = newSettings.getPlotType();
					// Ignore Plot type in settings use page book.showPlot(type) instead
					newSettings.setPlotType(null);

					for (AbstractNxmPlotWidget widget : plotPageBook.getAllPlotWidgets()) {
						widget.applySettings(newSettings); // apply settings to all plot widgets
					} // end for loop
					plotPageBook.showPlot(newType);
				}
			} // end method
		};

		action.setId(ADJUST_PLOT_SETTINGS_ACTION_ID);
		action.setEnabled(true);
		action.setText("Adjust Plot Settings");
		action.setToolTipText("Adjust/Override Plot Settings");
		
		return action;
	}

	public PlotPageBook2 getPlotPageBook() {
		return plotPageBook;
	}

	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	public void setTitleToolTip(String toolTip) {
		super.setTitleToolTip(toolTip);
	}
}
