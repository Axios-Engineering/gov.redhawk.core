/** 
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.sca.sad.diagram.providers;

import gov.redhawk.sca.sad.diagram.RedhawkSadDiagramPlugin;
import gov.redhawk.sca.sad.diagram.edit.parts.SadConnectInterfaceEditPart;
import gov.redhawk.sca.sad.diagram.part.SadDiagramEditor;
import gov.redhawk.sca.sad.validation.ConnectionsConstraint;
import mil.jpeojtrs.sca.sad.SadConnectInterface;
import mil.jpeojtrs.sca.sad.SadConnections;
import mil.jpeojtrs.sca.sad.SadPackage;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.sad.diagram.part.SadVisualIDRegistry;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.AbstractDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * @since 2.0
 */
public class RedundantConnectionsProvider extends AbstractProvider implements IDecoratorProvider {

	private static final String KEY = "redundantConnections"; //$NON-NLS-1$

	private static class RedundantConnectProvider extends AbstractDecorator {

		private final Label toolTip = new Label("Redundant connection(s) detected", getImage());

		public RedundantConnectProvider(final IDecoratorTarget decoratorTarget) {
			super(decoratorTarget);
		}

		private final Adapter listener = new AdapterImpl() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void notifyChanged(final Notification msg) {
				super.notifyChanged(msg);
				if (msg.getEventType() == Notification.SET || msg.getEventType() == Notification.REMOVE) {
					if (msg.getNotifier() instanceof SadConnections) {
						switch (msg.getFeatureID(SadConnections.class)) {
						case SadPackage.SOFTWARE_ASSEMBLY__CONNECTIONS:
							refresh();
							break;
						default:
							break;
						}
					}
				}
			}
		};

		/**
		 * {@inheritDoc}
		 */
		public void activate() {
			refresh();
		}

		/**
		 * {@inheritDoc}
		 */
		public void refresh() {
			removeDecoration();
			final View view = (View) getDecoratorTarget().getAdapter(View.class);
			if (view == null || view.eResource() == null) {
				return;
			}

			final EditPart editPart = (EditPart) getDecoratorTarget().getAdapter(EditPart.class);
			if (editPart instanceof SadConnectInterfaceEditPart) {
				final SadConnectInterfaceEditPart connPart = (SadConnectInterfaceEditPart) editPart;

				if (connPart == null || connPart.getRoot() == null) {
					return;
				}

				if (view.getElement() instanceof SadConnectInterface) {
					final SadConnectInterface conn = (SadConnectInterface) view.getElement();

					if (!ConnectionsConstraint.uniqueConnection(conn)) {
						int margin = 0;
						if (connPart instanceof org.eclipse.gef.GraphicalEditPart) {
							margin = MapModeUtil.getMapMode(((org.eclipse.gef.GraphicalEditPart) connPart).getFigure()).DPtoLP(margin);
						}

						setDecoration(getDecoratorTarget().addShapeDecoration(getImage(), IDecoratorTarget.Direction.CENTER, margin, false)); // SUPPRESS CHECKSTYLE MagicNumber
						getDecoration().setToolTip(this.toolTip);
					}

					final SoftwareAssembly sad = SoftwareAssembly.Util.getSoftwareAssembly(conn.eResource());
					if (sad != null && !sad.eAdapters().contains(this.listener)) {
						sad.eAdapters().add(this.listener);
					}
				}
			}
		}

		private Image getImage() {
			return RedhawkSadDiagramPlugin.getDefault().getBundledImage("icons/obj16/warning_x14.png");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void createDecorators(final IDecoratorTarget decoratorTarget) {
		final EditPart editPart = (EditPart) decoratorTarget.getAdapter(EditPart.class);
		if (editPart instanceof GraphicalEditPart || editPart instanceof AbstractConnectionEditPart) {
			final Object model = editPart.getModel();
			if ((model instanceof View)) {
				final View view = (View) model;
				if (!(view instanceof Edge) || !view.isSetElement() || (view.getElement() == null)) {
					return;
				} else if (view.getElement().eContainer() instanceof SadConnections) {
					final EditDomain editDomain = editPart.getViewer().getEditDomain();
					if (!(editDomain instanceof DiagramEditDomain)) {
						return;
					}
					if (((DiagramEditDomain) editDomain).getEditorPart() instanceof SadDiagramEditor) {
						decoratorTarget.installDecorator(RedundantConnectionsProvider.KEY, new RedundantConnectProvider(decoratorTarget));
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean provides(final IOperation operation) {
		if (!(operation instanceof CreateDecoratorsOperation)) {
			return false;
		}
		final IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation).getDecoratorTarget();
		final View view = (View) decoratorTarget.getAdapter(View.class);
		return view != null && mil.jpeojtrs.sca.sad.diagram.edit.parts.SoftwareAssemblyEditPart.MODEL_ID.equals(SadVisualIDRegistry.getModelID(view));
	}

}
