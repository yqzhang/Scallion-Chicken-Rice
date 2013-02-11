package bit.scallionchickenrice.raph.tool;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import bit.scallionchickenrice.raph.entity.OrderDetail;

public class OrderDetailLabelProvider  implements ITableLabelProvider {
	public String getColumnText(Object element, int col){
        OrderDetail od = (OrderDetail)element;
        if(col == 0) {
        	return od.getDishName();
        }
        else {
        	return od.getNumber();
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
