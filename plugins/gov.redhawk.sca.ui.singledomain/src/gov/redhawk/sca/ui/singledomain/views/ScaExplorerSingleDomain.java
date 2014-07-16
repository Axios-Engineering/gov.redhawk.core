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
package gov.redhawk.sca.ui.singledomain.views;

import gov.redhawk.model.sca.DomainConnectionException;
import gov.redhawk.model.sca.RefreshDepth;
import gov.redhawk.model.sca.ScaDomainManager;
import gov.redhawk.model.sca.ScaDomainManagerRegistry;
import gov.redhawk.model.sca.ScaPackage;
import gov.redhawk.sca.ScaPlugin;
import gov.redhawk.sca.ui.ScaUiPlugin;
import gov.redhawk.sca.ui.compatibility.CompatibilityUtil;
import gov.redhawk.sca.ui.singledomain.CustomControlItem;
import gov.redhawk.sca.ui.singledomain.CustomMouseEvent;
import gov.redhawk.sca.ui.singledomain.CustomMouseTrackListener;
import gov.redhawk.sca.ui.singledomain.ScaSingleDomainPlugin;
import gov.redhawk.sca.ui.singledomain.ScaSingleDomainPreferenceConstants;
import gov.redhawk.sca.ui.singledomain.TrackableLabel;
import gov.redhawk.sca.ui.singledomain.dialogs.DialogCloseJob;
import gov.redhawk.sca.ui.singledomain.dialogs.DomainsDialog;
import gov.redhawk.sca.ui.views.ScaExplorer;

import java.security.Principal;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * 
 * Workaround code commented out in this class is to enable a mouse listener using Juno 4.x
 * while bug 402593 is not yet fixed.
 *
 */
public class ScaExplorerSingleDomain extends ScaExplorer {

	protected static final long DIALOG_HIDE_WAIT_MS = 200;

	/**
	 * @since 1.1
	 */
	public static final String VIEW_ID = "gov.redhawk.ui.sca_explorer_sd";

	private DomainsDialog dialog;

	private CommonViewer viewer;

	private static IPreferenceStore prefs;

	public ScaExplorerSingleDomain() {

	}

	private AdapterImpl domainChangeAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(Notification msg) {
			if (msg.getNotifier() instanceof ScaDomainManagerRegistry) {
				switch (msg.getFeatureID(ScaDomainManagerRegistry.class)) {
				case ScaPackage.SCA_DOMAIN_MANAGER_REGISTRY__DOMAINS:
					ScaDomainManagerRegistry registry = (ScaDomainManagerRegistry) msg.getNotifier();
					switch (msg.getEventType()) {
					case Notification.REMOVE:
						ScaDomainManager domainRemoved = (ScaDomainManager) msg.getOldValue();
						if (domainRemoved.getLabel().equals(getActiveDomainName())) {
							if (registry.getDomains().size() > 0) {
								setActiveDomain(registry.getDomains().get(0).getLabel());
							} else {
								setActiveDomain("");
								dialog.checkHyperlinkEnabled(null);
							}
						} else {
							if (registry.getDomains().size() == 0) {
								setActiveDomain("");
								dialog.checkHyperlinkEnabled(null);
							}
						}
						break;
					case Notification.ADD:
						if (ScaExplorerSingleDomain.this.setNewDomainActive) {
							ScaDomainManager domainAdded = (ScaDomainManager) msg.getNewValue();
							setActiveDomain(domainAdded.getLabel());
							dialog.checkHyperlinkEnabled(domainAdded);
						}
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
			}
		}
	};

	
	public static String getActiveDomainName() {
		return prefs.getString(ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN);
	}

	/**
	 * @since 2.0
	 */
	public static ScaDomainManager getActiveDomain(Display display) {
		return ScaPlugin.getDefault().getDomainManagerRegistry(display).findDomain(getActiveDomainName());
	}

	private IPropertyChangeListener activeDomainListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN.equals(event.getProperty())) {
				String oldDomain = (String) event.getOldValue();
				if (!oldDomain.isEmpty() && prefs.getBoolean(ScaSingleDomainPreferenceConstants.SCA_DISCONNECT_INACTIVE)) {
					if (Display.getCurrent() != null) {
						ScaDomainManager domain = ScaPlugin.getDefault().getDomainManagerRegistry(getSite().getShell().getDisplay()).findDomain(oldDomain);
						if (domain != null) {
							domain.disconnect();
						}
					}
				}
				final ScaDomainManager activeDomain = getActiveDomain(getSite().getShell().getDisplay());
				String newDomain = (String) event.getNewValue();
				if (!newDomain.isEmpty()) {
					ScaDomainManager domain = ScaPlugin.getDefault().getDomainManagerRegistry(getSite().getShell().getDisplay()).findDomain(newDomain);
					if (domain != null && domain.isAutoConnect()) {
						try {
							domain.connect(new NullProgressMonitor(), RefreshDepth.SELF);
						} catch (DomainConnectionException e) {
							ScaSingleDomainPlugin.logError("Unable to connect to domain" + domain.getLabel(), e);
						}
					}
				}
				if (activeDomain != null) {
					viewer.getControl().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							fillToolBar(activeDomain.getLabel().trim().isEmpty() ? "NO ACTIVE DOMAIN" : activeDomain.getLabel());
							getViewSite().getActionBars().updateActionBars();
							viewer.setInput(activeDomain);
							viewer.refresh(true);
						}

					});
				} else {
					viewer.getControl().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							fillToolBar("NO ACTIVE DOMAIN");
							getViewSite().getActionBars().updateActionBars();
							viewer.setInput(activeDomain);
						}

					});
				}
			} else if (ScaSingleDomainPreferenceConstants.SCA_SET_NEW_DOMAIN_ACTIVE.equals(event.getProperty())) {
				//event.getNewValue() can return a String or Boolean
				ScaExplorerSingleDomain.this.setNewDomainActive = Boolean.valueOf(String.valueOf(event.getNewValue()));
			}
			dialog.checkHyperlinkEnabled(getActiveDomain(getSite().getShell().getDisplay()));
		}
	};

	private CustomMouseTrackListener rapMouseTrackListener = new CustomMouseTrackListener() {

		@Override
		public void mouseHover(CustomMouseEvent e) {
			/*
			 * Not sure we need mouse hover
			 */
			Point mouseLoc = getViewSite().getShell().getDisplay().getCursorLocation();
			ToolItem item = mgr.getControl().getItem(mgr.getControl().toControl(mouseLoc));
			if (item != null) {
				doMouseHover(item);
			}
		}

		@Override
		public void mouseEnter(CustomMouseEvent e) {
			Point mouseLoc = getViewSite().getShell().getDisplay().getCursorLocation();
			ToolItem item = mgr.getControl().getItem(mgr.getControl().toControl(mouseLoc));
			if (item != null) {
				doMouseEnter(domains.getControl());
			}
		}

		@Override
		public void mouseExit(CustomMouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	private MouseTrackListener rcpMouseTrackListener = new MouseTrackListener() {

		@Override
		public void mouseEnter(MouseEvent e) {
			Point mouseLoc = getViewSite().getShell().getDisplay().getCursorLocation();
			ToolItem item = mgr.getControl().getItem(mgr.getControl().toControl(mouseLoc));
			if (item != null) {
				doMouseEnter(domains.getControl());
			}
		}

		@Override
		public void mouseExit(MouseEvent e) {
			//PASS
		}

		@Override
		public void mouseHover(MouseEvent e) {
			/*
			 * Not sure we need mouse hover
			 */
			Point mouseLoc = getViewSite().getShell().getDisplay().getCursorLocation();
			ToolItem item = mgr.getControl().getItem(mgr.getControl().toControl(mouseLoc));
			if (item != null) {
				doMouseHover(item);
			}
		}

	};

	private ToolBarManager mgr;

	private CustomControlItem domains;

	private boolean setNewDomainActive;

	//private UIJob mouseMoveListenerJob; //Workaround code

	@Override
	protected Object getInitialInput() {
		for (ScaDomainManager domain : ScaPlugin.getDefault().getDomainManagerRegistry(getSite().getShell().getDisplay()).getDomains()) {
			if (domain.getLabel() != null && domain.getLabel().equals(getActiveDomainName())) {
				if (!domain.isConnected() && domain.isAutoConnect()) {
					try {
						domain.connect(new NullProgressMonitor(), RefreshDepth.CHILDREN);
					} catch (DomainConnectionException e) {
						ScaSingleDomainPlugin.logError("Unable to connect to domain", e);
					}
				}
				domains.setLabelText(domain.getLabel().trim().isEmpty() ? "NO ACTIVE DOMAIN" : domain.getLabel());
				getViewSite().getActionBars().updateActionBars();
				return domain;
			}
		}
		domains.setLabelText("NO ACTIVE DOMAIN");
		return null;
	}

	@Override
	protected CommonViewer createCommonViewerObject(final Composite aParent) {
		CommonViewer retVal = super.createCommonViewerObject(aParent);
		this.viewer = retVal;
		return retVal;
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		fillToolBar("");
		prefs = ScaUiPlugin.getDefault().getScaPreferenceStore();
		ScaDomainManagerRegistry registry = ScaPlugin.getDefault().getDomainManagerRegistry(site.getShell().getDisplay());
		if (!registry.eAdapters().contains(domainChangeAdapter)) {
			registry.eAdapters().add(domainChangeAdapter);
		}
		prefs.addPropertyChangeListener(activeDomainListener);
		this.setNewDomainActive = prefs.getBoolean(ScaSingleDomainPreferenceConstants.SCA_SET_NEW_DOMAIN_ACTIVE);
		String activeDomainPref = prefs.getString(ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN);
		if (activeDomainPref == null || "".equals(activeDomainPref)) {
			prefs.setToDefault(ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN);
		} else {
			setActiveDomain(activeDomainPref);
		}
		if (SWT.getPlatform().startsWith("rap")) {
			Principal user = CompatibilityUtil.getUserPrincipal(site.getShell().getDisplay());
			//TODO Create or retrieve user-specific preferences node, for persisting domain connection info
			//TODO TEMP code for confirming cert presence while testing. Remove after implementation of cert-based user data persistence
			ScaSingleDomainPlugin.logInfo("User CN from cert: " + ((user == null) ? "<NONE>" : user.getName()));
		}

		//UISession termination when leaving page provided in RAP 2.1 For earlier versions, app is hanging page is refreshed
		//Only occurs when domains (CustomControlItem) is created. Need to test if disposing it when session terminates fixes the problem.
		//		RWT.getSessionStore().addSessionStoreListener(new SessionStoreListener() {
		//
		//			@Override
		//			public void beforeDestroy(SessionStoreEvent event) {
		//				ScaExplorerSingleDomain.this.dispose();
		//			}
		//
		//		});
	}

	//BEGIN WORKAROUND CODE
	// see note above (below is only needed for Juno 4.2 if bug 402593 is not fixed)
	//	private void createMouseMoveListener() {
	//		/** remove after Juno bug 402593 is fixed, wherein listeners on toolbar don't work */
	//		this.mouseMoveListenerJob = new UIJob("MouseMoveListener Job") {
	//
	//			@Override
	//			public IStatus runInUIThread(IProgressMonitor monitor) {
	//				final Display display = getViewSite().getShell().getDisplay();
	//				while (!monitor.isCanceled()) {
	//					final Point mouseLoc = display.getCursorLocation();
	//					final Rectangle toolbarItemLoc = domains.getControl().getBounds();
	//					if (toolbarItemLoc.contains(domains.getControl().getParent().toControl(mouseLoc))) {
	//						if (dialog.getShell() == null || !dialog.getShell().isVisible()) {
	//							doMouseEnter(domains.getControl());
	//						}
	//					}
	//				}
	//				return null;
	//			}
	//
	//		};
	//		this.mouseMoveListenerJob.setSystem(true);
	//	}
	//END WORKAROUND CODE

	private void fillToolBar(String label) {
		mgr = (ToolBarManager) getViewSite().getActionBars().getToolBarManager();
		if (domains != null) {
			mgr.remove(domains);
			if (SWT.getPlatform().startsWith("rap")) {
				domains.removeMouseTrackListener(rapMouseTrackListener);
			} else {
				domains.removeMouseTrackListener(rcpMouseTrackListener);
			}
		}

		domains = new CustomControlItem(label); //causes UI to hang if page is refreshed
		mgr.insert(0, domains);

		dialog = new DomainsDialog(getViewSite().getShell());

		if (SWT.getPlatform().startsWith("rap")) {
			domains.addMouseTrackListener(rapMouseTrackListener);
		} else {
			domains.addMouseTrackListener(rcpMouseTrackListener);
		}
		//BEGIN WORKAROUND CODE
		// see note above (below is only needed for Juno 4.2 if bug 402593 is not fixed)
		//		if (this.mouseMoveListenerJob == null) {
		//			createMouseMoveListener();
		//		}
		//		if (this.mouseMoveListenerJob.getState() != Job.RUNNING) {
		//			// PASS
		//			//this.mouseMoveListenerJob.schedule();
		//		}
		//END WORKAROUND CODE
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		prefs.setDefault(ScaSingleDomainPreferenceConstants.SCA_DISCONNECT_INACTIVE, true);
		prefs.setDefault(ScaSingleDomainPreferenceConstants.SCA_SET_NEW_DOMAIN_ACTIVE, true);
	}

	private void setActiveDomain(final String name) {
		if (name == null || name.isEmpty()) {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					CompatibilityUtil.executeOnRequestThread(new Runnable() {

						@Override
						public void run() {
							prefs.setToDefault(ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN);
						}

					});
				}

			});
		} else {
			getSite().getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					CompatibilityUtil.executeOnRequestThread(new Runnable() {

						@Override
						public void run() {
							prefs.setValue(ScaSingleDomainPreferenceConstants.SCA_ACTIVE_DOMAIN, name);
						}

					});
				}

			});
		}
	}

	@Override
	public void dispose() {
		if (dialog != null) {
			dialog.dispose();
		}
		prefs.removePropertyChangeListener(activeDomainListener);
		ScaPlugin.getDefault().getDomainManagerRegistry(getSite().getShell().getDisplay()).eAdapters().remove(domainChangeAdapter);
		if (SWT.getPlatform().startsWith("rap")) {
			if (!domains.getControl().isDisposed()) {
				domains.removeMouseTrackListener(rapMouseTrackListener);
			}
		} else {
			if (!domains.getControl().isDisposed()) {
				domains.removeMouseTrackListener(rcpMouseTrackListener);
			}
		}
		//BEGIN WORKAROUND CODE
		//		if (this.mouseMoveListenerJob != null) {
		//			this.mouseMoveListenerJob.cancel();
		//		}
		//END WORKAROUND CODE
		super.dispose();
	}

	private void doMouseEnter(TrackableLabel control) {
		final int x = control.getBounds().x;
		final int y = control.getBounds().y;
		DialogCloseJob dialogCloseJob = new DialogCloseJob(dialog);
		dialog.show(control.getParent().toDisplay(new Point(x, y)), dialogCloseJob);
	}

	private void doMouseHover(ToolItem item) {
		int x = item.getBounds().x;
		int y = item.getBounds().y;
		DialogCloseJob dialogCloseJob = new DialogCloseJob(dialog);
		dialog.show(item.getParent().toDisplay(new Point(x, y)), dialogCloseJob);
	}
}
