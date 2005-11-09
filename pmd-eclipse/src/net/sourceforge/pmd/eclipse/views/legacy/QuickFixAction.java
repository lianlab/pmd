/*  
 * <copyright>  
 *  Copyright 1997-2003 PMD for Eclipse Development team
 *  under sponsorship of the Defense Advanced Research Projects  
 *  Agency (DARPA).  
 *   
 *  This program is free software; you can redistribute it and/or modify  
 *  it under the terms of the Cougaar Open Source License as published by  
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).   
 *   
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS   
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR   
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF   
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT   
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT   
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL   
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,   
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR   
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.   
 *   
 * </copyright>
 */ 
package net.sourceforge.pmd.eclipse.views.legacy;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.MarkerResolutionSelectionDialog;
import org.eclipse.ui.ide.IDE;

/**
 * Implements the quick fix menu item from the violations view
 * 
 * @author Philippe Herlin
 * @version $Revision$
 * 
 * $Log$
 * Revision 1.1  2005/10/24 22:45:58  phherlin
 * Integrating Sebastian Raffel's work
 * Move orginal Violations view to legacy
 *
 * Revision 1.3  2003/12/02 22:30:54  phherlin
 * Adapting to Eclipse v3M5
 *
 * Revision 1.2  2003/11/30 22:57:43  phherlin
 * Merging from eclipse-v2 development branch
 *
 * Revision 1.1.2.1  2003/11/05 13:11:47  phherlin
 * Add the Quick fix menu item to the violations view
 *
 */
public class QuickFixAction extends Action {
    private ViolationView violationView;
    
    /**
     * Constructor with a violation view
     * @param violationView
     */
    public QuickFixAction(ViolationView violationView) {
        this.violationView = violationView;
    }

    /**
     * Test if this action should be enabled or not
     * @param marker
     * @return
     */    
    public boolean hasQuickFix() {
        boolean hasQuickFix = false;
        IMarker[] selectedMarkers = violationView.getSelectedViolations();

        if ((selectedMarkers != null) && (selectedMarkers.length == 1)) {
            hasQuickFix = IDE.getMarkerHelpRegistry().hasResolutions(selectedMarkers[0]);
        }
        
        return hasQuickFix;
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        IMarker[] selectedMarkers = violationView.getSelectedViolations();
        IWorkbench workbench = PlatformUI.getWorkbench();
        IMarkerResolution resolutions[] = IDE.getMarkerHelpRegistry().getResolutions(selectedMarkers[0]);
        if (resolutions.length != 0) {
            MarkerResolutionSelectionDialog dialog = new MarkerResolutionSelectionDialog(workbench.getActiveWorkbenchWindow().getShell(), resolutions);
            if (dialog.open() == Dialog.OK) {
                Object[] result = dialog.getResult();
                if ((result != null) && (result.length > 0)) {
                    IMarkerResolution selectedResolution = (IMarkerResolution) result[0];
                    selectedResolution.run(selectedMarkers[0]); 
                }
            }
        }
    }

}