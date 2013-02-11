package bit.scallionchickenrice.leo.tool;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import bit.scallionchickenrice.leo.entity.KeyInfo;

public class KeyInfoLabelProvider implements ITableLabelProvider {
	public String getColumnText(Object element, int col){
        KeyInfo ki = (KeyInfo)element;
        
        return ki.getActiveKey();
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		
	}

	public void dispose() {
		
	}

	public boolean isLableProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

}