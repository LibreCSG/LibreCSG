package net.sf.avocado_cad.eclipse.ui;

import net.sf.avocado_cad.eclipse.ui.views.MainView;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		
//		layout.addView(BrowserApp.BROWSER_VIEW_ID, IPageLayout.RIGHT, .25f, IPageLayout.ID_EDITOR_AREA);
//		layout.addPlaceholder(BrowserApp.HISTORY_VIEW_ID, IPageLayout.LEFT, .3f, IPageLayout.ID_EDITOR_AREA); 
//		IViewLayout historyLayout = layout.getViewLayout(BrowserApp.HISTORY_VIEW_ID);
//		historyLayout.setCloseable(true);
		layout.setEditorAreaVisible(false);
		layout.addView(MainView.MAIN_VIEW_ID, IPageLayout.LEFT, .5f, IPageLayout.ID_EDITOR_AREA);
	}
}
