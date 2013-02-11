package bit.scallionchickenrice.raph.tool;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import bit.scallionchickenrice.raph.entity.UserClient;

public class UserClientLabelProvider implements ITableLabelProvider {
	public String getColumnText(Object element, int col){
        UserClient uc = (UserClient)element;
        if(col == 0) {
        	return uc.getUsername();
        }
        else if(col == 1) {
        	return uc.getPassword();
        }
        else if(col == 2) {
        	return uc.getUserType();
        }
        else if(col == 3) {
        	return uc.getCondition();
        }
        else {
        	return uc.getIpAddress();
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
