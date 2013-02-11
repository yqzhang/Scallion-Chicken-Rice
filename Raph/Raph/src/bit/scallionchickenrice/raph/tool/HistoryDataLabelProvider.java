package bit.scallionchickenrice.raph.tool;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import bit.scallionchickenrice.raph.entity.HistoryData;

public class HistoryDataLabelProvider implements ITableLabelProvider {
	public String getColumnText(Object element, int col){
        HistoryData hd = (HistoryData)element;
        if(col == 0) {
        	return hd.getOrderId();
        }
        else if(col == 1) {
        	return hd.getTableNumber();
        }
        else if(col == 2) {
        	return hd.getHistoryDiscount();
        }
        else if(col == 3) {
        	return hd.isIfPaid() ? "Yes" : "No";
        }
        else if(col == 4) {
        	return hd.getHistoryPrice();
        }
        else {
        	return hd.getHistoryTime();
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
