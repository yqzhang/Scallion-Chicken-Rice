package bit.scallionchickenrice.leo.tool;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import bit.scallionchickenrice.leo.entity.ActiveInfo;

public class ActiveInfoLabelProvider implements ITableLabelProvider {
	public String getColumnText(Object element, int col){
        ActiveInfo ai = (ActiveInfo)element;
        if(col == 0) {
        	return ai.getActiveTime();
        }
        else if(col == 1) {
        	return ai.getActiveType();
        }
        else if(col == 2) {
        	return ai.getMACAddress();
        }
        else {
        	return ai.getActiveKey();
        }
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
