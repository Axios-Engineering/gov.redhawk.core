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
package gov.redhawk.sca.sad.diagram.edit.policies;

import gov.redhawk.diagram.edit.policies.IUsesPortStubItemSemanticEditPolicy;
import gov.redhawk.diagram.edit.policies.UsesPortStubItemSemanticEditPolicyHelper;
import gov.redhawk.sca.sad.diagram.edit.commands.ConnectInterfaceCreateCommand;
import gov.redhawk.sca.sad.diagram.edit.commands.SadConnectInterfaceReorientCommand;
import gov.redhawk.sca.sad.diagram.providers.RedhawkSadElementTypes;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.sad.diagram.edit.parts.SadConnectInterfaceEditPart;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 */
public class UsesPortStubItemSemanticEditPolicy extends mil.jpeojtrs.sca.sad.diagram.edit.policies.UsesPortStubItemSemanticEditPolicy implements
        IUsesPortStubItemSemanticEditPolicy {

	private final UsesPortStubItemSemanticEditPolicyHelper editPolicyHelper = new UsesPortStubItemSemanticEditPolicyHelper(RedhawkSadElementTypes.INSTANCE,
	        this);

	@Override
	protected Command getStartCreateRelationshipCommand(final CreateRelationshipRequest req) {
		return this.editPolicyHelper.getStartCreateRelationshipCommand(req);
	}

	@Override
	protected boolean shouldProceed(final DestroyRequest destroyRequest) {
		Node node = (Node) getHost().getModel();
		return this.editPolicyHelper.shouldProceed(destroyRequest, node.getElement());
	}

	@Override
	protected Command getDuplicateCommand(final DuplicateElementsRequest req) {
		return this.editPolicyHelper.getDuplicateCommand(req);
	}

	@Override
	protected Command getMoveCommand(final MoveRequest req) {
		return this.editPolicyHelper.getMoveCommand(req);
	}

	public gov.redhawk.diagram.edit.commands.ConnectInterfaceCreateCommand createConnectInterfaceCreateCommand(final CreateRelationshipRequest req,
	        final EObject source, final EObject target) {
		return new ConnectInterfaceCreateCommand((SoftwareAssembly) ((View) getHost().getRoot().getContents().getModel()).getElement(), req, source, target);
	}

	public Command getGEFWrapper(final gov.redhawk.diagram.edit.commands.ConnectInterfaceCreateCommand createConnectInterfaceCreateCommand) {
		return super.getGEFWrapper(createConnectInterfaceCreateCommand);
	}
	
	protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
		switch (getVisualID(req)) {
		case SadConnectInterfaceEditPart.VISUAL_ID:
			return getGEFWrapper(new SadConnectInterfaceReorientCommand(req));
		default:
			break;
		}
		return super.getReorientRelationshipCommand(req);
	}

}
